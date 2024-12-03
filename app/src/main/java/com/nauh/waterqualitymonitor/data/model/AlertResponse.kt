package com.nauh.waterqualitymonitor.data.model

import com.google.gson.annotations.SerializedName

data class AlertResponse(
    val message: String,
    val status: Int,
    val metadata: List<Alert>
)
