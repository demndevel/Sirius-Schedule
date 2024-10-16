package sirius.schedule.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import sirius.schedule.core.models.Group
import sirius.schedule.core.models.LessonType
import sirius.schedule.core.models.WeeklySchedule
import sirius.schedule.core.models.Lesson
import sirius.schedule.core.models.DailySchedule
import java.time.DayOfWeek
import java.time.LocalTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreenSuccessState(
	weeklySchedule: WeeklySchedule,
	modifier: Modifier = Modifier
) {
	val pagerState = rememberPagerState(
		initialPage = 0,
		pageCount = { 7 },
	)
	val daysOfWeekScrollLazyState = rememberLazyListState()
	val coroutineScope = rememberCoroutineScope()

	LaunchedEffect(pagerState.currentPage) {
		daysOfWeekScrollLazyState
			.animateScrollToItem(pagerState.currentPage)
	}

	Column(modifier = modifier) {
		LazyRow(
			modifier = Modifier
				.fillMaxWidth(),
			state = daysOfWeekScrollLazyState,
			horizontalArrangement = Arrangement.spacedBy(8.dp)
		) {
			items(DayOfWeek.entries) { dayOfWeek ->
				Text(
					text = dayOfWeek.name.lowercase(),
					style = MaterialTheme.typography.headlineMedium,
					color = if (pagerState.currentPage == dayOfWeek.ordinal) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.secondary,
					modifier = Modifier
						.clip(RoundedCornerShape(4.dp))
						.clickable {
							coroutineScope.launch {
								pagerState.animateScrollToPage(dayOfWeek.ordinal)
							}
						}
						.padding(
							horizontal = 4.dp,
							vertical = 6.dp
						)
				)
			}
		}

		Spacer(Modifier.height(16.dp))

		HorizontalPager(
			state = pagerState,
			modifier = Modifier.weight(1f),
		) { page ->
			weeklySchedule.days.forEach { day ->
				if (day.dayOfWeek.ordinal == page) {
					DailySchedule(
						dailySchedule = day,
						modifier = Modifier.fillMaxSize()
					)
				}
			}
		}
	}
}

@Preview
@Composable
fun MainScreenSuccessStatePreview() {
	MainScreenSuccessState(
		weeklySchedule = WeeklySchedule(
			Group("Some assy group code"),
			listOf(
				DailySchedule(
					dayOfWeek = DayOfWeek.MONDAY,
					lessons = listOf(
						Lesson(
							startTime = LocalTime.of(9, 30),
							endTime = LocalTime.of(9, 30),
							lessonName = "Matematika",
							lessonType = LessonType.Lecture,
							auditory = "K90",
							teacher = "Golovin",
						),
						Lesson(
							startTime = LocalTime.of(9, 30),
							endTime = LocalTime.of(9, 30),
							lessonName = "Matematika",
							lessonType = LessonType.Lecture,
							auditory = "K90",
							teacher = "Golovin",
						),
						Lesson(
							startTime = LocalTime.of(9, 30),
							endTime = LocalTime.of(9, 30),
							lessonName = "Matematika",
							lessonType = LessonType.Lecture,
							auditory = "K90",
							teacher = "Golovin",
						),
						Lesson(
							startTime = LocalTime.of(9, 30),
							endTime = LocalTime.of(9, 30),
							lessonName = "Matematika",
							lessonType = LessonType.Lecture,
							auditory = "K90",
							teacher = "Golovin",
						),
					)
				)
			)
		),
		modifier = Modifier.fillMaxSize()
	)
}