package androidnative.lovelyfamily.android.pages.login

import android.app.Application
import androidnative.lovelyfamily.android.features.login.LoginRepository
import androidnative.lovelyfamily.android.features.login.model.LoginFormModel
import androidnative.lovelyfamily.android.utils.PreferenceKeys
import androidnative.lovelyfamily.android.utils.RequestState
import androidnative.lovelyfamily.android.utils.dataStore
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val application: Application
): ViewModel() {
    var loginForm = MutableStateFlow(LoginFormModel()); private set
    var loginState = MutableSharedFlow<RequestState>(); private set

    val loginHandler = CoroutineExceptionHandler { _, throwable ->
        println("Error happening, Error: $throwable")
    }

    fun updateLoginForm(update: LoginFormModel.() -> LoginFormModel) {
        loginForm.update {
            it.update()
        }
    }

    fun login() {
        viewModelScope.launch(loginHandler) {
            loginState.emit(RequestState.LOADING)

            delay(1000L)

            application.applicationContext.dataStore.edit { preferences ->
                preferences[PreferenceKeys.Companion.IS_LOGIN] = true
            }

            loginState.emit(RequestState.SUCCESS)
        }
    }
}