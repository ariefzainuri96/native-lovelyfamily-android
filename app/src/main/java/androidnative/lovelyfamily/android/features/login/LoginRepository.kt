package androidnative.lovelyfamily.android.features.login

import androidnative.lovelyfamily.android.data.remote.MyApi
import androidnative.lovelyfamily.android.features.login.model.LoginFormModel
import androidnative.lovelyfamily.android.features.login.model.LoginResponse
import retrofit2.Response

interface LoginRepository {
    suspend fun login(form: LoginFormModel): Response<LoginResponse>
}

class LoginRepositoryImpl(private val api: MyApi): LoginRepository {
    override suspend fun login(form: LoginFormModel): Response<LoginResponse> = api.login(form)
}