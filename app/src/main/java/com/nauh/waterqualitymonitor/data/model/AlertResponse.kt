package com.nauh.waterqualitymonitor.data.model

import com.google.gson.annotations.SerializedName

data class AlertResponse(
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int,
    @SerializedName("metadata") val metadata: List<Alert>
)
