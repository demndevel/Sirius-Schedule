package sirius.schedule.core.models

import java.time.DayOfWeek
import java.time.LocalDate

data class WeeklySchedule(
	val group: Group,
	val days: List<DailySchedule>
)

data class DailySchedule(
	val date: LocalDate,
	val lessons: List<Lesson>,
)
