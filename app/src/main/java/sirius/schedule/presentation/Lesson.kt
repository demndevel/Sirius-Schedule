package sirius.schedule.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import sirius.schedule.core.models.Lesson
import sirius.schedule.core.models.LessonType
import sirius.schedule.ui.theme.LocalScheduleColors

@Composable
fun Lesson(
	lesson: Lesson,
	modifier: Modifier = Modifier
) {
	Card(
		modifier = modifier,
		colors = getLessonCardColorsByType(lesson.lessonType)
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(8.dp),
			verticalArrangement = Arrangement.spacedBy(4.dp)
		) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically
			) {
				Column(Modifier) {
					Text(
						text = lesson.startTime.toString(),
						style = MaterialTheme.typography.titleMedium
					)
					Text(
						text = lesson.endTime.toString(),
						style = MaterialTheme.typography.titleMedium
					)
				}

				Spacer(Modifier.width(4.dp))

				VerticalDivider()

				Spacer(Modifier.width(4.dp))

				Column(Modifier) {
					Text(
						text = lesson.lessonName,
						style = MaterialTheme.typography.titleMedium,
						fontWeight = FontWeight.Bold,
						modifier = Modifier
					)

					Text(
						text = "${lesson.teacher} – ${lesson.auditory}",
						modifier = Modifier
					)
				}
			}
		}
	}
}

@Composable
fun getLessonCardColorsByType(lessonType: LessonType): CardColors {
	return when (lessonType) {
		LessonType.Practice -> CardDefaults
			.cardColors(containerColor = LocalScheduleColors.current.practiceContainerColor)

		LessonType.Lecture -> CardDefaults
			.cardColors(containerColor = LocalScheduleColors.current.lectureContainerColor)

		LessonType.ExtracurricularActivity -> CardDefaults
			.cardColors(containerColor = LocalScheduleColors.current.extracurricularContainerColor)

		else -> CardDefaults
			.cardColors(containerColor = LocalScheduleColors.current.otherContainerColor)
	}
}

