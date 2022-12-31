package com.ss.delivery.booking.garage.presenter.home

import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ss.delivery.booking.garage.R
import com.ss.delivery.booking.garage.data.model.MyBooking
import com.ss.delivery.booking.garage.databinding.ActivityBookingConfirmationBinding
import com.ss.delivery.booking.garage.utils.Constants
import com.ss.delivery.booking.garage.utils.SharedPreferences
import com.ss.delivery.booking.garage.utils.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.reflect.Type


class BookingConfirmationActivity : AppCompatActivity() {

    lateinit var binding: ActivityBookingConfirmationBinding
    var time = ""
    private var slot = ""
    var type = ""
    var date = ""
    var bookingList = ArrayList<MyBooking>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking_confirmation)

        binding.ivBackBooking.setOnClickListener {
            onBackPressed()
        }

        time = intent.extras!!.getString("time")!!
        slot = intent.extras!!.getString("slot")!!
        type = intent.extras!!.getString("type")!!
        date = intent.extras!!.getString("date")!!

      //  binding.txtDate.text = Utils.getDateToday()
        binding.txtDate.text = Utils.getCurrentTimeAndDate()
        lifecycleScope.launch {
            delay(4000)
            val bookingId : String =
                SharedPreferences.getStringValueFromPreference(
                    Constants.BookingID,
                    "000",
                    this@BookingConfirmationActivity
                ).toString()
            binding.txtBookingId.text = bookingId
            val hello = getString(R.string.hello)
            val your = getString(R.string.your_booking_for)
            val isC = getString(R.string.is_confirmed_on)
            val at = getString(R.string.at)
            val forr = getString(R.string.forr)
            val thank = getString(R.string.thank)


            val name = "<font color='#EE0000'>${
                SharedPreferences.getStringValueFromPreference(
                    Constants.RiderName,
                    "",
                    this@BookingConfirmationActivity
                )
            }</font>"
            val mTime = "<font color='#00ADEF'>$time</font>"
            val mDate = "<font color='#00ADEF'>$date</font>"
            val mSlot = "<font color='#00ADEF'>$slot</font>"
            val mType = "<font color='#00ADEF'>$type</font>"

            binding.txtBookingDetails.text = Html.fromHtml("$hello $name " +
                      "$your $mType $isC $mDate $at $mTime $forr $mSlot. $thank")

            val booking = MyBooking(type,bookingId, Utils.getCurrentTimeAndDate(), binding.txtBookingDetails.text.trim().toString())
            val gson = Gson()

            val json : String =
                SharedPreferences.getStringValueFromPreference(Constants.MuBookings, "", this@BookingConfirmationActivity)!!
            if (json.isNotEmpty()) {
                val type: Type = object : TypeToken<ArrayList<MyBooking?>?>() {}.type
                bookingList = gson.fromJson<Any>(json, type) as ArrayList<MyBooking>
            }
            bookingList.add(booking)
            val list : String = gson.toJson(bookingList)

            SharedPreferences.saveStringToPreferences(Constants.MuBookings, list, this@BookingConfirmationActivity)
        }

    }
}