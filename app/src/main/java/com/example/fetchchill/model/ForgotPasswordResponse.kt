package com.example.fetchchill.model

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String
)