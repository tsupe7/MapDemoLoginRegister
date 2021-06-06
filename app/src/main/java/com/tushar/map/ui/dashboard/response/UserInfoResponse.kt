package com.tushar.map.ui.dashboard.response

import com.google.gson.annotations.SerializedName
import com.tushar.map.ui.registration.model.response.Role


data class UserInfoResponse (
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("display_name")
    val displayName: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("key")
    val key: String?,
    @SerializedName("role")
    val role: Role?,
    @SerializedName("updated_at")
    val updatedAt: String?
)
