package sirius.schedule.api

import sirius.schedule.core.models.Group
import sirius.schedule.core.models.WeeklySchedule

interface ScheduleApi {
	suspend fun getGroups(): List<Group>

	suspend fun getScheduleByGroup(group: Group): WeeklySchedule
}