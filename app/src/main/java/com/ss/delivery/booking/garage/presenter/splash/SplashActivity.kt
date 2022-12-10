package com.ss.delivery.booking.garage.presenter.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ss.delivery.booking.garage.R
import com.ss.delivery.booking.garage.presenter.home.SelectDateActivity
import com.ss.delivery.booking.garage.presenter.login.LoginActivity
import com.ss.delivery.booking.garage.utils.Constants
import com.ss.delivery.booking.garage.utils.SharedPreferences
import com.ss.delivery.booking.garage.utils.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // your language
        val languageToLoad = Utils.getLocale(this)
        val config = applicationContext.resources.configuration
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        config.setLocale(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            applicationContext.createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(4000L)
            if (SharedPreferences.getBooleanValueFromPreference(
                    Constants.LoginStatus,
                    this@SplashActivity
                )
            )
                startActivity(Intent(this@SplashActivity, SelectDateActivity::class.java))
            else
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))

            finish()
        }

    }
}