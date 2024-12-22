package androidnative.lovelyfamily.android.features.login

import retrofit2.Response
import androidnative.lovelyfamily.android.data.remote.MyApi
import androidnative.lovelyfamily.android.features.login.model.LoginFormModel

class LoginRepositoryImpl(private val api: MyApi): LoginRepository {
    override suspend fun login(form: LoginFormModel): Response<String> = api.login(form)
}

interface LoginRepository {
    suspend fun login(form: LoginFormModel): Response<String>
}