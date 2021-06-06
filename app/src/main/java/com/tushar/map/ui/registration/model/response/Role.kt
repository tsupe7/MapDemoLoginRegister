package com.tushar.map.ui.registration.model.response

import com.google.gson.annotations.SerializedName

data class Role (
    @SerializedName("key")
    val key: String,
    @SerializedName("rank")
    val rank: String,
)