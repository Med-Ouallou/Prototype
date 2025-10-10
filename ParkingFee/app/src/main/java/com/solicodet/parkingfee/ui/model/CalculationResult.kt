package com.solicodet.parkingfee.ui.model

data class CalculationResult(
    val startInput: String,
    val endInput: String,
    val mode: String,
    val total: Double,
    val durationMinutes: Int,
    val details: List<FeeDetail>
)

data class FeeDetail(
    val label: String,
    val minutes: Int,
    val amount: Double
)