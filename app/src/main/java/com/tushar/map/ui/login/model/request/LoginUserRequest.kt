package com.tushar.map.ui.login.model.request

import com.google.gson.annotations.SerializedName


data class LoginUserRequest (
    @SerializedName("email")
    val email: String?,
    @SerializedName("password")
    val password: String?
    )