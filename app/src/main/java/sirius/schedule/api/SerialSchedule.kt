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
	val dayWeek: String? = null,
	val startTime: String? = null,
	val endTime: String? = null,
	val discipline: String? = null,
	val groupType: String? = null,
	val address: String? = null,
	val classroom: String? = null,
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
