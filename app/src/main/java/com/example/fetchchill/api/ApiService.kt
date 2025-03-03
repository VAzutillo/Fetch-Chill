package com.example.fetchchill.api

import com.example.fetchchill.model.LoginResponse
import com.example.fetchchill.model.SignUpResponse
import com.example.fetchchill.model.User
import com.example.fetchchill.model.LoginUser
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("signup.php")
    fun signUp(@Body user: User): Call<SignUpResponse>

    @Headers("Content-Type: application/json") // Set the content type for the login request
    @POST("login.php")
    fun userLogin(@Body user: LoginUser ): Call<LoginResponse> // Accept LoginUser  object

//    @Headers("Content-Type: application/json")
//    @GET()

}