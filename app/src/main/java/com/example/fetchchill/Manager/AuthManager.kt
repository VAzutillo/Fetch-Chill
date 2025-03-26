package com.example.fetchchill.utils

import android.content.SharedPreferences
import android.provider.Settings.Global.putInt
import android.util.Log
import com.example.fetchchill.api.RetrofitClient
import com.example.fetchchill.model.LoginResponse
import com.example.fetchchill.model.LoginUser
import com.example.fetchchill.model.LogoutResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.HttpException
import java.io.IOException

class AuthManager(private val sharedPreferences: SharedPreferences) {

    companion object {
        private lateinit var instance: AuthManager

        // Initialize the AuthManager instance
        fun initialize(sharedPreferences: SharedPreferences) {
            instance = AuthManager(sharedPreferences)
        }

        // Retrieve the token from SharedPreferences
        fun getToken(): String? {
            return instance.sharedPreferences.getString("auth_token", null)
        }

        // Retrieve user ID from SharedPreferences statically
        fun getUserId(): Int {
            return instance.userId()
        }
    }
//    var userId: Int?
//        get() = sharedPreferences.getInt("user_id", -1).takeIf { it != -1 }
//        set(value) = sharedPreferences.edit() { putInt("user_id", value ?: -1) }

    // Save user ID to SharedPreferences
    fun saveUserId(userId: Int) {
        sharedPreferences.edit().putInt("user_id", userId).apply()
        Log.d("AuthManager", "User ID saved: $userId")
    }

    // Retrieve user ID from SharedPreferences
    fun userId(): Int {
        val id = sharedPreferences.getInt("user_id", -1) // Return -1 if user ID is not found
        Log.d("AuthManager", "Retrieved user ID: $id from SharedPreferences")
        return id
    }

    // Save authentication token to SharedPreferences
    fun saveToken(token: String) {
        sharedPreferences.edit().putString("auth_token", token).apply()
        Log.d("AuthManager", "Token saved: $token")
    }

    // Login function
    fun login(
        email: String,
        password: String,
        onSuccess: (LoginResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val loginUser = LoginUser(email, password)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = RetrofitClient.apiService.userLogin(loginUser)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.success) {
                        // Save user ID and token
                        val userId = loginResponse.id ?: loginResponse.userId
                        if (userId != null) {
                            saveUserId(userId)
                        } else {
                            Log.e("AuthManager", "User ID is null!")
                        }

                        val token = loginResponse.token // Retrieve token from the response
                        if (!token.isNullOrEmpty()) {
                            saveToken(token)
                        }

                        onSuccess(loginResponse)
                        Log.d("AuthManager", "Login successful: $loginResponse")
                    } else {
                        onError(loginResponse?.message ?: "Login failed")
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("AuthManager", "Login failed: $errorMsg")
                    onError("Login failed: $errorMsg")
                }
            } catch (e: IOException) {
                Log.e("AuthManager", "Network error: ${e.message}")
                onError("Network error: ${e.message}")
            } catch (e: HttpException) {
                Log.e("AuthManager", "HTTP error: ${e.message}")
                onError("HTTP error: ${e.message}")
            } catch (e: Exception) {
                Log.e("AuthManager", "Unexpected error: ${e.message}")
                onError("Unexpected error: ${e.message}")
            }
        }
    }

    // Local logout - clear SharedPreferences
    fun logout() {
        sharedPreferences.edit().clear().apply()
        Log.d("AuthManager", "Local logout - cleared SharedPreferences")
    }

    // Network logout function
    fun networkLogout(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        // Include the authentication token in the request if possible
        val token = getToken()

        RetrofitClient.apiService.logout().enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(
                call: Call<LogoutResponse>,
                response: Response<LogoutResponse>
            ) {
                if (response.isSuccessful) {
                    val logoutResponse = response.body()

                    // Consider both successful logout and "No active session" as successful scenarios
                    if (logoutResponse?.success == true || logoutResponse?.message == "No active session") {
                        // Clear local storage after successful network logout or no active session
                        logout()
                        Log.d("AuthManager", "Logout successful or no active session")
                        onSuccess()
                    } else {
                        val errorMsg = logoutResponse?.message ?: "Logout failed"
                        Log.e("AuthManager", errorMsg)
                        onError(errorMsg)
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown logout error"
                    Log.e("AuthManager", errorMsg)
                    onError(errorMsg)
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                val errorMsg = "Network error: ${t.message}"
                Log.e("AuthManager", errorMsg)

                // Even on network failure, perform local logout
                logout()
                onSuccess()
            }
        })
    }
}