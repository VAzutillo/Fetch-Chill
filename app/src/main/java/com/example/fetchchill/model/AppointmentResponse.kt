package com.example.fetchchill.model



data class Appointment(
    val id: Int,
    val user_id: Int,
    val service_type: String,
    val appointment_date: String,
    val appointment_time: String,
    val status: String
)

data class AppointmentRequest(
    val user_id: Int,
    val service_type: String,
    val appointment_date: String,
    val appointment_time: String
)

data class ResponseMessage(
    val message: String
)


