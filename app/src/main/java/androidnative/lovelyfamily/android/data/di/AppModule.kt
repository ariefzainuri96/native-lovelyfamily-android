package androidnative.lovelyfamily.android.data.di

import androidnative.lovelyfamily.android.data.remote.MyApi
import androidnative.lovelyfamily.android.features.dashboard.DashboardRepository
import androidnative.lovelyfamily.android.features.dashboard.DashboardRepositoryImpl
import androidnative.lovelyfamily.android.features.login.LoginRepository
import androidnative.lovelyfamily.android.features.login.LoginRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMyApi(): MyApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .callTimeout(5, TimeUnit.SECONDS)
            .build()

        // 192.168.124.187 -> local ip server -> open cmd -> run command "ipconfig"
        // 9000 -> running port
        // update network_security_config.xml with local ip
        return Retrofit.Builder()
            .baseUrl("http://192.168.165.161:9000/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyApi::class.java)
    }

    @Provides
    @Singleton
    fun providedDashboardRepository(api: MyApi): DashboardRepository {
        return DashboardRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun providedLoginRepository(api: MyApi): LoginRepository {
        return LoginRepositoryImpl(api)
    }
}