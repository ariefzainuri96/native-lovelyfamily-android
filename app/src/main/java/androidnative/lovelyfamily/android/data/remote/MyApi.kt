package androidnative.lovelyfamily.android.data.remote

import androidnative.lovelyfamily.android.features.dashboard.model.PengumumanResponse
import androidnative.lovelyfamily.android.features.login.model.LoginFormModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MyApi {
    @POST("login")
    suspend fun login(@Body form: LoginFormModel): Response<String>

    @GET("pengumuman")
    suspend fun getPengumuman(): Response<PengumumanResponse>
}