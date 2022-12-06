package com.ss.delivery.booking.garage.presenter.settings

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
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


        // go back
        binding.ivBackSettings.setOnClickListener {
            onBackPressed()
        }

        // profile
        binding.llProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        // intro
        binding.llIntro.setOnClickListener {

        }
        // support
        binding.llSupport.setOnClickListener {

        }
        // terms and conditions
        binding.llTAndC.setOnClickListener {
            startActivity(Intent(this, TandCActivity::class.java))

        }
        // logout
        binding.llLogout.setOnClickListener {
         showAlert()
        }
    }

    private fun showAlert() {
        val dialog = Dialog(this, android.R.style.ThemeOverlay)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.info_dialog_layout)

        //  initializing dialog screen
        val txtMessage: TextView = dialog.findViewById(R.id.txtMessage)
        val ivClose: ImageView = dialog.findViewById(R.id.ivCancelPopUp)
        val btnOk: AppCompatButton = dialog.findViewById(R.id.btnOk)

        ivClose.visibility = View.VISIBLE

        txtMessage.text =
            "Are  you  sure  you  want  to  Logout?"


        ivClose.setOnClickListener {
            dialog.dismiss()
        }

        btnOk.setOnClickListener {
            SharedPreferences.saveBooleanToPreferences(Constants.LoginStatus, false, this)
            SharedPreferences.saveStringToPreferences(Constants.RiderName, "", this)
            SharedPreferences.saveStringToPreferences(Constants.DrivingLicense, "", this)

            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }

        dialog.show()
    }
}