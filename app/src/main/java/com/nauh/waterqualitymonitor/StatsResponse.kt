package com.nauh.waterqualitymonitor


data class StatsResponse(
    val message: String,
    val status: Int,
    val metadata: MetadataContainer
)