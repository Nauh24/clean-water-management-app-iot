package com.nauh.waterqualitymonitor.data.model

// Lớp đại diện cho từng item trong mảng "data"
data class StatData(
//    val _id: String,
    val temperature: Double,
    val tds: Double,
    val flowRate: Double,
    val relay: Int,
    var createdAt: String,
//    val __v: Int
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