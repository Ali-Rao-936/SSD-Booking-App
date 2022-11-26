package com.ss.delivery.booking.garage.data.model

data class TimeModel(
    val name: String? = null,
    val value: String? = null,
    val slots: ArrayList<TimeSlot>? = null
)
 class TimeModels(
    val name: String,
    val value: String,
    val slots: ArrayList<TimeSlot>
    ) {

    constructor() : this("", "", ArrayList<TimeSlot>())
}

data class TimeSlot(
    val name: String? = null,
    val status: Boolean? = null
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
