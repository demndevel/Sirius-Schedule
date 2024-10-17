package sirius.schedule.cache

import android.content.Context
import androidx.core.content.edit
import sirius.schedule.core.models.Group

class ScheduleCache(
	private val context: Context
) {
	companion object {
		private const val SHARED_PREF_NAME = "Schedule"
		private const val GROUP_KEY = "group"
	}

	private val preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

	fun getGroup(): Group? {
		preferences.getString(GROUP_KEY, null)?.let {
			return Group(it)
		}

		return null
	}

	fun saveGroup(group: Group) {
		preferences.edit {
			putString(GROUP_KEY, group.code)
			apply()
			commit()
		}
	}
}