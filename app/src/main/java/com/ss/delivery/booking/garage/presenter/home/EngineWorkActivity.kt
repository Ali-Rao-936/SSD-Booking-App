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
import com.ss.delivery.booking.garage.data.model.TimeSlot
import com.ss.delivery.booking.garage.databinding.ActivityEngineWorkBinding
import com.ss.delivery.booking.garage.presenter.adapter.HomeTimeAdapter
import com.ss.delivery.booking.garage.presenter.adapter.OnCheckBoxClick
import com.ss.delivery.booking.garage.utils.Constants
import com.ss.delivery.booking.garage.utils.Utils

class EngineWorkActivity : AppCompatActivity() {

    lateinit var binding: ActivityEngineWorkBinding
    var timeList = ArrayList<TimeModel>()
    lateinit var myRef: DatabaseReference
    private var selectDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_engine_work)

        binding.ivBackHome.setOnClickListener {
            onBackPressed()
        }

        selectDate = intent.extras!!.getString(Constants.SelectDate)!!

        val database = Firebase.database
        myRef =
            database.getReference("${Utils.getCurrentMonthName()}-${Utils.getCurrentYear()}").child("Engine Work").child(selectDate)
        binding.rvHome.layoutManager = LinearLayoutManager(this)

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Log.d(
                        "QOO",
                        " ........get data........ ${Utils.getCurrentMonth() + 1}        ${Utils.getMonthDaysCount()}"
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
                        val adapter =
                            HomeTimeAdapter(this@EngineWorkActivity, timeList, object : OnCheckBoxClick {
                                override fun onCbClick(
                                    timePosition: Int,
                                    slotPosition: Int,
                                    isChecked: Boolean
                                ) {
                                    Log.d(
                                        "QOO",
                                        "  timePosition  $timePosition    slotPosition   $slotPosition "
                                    )

                                    if (isChecked) {
                                        if (Utils.lastMainSelectedPosition == -1 && Utils.lastSelectedPosition == -1) {
                                            // first time
                                            Utils.lastMainSelectedPosition = timePosition
                                            Utils.lastSelectedPosition = slotPosition
                                        } else {
                                            Utils.lastMainSelectedPosition = timePosition
                                            Utils.lastSelectedPosition = slotPosition

                                        }
                                    }
                                }

                            })
                        binding.rvHome.adapter = adapter
                    } else
                        Utils.showSnack("no data found", binding.root)

                } else {
                    Utils.showSnack("Something went wrong. Please try again", binding.root)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("QOO", "  Get value on Home got cancelled  ${error.message}")
            }

        })
    }
}