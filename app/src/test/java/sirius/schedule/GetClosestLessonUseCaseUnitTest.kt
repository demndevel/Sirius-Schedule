package sirius.schedule

import kotlinx.coroutines.test.runTest
import org.junit.Test
import sirius.schedule.api.ScheduleApi
import sirius.schedule.cache.ScheduleCache
import sirius.schedule.core.models.DailySchedule
import sirius.schedule.core.models.Group
import sirius.schedule.core.models.Lesson
import sirius.schedule.core.models.LessonType
import sirius.schedule.core.models.WeeklySchedule
import sirius.schedule.usecase.GetClosestLessonUseCase
import sirius.schedule.usecase.LessonStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class GetClosestLessonUseCaseUnitTest {
	private val mockCache = object : ScheduleCache {
		override fun getGroup(): Group? = Group("test")

		override fun saveGroup(group: Group) = Unit
	}

	private val mockApi = object : ScheduleApi {
		override suspend fun getGroups(): List<Group> = emptyList()

		override suspend fun getScheduleByGroup(group: Group): WeeklySchedule = WeeklySchedule(
			group = Group("test"),
			days = listOf(
				DailySchedule(
					date = LocalDate.of(2024, 3, 3),
					lessons = listOf(
						Lesson(
							startTime = LocalTime.of(3, 0),
							endTime = LocalTime.of(4, 0),
							lessonName = "TODO",
							lessonType = LessonType.Practice,
							auditory = "TODO",
							teacher = "TODO"
						)
					)
				)
			)
		)
	}

	@Test
	fun `closest lesson is pending`() = runTest {
		val useCase = GetClosestLessonUseCase(
			api = mockApi,
			cache = mockCache,
		)
		val now = LocalDateTime.of(2024, 3, 3, 2, 3)

		val result = useCase(now)

		assert(result?.status == LessonStatus.Pending)
	}

	@Test
	fun `closest lesson is current`() = runTest {
		val useCase = GetClosestLessonUseCase(
			api = mockApi,
			cache = mockCache,
		)
		val now = LocalDateTime.of(2024, 3, 3, 3, 3)

		val result = useCase(now)

		assert(result?.status == LessonStatus.Current)
	}

	@Test
	fun `today is no lessons`() = runTest {
		val useCase = GetClosestLessonUseCase(
			api = mockApi,
			cache = mockCache,
		)
		val now = LocalDateTime.of(2024, 3, 4, 3, 3)

		val result = useCase(now)

		assert(result == null)
	}
}
