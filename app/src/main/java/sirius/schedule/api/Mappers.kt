package sirius.schedule.api

import sirius.schedule.core.models.DailySchedule
import sirius.schedule.core.models.Group
import sirius.schedule.core.models.Lesson
import sirius.schedule.core.models.LessonType
import sirius.schedule.core.models.WeeklySchedule
import java.time.DayOfWeek
import java.time.LocalTime

fun SerializableSchedule.toWeeklySchedule(): WeeklySchedule {
	return WeeklySchedule(
		group = Group(this.group),
		days = this.schedule.map { day ->
			DailySchedule(
				dayOfWeek = getDayOfWeekByString(day.key),
				lessons = day.value.map(SerializableLesson::toLesson)
			)
		}
	)
}

private fun SerializableLesson.toLesson(): Lesson {
	val (startTime, endTime) = getStartEndTimeByString(this.time)

	return Lesson(
		startTime = startTime,
		endTime = endTime,
		lessonName = this.lessonName ?: "No lesson name",
		lessonType = getLessonTypeByString(this.lessonType),
		auditory = this.auditory ?: "No auditory",
		teacher = this.teacher ?: "No teacher"
	)
}

fun getStartEndTimeByString(time: String?): Pair<LocalTime, LocalTime> {
	time?.let {
		val split = it.split('-')

		val startTime =
			LocalTime.of(
				split[0].split(':')[0].toIntOrNull() ?: return@let,
				split[0].split(':')[1].toIntOrNull() ?: return@let
			)
		val endTime =
			LocalTime.of(
				split[1].split(':')[0].toIntOrNull() ?: return@let,
				split[1].split(':')[1].toIntOrNull() ?: return@let
			)

		return Pair(startTime, endTime)
	}

	return Pair(LocalTime.MIN, LocalTime.MAX)
}


fun getLessonTypeByString(lessonType: String?): LessonType {
	return when (lessonType) {
		"Лекции", "Лекция" -> LessonType.Lecture

		"Практические занятия", "Практика" -> LessonType.Practice

		"Внеучебное мероприятие" -> LessonType.ExtracurricularActivity

		else -> LessonType.Other
	}
}

private fun getDayOfWeekByString(day: String): DayOfWeek {
	return when (day) {
		"monday" -> DayOfWeek.MONDAY

		"tuesday" -> DayOfWeek.TUESDAY

		"wednesday" -> DayOfWeek.WEDNESDAY

		"thursday" -> DayOfWeek.THURSDAY

		"friday" -> DayOfWeek.FRIDAY

		"saturday" -> DayOfWeek.SATURDAY

		else -> DayOfWeek.SUNDAY
	}
}