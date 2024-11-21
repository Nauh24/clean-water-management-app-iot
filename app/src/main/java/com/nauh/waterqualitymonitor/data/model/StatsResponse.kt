package com.nauh.waterqualitymonitor.data.model


data class StatsResponse(
    val message: String,
    val status: Int,
    val metadata: MetadataContainer
)