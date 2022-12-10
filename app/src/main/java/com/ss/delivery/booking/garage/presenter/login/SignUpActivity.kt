package com.ss.delivery.booking.garage.presenter.login

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ss.delivery.booking.garage.R
import com.ss.delivery.booking.garage.data.model.BikeModel
import com.ss.delivery.booking.garage.data.model.Rider
import com.ss.delivery.booking.garage.databinding.ActivitySignUpBinding
import com.ss.delivery.booking.garage.presenter.home.SelectDateActivity
import com.ss.delivery.booking.garage.utils.Constants
import com.ss.delivery.booking.garage.utils.ImageChooseSheet
import com.ss.delivery.booking.garage.utils.SharedPreferences
import com.ss.delivery.booking.garage.utils.Utils.showSnack
import java.io.ByteArrayOutputStream

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    lateinit var myRef: DatabaseReference
    private val REQUEST_IMAGE_CAPTURE = 1
    val SELECT_FILE = 2
    var bikesRecordList = ArrayList<BikeModel>()
    var ridersList = ArrayList<Rider>()
    var bikePlatesList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        binding.ivBackSignUp.setOnClickListener {
            onBackPressed()
        }

        getBikeRecords()
        getRidersRecord()

        binding.ivEditProfile.setOnClickListener {
            val modalBottomSheet = ImageChooseSheet(this@SignUpActivity)
            modalBottomSheet.show(supportFragmentManager, ImageChooseSheet.TAG)
        }

        myRef = Firebase.database.getReference(Constants.RiderTable)

        binding.btnSignUp.setOnClickListener {
            if (binding.etName.text.trim().toString().isEmpty())
                showSnack(getString(R.string.please_enter_name), binding.root)
            else if (binding.etPlateNo.text.trim().toString().isEmpty())
                showSnack(getString(R.string.please_enter_plate), binding.root)
            else if (binding.etLicenseNo.text.trim().toString().isEmpty())
                showSnack(getString(R.string.please_enter_license), binding.root)
            else if (binding.etMobileNo.text.trim().toString().isEmpty())
                showSnack(getString(R.string.please_enter_phone), binding.root)
            else if (binding.etPassword.text.trim().toString().isEmpty())
                showSnack(getString(R.string.please_enter_password), binding.root)
            else {

                val bike = findBike(binding.etPlateNo.text.trim().toString())

                if (bike == "not found") {
                    val rider = Rider(
                        binding.etName.text.trim().toString(),
                        binding.etFatherN.text.trim().toString(),
                        binding.etRiderId.text.trim().toString(),
                        binding.etPlateNo.text.trim().toString(),
                        binding.etLicenseNo.text.trim().toString(),
                        binding.etMobileNo.text.trim().toString(),
                        binding.etPassword.text.trim().toString()
                    )
               //     ridersList.add(Rider("Ali", "Khalid","ali11","121212","12345","0586598884","12345678"))
                    ridersList.add(rider)
                    myRef.setValue(ridersList)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                showSnack(getString(R.string.registerd), binding.root)
                                startActivity(
                                    Intent(
                                        this,
                                        SelectDateActivity::class.java
                                    )
                                )
                                finishAffinity()
                            }
                            else
                                showSnack(
                                    getString(R.string.something_went_wrong),
                                    binding.root
                                )
                        }

                    return@setOnClickListener
                } else
                    showSnack(bike, binding.root)
            }
        }
    }

    private fun findBike(plate: String): String {
        if (bikesRecordList.isEmpty())
            return getString(R.string.no_record)
        else {

            for (item in bikesRecordList) {
                if (item.Bike_Number.toString() == plate)
                    return isBikeAlreadyRegistered(plate)
            }

            return getString(R.string.bike_register)
        }

    }

    private fun isBikeAlreadyRegistered(plate: String): String {
        return if (bikePlatesList.isEmpty())
            "not found"
        else {
            if (bikePlatesList.contains(plate))
                getString(R.string.alraedy_registered)
            else
                "not found"
        }
    }

    private fun getRidersRecord() {
        Firebase.database.getReference(Constants.RiderTable)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        ridersList.clear()
                        bikePlatesList.clear()
                        val rider =
                            (snapshot.value as ArrayList<*>).filterIsInstance<HashMap<String, *>>()
                                .map {
                                    Rider(it["full_Name"].toString(), it["father_Name"].toString(),it["rider_Id"].toString(),it["plate_Number"].toString(),
                                        it["license_Number"].toString(), it["mobile_Number"].toString(),it["password"].toString())
                                }
                        ridersList.addAll(rider)

                        val plateNumber =
                            (snapshot.value as ArrayList<*>).filterIsInstance<HashMap<String, *>>()
                                .map {
                                    it["plate_Number"].toString()
                                }
                        bikePlatesList.addAll(plateNumber)
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

    private fun getBikeRecords() {
        Firebase.database.getReference(Constants.BikesTable)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        bikesRecordList.clear()
                        val bike =
                            (snapshot.value as ArrayList<*>).filterIsInstance<HashMap<String, *>>()
                                .map {
                                    BikeModel(
                                        it["Bike Number"] as Long,
                                        it["Bike Source"] as String,
                                        it["Color"] as String,
                                        it["Model Year"] as Long,
                                        it["Vehicle Chassis"] as String,
                                        it["Vehicle Engine"] as String,
                                        it["Vehicle Model"] as String
                                    )
                                }

                        bikesRecordList.addAll(bike)
                        Log.d(
                            "QOO",
                            " ........get data........ ${bikesRecordList.size}"
                        )

                    } else {
                        showSnack(getString(R.string.no_record), binding.root)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("QOO", "  Get bikes records got cancelled  ${error.message}")
                }


            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            Glide.with(this).load(imageBitmap).placeholder(R.drawable.man)
                .into(binding.ivProfile)

            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b: ByteArray = baos.toByteArray()
            val base64: String = Base64.encodeToString(b, Base64.DEFAULT)
            SharedPreferences.saveStringToPreferences("image", base64, this)

        } else if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            val imageBitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)

            Glide.with(this).load(imageBitmap).placeholder(R.drawable.man)
                .into(binding.ivProfile)

            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b: ByteArray = baos.toByteArray()
            val base64: String = Base64.encodeToString(b, Base64.DEFAULT)
            SharedPreferences.saveStringToPreferences("image", base64, this)

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun dispatchTakePictureIntent() {
        if (checkSelfPermission(Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                1002
            )
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                showSnack(getString(R.string.something_went_wrong), binding.root)
            }
        }
    }

    fun galleryAddPic() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        intent.type = "image/*"
        startActivityForResult(
            Intent.createChooser(intent, "Select File"),
            SELECT_FILE
        )
    }
}