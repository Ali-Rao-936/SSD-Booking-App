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
import com.ss.delivery.booking.garage.utils.Utils.getCurrentMonth
import com.ss.delivery.booking.garage.utils.Utils.getCurrentMonthName
import com.ss.delivery.booking.garage.utils.Utils.getCurrentYear
import com.ss.delivery.booking.garage.utils.Utils.getMonthDaysCount
import com.ss.delivery.booking.garage.utils.Utils.showSnack

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    var timeList = ArrayList<TimeModel>()
    lateinit var myRef: DatabaseReference
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
            database.getReference("${getCurrentMonthName()}-${getCurrentYear()}").child(selectDate)
        binding.rvHome.layoutManager = LinearLayoutManager(this)

        myRef.addValueEventListener(object : ValueEventListener {
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
                        binding.rvHome.adapter =
                            HomeTimeAdapter(this@HomeActivity, timeList, object : OnCheckBoxClick {
                                override fun onCbClick(
                                    timePosition: Int,
                                    slotPosition: Int,
                                    isChecked: Boolean
                                ) {
                                    Log.d(
                                        "QOO",
                                        "  timePosition  $timePosition    slotPosition   $slotPosition "
                                    )
                                    if (selectedCount == 0) {
                                        binding.rlButton.visibility = View.VISIBLE
                                        timesPosition = timePosition
                                        slotsPosition = slotPosition
                                        if (isChecked)
                                            selectedCount = 1
                                        else
                                            selectedCount = 0
                                    } else {
                                        showSnack("You can not select more than one", binding.root)
                                    }
                                }

                            })
                    } else
                        showSnack("no data found", binding.root)

                } else {
                    setupDataBase()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("QOO", "  Get value on Home got cancelled  ${error.message}")
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
                } else {
                    Log.d("QOO", "${it.exception?.message}")
                    showSnack("Something goes wrong, Please try again", binding.root)
                }
            }
        }


    }

    private fun setupDataBase() {
        timeList.add(TimeModel("Time 1", "09:00 to 09:30", getSlots()))
        timeList.add(TimeModel("Time 2", "09:30 to 10:00", getSlots()))
        timeList.add(TimeModel("Time 3", "10:00 to 10:30", getSlots()))
        timeList.add(TimeModel("Time 4", "10:30 to 11:00", getSlots()))
        timeList.add(TimeModel("Time 5", "11:00 to 11:30", getSlots()))
        timeList.add(TimeModel("Time 6", "09:00 to 12:00", getSlots()))
        timeList.add(TimeModel("Time 7", "12:00 to 00:30", getSlots()))
        timeList.add(TimeModel("Time 8", "00:30 to 13:00", getSlots()))
        timeList.add(TimeModel("Time 9", "13:00 to 13:30", getSlots()))
        timeList.add(TimeModel("Time 10", "13:30 to 14:00", getSlots()))
        timeList.add(TimeModel("Time 11", "14:00 to 14:30", getSlots()))
        timeList.add(TimeModel("Time 12", "14:30 to 15:00", getSlots()))
        timeList.add(TimeModel("Time 13", "15:00 to 15:30", getSlots()))
        timeList.add(TimeModel("Time 14", "15:30 to 16:00", getSlots()))
        timeList.add(TimeModel("Time 15", "16:00 to 16:30", getSlots()))
        timeList.add(TimeModel("Time 16", "16:30 to 17:00", getSlots()))
        timeList.add(TimeModel("Time 17", "17:00 to 17:30", getSlots()))
        timeList.add(TimeModel("Time 18", "17:30 to 18:00", getSlots()))
        timeList.add(TimeModel("Time 19", "18:00 to 18:30", getSlots()))
        timeList.add(TimeModel("Time 20", "18:30 to 19:00", getSlots()))
        timeList.add(TimeModel("Time 21", "19:00 to 19:30", getSlots()))
        timeList.add(TimeModel("Time 22", "19:30 to 20:00", getSlots()))
        timeList.add(TimeModel("Time 23", "20:00 to 20:30", getSlots()))
        timeList.add(TimeModel("Time 24", "20:30 to 21:00", getSlots()))
        timeList.add(TimeModel("Time 25", "21:00 to 21:30", getSlots()))
        timeList.add(TimeModel("Time 26", "21:30 to 22:00", getSlots()))

        myRef.setValue(timeList)
    }

    private fun getSlots(): ArrayList<TimeSlot> {
        val list = ArrayList<TimeSlot>()
        list.add(TimeSlot("Slot 1", false))
        list.add(TimeSlot("Slot 2", true))
        list.add(TimeSlot("Slot 3", false))
        list.add(TimeSlot("Slot 4", false))
        list.add(TimeSlot("Slot 5", true))
        list.add(TimeSlot("Slot 6", false))

        return list
    }


}