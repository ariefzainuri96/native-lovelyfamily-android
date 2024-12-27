package androidnative.lovelyfamily.android.data.remote

import androidnative.lovelyfamily.android.features.dashboard.model.PengumumanResponse
import androidnative.lovelyfamily.android.features.login.model.LoginFormModel
import androidnative.lovelyfamily.android.features.login.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MyApi {
    @POST("users/login")
    suspend fun login(@Body form: LoginFormModel): Response<LoginResponse>

    @GET("pengumuman")
    suspend fun getPengumuman(): Response<PengumumanResponse>
}