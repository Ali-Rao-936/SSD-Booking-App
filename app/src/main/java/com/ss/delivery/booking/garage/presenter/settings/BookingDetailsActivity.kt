package com.ss.delivery.booking.garage.presenter.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ss.delivery.booking.garage.R
import com.ss.delivery.booking.garage.databinding.ActivityBookingDetailsBinding
import com.ss.delivery.booking.garage.presenter.settings.MyBookingsActivity.Companion.bookingList

class BookingDetailsActivity : AppCompatActivity() {

    lateinit var binding : ActivityBookingDetailsBinding

    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      binding = DataBindingUtil.setContentView(this, R.layout.activity_booking_details)


        position = intent.extras?.getInt("detailsPosition") ?: 0

        binding.ivBackBooking.setOnClickListener { onBackPressed() }

        binding.txtBookingDetails.text = bookingList[position].Details
        binding.txtBookingId.text = bookingList[position].ID
        binding.txtDate.text = bookingList[position].Time
    }
}