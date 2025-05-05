package sirius.schedule.api

import sirius.schedule.core.models.DailySchedule
import sirius.schedule.core.models.Group
import sirius.schedule.core.models.Lesson
import sirius.schedule.core.models.LessonType
import sirius.schedule.core.models.WeeklySchedule
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun List<ScheduleItem>.toWeeklySchedule(group: Group): WeeklySchedule {
	val days: Map<String, List<ScheduleItem>> = this.groupBy {
		it.date
	}

	val domainDays = days.map { (date: String, lessons: List<ScheduleItem>) ->
		val domainLessons: List<Lesson> = lessons
			.map(ScheduleItem::toLesson)
			.sortedBy { it.startTime }
		val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
		val localDate = LocalDate.parse(date, formatter)

		DailySchedule(
			dayOfWeek = localDate.dayOfWeek,
			lessons = domainLessons,
		)
	}

	return WeeklySchedule(
		group = group,
		days = domainDays,
	)
}

fun ScheduleItem.toLesson(): Lesson {
	val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
	val parsedStartTime = LocalTime.parse(startTime, timeFormatter)
	val parsedEndTime = LocalTime.parse(endTime, timeFormatter)

	val lessonType = getLessonTypeByString(groupType)

	val teacherName = if (teachers.isNotEmpty()) {
		teachers.values.first().fio
	} else {
		"N/A"
	}

	val auditoryInfo = buildString {
		if (address.isNotBlank() && address != "Основной") {
			append(address)
			append(", ")
		}
		append(classroom)
	}

	return Lesson(
		startTime = parsedStartTime,
		endTime = parsedEndTime,
		lessonName = discipline,
		lessonType = lessonType,
		auditory = auditoryInfo,
		teacher = teacherName
	)
}

fun getLessonTypeByString(lessonType: String?): LessonType {
	return when (lessonType) {
		"Лекции", "Лекция" -> LessonType.Lecture

		"Практические занятия", "Практика" -> LessonType.Practice

		"Внеучебное мероприятие" -> LessonType.ExtracurricularActivity

		else -> LessonType.Other
	}
}
