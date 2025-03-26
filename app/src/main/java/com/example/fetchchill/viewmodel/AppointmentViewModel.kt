//package com.example.fetchchill.viewmodel
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.example.fetchchill.api.ApiService
//import com.example.fetchchill.model.Appointment
//import com.example.fetchchill.network.ApiClient
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class AppointmentViewModel : ViewModel() {
//    private val _appointments = MutableLiveData<List<Appointment>>()
//    val appointments: LiveData<List<Appointment>> = _appointments
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    private val _error = MutableLiveData<String?>()
//    val error: LiveData<String?> = _error
//
//    private val apiService = ApiClient.getInstance().create(ApiService::class.java)
//
//    fun fetchAppointments() {
//        // Set loading state
//        _isLoading.value = true
//        _error.value = null
//
//        apiService.getAllAppointments().enqueue(object : Callback<List<Appointment>> {
//            override fun onResponse(
//                call: Call<List<Appointment>>,
//                response: Response<List<Appointment>>
//            ) {
//                // Stop loading
//                _isLoading.value = false
//
//                if (response.isSuccessful) {
//                    // Set appointments, use empty list if response is null
//                    _appointments.value = response.body() ?: emptyList()
//                } else {
//                    // Set error message
//                    _error.value = "Failed to fetch appointments: ${response.code()}"
//                    _appointments.value = emptyList()
//                }
//            }
//
//            override fun onFailure(call: Call<List<Appointment>>, t: Throwable) {
//                // Stop loading
//                _isLoading.value = false
//
//                // Set error message
//                _error.value = "Network error: ${t.localizedMessage}"
//                _appointments.value = emptyList()
//            }
//        })
//    }
//}