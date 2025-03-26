package com.example.fetchchill.api

import com.example.fetchchill.utils.AuthManager
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.100.18/fetch_chill/"
//    192.168.35.54 gaea
//    192.168.100.18 wifi
    // Create a logging interceptor to log request and response details
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Logs request & response body
    }

    // Create an OkHttpClient with the logging interceptor and token interceptor
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            // Retrieve the token from AuthManager
            val token = AuthManager.getToken() // Use the companion object method
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token") // Add the token to the request header
                .build()
            chain.proceed(newRequest)
        }
        .build()

    // Create a Gson instance with lenient parsing
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    // Create a Retrofit instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // Create a single instance of ApiService
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}