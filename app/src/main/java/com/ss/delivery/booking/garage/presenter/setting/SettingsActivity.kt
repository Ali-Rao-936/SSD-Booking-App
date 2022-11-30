package com.ss.delivery.booking.garage.presenter.setting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ss.delivery.booking.garage.R
import com.ss.delivery.booking.garage.databinding.ActivitySettingsBinding
import com.ss.delivery.booking.garage.presenter.login.LoginActivity
import com.ss.delivery.booking.garage.utils.Constants
import com.ss.delivery.booking.garage.utils.SharedPreferences

class SettingsActivity : AppCompatActivity() {

    lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)

        binding.btnLogout.setOnClickListener {
            SharedPreferences.saveBooleanToPreferences(Constants.LoginStatus, true, this)
            SharedPreferences.saveStringToPreferences(Constants.RiderName, "", this)
            SharedPreferences.saveStringToPreferences(Constants.DrivingLicense, "", this)

            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }


    }
}