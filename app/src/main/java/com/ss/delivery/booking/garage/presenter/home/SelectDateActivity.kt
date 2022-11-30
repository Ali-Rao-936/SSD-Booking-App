package com.ss.delivery.booking.garage.presenter.home

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ss.delivery.booking.garage.R
import com.ss.delivery.booking.garage.data.model.TimeModel
import com.ss.delivery.booking.garage.data.model.TimeSlot
import com.ss.delivery.booking.garage.databinding.ActivitySelectDateBinding
import com.ss.delivery.booking.garage.presenter.setting.SettingsActivity
import com.ss.delivery.booking.garage.utils.Constants
import com.ss.delivery.booking.garage.utils.Utils.getCurrentDay
import com.ss.delivery.booking.garage.utils.Utils.getCurrentDayOfWeek
import com.ss.delivery.booking.garage.utils.Utils.getCurrentMonth
import com.ss.delivery.booking.garage.utils.Utils.getCurrentMonthName
import com.ss.delivery.booking.garage.utils.Utils.getCurrentYear
import com.ss.delivery.booking.garage.utils.Utils.getMonthDaysCount
import com.ss.delivery.booking.garage.utils.Utils.monthList
import com.ss.delivery.booking.garage.utils.Utils.showSnack
import java.util.*

class SelectDateActivity : AppCompatActivity() {

