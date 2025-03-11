package com.example.fetchchill.model

data class ProfileSaveResponse(
    val success: Boolean,
    val message: String,
    val profile_id: Int? = null,
    val image_url: String? = null
)
data class ProfileResponse(
    val owner_name: String,
    val pet_name: String,
    val breed: String,
    val age: Int,
    val image_url: String?
)
