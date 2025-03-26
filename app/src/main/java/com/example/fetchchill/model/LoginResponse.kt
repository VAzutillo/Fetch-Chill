package com.example.fetchchill.model

data class LoginResponse(
    val success: Boolean,
    val id: Int?, // Assuming id is an Integer
    val message: String?,
    val userId: Int?,
    val token: String? // Assuming token is a String
)

data class LoginUser(
    val email: String,
    val password: String
) {
    companion object {
        fun from(email: String, password: String): LoginUser  {
            return LoginUser (email, password)
        }
    }
}