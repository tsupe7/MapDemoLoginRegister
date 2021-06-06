package com.tushar.map.ui.registration.model.response

import com.google.gson.annotations.SerializedName


data class RegisterUserResponse (
    @SerializedName("authentication_token")
    val authenticationToken: String,

    @SerializedName("person")
    val person: Person,
)


