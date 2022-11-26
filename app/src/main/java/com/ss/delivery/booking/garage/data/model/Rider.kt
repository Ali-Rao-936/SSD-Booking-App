package com.ss.delivery.booking.garage.data.model

data class TimeModel(
    val name: String? = null,
    val value: String? = null,
    val slots: ArrayList<TimeSlot>? = null
)

data class TimeSlot(
    val name: String,
    val status: Boolean
)

data class TimeData(
    val name: String,
    val value: TimeModel
)

class TimeList {
    companion object {
        lateinit var arrayList: ArrayList<TimeModel>
    }
}
