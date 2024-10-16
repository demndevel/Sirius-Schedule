package sirius.schedule.core.models

import java.time.DayOfWeek

data class WeeklySchedule(
	val group: Group,
	val days: List<DailySchedule>
)

data class DailySchedule(
	val dayOfWeek: DayOfWeek,
	val lessons: List<Lesson>
)