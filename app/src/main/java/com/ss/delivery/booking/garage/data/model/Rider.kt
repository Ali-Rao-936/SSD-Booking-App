package com.ss.delivery.booking.garage.data.model


data class Rider(
    val Full_Name: String,
    val Father_Name: String,
    val Rider_Id: String,
    val Plate_Number: String,
    val License_Number: String,
    val Mobile_Number: String,
    val Password: String
)

data class TimeModel(
    val name: String? = null,
    val value: String? = null,
    val slots: List<TimeSlot>? = null
)

data class RiderLog(
    val Full_Name: String,
    val License_Number: String,
    val Plate_Number: String,
    val Phone_Number: String,
    val Time: String,
    val Slot: String,
    val Message: String,
    val Reason: String,
    val Bill: String,
    val KM: String,
    val Booking_Time: String,
    val Invoice_Number: String,
    val Rider_Amount: String,
    val Transaction_ID: String
)

data class TimeSlot(
    val name: String? = null,
    var status: Boolean? = null
)

data class TimeData(
    val name: String,
    val value: TimeModel
)


data class BikeModel(
    val Bike_Number: Long,
    val Bike_Source: String?,
    val Bike_Color: String?,
    val Bike_Model: Long?,
    val Vehicle_Chassis: String?,
    val Vehicle_Engine: String?,
    val Vehicle_Model: String?
)