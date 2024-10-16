package sirius.schedule.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import sirius.schedule.core.models.Group

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenTopBar(
	currentGroup: Group?,
	groups: List<Group>,
	onGroupPick: (Group) -> Unit,
	onQuestionMarkClick: () -> Unit,
	modifier: Modifier = Modifier
) {
	var dropdownVisible by remember { mutableStateOf(false) }

	CenterAlignedTopAppBar(
		title = {
			Text(
				text = currentGroup?.code ?: "No group picked",
				modifier = Modifier
					.clickable {
						dropdownVisible = !dropdownVisible
					}
			)

			DropdownMenu(
				expanded = dropdownVisible,
				onDismissRequest = {
					dropdownVisible = false
				}
			) {
				groups.forEach { group ->
					DropdownMenuItem(
						text = { Text(group.code) },
						onClick = {
							onGroupPick(group)
							dropdownVisible = false
						},
					)
				}
			}
		},
		actions = {
			IconButton(
				onClick = onQuestionMarkClick
			) {
				Icon(
					Icons.Rounded.Favorite,
					null
				)
			}
		},
		modifier = modifier
	)
}

@Preview
@Composable
fun MainScreenTopBarPreview() {
	Scaffold(
		topBar = {
			MainScreenTopBar(
				currentGroup = Group("К0101-23/3"),
				groups = emptyList(),
				{},
				{}
			)
		}
	) {
		Box(Modifier.padding(it))
	}
}
