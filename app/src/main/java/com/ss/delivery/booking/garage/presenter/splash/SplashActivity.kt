package com.ss.delivery.booking.garage.presenter.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ss.delivery.booking.garage.R
import com.ss.delivery.booking.garage.presenter.home.SelectDateActivity
import com.ss.delivery.booking.garage.presenter.login.LoginActivity
import com.ss.delivery.booking.garage.utils.Constants
import com.ss.delivery.booking.garage.utils.SharedPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(4000L)
            if (SharedPreferences.getBooleanValueFromPreference(Constants.LoginStatus, this@SplashActivity))
                startActivity(Intent(this@SplashActivity, SelectDateActivity::class.java))
            else
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))

            finish()
        }

    }
}