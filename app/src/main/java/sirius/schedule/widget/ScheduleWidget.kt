package sirius.schedule.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import kotlinx.coroutines.flow.MutableStateFlow
import sirius.schedule.R
import sirius.schedule.core.models.LessonType
import sirius.schedule.ui.theme.DarkScheduleColors
import sirius.schedule.ui.theme.LightScheduleColors
import sirius.schedule.ui.theme.ScheduleColors
import sirius.schedule.usecase.ClosestLesson
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.glance.Button
import androidx.glance.LocalContext
import androidx.glance.layout.height
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import sirius.schedule.usecase.GetClosestLessonUseCase
import java.time.LocalDateTime


class ScheduleWidget : GlanceAppWidget(), KoinComponent {

	val useCase by inject<GetClosestLessonUseCase>()

	override suspend fun provideGlance(context: Context, id: GlanceId) {
		val widgetState = MutableStateFlow<ClosestLesson?>(null)

		provideContent {
			val closestLesson by widgetState.collectAsState()
			val scope = rememberCoroutineScope()
			val darkTheme: Boolean = false

			val update: () -> Unit = remember {
				{
					scope.launch {
						widgetState.value = useCase.invoke(LocalDateTime.now())
					}
				}
			}

			LaunchedEffect(Unit) {
				update()
			}

			WidgetContent(
				closestLesson = closestLesson,
				onUpdate = update,
				scheduleColors = if (darkTheme) DarkScheduleColors else LightScheduleColors
			)
		}
	}

	companion object {
		private val HORIZONTAL_RECTANGLE = DpSize(250.dp, 100.dp)
	}

	override val sizeMode = SizeMode.Responsive(
		setOf(
			HORIZONTAL_RECTANGLE,
		)
	)

	val headerTextStyle = TextStyle(
		fontWeight = FontWeight.Bold,
		fontSize = 24.sp,
	)
	val timeTextStyle = TextStyle(
		fontWeight = FontWeight.Bold,
		fontSize = 16.sp,
	)
	val bodyTextStyle = TextStyle(
		fontWeight = FontWeight.Bold,
		fontSize = 18.sp,
	)

	@Composable
	private fun WidgetContent(
		closestLesson: ClosestLesson?,
		scheduleColors: ScheduleColors,
		onUpdate: () -> Unit,
	) {
		Column(
			modifier = GlanceModifier.fillMaxSize(),
			verticalAlignment = Alignment.Top,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			WidgetLesson(
				colors = scheduleColors,
				closestLesson = closestLesson,
				onUpdate = onUpdate,
				lessonDate = LocalDate.now(),
				modifier = GlanceModifier
			)
		}
	}

	@Composable
	private fun WidgetLesson(
		colors: ScheduleColors,
		closestLesson: ClosestLesson?,
		lessonDate: LocalDate,
		onUpdate: () -> Unit,
		modifier: GlanceModifier = GlanceModifier,
	) {
		val currentDateString = lessonDate.format(DateTimeFormatter.ofPattern("dd.MM"))
		val context = LocalContext.current

		val mainBackground =
			if (closestLesson == null) GlanceTheme.colors.background.getColor(context)
			else getBackgroundByLessonType(colors, closestLesson.lesson.lessonType)

		Column(
			modifier = modifier
				.padding(horizontal = 8.dp, vertical = 10.dp)
				.background(mainBackground),
		) {
			if (closestLesson == null) {
				NoLessonContent(
					currentDateString = currentDateString,
					onUpdate = onUpdate,
				)
			} else {
				HasLessonContent(
					currentDateString = currentDateString,
					closestLesson = closestLesson,
					onUpdate = onUpdate,
					modifier = GlanceModifier.fillMaxWidth()
				)
			}
		}
	}

	@Composable
	fun NoLessonContent(
		currentDateString: String,
		onUpdate: () -> Unit,
		modifier: GlanceModifier = GlanceModifier,
	) {
		Column(
			verticalAlignment = Alignment.CenterVertically,
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = modifier
				.padding(
					vertical = 24.dp,
					horizontal = 12.dp,
				)
		) {
			Text(
				text = "No lessons for $currentDateString",
				style = TextStyle(
					color = GlanceTheme.colors.secondary,
					fontSize = 30.sp,
					fontWeight = FontWeight.Normal,
				)
			)

			Spacer(GlanceModifier.height(8.dp))

			Button(
				onClick = onUpdate,
				text = "Refresh",
				modifier = GlanceModifier,
			)
		}
	}

	@Composable
	private fun HasLessonContent(
		currentDateString: String?,
		closestLesson: ClosestLesson,
		onUpdate: () -> Unit,
		modifier: GlanceModifier = GlanceModifier,
	) {
		Column(
			modifier = modifier,
		) {
			Row(GlanceModifier.fillMaxWidth()) {
				Column {
					Text(
						text = "Pending lesson, $currentDateString",
						style = headerTextStyle,
					)

					val timeRange =
						"${closestLesson.lesson.startTime.format(DateTimeFormatter.ofPattern("hh:mm"))} – ${
							closestLesson.lesson.endTime.format(
								DateTimeFormatter.ofPattern("hh:mm")
							)
						}"

					Text(
						text = timeRange,
						style = timeTextStyle,
					)
				}

				Spacer(
					GlanceModifier
						.defaultWeight()
				)

				CircleIconButton(
					imageProvider = ImageProvider(R.drawable.ic_refresh),
					contentDescription = null,
					onClick = onUpdate,
					backgroundColor = GlanceTheme.colors.widgetBackground,
					modifier = GlanceModifier,
				)
			}

			Text(
				text = closestLesson.lesson.lessonName,
				style = bodyTextStyle,
				modifier = GlanceModifier,
			)
			Row(GlanceModifier.fillMaxWidth()) {
				Text(
					text = closestLesson.lesson.teacher,
					style = bodyTextStyle.copy(fontWeight = FontWeight.Medium),
					modifier = GlanceModifier
				)

				Spacer(GlanceModifier.defaultWeight())

				Text(
					text = closestLesson.lesson.auditory,
					style = bodyTextStyle,
				)
			}
		}
	}

	@Composable
	private fun getBackgroundByLessonType(colors: ScheduleColors, lessonType: LessonType): Color {
		return when (lessonType) {
			LessonType.Practice -> colors.practiceContainerColor

			LessonType.Lecture -> colors.lectureContainerColor

			LessonType.ExtracurricularActivity -> colors.extracurricularContainerColor

			else -> colors.otherContainerColor
		}
	}
}
