package com.ss.delivery.booking.garage.presenter.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ss.delivery.booking.garage.R
import com.ss.delivery.booking.garage.data.model.Rider
import com.ss.delivery.booking.garage.databinding.ActivitySignUpBinding
import com.ss.delivery.booking.garage.utils.Utils.showSnack

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        binding.ivBackSignUp.setOnClickListener { 
            onBackPressed()
        }

        myRef = Firebase.database.getReference("RidersInfo")

        binding.btnSignUp.setOnClickListener {
            if (binding.etName.text.trim().toString().isEmpty())
                showSnack("PLease enter Name", binding.root)
            else if (binding.etPlateNo.text.trim().toString().isEmpty())
                showSnack("PLease enter Bike plate number", binding.root)
            else if (binding.etLicenseNo.text.trim().toString().isEmpty())
                showSnack("Please enter Driving License number", binding.root)
            else if (binding.etMobileNo.text.trim().toString().isEmpty())
                showSnack("Please enter Mobile number ", binding.root)
            else if (binding.etPassword.text.trim().toString().isEmpty())
                showSnack("Please enter Password", binding.root)
            else {
                val rider = Rider(
                    binding.etName.text.trim().toString(),
                    binding.etFatherN.text.trim().toString(),
                    binding.etRiderId.text.trim().toString(),
                    binding.etPlateNo.text.trim().toString(),
                    binding.etLicenseNo.text.trim().toString(),
                    binding.etMobileNo.text.trim().toString(),
                    binding.etPassword.text.trim().toString()
                )
                myRef.child(binding.etLicenseNo.text.trim().toString()).setValue(rider)
                    .addOnCompleteListener {
                        if (it.isSuccessful)
                            showSnack("You are Successfully Registered.", binding.root)
                        else
                            showSnack("Something goes wrong please try again..", binding.root)
                    }
            }
        }
    }
}