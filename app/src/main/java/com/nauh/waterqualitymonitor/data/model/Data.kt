package com.nauh.waterqualitymonitor.data.model

// Lớp đại diện cho từng item trong mảng "data"
data class StatData(
    val _id: String,
    val temperature: Int,
    val tds: Int,
    val flowRate: Int,
    val relay: Int,
    val createdAt: String,
    val __v: Int
)

// Lớp đại diện cho phần "meta" trong "metadata"
data class Metadata(
    val total: Int,
    val page: Int,
    val limit: Int,
    val totalPages: Int
)

// Lớp đại diện cho phần "metadata"
data class MetadataContainer(
    val data: List<StatData>, // Mảng các StatData
    val meta: Metadata        // Thông tin meta
)