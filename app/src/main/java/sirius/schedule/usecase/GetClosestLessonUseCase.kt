package sirius.schedule.usecase

import sirius.schedule.api.ScheduleApi
import sirius.schedule.cache.ScheduleCache
import sirius.schedule.cache.ScheduleCacheImpl
import sirius.schedule.core.models.Group
import sirius.schedule.core.models.Lesson
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

enum class LessonStatus {
	Current,
	Pending,
}

data class ClosestLesson(
	val status: LessonStatus,
	val date: LocalDate,
	val lesson: Lesson,
)

class GetClosestLessonUseCase(
	private val api: ScheduleApi,
	private val cache: ScheduleCache,
) {
	suspend operator fun invoke(now: LocalDateTime): ClosestLesson? =
		cache.getGroup()?.let { invoke(now, it) }

	suspend operator fun invoke(now: LocalDateTime, group: Group): ClosestLesson? {
		val scheduleByGroup = api.getScheduleByGroup(group)
		val today = now.toLocalDate()
		val nowTime = now.toLocalTime()

		val dailySchedule = scheduleByGroup.days.find { daily -> daily.date == today }

		if (dailySchedule == null) return null

		val currentLesson: Lesson? = dailySchedule.lessons
			.sortedBy { it.startTime }
			.firstOrNull { lesson ->
				(lesson.startTime <= nowTime && lesson.endTime >= nowTime)
			}

		if (currentLesson != null)
			return ClosestLesson(LessonStatus.Current, today, currentLesson)

		val pendingLesson: Lesson? = dailySchedule.lessons
			.sortedBy { it.startTime }
			.first { lesson ->
				lesson.startTime >= nowTime
			}

		return pendingLesson?.let { ClosestLesson(LessonStatus.Pending, today, pendingLesson) }
	}
}
