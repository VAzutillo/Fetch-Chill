package com.example.fetchchill.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fetchchill.api.RetrofitClient
import com.example.fetchchill.model.LoginResponse
import com.example.fetchchill.model.SignUpResponse
import com.example.fetchchill.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthenticationViewModel: ViewModel() {
    
//    private var _userSignup = MutableLiveData<LoginResponse>()
//    val userLogin = _userSignup.value
//
//    private val apiService = RetrofitClient.apiService
//    fun login(user: User) {
//        apiService.signUp(user).enqueue(object : Callback <SignUpResponse>{
//            override fun onResponse(
//                call: Call<SignUpResponse>,
//                response: Response<SignUpResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val userRegistration = response.body()?.message
//                }
//            }
//
//            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//
//        })
//    }
}