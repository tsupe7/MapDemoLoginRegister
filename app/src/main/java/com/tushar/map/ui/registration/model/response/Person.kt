package com.tushar.map.ui.registration.model.response

import com.google.gson.annotations.SerializedName

data class Person (
    @SerializedName("key")
    val key: String,
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("role")
    val role: Role,
)
