package com.example.fetchchill.view

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fetchchill.api.RetrofitClient
import com.example.fetchchill.model.AppointmentRequest
import com.example.fetchchill.model.ResponseMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseAppointmentFragment : Fragment() {

    // Common method to create an appointment
    protected fun createAppointment(userId: Int, serviceType: String, appointmentDate: String, appointmentTime: String) {
        val appointmentRequest = AppointmentRequest(userId, serviceType, appointmentDate, appointmentTime)

        RetrofitClient.apiService.createAppointment(appointmentRequest).enqueue(object : Callback<ResponseMessage> {
            override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                if (response.isSuccessful) {
                    // Handle success
                    val message = response.body()?.message
                    showToast(message ?: "Appointment created successfully")
                } else {
                    // Handle error
                    showToast("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                // Handle failure
                showToast("Failed to create appointment: ${t.message}")
            }
        })
    }

    // Helper method to show a toast message
    protected fun showToast(message: String) { // Change from private to protected
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}