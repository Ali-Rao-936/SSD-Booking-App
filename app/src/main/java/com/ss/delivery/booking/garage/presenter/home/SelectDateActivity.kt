package com.ss.delivery.booking.garage.presenter.home

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
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
import com.ss.delivery.booking.garage.presenter.settings.SettingsActivity
import com.ss.delivery.booking.garage.presenter.splash.SplashActivity.Companion.isUpdateAvailable
import com.ss.delivery.booking.garage.utils.Constants
import com.ss.delivery.booking.garage.utils.Utils
import com.ss.delivery.booking.garage.utils.Utils.getCurrentDay
import com.ss.delivery.booking.garage.utils.Utils.getCurrentDayOfWeek
import com.ss.delivery.booking.garage.utils.Utils.getCurrentMonth
import com.ss.delivery.booking.garage.utils.Utils.getCurrentMonthName
import com.ss.delivery.booking.garage.utils.Utils.getCurrentYear
import com.ss.delivery.booking.garage.utils.Utils.getMonthDaysCount
import com.ss.delivery.booking.garage.utils.Utils.monthList
import com.ss.delivery.booking.garage.utils.Utils.showSnack
import com.ss.delivery.booking.garage.utils.Utils.showUpdatePopup
import java.util.*

class SelectDateActivity : AppCompatActivity() {

