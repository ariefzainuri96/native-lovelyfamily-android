package androidnative.lovelyfamily.android.pages.dashboard

import android.app.Application
import androidnative.lovelyfamily.android.R
import androidnative.lovelyfamily.android.features.dashboard.DashboardRepository
import androidnative.lovelyfamily.android.features.dashboard.model.MenuModel
import androidnative.lovelyfamily.android.features.dashboard.model.PengumumanData
import androidnative.lovelyfamily.android.utils.PreferenceKeys
import androidnative.lovelyfamily.android.utils.RequestState
import androidnative.lovelyfamily.android.utils.dataStore
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor (
    private val repository: DashboardRepository,
    private val application: Application
): ViewModel() {
    var news = MutableStateFlow(listOf<PengumumanData>()); private set
    var newsState = MutableStateFlow(RequestState.IDLE); private set
    var logoutState = MutableStateFlow(RequestState.IDLE); private set

    val menus = listOf<MenuModel>(
        MenuModel("Manajemen Inventaris", R.drawable.ic_manajemen_inventaris, application.applicationContext.getColor(R.color.blue2)),
        MenuModel("Manajemen Barang Pakai Habis", R.drawable.manajemen_barang_habis_pakai, application.applicationContext.getColor(R.color.purple2)),
        MenuModel("Manajemen Aset", R.drawable.manajemen_aset, application.applicationContext.getColor(R.color.blue3)),
        MenuModel("Task Approval", R.drawable.ic_check_square, application.applicationContext.getColor(R.color.green)),
    )
    
    fun logout() {
        viewModelScope.launch {
            application.applicationContext.dataStore.edit { preferences ->
                preferences[PreferenceKeys.Companion.IS_LOGIN] = false
            }

            logoutState.value = RequestState.SUCCESS
        }
    }

    fun getNews() {
        val handler = CoroutineExceptionHandler {_, throwable ->
            println("Error happening: $throwable")

            newsState.value = RequestState.ERROR
        }

        newsState.value = RequestState.LOADING

        viewModelScope.launch(handler) {
            withContext(Dispatchers.IO) {
                val response = (async { repository.getPengumuman() }).await()

                response.body()?.data?.let { news.value = it }

                newsState.value = if (response.isSuccessful) RequestState.SUCCESS else RequestState.ERROR
            }
        }
    }

    init {
        getNews()
    }
}