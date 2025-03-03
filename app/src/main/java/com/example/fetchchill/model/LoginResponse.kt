package com.example.fetchchill.model

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val userId: Int? = null
)

data class LoginUser (
    val email: String,
    val password: String
)