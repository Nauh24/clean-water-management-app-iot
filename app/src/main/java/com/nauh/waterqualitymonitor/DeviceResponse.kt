package com.nauh.waterqualitymonitor

data class DeviceResponse(
    val message: String,
    val status: Int,
    val metadata: List<Device>
)