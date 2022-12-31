package com.ss.delivery.booking.garage.presenter.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ss.delivery.booking.garage.R
import com.ss.delivery.booking.garage.data.model.MyBooking
import com.ss.delivery.booking.garage.databinding.ActivityMyBookingsBinding
import com.ss.delivery.booking.garage.presenter.adapter.MyBookingsAdapter
import com.ss.delivery.booking.garage.utils.Constants
import com.ss.delivery.booking.garage.utils.SharedPreferences
import java.lang.reflect.Type

class MyBookingsActivity : AppCompatActivity() {

    lateinit var binding : ActivityMyBookingsBinding
    companion object {
        var bookingList = ArrayList<MyBooking>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     binding = DataBindingUtil.setContentView(this,R.layout.activity_my_bookings)

        binding.ivBackMB.setOnClickListener { onBackPressed() }

        val gson = Gson()
        val json : String =
            SharedPreferences.getStringValueFromPreference(Constants.MuBookings, "", this)!!
        if (json.isNotEmpty()) {
            val type: Type = object : TypeToken<ArrayList<MyBooking?>?>() {}.type
            bookingList = gson.fromJson<Any>(json, type) as ArrayList<MyBooking>
        }

        if (bookingList.isEmpty())
            binding.txtNoBookings.visibility = View.VISIBLE
        else{
            binding.rvMyBookings.visibility = View.VISIBLE
            binding.rvMyBookings.layoutManager = LinearLayoutManager(this)

            binding.rvMyBookings.adapter = MyBookingsAdapter(bookingList, this)
        }
    }
}