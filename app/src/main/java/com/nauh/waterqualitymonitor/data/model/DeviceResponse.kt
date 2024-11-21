package com.nauh.waterqualitymonitor.data.model

data class DeviceResponse(
    val message: String,
    val status: Int,
    val metadata: List<Device>
)