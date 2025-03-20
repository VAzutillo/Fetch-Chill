package com.example.fetchchill.api

import com.example.fetchchill.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // Sign-up API
    @Headers("Content-Type: application/json")
    @POST("signup.php")
    fun signUp(@Body user: User): Call<SignUpResponse>

    // Check Username API
    @Headers("Content-Type: application/json")
    @POST("check-username.php")
    fun checkUsername(@Body request: UsernameRequest): Call<UsernameCheckResponse>

    // Check Email API
    @Headers("Content-Type: application/json")
    @POST("check-email.php")
    fun checkEmail(@Body request: EmailRequest): Call<EmailCheckResponse>

    // Login API
    @Headers("Content-Type: application/json")
    @POST("login.php")
    fun userLogin(@Body user: LoginUser ): Call<LoginResponse>

    // Save Profile API
    @Multipart
    @POST("saveProfile.php")
    fun saveProfile(
        @Part("owner_name") ownerName: RequestBody,
        @Part("pet_name") petName: RequestBody,
        @Part("breed") breed: RequestBody,
        @Part("age") age: RequestBody,
        @Part profile_image: MultipartBody.Part? // Optional image
    ): Call<ProfileSaveResponse>

    // Get Profile API
    @GET("getProfile.php")
    fun getProfile(): Call<ProfileResponse>

    // Logout API
    @Headers("Content-Type: application/json")
    @POST("logout.php")
    fun logout(): Call<LogoutResponse>

    // Get All Appointments API
    @GET("api/index.php/appointment")
    fun getAllAppointments(): Call<List<Appointment>>

    // Create Appointment API
    @Headers("Content-Type: application/json")
    @POST("api/index.php/appointment")
    fun createAppointment(@Body appointment: AppointmentRequest): Call<ResponseMessage>

    // Get Specific Appointment API
    @GET("api/index.php/getAppointment/{id}")
    fun getAppointment(@Path("id") id: Int): Call<Appointment>
}