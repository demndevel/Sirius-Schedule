package sirius.schedule.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SerializableSchedule(
	val lessons: List<ScheduleItem>,
)

@Serializable
data class ScheduleItem(
	val date: String,
	val dayWeek: String,
	val startTime: String,
	val endTime: String,
	val discipline: String,
	val groupType: String,
	val address: String,
	val classroom: String,
	val comment: String? = null,
	val place: String? = null,
	val teachers: Map<String, Teacher>,
	val urlOnline: String? = null,
	val group: String,
)

@Serializable
data class Teacher(
	val id: String,
	@SerialName("last_name") val lastName: String,
	@SerialName("first_name_one") val firstNameOne: String,
	@SerialName("first_name") val firstName: String,
	@SerialName("middle_name") val middleName: String,
	@SerialName("middle_name_one") val middleNameOne: String,
	val fio: String,
	@SerialName("department_fio") val departmentFio: String,
	val department: String
)
