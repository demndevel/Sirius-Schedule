package sirius.schedule.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sirius.schedule.core.models.DailySchedule

@Composable
fun DailySchedule(
	dailySchedule: DailySchedule,
	modifier: Modifier = Modifier
) {
	LazyColumn(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		items(dailySchedule.lessons) { lesson ->
			Lesson(
				lesson = lesson,
				modifier = Modifier.fillMaxWidth()
			)
		}
	}
}