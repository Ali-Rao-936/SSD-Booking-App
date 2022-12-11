package com.ss.delivery.booking.garage.presenter.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ss.delivery.booking.garage.R
import com.ss.delivery.booking.garage.databinding.ActivityTandCactivityBinding
import com.ss.delivery.booking.garage.utils.Utils

class TandCActivity : AppCompatActivity() {

    lateinit var binding : ActivityTandCactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      binding = DataBindingUtil.setContentView(this, R.layout.activity_tand_cactivity)

        binding.ivBackT.setOnClickListener {
            onBackPressed()
        }

        binding.btnAccept.setOnClickListener {
            if (binding.cbTC.isChecked){
                startActivity(Intent(this, TandCActivity::class.java))
            }else
                Utils.showSnack(getString(R.string.please_accept), binding.root)
        }
    }
}