package com.example.fetchchill.model

data class User(
    val id: Int? = null, // Add this line to include the id
    val name: String,
    val email: String,
    val password: String
)