package com.ss.delivery.booking.garage.presenter.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ss.delivery.booking.garage.R
import com.ss.delivery.booking.garage.databinding.ActivityLoginBinding
import com.ss.delivery.booking.garage.presenter.home.SelectDateActivity
import com.ss.delivery.booking.garage.utils.Constants
import com.ss.delivery.booking.garage.utils.SharedPreferences
import com.ss.delivery.booking.garage.utils.Utils

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        myRef = Firebase.database.getReference(Constants.RiderTable)

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, SelectDateActivity::class.java))
        }

        binding.txtSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            if (binding.etLicenseNo.text.trim().toString().isEmpty())
                Utils.showSnack("Please enter Driving License number ", binding.root)
            else if (binding.etPassword.text.trim().toString().isEmpty())
                Utils.showSnack("Please enter Password", binding.root)
            else {

                myRef.child(binding.etLicenseNo.text.trim().toString())
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                val data = (snapshot.value as HashMap<String, *>)
                                if (data["password"] == binding.etPassword.text.trim().toString()) {
                                   SharedPreferences.saveStringToPreferences(Constants.RiderName, data["full_Name"].toString(), this@LoginActivity)
                                   SharedPreferences.saveStringToPreferences(Constants.DrivingLicense,binding.etLicenseNo.text.trim().toString() , this@LoginActivity)
                                    SharedPreferences.saveBooleanToPreferences(Constants.LoginStatus, true, this@LoginActivity)
                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            SelectDateActivity::class.java
                                        )
                                    )
                                }

                            } else
                                Utils.showSnack("No record found.", binding.root)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Utils.showSnack("Something went wrong. Please try again.", binding.root)
                            Log.d("QOO", " ${error.message}")
                        }

                    })
            }
        }

    }
}