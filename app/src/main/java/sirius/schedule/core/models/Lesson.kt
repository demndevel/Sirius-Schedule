package sirius.schedule.core.models

import java.time.LocalTime

enum class LessonType {
	Lecture,
	Practice,
	ExtracurricularActivity,
	Other
}

data class Lesson(
	val startTime: LocalTime,
	val endTime: LocalTime,
	val lessonName: String,
	val lessonType: LessonType = LessonType.Other,
	val auditory: String,
	val teacher: String,
)