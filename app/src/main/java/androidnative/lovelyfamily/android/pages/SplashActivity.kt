package androidnative.lovelyfamily.android.pages

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidnative.lovelyfamily.android.R
import androidnative.lovelyfamily.android.pages.dashboard.DashboardActivity
import androidnative.lovelyfamily.android.pages.login.LoginActivity
import androidnative.lovelyfamily.android.utils.isLoggedIn

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            isLoggedIn().collect {
                if (it == true) {
                    startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                }
            }
        }
    }
}