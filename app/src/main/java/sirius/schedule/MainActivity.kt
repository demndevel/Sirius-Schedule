package sirius.schedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import sirius.schedule.api.ScheduleApi
import sirius.schedule.api.ScheduleApiImpl
import sirius.schedule.core.models.Group
import sirius.schedule.presentation.MainScreen
import sirius.schedule.presentation.MainScreenViewModel
import sirius.schedule.ui.theme.SiriusScheduleTheme

val mainModule = module {
	factory<ScheduleApi> { ScheduleApiImpl() }
	factory<MainScreenViewModel> { MainScreenViewModel(get()) }
}

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		enableEdgeToEdge()

		if (GlobalContext.getKoinApplicationOrNull() == null) {
			startKoin {
				modules(mainModule)
			}
		}

		val api = ScheduleApiImpl()

		lifecycleScope.launch {
			val groups = api.getGroups()
			val schedule = api.getScheduleByGroup(Group("К0609-23"))
			println(schedule.group)
			schedule.days.forEach {
				println(it.dayOfWeek)
				println("lessons:")

				it.lessons.forEach { lesson ->
					println(lesson.lessonName)
					println(lesson.lessonType)
					println(lesson.auditory)
					println(lesson.startTime)
					println(lesson.endTime)
					println(lesson.teacher)
				}
			}
		}

		setContent {
			SiriusScheduleTheme {
				MainScreen(Modifier.fillMaxSize())
			}
		}
	}
}