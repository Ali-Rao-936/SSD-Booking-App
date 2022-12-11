package com.ss.delivery.booking.garage.presenter.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ss.delivery.booking.garage.R
import com.ss.delivery.booking.garage.databinding.ActivitySupportBinding

class SupportActivity : AppCompatActivity() {

    lateinit var binding : ActivitySupportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      binding = DataBindingUtil.setContentView(this, R.layout.activity_support)

        binding.ivBackSupport.setOnClickListener {
            onBackPressed()
        }


    }
}