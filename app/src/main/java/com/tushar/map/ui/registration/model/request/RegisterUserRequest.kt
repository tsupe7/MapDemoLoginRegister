package com.tushar.map.ui.registration.model.request

import com.google.gson.annotations.SerializedName

data class RegisterUserRequest(
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

