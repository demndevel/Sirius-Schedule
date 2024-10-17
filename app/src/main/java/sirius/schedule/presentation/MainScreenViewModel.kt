package sirius.schedule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sirius.schedule.api.ScheduleApi
import sirius.schedule.cache.ScheduleCache
import sirius.schedule.core.models.Group
import sirius.schedule.core.models.WeeklySchedule

interface MainScreenUiState {
	val currentGroup: Group?
	val groups: List<Group>

	data class Success(
		override val currentGroup: Group?,
		override val groups: List<Group>,
		val weeklySchedule: WeeklySchedule
	) : MainScreenUiState

	data class Loading(
		override val currentGroup: Group?,
		override val groups: List<Group>,
	) : MainScreenUiState
}

data class MainScreenVmState(
	val currentGroup: Group? = null,
	val groups: List<Group> = emptyList(),
	val weeklySchedule: WeeklySchedule? = null,
	val isLoading: Boolean = false
) {
	fun toUiState(): MainScreenUiState {
		if (!isLoading && weeklySchedule != null) {
			return MainScreenUiState.Success(
				currentGroup = currentGroup,
				groups = groups,
				weeklySchedule = weeklySchedule
			)
		} else {
			return MainScreenUiState.Loading(
				currentGroup = currentGroup,
				groups = groups
			)
		}
	}
}

class MainScreenViewModel(
	private val api: ScheduleApi,
	private val cache: ScheduleCache
) : ViewModel() {
	private val _state = MutableStateFlow(
		MainScreenVmState()
	)

	val state: StateFlow<MainScreenUiState> = _state
		.map(MainScreenVmState::toUiState)
		.stateIn(
			viewModelScope,
			SharingStarted.Eagerly,
			initialValue = MainScreenUiState.Loading(Group("Unspecified"), emptyList())
		)

	fun load() {
		val cachedGroup = cache.getGroup()

		_state.update {
			it.copy(
				isLoading = true
			)
		}

		viewModelScope.launch {
			val groups = api.getGroups()

			if (_state.value.currentGroup != null) {
				_state.value.currentGroup?.let { group ->
					loadScheduleByGroup(group)
				}
			} else if (cachedGroup != null) {
				_state.update {
					it.copy(
						currentGroup = cachedGroup
					)
				}

				loadScheduleByGroup(cachedGroup)
			}

			_state.update {
				it.copy(
					groups = groups,
				)
			}
		}
	}

	private suspend fun loadScheduleByGroup(group: Group) {
		val schedule = api.getScheduleByGroup(group)

		_state.update {
			it.copy(
				weeklySchedule = filterFromInvalidLessons(schedule),
				isLoading = false
			)
		}
	}

	private fun filterFromInvalidLessons(schedule: WeeklySchedule): WeeklySchedule {
		return schedule.copy(
			days = schedule.days.map { day ->
				day.copy(
					lessons = day.lessons.mapNotNull { lesson ->
						if (lesson.lessonName.isBlank()) return@mapNotNull null

						return@mapNotNull lesson
					}
				)
			}
		)
	}

	fun updateCurrentGroup(newGroup: Group) {
		_state.update {
			it.copy(
				currentGroup = newGroup
			)
		}

		cache.saveGroup(newGroup)

		load()
	}
}