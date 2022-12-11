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
import com.ss.delivery.booking.garage.data.model.Rider
import com.ss.delivery.booking.garage.databinding.ActivityLoginBinding
import com.ss.delivery.booking.garage.presenter.home.SelectDateActivity
import com.ss.delivery.booking.garage.presenter.splash.SplashActivity
import com.ss.delivery.booking.garage.utils.Constants
import com.ss.delivery.booking.garage.utils.SharedPreferences
import com.ss.delivery.booking.garage.utils.Utils

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var myRef: DatabaseReference
    var ridersList = ArrayList<Rider>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.txtLanguageName.paint?.isUnderlineText = true

        if (Utils.getLocale(this) == Constants.URDU){
            binding.txtLanguageName.text = "Eng"
        }else
            binding.txtLanguageName.text = "URDU"

        myRef = Firebase.database.getReference(Constants.RiderTable)
        getRecord()


        binding.txtLanguageName.setOnClickListener {
            if ( binding.txtLanguageName.text == "Eng"){
                Utils.setLocale(this, Constants.ENGLISH)
                startActivity(Intent(this, SplashActivity::class.java))
                finishAffinity()
            }else{
                Utils.setLocale(this, Constants.URDU)
                startActivity(Intent(this, SplashActivity::class.java))
                finishAffinity()
            }
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, SelectDateActivity::class.java))
        }

        binding.txtSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            if (binding.etLicenseNo.text.trim().toString().isEmpty())
                Utils.showSnack(getString(R.string.please_enter_license), binding.root)
            else if (binding.etPassword.text.trim().toString().isEmpty())
                Utils.showSnack(getString(R.string.please_enter_password), binding.root)
            else {

                val license : String = binding.etLicenseNo.text.trim().toString()
                val pwd : String = binding.etPassword.text.trim().toString()

                if (ridersList.isEmpty())
                    Utils.showSnack(getString(R.string.no_record), binding.root)
                else{
                    for (item in ridersList){
                        if (item.License_Number == license && item.Password == pwd){
                            SharedPreferences.saveStringToPreferences(Constants.RiderName, item.Full_Name, this@LoginActivity)
                                   SharedPreferences.saveStringToPreferences(Constants.PlateNumber, item.Plate_Number, this@LoginActivity)
                                   SharedPreferences.saveStringToPreferences(Constants.PhoneNumber, item.Mobile_Number, this@LoginActivity)
                                   SharedPreferences.saveStringToPreferences(Constants.DrivingLicense,item.License_Number , this@LoginActivity)
                                    SharedPreferences.saveBooleanToPreferences(Constants.LoginStatus, true, this@LoginActivity)
                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            SelectDateActivity::class.java
                                        )
                                    )
                            finish()
                            return@setOnClickListener
                        }
                    }
                    Utils.showSnack(getString(R.string.no_record), binding.root)
                }

//                myRef.child(binding.etLicenseNo.text.trim().toString())
//                    .addListenerForSingleValueEvent(object : ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            if (snapshot.exists()) {
//                                val data = (snapshot.value as HashMap<String, *>)
//                                if (data["password"] == binding.etPassword.text.trim().toString()) {
//                                   SharedPreferences.saveStringToPreferences(Constants.RiderName, data["full_Name"].toString(), this@LoginActivity)
//                                   SharedPreferences.saveStringToPreferences(Constants.PlateNumber, data["plate_Number"].toString(), this@LoginActivity)
//                                   SharedPreferences.saveStringToPreferences(Constants.PhoneNumber, data["mobile_Number"].toString(), this@LoginActivity)
//                                   SharedPreferences.saveStringToPreferences(Constants.DrivingLicense,binding.etLicenseNo.text.trim().toString() , this@LoginActivity)
//                                    SharedPreferences.saveBooleanToPreferences(Constants.LoginStatus, true, this@LoginActivity)
//                                    startActivity(
//                                        Intent(
//                                            this@LoginActivity,
//                                            SelectDateActivity::class.java
//                                        )
//                                    )
//                                }
//
//                            } else
//                                Utils.showSnack(getString(R.string.no_record), binding.root)
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {
//                            Utils.showSnack(getString(R.string.something_went_wrong), binding.root)
//                            Log.d("QOO", " ${error.message}")
//                        }
//
//                    })
            }
        }

    }

    fun getRecord(){
        Firebase.database.getReference(Constants.RiderTable)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        ridersList.clear()
                        val rider =
                            (snapshot.value as ArrayList<*>).filterIsInstance<HashMap<String, *>>()
                                .map {
                                    Rider(it["full_Name"].toString(), it["father_Name"].toString(),it["rider_Id"].toString(),it["plate_Number"].toString(),
                                        it["license_Number"].toString(), it["mobile_Number"].toString(),it["password"].toString())
                                }
                        ridersList.addAll(rider)
                        Log.d(
                            "QOO",
                            " ........get data........ ${ridersList.size}"
                        )
                    } else {
                        Log.d("QOO", "  no riders records found")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("QOO", "  Get riders records got cancelled  ${error.message}")
                }

            })
    }


}