    lateinit var binding: ActivitySelectDateBinding
    private var timeList = ArrayList<TimeModel>()
    lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_date)


        if (isUpdateAvailable){
            showUpdatePopup(this@SelectDateActivity)
        }else {

            Utils.lastSelectedPosition = -1
            Utils.lastMainSelectedPosition = -1

            myRef = Firebase.database.getReference("${getCurrentMonthName()}-${getCurrentYear()}")

            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) // check if there is no data setup database
                        setUpDataBaseForMonth()

                }

                override fun onCancelled(error: DatabaseError) {
                    showSnack(getString(R.string.no_record), binding.root)
                    Log.d("QOO", " error for data base of times  :  ${error.message}")
                }

            })

            // go to settings
            binding.ivSettings.setOnClickListener {
                startActivity(Intent(this, SettingsActivity::class.java))
            }

            // change engine oil
            binding.llOilChange.setOnClickListener {
                showCalendar("Engine Oil")
            }

            // other services
            binding.llOtherServices.setOnClickListener {
                showCalendar("Other Services")
            }
            // outdoor
            binding.llOutdoorServices.setOnClickListener {
                showSnack("Coming Soon", binding.root)
            }
            // engine work
            binding.llEngineWork.setOnClickListener {

                val calendar = Calendar.getInstance()
                calendar.set(getCurrentYear(), getCurrentMonth(), getCurrentDay())
                val min = calendar.timeInMillis
                calendar.set(getCurrentYear(), getCurrentMonth(), getMonthDaysCount())
                val max = calendar.timeInMillis
                val dpd = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->

                    Log.d("QOO", "" + dayOfMonth + " " + monthList[monthOfYear] + ", " + year)
                    Log.d("QOO", " current day :  ${getCurrentDayOfWeek(dayOfMonth)}")
                    if (getCurrentDayOfWeek(dayOfMonth) == 6 || getCurrentDayOfWeek(dayOfMonth) == 7)
                        goToEngineWork("" + dayOfMonth + "-" + monthList[monthOfYear] + "-" + year)
                    else
                        showInfoPopup("")

                }, getCurrentYear(), getCurrentMonth(), getCurrentDay()).apply {
                    datePicker.minDate = min
                    datePicker.maxDate = max
                }
                dpd.show()
            }

        }
    }

    private fun goToEngineWork(date: String) {
        Log.d("QOO", " date :  $date")
        startActivity(
            Intent(this, EngineWorkActivity::class.java).putExtra(
                Constants.SelectDate,
                date
            )
        )
    }

    private fun showInfoPopup(s: String) {
        val dialog = Dialog(this, android.R.style.ThemeOverlay)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.info_dialog_layout)

        //  initializing dialog screen
        val txtMessage: TextView = dialog.findViewById(R.id.txtMessage)
        val btnOk: AppCompatButton = dialog.findViewById(R.id.btnOk)

        if (s.isEmpty())
            txtMessage.text = getString(R.string.engine_work_message)
        else
            txtMessage.text = getString(R.string.oil_work_message)

        btnOk.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showCalendar(type: String) {
        val calendar = Calendar.getInstance()
        calendar.set(getCurrentYear(), getCurrentMonth(), getCurrentDay())
        val min = calendar.timeInMillis
        calendar.set(getCurrentYear(), getCurrentMonth(), getMonthDaysCount())
        val max = calendar.timeInMillis
        val dpd = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->

            Log.d("QOO", "" + dayOfMonth + " " + monthList[monthOfYear] + ", " + year)
            Log.d("QOO", " current day :  ${getCurrentDayOfWeek(dayOfMonth)}")
            if (getCurrentDayOfWeek(dayOfMonth) == 1)
                showInfoPopup("Sunday")
            else
                goToHome("" + dayOfMonth + "-" + monthList[monthOfYear] + "-" + year, type)

        }, getCurrentYear(), getCurrentMonth(), getCurrentDay()).apply {
            datePicker.minDate = min
            datePicker.maxDate = max
        }
        dpd.show()
    }

    private fun goToHome(date: String, type: String) {
        Log.d("QOO", " date :  $date")
        startActivity(
            Intent(this, HomeActivity::class.java).putExtra(Constants.SelectDate, date)
                .putExtra("type", type)
        )
    }

    private fun setUpDataBaseForMonth() {
        Log.d("QOO", " days count:  ${getMonthDaysCount()}")
        for (i in 1..getMonthDaysCount()) {
            Log.d("QOO", " current day :  ${getCurrentDayOfWeek(i)}")
            if (getCurrentDayOfWeek(i) == 1)
                Log.d("QOO", " today is sunday")
            else if (getCurrentDayOfWeek(i) == 6) {
                addDataForDay("$i-${getCurrentMonthName()}-${getCurrentYear()}") // today is friday
                addDataForEngineWork("$i-${getCurrentMonthName()}-${getCurrentYear()}")
            } else if (getCurrentDayOfWeek(i) == 7) {
                addDataForDay("$i-${getCurrentMonthName()}-${getCurrentYear()}") // today is saturday
                addDataForEngineWork("$i-${getCurrentMonthName()}-${getCurrentYear()}")
            } else {
                addDataForWorkDay("$i-${getCurrentMonthName()}-${getCurrentYear()}")
            }
        }
    }

    private fun addDataForWorkDay(date: String) {
        timeList.clear()
        timeList.add(TimeModel("Time 1", "09:00 to 09:45", getFullSlots()))
        timeList.add(TimeModel("Time 2", "09:45 to 10:30", getFullSlots()))
        timeList.add(TimeModel("Time 3", "10:30 to 11:15", getFullSlots()))
        timeList.add(TimeModel("Time 4", "11:15 to 00:00", getFullSlots()))
        timeList.add(TimeModel("Time 5", "00:00 to 00:45", getFullSlots()))
        timeList.add(TimeModel("Time 6", "00:45 to 13:30", getFullSlots()))
        timeList.add(TimeModel("Time 7", "14:30 to 15:15", getFullSlots()))
        timeList.add(TimeModel("Time 8", "15:15 to 16:00", getFullSlots()))
        timeList.add(TimeModel("Time 9", "16:00 to 16:45", getFullSlots()))
        timeList.add(TimeModel("Time 10", "17:30 to 18:15", getFullSlots()))
        timeList.add(TimeModel("Time 11", "18:15 to 19:00", getFullSlots()))
        timeList.add(TimeModel("Time 12", "19:00 to 19:45", getFullSlots()))
        timeList.add(TimeModel("Time 13", "19:45 to 20:15", getFullSlots()))
        timeList.add(TimeModel("Time 14", "20:15 to 21:00", getFullSlots()))

        myRef.child("Other Services").child(date).setValue(timeList)
    }

    private fun addDataForDay(date: String) {
        timeList.clear()
        timeList.add(TimeModel("Time 1", "09:00 to 09:45", getSlots()))
        timeList.add(TimeModel("Time 2", "09:45 to 10:30", getSlots()))
        timeList.add(TimeModel("Time 3", "10:30 to 11:15", getSlots()))
        timeList.add(TimeModel("Time 4", "11:15 to 00:00", getSlots()))
        timeList.add(TimeModel("Time 5", "00:00 to 00:45", getSlots()))
        timeList.add(TimeModel("Time 6", "00:45 to 13:30", getSlots()))
        timeList.add(TimeModel("Time 7", "14:30 to 15:15", getSlots()))
        timeList.add(TimeModel("Time 8", "15:15 to 16:00", getSlots()))
        timeList.add(TimeModel("Time 9", "16:00 to 16:45", getSlots()))
        timeList.add(TimeModel("Time 10", "17:30 to 18:15", getSlots()))
        timeList.add(TimeModel("Time 11", "18:15 to 19:00", getSlots()))
        timeList.add(TimeModel("Time 12", "19:00 to 19:45", getSlots()))
        timeList.add(TimeModel("Time 13", "19:45 to 20:15", getSlots()))
        timeList.add(TimeModel("Time 14", "20:15 to 21:00", getSlots()))

        myRef.child("Other Services").child(date).setValue(timeList)
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

    private fun getEngineSlots(): ArrayList<TimeSlot> {
        val list = ArrayList<TimeSlot>()
        list.add(TimeSlot("Slot 4", false))
        list.add(TimeSlot("Slot 5", false))
        list.add(TimeSlot("Slot 6", false))
        return list
    }

    private fun addDataForEngineWork(date: String) {
        timeList.clear()
        timeList.add(TimeModel("Time 1", "09:00 to 14:00", getEngineSlots()))
        timeList.add(TimeModel("Time 2", "16:00 to 21:00", getEngineSlots()))
        myRef.child("Engine Work").child(date).setValue(timeList)
    }

}