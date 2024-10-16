package sirius.schedule.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
	primary = Purple80,
	secondary = PurpleGrey80,
	tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
	primary = Purple40,
	secondary = PurpleGrey40,
	tertiary = Pink40
)

data class ScheduleColors(
	val lectureContainerColor: Color,
	val practiceContainerColor: Color,
	val otherContainerColor: Color,
	val extracurricularContainerColor: Color
)

val LightScheduleColors = ScheduleColors(
	lectureContainerColor = Color(0xFFcdeda3),
	practiceContainerColor = Color(0xFFd6e3ff),
	otherContainerColor = Color(0xFFfad8fd),
	extracurricularContainerColor = Color(0xFFf8e287),
)

val DarkScheduleColors = ScheduleColors(
	lectureContainerColor = Color(0xFF354e16),
	practiceContainerColor = Color(0xFF284777),
	otherContainerColor = Color(0xFF573e5c),
	extracurricularContainerColor = Color(0xFF534600),
)

val LocalScheduleColors = compositionLocalOf { LightScheduleColors }

@Composable
fun SiriusScheduleTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	dynamicColor: Boolean = true,
	content: @Composable () -> Unit
) {
	val colorScheme = when {
		dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
			val context = LocalContext.current
			if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
		}

		darkTheme -> DarkColorScheme
		else -> LightColorScheme
	}

	val scheduleColors = if (darkTheme) DarkScheduleColors else LightScheduleColors

	CompositionLocalProvider(LocalScheduleColors provides scheduleColors) {
		MaterialTheme(
			colorScheme = colorScheme,
			typography = Typography,
			content = content
		)
	}
}