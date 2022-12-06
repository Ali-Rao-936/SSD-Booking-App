package com.ss.delivery.booking.garage.presenter.settings

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.ss.delivery.booking.garage.R
import com.ss.delivery.booking.garage.databinding.ActivityProfileBinding
import com.ss.delivery.booking.garage.utils.*
import java.io.ByteArrayOutputStream


class ProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileBinding
    private val REQUEST_IMAGE_CAPTURE = 1
    val SELECT_FILE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)

        binding.ivBackProfile.setOnClickListener {
            onBackPressed()
        }

        val encodedImage = SharedPreferences.getStringValueFromPreference("image", "", this)
        val decodedString: ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

        Glide.with(this).load(decodedByte)
            .error(R.drawable.man).into(binding.profileImageView)

        binding.txtName.text =
            SharedPreferences.getStringValueFromPreference(Constants.RiderName, "", this)
        binding.txtDrivingLicenseNumber.text =
            SharedPreferences.getStringValueFromPreference(Constants.DrivingLicense, "", this)
        binding.txtPhoneNumber.text =
            SharedPreferences.getStringValueFromPreference(Constants.PhoneNumber, "", this)
        binding.txtPlateNumber.text =
            SharedPreferences.getStringValueFromPreference(Constants.PlateNumber, "", this)


        binding.ivEditProfile.setOnClickListener {
            val modalBottomSheet = EditImageChooseSheet(this@ProfileActivity)
            modalBottomSheet.show(supportFragmentManager, EditImageChooseSheet.TAG)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            Glide.with(this).load(imageBitmap).placeholder(R.drawable.man)
                .into(binding.profileImageView)

            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b: ByteArray = baos.toByteArray()
            val base64: String = Base64.encodeToString(b, Base64.DEFAULT)
            SharedPreferences.saveStringToPreferences("image", base64, this)

        } else if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            val imageBitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)

            Glide.with(this).load(imageBitmap).placeholder(R.drawable.man)
                .into(binding.profileImageView)

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
                Utils.showSnack(getString(R.string.something_went_wrong), binding.root)
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