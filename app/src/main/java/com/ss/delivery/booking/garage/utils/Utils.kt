package com.ss.delivery.booking.garage.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.*
import kotlin.collections.ArrayList

object Utils {

    val monthList =  arrayListOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

    fun getDateToday(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        return dateFormat.format(calendar.time)
    }

    fun showSnack(message: String, view: View) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    fun getCurrentDay(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_MONTH   )
    }

    fun getCurrentMonth(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.MONTH)
    }

    fun getCurrentMonthName(): String {
        val calendar = Calendar.getInstance()
        return monthList[calendar.get(Calendar.MONTH)]
    }

    fun getCurrentYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
    }

    fun getMonthDaysCount(): Int {
        val calendar = Calendar.getInstance()
       return calendar.getActualMaximum(Calendar.DATE)

    }

    fun getCurrentDayOfWeek(date : Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(getCurrentYear(), getCurrentMonth(), date)
        return calendar.get(Calendar.DAY_OF_WEEK)
    }
}