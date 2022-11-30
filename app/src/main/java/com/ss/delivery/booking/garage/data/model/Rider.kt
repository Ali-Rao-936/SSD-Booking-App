package com.ss.delivery.booking.garage.data.model


data class Rider(
    val Full_Name: String,
    val Father_Name: String,
    val Rider_Id: String,
    val Plate_Number: String,
    val License_Number: String,
    val Mobile_Number: String,
    val Password: String,
)

data class TimeModel(
    val name: String? = null,
    val value: String? = null,
    val slots: List<TimeSlot>? = null
)

data class RiderLog(
    val Full_Name: String,
    val License_Number: String,
    val Time: String,
    val Slot: String,
    val Message: String
)

data class TimeSlot(
    val name: String? = null,
    var status: Boolean? = null
)

data class TimeData(
    val name: String,
    val value: TimeModel
)

