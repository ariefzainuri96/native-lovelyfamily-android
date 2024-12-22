package androidnative.lovelyfamily.android.features.dashboard
import androidnative.lovelyfamily.android.data.remote.MyApi
import androidnative.lovelyfamily.android.features.dashboard.model.PengumumanResponse
import retrofit2.Response

interface DashboardRepository {
    suspend fun getPengumuman(): Response<PengumumanResponse>
}

class DashboardRepositoryImpl(
    private val api: MyApi
) : DashboardRepository {
    override suspend fun getPengumuman(): Response<PengumumanResponse> = api.getPengumuman()
}