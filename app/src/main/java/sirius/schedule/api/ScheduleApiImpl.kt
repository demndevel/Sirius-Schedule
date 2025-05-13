package sirius.schedule.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import sirius.schedule.core.models.Group
import sirius.schedule.core.models.WeeklySchedule

class ScheduleApiImpl(
	private val apiUrl: String
) : ScheduleApi {
	private val json = Json {
		ignoreUnknownKeys = true
		coerceInputValues = true
	}

	private val httpClient = HttpClient(OkHttp)

	// https://api.eralas.ru/api/groups
	override suspend fun getGroups(): List<Group> {
		val response = httpClient.get("$apiUrl/groups")

		val body = response.bodyAsText()

		val result = json.decodeFromString<List<String>>(body)

		return result.map { Group(it) }
	}

	// https://api.eralas.ru/api/schedule?group=<НОМЕР ГРУППЫ>&week=<НЕДЕЛЯ>
	// https://api.eralas.ru/api/schedule?group=<НОМЕР ГРУППЫ>
	override suspend fun getScheduleByGroup(group: Group): WeeklySchedule {
		val response = httpClient.get("$apiUrl/schedule?group=${group.code}")
		val body = response.bodyAsText()

		val result = json.decodeFromString<List<ScheduleItem>>(body)

		return result.toWeeklySchedule(group)
	}
}
