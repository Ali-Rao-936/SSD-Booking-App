package com.ss.delivery.booking.garage.utils

import android.R
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.View
import android.view.Window
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


object Utils {

    val monthList = arrayListOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )
    var lastMainSelectedPosition = -1
    var lastSelectedPosition = -1

    fun getDateToday(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        return dateFormat.format(calendar.time)
    }

    fun getLocale(context: Context): String {
        return SharedPreferences.getStringValueFromPreference(
            Constants.LOCALE_KEY,
            Constants.ENGLISH,
            context
        )!!
    }

    fun setLocale(context: Context, locale: String) {
        SharedPreferences
            .saveStringToPreferences(
                Constants.LOCALE_KEY,
                locale,
                context
            )
    }

    fun showSnack(message: String, view: View) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    fun getCurrentDay(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_MONTH)
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

    fun getCurrentDayOfWeek(date: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(getCurrentYear(), getCurrentMonth(), date)
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    fun getCurrentTimeAndDate(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy,KK:mm aaa")
        return sdf.format(Date())
    }



    fun showUpdatePopup(context: Context) {
        val dialog = Dialog(context, R.style.ThemeOverlay)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(com.ss.delivery.booking.garage.R.layout.version_update_dialog)
        dialog.setCancelable(false)



        //  initializing dialog screen
        val btnUpdate: AppCompatButton = dialog.findViewById(com.ss.delivery.booking.garage.R.id.btnUpdate)

        btnUpdate.setOnClickListener {

            try {
               context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${context.packageName}")))
            } catch (e: ActivityNotFoundException) {
               context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")))
            }
            dialog.dismiss()
            (context as Activity).finish()
        }

        dialog.show()
    }
}