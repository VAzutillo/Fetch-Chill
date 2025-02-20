package com.example.fetchchill.api

import com.example.fetchchill.model.SignUpResponse
import com.example.fetchchill.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("signup.php")
    fun signUp(@Body user: User): Call<SignUpResponse>
}