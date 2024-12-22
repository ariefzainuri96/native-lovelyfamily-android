package androidnative.lovelyfamily.android.utils

import androidx.datastore.preferences.core.booleanPreferencesKey

class PreferenceKeys {
    companion object {
        val IS_LOGIN = booleanPreferencesKey("is-login")
    }
}