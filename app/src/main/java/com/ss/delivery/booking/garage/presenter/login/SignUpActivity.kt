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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ss.delivery.booking.garage.R
import com.ss.delivery.booking.garage.data.model.Rider
import com.ss.delivery.booking.garage.databinding.ActivitySignUpBinding
import com.ss.delivery.booking.garage.utils.ImageChooseSheet
import com.ss.delivery.booking.garage.utils.SharedPreferences
import com.ss.delivery.booking.garage.utils.Utils.showSnack
import java.io.ByteArrayOutputStream

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    lateinit var myRef: DatabaseReference
    private val REQUEST_IMAGE_CAPTURE = 1
    val SELECT_FILE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        binding.ivBackSignUp.setOnClickListener {
            onBackPressed()
        }

        binding.ivEditProfile.setOnClickListener {
            val modalBottomSheet = ImageChooseSheet(this@SignUpActivity)
            modalBottomSheet.show(supportFragmentManager, ImageChooseSheet.TAG)
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