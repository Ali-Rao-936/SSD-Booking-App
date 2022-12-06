package com.ss.delivery.booking.garage.utils

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ss.delivery.booking.garage.R
import com.ss.delivery.booking.garage.presenter.login.SignUpActivity

class ImageChooseSheet constructor(context: SignUpActivity) : BottomSheetDialogFragment() {


    val activity: SignUpActivity = context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.image_bottom_sheet_layout, container, false)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.txtCameraImage).setOnClickListener {
            activity.dispatchTakePictureIntent()
            dismiss()
        }

        view.findViewById<TextView>(R.id.txtGalleryImage).setOnClickListener {
            activity.galleryAddPic()
            dismiss()
        }
    }

    companion object {
        const val TAG = "ImageBottomSheet"
    }


}