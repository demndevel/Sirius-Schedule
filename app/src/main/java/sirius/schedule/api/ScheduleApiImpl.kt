package sirius.schedule.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import sirius.schedule.core.models.Group
import sirius.schedule.core.models.WeeklySchedule

class ScheduleApiImpl : ScheduleApi {
	companion object {
		const val apiUrl = "http://unit-lab.tech:8083"
	}

	private val json = Json {
		ignoreUnknownKeys = true
	}

	private val httpClient = HttpClient(OkHttp)

	override suspend fun getGroups(): List<Group> {
		val response = httpClient.get("$apiUrl/groups/get")

		val body = response.bodyAsText()

		val result = json.decodeFromString<List<String>>(body)

		return result.map { Group(it) }
	}

	override suspend fun getScheduleByGroup(group: Group): WeeklySchedule {
		val response = httpClient.get("$apiUrl/schedule/get?group=${group.code}")
		val body = response.bodyAsText()

		val result = json.decodeFromString<SerializableSchedule>(body)

		return result.toWeeklySchedule()
	}
}