package com.ss.delivery.booking.garage.presenter.settings

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.ss.delivery.booking.garage.databinding.ActivitySupportBinding


class SupportActivity : AppCompatActivity() {

    lateinit var binding: ActivitySupportBinding
    private val code = 1122

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            com.ss.delivery.booking.garage.R.layout.activity_support
        )

        binding.ivBackSupport.setOnClickListener {
            onBackPressed()
        }

        binding.ivCall1.setOnClickListener {
            makeCall("+971542043616")
        }
        binding.ivCall12.setOnClickListener {
            makeCall("+971504770024")
        }
        binding.ivCall3.setOnClickListener {
            makeCall("+971549955102")
        }
        binding.ivCall4.setOnClickListener {
            makeCall("+971529777073")
        }

        // whatsApp
        binding.ivCallWhatsApp1.setOnClickListener {
            openWhatsappContact("+971542043616")
        }
        binding.ivCallWhatsApp2.setOnClickListener {
            openWhatsappContact("+971504770024")
        }
        binding.ivCallWhatsApp3.setOnClickListener {
            openWhatsappContact("+971549955102")
        }
        binding.ivCallWhatsApp4.setOnClickListener {
            openWhatsappContact("+971529777073")
        }

    }

    private fun makeCall(phone: String) {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.CALL_PHONE),
                code
            )

        } else {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone"))
            startActivity(intent)
        }
    }

    private fun sendMessage() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello,")
        sendIntent.type = "text/plain"
        sendIntent.setPackage("com.whatsapp")
        startActivity(sendIntent)


    }

    private fun openWhatsappContact(number: String) {
        val uri = Uri.parse("smsto:$number")
        val i = Intent(Intent.ACTION_SENDTO, uri)
        i.setPackage("com.whatsapp")
        startActivity(Intent.createChooser(i, ""))
    }
}
