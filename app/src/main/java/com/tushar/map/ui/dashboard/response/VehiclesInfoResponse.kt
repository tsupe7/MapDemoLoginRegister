package com.tushar.map.ui.dashboard.response

import com.google.gson.annotations.SerializedName


data class VehiclesInfoResponse (
    @SerializedName("id")
    val id: Int?,
    @SerializedName("is_active")
    val isActive: Boolean?,
    @SerializedName("is_available")
    val isAvailable: Boolean?,
    @SerializedName("lat")
    val lat: String?,
    @SerializedName("lng")
    val lng: String?,
    @SerializedName("license_plate_number")
    val licensePlateNumber: String?,
    @SerializedName("remaining_mileage")
    val remainingMileage: String?,
    @SerializedName("remaining_range_in_meters")
    val remainingRangeInMeters: String?,
    @SerializedName("transmission_mode")
    val transmissionMode: String?,
    @SerializedName("vehicle_make")
    val vehicleMake: String?,
    @SerializedName("vehicle_pic_absolute_url")
    val vehiclePicAbsoluteUrl: String?,
    @SerializedName("vehicle_type")
    val vehicleType: String?,
    @SerializedName("vehicle_type_id")
    val vehicleTypeId: String?
)