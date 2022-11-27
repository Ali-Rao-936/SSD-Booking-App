package com.ss.delivery.booking.garage.presenter.home

import android.os.Bundle
import android.util.Log
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
import com.ss.delivery.booking.garage.data.model.TimeModel
import com.ss.delivery.booking.garage.data.model.TimeModels
import com.ss.delivery.booking.garage.data.model.TimeSlot
import com.ss.delivery.booking.garage.databinding.ActivityHomeBinding
import com.ss.delivery.booking.garage.presenter.adapter.HomeTimeAdapter
import com.ss.delivery.booking.garage.presenter.adapter.OnCheckBoxClick
import com.ss.delivery.booking.garage.utils.Utils.getDateToday
import com.ss.delivery.booking.garage.utils.Utils.showSnack
import java.sql.Time

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    var timeList = ArrayList<TimeModel>()
    lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        val database = Firebase.database
        myRef = database.getReference("UpdatedTimeSlotTable").child(getDateToday())
        binding.rvHome.layoutManager = LinearLayoutManager(this)

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

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
                                override fun onCbClick(timePosition: Int, slotPosition: Int) {
                                    Log.d(
                                        "QOO",
                                        "  timePosition  $timePosition    slotPosition   $slotPosition "
                                    )
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