package com.tushar.map.ui.login.model.response

import com.google.gson.annotations.SerializedName
import com.tushar.map.ui.registration.model.response.Person


data class LoginUserResponse (
    @SerializedName("authentication_token")
    val authenticationToken: String,

    @SerializedName("person")
    val person: Person
)