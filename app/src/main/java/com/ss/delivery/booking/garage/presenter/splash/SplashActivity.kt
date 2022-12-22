package com.ss.delivery.booking.garage.presenter.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ss.delivery.booking.garage.BuildConfig
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

    companion object{
        var isUpdateAvailable = false
    }

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

        Firebase.database.getReference(Constants.VersionTable).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val code : Int? = snapshot.getValue(Int::class.java)
                    if (code != null){
                    if (BuildConfig.VERSION_CODE < code){
                        isUpdateAvailable = true
                    }else
                        Log.d("Qoo", "version is equal")
                    }else
                        Log.d("Qoo", "version is null")
                }else
                    Log.d("Qoo", "version is not found")
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

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