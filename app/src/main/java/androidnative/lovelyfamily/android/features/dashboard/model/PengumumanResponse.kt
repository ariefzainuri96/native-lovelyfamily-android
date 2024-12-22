package androidnative.lovelyfamily.android.features.dashboard.model

data class PengumumanResponse (
    val status: Long? = null,
    val message: String? = null,
    val data: List<PengumumanData>? = null
)

data class PengumumanData (
    val id: Long? = null,
    val title: String? = null,
    val date: String? = null,
    val pengumuman: String? = null
)
