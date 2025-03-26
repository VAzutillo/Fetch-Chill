package com.example.fetchchill.api

import ProfileResponse
import ProfileSaveResponse
import com.example.fetchchill.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Sign-up API
    @POST("signup.php")
    suspend fun signUp(@Body user: User): Response<SignUpResponse>

    // Check Username API
//    @POST("check-username")
//    suspend fun checkUsername(@Body request: UsernameRequest): Response<UsernameCheckResponse>
//
//    // Check Email API
//    @POST("check-email")
//    suspend fun checkEmail(@Body request: EmailRequest): Response<EmailCheckResponse>

    // Login API
    @Headers("Content-Type: application/json")
    @POST("login.php") // Ensure this endpoint is correct
    suspend fun userLogin(@Body login: LoginUser ): Response<LoginResponse>

    // Forgot Password API
    @Headers("Content-Type: application/json")
    @POST("forgotpassword.php")
    suspend fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Response<ForgotPasswordResponse>

    // Save Profile API
    @Multipart
    @POST("saveProfile.php")
    fun saveProfile(
        @Part("id") userId: RequestBody,
        @Part("owner_name") ownerName: RequestBody,
        @Part("pet_name") petName: RequestBody,
        @Part("breed") breed: RequestBody,
        @Part("age") age: RequestBody,
        @Part image_profile: MultipartBody.Part?
    ): Call<ProfileSaveResponse>

    // Get Profile API
    @GET("getProfile.php") // Ensure this endpoint is correct
    fun getProfile(@Query("id") userId: Int): Call<ProfileResponse>

    // Logout API
    @Headers("Content-Type: application/json")
    @POST("logout.php") // Ensure this endpoint is correct
    fun logout(): Call<LogoutResponse>

    // Get All Appointments API
    @GET("api/index.php/appointment") // Ensure this endpoint is correct
    fun getAllAppointments(): Call<List<Appointment>>

    // Create Appointment API
    @Headers("Content-Type: application/json")
    @POST("api/index.php/appointment") // Ensure this endpoint is correct
    fun createAppointment(@Body appointment: AppointmentRequest): Call<ResponseMessage>

    // Get Specific Appointment API
    @GET("api/index.php/getAppointment/{id}") // Ensure this endpoint is correct
    fun getAppointment(@Path("id") id: Int): Call<Appointment>
}