package com.ss.delivery.booking.garage.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferences {

    var sharedPreferences: SharedPreferences? = null
    private var shareEditor: SharedPreferences.Editor? = null

    fun saveStringToPreferences(key: String?, value: String, context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences =
                context.getSharedPreferences("SSD_Pref", 0)
        }
        shareEditor = sharedPreferences?.edit()
        shareEditor?.putString(key, value)
        shareEditor?.commit()
    }

    fun getStringValueFromPreference(
        key: String?,
        defaultValue: String?,
        context: Context
    ): String? {
        if (sharedPreferences == null) {
            sharedPreferences =
                context.getSharedPreferences("SSD_Pref", 0)
        }
        return sharedPreferences!!.getString(key, defaultValue)
    }

    fun saveBooleanToPreferences(key: String?, value: Boolean, context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences =
                context.getSharedPreferences("SSD_Pref", 0)
        }
        shareEditor = sharedPreferences?.edit()
        shareEditor?.putBoolean(key, value)
        shareEditor?.commit()
    }

    fun getBooleanValueFromPreference(
        key: String?,
        context: Context
    ): Boolean {
        if (sharedPreferences == null) {
            sharedPreferences =
                context.getSharedPreferences("SSD_Pref", 0)
        }
        return sharedPreferences!!.getBoolean(key, false)
    }
}