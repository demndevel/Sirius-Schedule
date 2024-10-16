package sirius.schedule.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SerializableSchedule(
	val group: String,
	val schedule: Map<String, List<SerializableLesson>>,
)

@Serializable
data class SerializableLesson(
	@SerialName("time") var time: String? = null,
	@SerialName("lesson_type") var lessonType: String? = null,
	@SerialName("lesson_name") var lessonName: String? = null,
	@SerialName("auditory") var auditory: String? = null,
	@SerialName("teacher") var teacher: String? = null,
)
