package com.example.fetchchill.model

data class SignUpResponse(
    val success: Boolean,
    val message: String,
    val user: User? = null // Keep this if you want to return user details
)



