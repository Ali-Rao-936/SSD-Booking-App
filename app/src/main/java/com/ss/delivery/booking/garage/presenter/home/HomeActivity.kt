package com.ss.delivery.booking.garage.presenter.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ss.delivery.booking.garage.R
import com.ss.delivery.booking.garage.data.model.RiderLog
import com.ss.delivery.booking.garage.data.model.TimeModel
import com.ss.delivery.booking.garage.data.model.TimeSlot
import com.ss.delivery.booking.garage.databinding.ActivityHomeBinding
import com.ss.delivery.booking.garage.presenter.adapter.HomeTimeAdapter
import com.ss.delivery.booking.garage.presenter.adapter.OnCheckBoxClick
import com.ss.delivery.booking.garage.utils.Constants
import com.ss.delivery.booking.garage.utils.SharedPreferences
import com.ss.delivery.booking.garage.utils.Utils
import com.ss.delivery.booking.garage.utils.Utils.getCurrentMonth
import com.ss.delivery.booking.garage.utils.Utils.getCurrentMonthName
import com.ss.delivery.booking.garage.utils.Utils.getCurrentYear
import com.ss.delivery.booking.garage.utils.Utils.getMonthDaysCount
import com.ss.delivery.booking.garage.utils.Utils.showSnack

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    var timeList = ArrayList<TimeModel>()
    lateinit var myRef: DatabaseReference
    lateinit var adapter : HomeTimeAdapter
    private var selectedCount = 0
    private var timesPosition = 0
    private var slotsPosition = 0
    private var selectDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        selectDate = intent.extras!!.getString(Constants.SelectDate)!!

        val database = Firebase.database
        myRef =
            database.getReference("${getCurrentMonthName()}-${getCurrentYear()}").child("Other Services").child(selectDate)
        binding.rvHome.layoutManager = LinearLayoutManager(this)

        binding.ivBackHome.setOnClickListener {
            onBackPressed()
        }

        myRef.addValueEventListener(object : ValueEventListener, OnCheckBoxClick {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Log.d(
                        "QOO",
                        " ........get data........ ${getCurrentMonth() + 1}        ${getMonthDaysCount()}"
                    )
                    timeList.clear()
                    val pieceOfShit =
                        (snapshot.value as ArrayList<*>).filterIsInstance<HashMap<String, *>>()
                            .map {
                                val slots =
                                    (it.get("slots") as? ArrayList<*>)?.filterIsInstance<HashMap<String, *>>()
                                        ?.map { timeSlot ->
                                            val name = timeSlot.get("name") as? String
                                            val status = timeSlot.get("status") as? Boolean
                                            TimeSlot(name, status)
                                        }
                                val name = it.get("name") as? String
                                val value = it.get("value") as? String
                                TimeModel(name, value, slots)
                            }

                    timeList.addAll(pieceOfShit)

                    if (timeList.isNotEmpty()) {
                         adapter =
                            HomeTimeAdapter(this@HomeActivity, timeList, this)
                        binding.rvHome.adapter = adapter

                    } else
                        showSnack("no data found", binding.root)

                } else {

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("QOO", "  Get value on Home got cancelled  ${error.message}")
            }

            override fun onCbClick(timePosition: Int, slotPosition: Int, isChecked: Boolean) {

                        Log.d(
                            "QOO",
                            "  timePosition  $timePosition    slotPosition   $slotPosition "
                        )

                        if (isChecked) {
                            binding.rlButton.visibility = View.VISIBLE
                            if (Utils.lastMainSelectedPosition == -1 && Utils.lastSelectedPosition == -1) {
                                // first time
                                Utils.lastMainSelectedPosition = timePosition
                                Utils.lastSelectedPosition = slotPosition
                            } else {
                                Utils.lastMainSelectedPosition = timePosition
                                Utils.lastSelectedPosition = slotPosition
                           //     timeList[timesPosition].slots?.get(slotsPosition)?.status = true
                                adapter.updateAdapter(timeList)
                            }
                        }else{
                            Utils.lastMainSelectedPosition = -1
                            Utils.lastSelectedPosition = -1
                            binding.rlButton.visibility = View.GONE
                        }
            }

        })

        binding.btnBook.setOnClickListener {
            timeList[timesPosition].slots?.get(slotsPosition)?.status = true
            myRef.setValue(timeList).addOnCompleteListener {
                if (it.isSuccessful) {
                    binding.rlButton.visibility = View.GONE
                    Firebase.database.getReference(Constants.BookingLogTable).child(
                        SharedPreferences.getStringValueFromPreference(
                            Constants.DrivingLicense,
                            "ad",
                            this
                        ) ?: ""
                    )
                        .setValue(
                            RiderLog(
                                SharedPreferences.getStringValueFromPreference(
                                    Constants.RiderName,
                                    "",
                                    this
                                ) ?: "",
                                SharedPreferences.getStringValueFromPreference(
                                    Constants.DrivingLicense,
                                    "ad",
                                    this
                                ) ?: "",
                                timeList[timesPosition].value!!,
                                timeList[timesPosition].slots?.get(slotsPosition)?.name ?: "",
                                "booked appointment"
                            )
                        )
                    showSnack("Your Booking is confirmed", binding.root)
                    Utils.lastMainSelectedPosition = -1
                    Utils.lastSelectedPosition = -1
                } else {
                    Log.d("QOO", "${it.exception?.message}")
                    showSnack("Something goes wrong, Please try again", binding.root)
                }
            }
        }


    }



}