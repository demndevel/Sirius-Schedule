package sirius.schedule.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
	modifier: Modifier = Modifier,
	vm: MainScreenViewModel = koinViewModel(),
) {
	val state by vm.state.collectAsState()
	val context = LocalContext.current

	LaunchedEffect(Unit) {
		vm.load()
	}

	Scaffold(
		topBar = {
			MainScreenTopBar(
				currentGroup = state.currentGroup,
				groups = state.groups,
				onGroupPick = vm::updateCurrentGroup,
				onQuestionMarkClick = {
					Toast(context).apply {
						setText("meow")
						duration = Toast.LENGTH_SHORT
					}.show()
				}
			)
		},
		modifier = modifier
	) { innerPadding ->
		when (state) {
			is MainScreenUiState.Loading -> {
				Box(Modifier.fillMaxSize(), Alignment.Center) {
					CircularProgressIndicator(Modifier.size(32.dp))
				}
			}

			is MainScreenUiState.Success -> {
				val successState = state as MainScreenUiState.Success

				Column(Modifier.padding(innerPadding)) {
					MainScreenSuccessState(
						dayOfWeek = successState.dayOfWeek,
						weeklySchedule = successState.weeklySchedule,
						modifier = Modifier.fillMaxSize()
					)
				}
			}
		}
	}
}
