package sirius.schedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import sirius.schedule.api.ScheduleApi
import sirius.schedule.api.ScheduleApiImpl
import sirius.schedule.cache.ScheduleCache
import sirius.schedule.cache.ScheduleCacheImpl
import sirius.schedule.core.models.Group
import sirius.schedule.presentation.MainScreen
import sirius.schedule.presentation.MainScreenViewModel
import sirius.schedule.ui.theme.SiriusScheduleTheme
import sirius.schedule.usecase.GetClosestLessonUseCase

val mainModule = module {
	factory<ScheduleApi> { ScheduleApiImpl("https://api.eralas.ru/api") }
	factory<GetClosestLessonUseCase> { GetClosestLessonUseCase(get(), get()) }
	factory<ScheduleCache> { ScheduleCacheImpl(get()) }
	factory<MainScreenViewModel> { MainScreenViewModel(get(), get()) }
}

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		enableEdgeToEdge()

		if (GlobalContext.getKoinApplicationOrNull() == null) {
			startKoin {
				androidContext(this@MainActivity)
				modules(mainModule)
			}
		}

		val api = ScheduleApiImpl("https://api.eralas.ru/api")

		setContent {
			SiriusScheduleTheme {
				MainScreen(Modifier.fillMaxSize())
			}
		}
	}
}
