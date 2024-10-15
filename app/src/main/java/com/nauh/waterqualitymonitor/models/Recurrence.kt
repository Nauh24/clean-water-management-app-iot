package com.nauh.waterqualitymonitor.models

sealed class Recurrence(val name: String, val target: String) {
    object None : Recurrence("Không", "None")
    object Monthly : Recurrence("Hàng tháng", "Tháng này")
    object Yearly : Recurrence("Hàng năm", "Năm nay")
}

fun String.toRecurrence(): Recurrence {
    return when(this) {
        "None" -> Recurrence.None
        "Monthly" -> Recurrence.Monthly
        "Yearly" -> Recurrence.Yearly
        else -> Recurrence.None
    }
}