package com.nauh.waterqualitymonitor.data.model

import com.google.gson.annotations.SerializedName

data class Alert(
    @SerializedName("_id") val id: String,
    @SerializedName("alert_type") val alertType: String,
    @SerializedName("message") val message: String,
    @SerializedName("createdAt") val createdAt: String
)

