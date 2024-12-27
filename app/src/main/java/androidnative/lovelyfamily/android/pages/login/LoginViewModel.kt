package androidnative.lovelyfamily.android.pages.login

import android.app.Application
import androidnative.lovelyfamily.android.features.login.LoginRepository
import androidnative.lovelyfamily.android.features.login.model.LoginFormModel
import androidnative.lovelyfamily.android.shared.model.FormErrorModel
import androidnative.lovelyfamily.android.utils.PreferenceKeys
import androidnative.lovelyfamily.android.utils.RequestState
import androidnative.lovelyfamily.android.utils.Utils.Companion.checkAllProperties
import androidnative.lovelyfamily.android.utils.dataStore
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val application: Application
) : ViewModel() {
    var loginForm = MutableStateFlow(LoginFormModel()); private set
    var errorLoginForm = MutableStateFlow(listOf<FormErrorModel>()); private set
    var loginState = MutableStateFlow(RequestState.IDLE); private set

    val loginHandler = CoroutineExceptionHandler { _, throwable ->
        println("Error happening, Error: $throwable")

        loginState.value = RequestState.ERROR
    }

    fun isLoginEnable(): Boolean {
        errorLoginForm.value = loginForm.value.checkAllProperties()

        return errorLoginForm.value.isEmpty()
    }

    fun updateLoginForm(update: LoginFormModel.() -> LoginFormModel) {
        loginForm.update {
            it.update()
        }
    }

    fun login() {
        loginState.value = RequestState.LOADING

        viewModelScope.launch(loginHandler) {
            val response = async { repository.login(loginForm.value) }.await()

            loginState.value = if (response.isSuccessful) RequestState.SUCCESS else RequestState.ERROR

            if (response.isSuccessful) {
                application.applicationContext.dataStore.edit { preferences ->
                    preferences[PreferenceKeys.Companion.IS_LOGIN] = true
                }
            }
        }
    }
}