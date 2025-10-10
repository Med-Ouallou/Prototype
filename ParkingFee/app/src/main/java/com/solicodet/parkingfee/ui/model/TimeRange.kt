package com.solicodet.parkingfee.ui.model

data class TimeRange(
    val label: String,
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int,
    val rate: Double
)