    lateinit var binding: ActivitySelectDateBinding
    var timeList = ArrayList<TimeModel>()
    lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_date)

        myRef = Firebase.database.getReference("${getCurrentMonthName()}-${getCurrentYear()}")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) // check if there is no data setup database
                    setUpDataBaseForMonth()

            }

            override fun onCancelled(error: DatabaseError) {
                showSnack("error for data base of times ", binding.root)
                Log.d("QOO", " error for data base of times  :  ${error.message}")
            }

        })

        // go to settings
        binding.ivSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }


        // select date
        binding.btnSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.set(getCurrentYear(), getCurrentMonth(), getCurrentDay())
            val min = calendar.timeInMillis
            calendar.set(getCurrentYear(), getCurrentMonth(), getMonthDaysCount())
            val max = calendar.timeInMillis
            val dpd = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->

                Log.d("QOO", "" + dayOfMonth + " " + monthList[monthOfYear] + ", " + year)
                Log.d("QOO", " current day :  ${getCurrentDayOfWeek(dayOfMonth)}")
                if (getCurrentDayOfWeek(dayOfMonth) == 1)
                    showSnack("Today is sunday", binding.root)
                else
                    goToHome("" + dayOfMonth + "-" + monthList[monthOfYear] + "-" + year)

            }, getCurrentYear(), getCurrentMonth(), getCurrentDay()).apply {
                datePicker.minDate = min
                datePicker.maxDate = max
            }
            dpd.show()
        }
    }

    private fun goToHome(date: String) {
        Log.d("QOO", " date :  $date")
        startActivity(Intent(this, HomeActivity::class.java).putExtra(Constants.SelectDate, date))
    }

    private fun setUpDataBaseForMonth() {
        Log.d("QOO", " days count:  ${getMonthDaysCount()}")
        for (i in 1..getMonthDaysCount()) {
            Log.d("QOO", " current day :  ${getCurrentDayOfWeek(i)}")
            if (getCurrentDayOfWeek(i) == 1)
                Log.d("QOO", " today is sunday")
            else if (getCurrentDayOfWeek(i) == 6)
                addDataForDay("$i-${getCurrentMonthName()}-${getCurrentYear()}") // today is friday
            else if (getCurrentDayOfWeek(i) == 7)
                addDataForDay("$i-${getCurrentMonthName()}-${getCurrentYear()}") // today is saturday
            else {
                addDataForWorkDay("$i-${getCurrentMonthName()}-${getCurrentYear()}")
            }
        }
    }

    private fun addDataForWorkDay(date: String) {
        timeList.clear()
        timeList.add(TimeModel("Time 1", "09:00 to 09:30", getFullSlots()))
        timeList.add(TimeModel("Time 2", "09:30 to 10:00", getFullSlots()))
        timeList.add(TimeModel("Time 3", "10:00 to 10:30", getFullSlots()))
        timeList.add(TimeModel("Time 4", "10:30 to 11:00", getFullSlots()))
        timeList.add(TimeModel("Time 5", "11:00 to 11:30", getFullSlots()))
        timeList.add(TimeModel("Time 6", "09:00 to 12:00", getFullSlots()))
        timeList.add(TimeModel("Time 7", "12:00 to 00:30", getFullSlots()))
        timeList.add(TimeModel("Time 8", "00:30 to 13:00", getFullSlots()))
        timeList.add(TimeModel("Time 9", "13:00 to 13:30", getFullSlots()))
        timeList.add(TimeModel("Time 10", "13:30 to 14:00", getFullSlots()))
        timeList.add(TimeModel("Time 11", "14:00 to 14:30", getFullSlots()))
        timeList.add(TimeModel("Time 12", "14:30 to 15:00", getFullSlots()))
        timeList.add(TimeModel("Time 13", "15:00 to 15:30", getFullSlots()))
        timeList.add(TimeModel("Time 14", "15:30 to 16:00", getFullSlots()))
        timeList.add(TimeModel("Time 15", "16:00 to 16:30", getFullSlots()))
        timeList.add(TimeModel("Time 16", "16:30 to 17:00", getFullSlots()))
        timeList.add(TimeModel("Time 17", "17:00 to 17:30", getFullSlots()))
        timeList.add(TimeModel("Time 18", "17:30 to 18:00", getFullSlots()))
        timeList.add(TimeModel("Time 19", "18:00 to 18:30", getFullSlots()))
        timeList.add(TimeModel("Time 20", "18:30 to 19:00", getFullSlots()))
        timeList.add(TimeModel("Time 21", "19:00 to 19:30", getFullSlots()))
        timeList.add(TimeModel("Time 22", "19:30 to 20:00", getFullSlots()))
        timeList.add(TimeModel("Time 23", "20:00 to 20:30", getFullSlots()))
        timeList.add(TimeModel("Time 24", "20:30 to 21:00", getFullSlots()))
        timeList.add(TimeModel("Time 25", "21:00 to 21:30", getFullSlots()))
        timeList.add(TimeModel("Time 26", "21:30 to 22:00", getFullSlots()))

        myRef.child(date).setValue(timeList)
    }

    private fun addDataForDay(date: String) {
        timeList.clear()
        timeList.add(TimeModel("Time 1", "09:00 to 10:00", getSlots()))
        timeList.add(TimeModel("Time 2", "10:00 to 11:00", getSlots()))
        timeList.add(TimeModel("Time 3", "11:00 to 00:00", getSlots()))
        timeList.add(TimeModel("Time 4", "00:00 to 13:00", getSlots()))
        timeList.add(TimeModel("Time 5", "13:00 to 14:00", getSlots()))
        timeList.add(TimeModel("Time 6", "14:00 to 15:00", getSlots()))
        timeList.add(TimeModel("Time 7", "15:00 to 16:00", getSlots()))
        timeList.add(TimeModel("Time 8", "16:00 to 17:00", getSlots()))
        timeList.add(TimeModel("Time 9", "17:00 to 18:00", getSlots()))
        timeList.add(TimeModel("Time 10", "18:00 to 19:00", getSlots()))
        timeList.add(TimeModel("Time 11", "19:00 to 12:00", getSlots()))
        timeList.add(TimeModel("Time 12", "20:00 to 21:00", getSlots()))
        timeList.add(TimeModel("Time 13", "21:00 to 22:00", getSlots()))

        myRef.child(date).setValue(timeList)
    }

    private fun getFullSlots(): ArrayList<TimeSlot> {
        val list = ArrayList<TimeSlot>()
        list.add(TimeSlot("Slot 1", false))
        list.add(TimeSlot("Slot 2", false))
        list.add(TimeSlot("Slot 3", false))
        list.add(TimeSlot("Slot 4", false))
        list.add(TimeSlot("Slot 5", false))
        list.add(TimeSlot("Slot 6", false))

        return list
    }

    private fun getSlots(): ArrayList<TimeSlot> {
        val list = ArrayList<TimeSlot>()
        list.add(TimeSlot("Slot 1", false))
        list.add(TimeSlot("Slot 2", false))
        list.add(TimeSlot("Slot 3", false))
        return list
    }
}