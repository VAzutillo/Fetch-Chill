package com.example.fetchchill.api

import com.example.fetchchill.model.EmailCheckResponse
import com.example.fetchchill.model.EmailRequest
import com.example.fetchchill.model.LoginResponse
import com.example.fetchchill.model.SignUpResponse
import com.example.fetchchill.model.User
import com.example.fetchchill.model.LoginUser
import com.example.fetchchill.model.LogoutResponse
import com.example.fetchchill.model.ProfileResponse
import com.example.fetchchill.model.ProfileSaveResponse
import com.example.fetchchill.model.UsernameCheckResponse
import com.example.fetchchill.model.UsernameRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // Sign-up API (Registers a new user)
    @Headers("Content-Type: application/json")
    @POST("signup.php")
    fun signUp(@Body user: User): Call<SignUpResponse>

    // Check Username API
    @Headers("Content-Type: application/json")
    @POST("check-username.php") // Updated to use the correct endpoint
    fun checkUsername(@Body request: UsernameRequest): Call<UsernameCheckResponse>

    // Check Email API
    @Headers("Content-Type: application/json")
    @POST("check-email.php") // Updated to use the correct endpoint
    fun checkEmail(@Body request: EmailRequest): Call<EmailCheckResponse>

    // Login API (Authenticates a user)
    @Headers("Content-Type: application/json")
    @POST("login.php")
    fun userLogin(@Body user: LoginUser ): Call<LoginResponse>

    // Save Profile API (Saves user profile with an optional image upload)
    @Multipart
    @POST("saveProfile.php")
    fun saveProfile(
        @Part("owner_name") ownerName: RequestBody,
        @Part("pet_name") petName: RequestBody,
        @Part("breed") breed: RequestBody,
        @Part("age") age: RequestBody,
        @Part profile_image: MultipartBody.Part? // Image is optional
    ): Call<ProfileSaveResponse>

    // Get Profile API
    @GET("getProfile.php")
    fun getProfile(): Call<ProfileResponse>

    // Logout API
    @Headers("Content-Type: application/json")
    @POST("logout.php") // Adjust the path as necessary
    fun logout(): Call<LogoutResponse> // Assuming you have a LogoutResponse model
}