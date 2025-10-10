package com.solicodet.parkingsystem.ui

import androidx.compose.runtime.mutableStateListOf
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

// Data class
data class TimeSlot(val label: String, val startMin: Int, val endMin: Int, val rate: Double)

// Enum class to chose between to modes
enum class RoundingMode {
    Proportionnel,
    HeureEntamee
}


data class SlotUsage(val slotLabel: String, val minutes: Int, val cost: Double)
data class CalculationResult(val usages: List<SlotUsage>, val totalMinutes: Int, val totalCost: Double)

data class HistoryItem(val startTime: String, val endTime: String, val mode: RoundingMode, val total: Double)

// Predefined list of time slots for a full day
val slots = listOf(
    TimeSlot("Nuit", 0, 480, 4.0),       // 00:00 - 07:59
    TimeSlot("Jour", 480, 1140, 8.0),    // 08:00 - 18:59
    TimeSlot("Soir", 1140, 1440, 6.0)     // 19:00 - 23:59
)

// A mutable state list to hold the history of calculations, observable by the Compose UI
val history = mutableStateListOf<HistoryItem>()

/**
 * Parses a time string in "HH:mm" format into total minutes from midnight.
 * @return The total minutes, or null if the format is invalid.
 */
fun parseTime(timeStr: String): Int? {
    // Check for the correct format "HH:mm" before proceeding
    if (!timeStr.matches(Regex("\\d{2}:\\d{2}"))) return null

    return try {
        val parts = timeStr.split(":")
        val h = parts[0].toInt()
        val m = parts[1].toInt()
        if (h !in 0..23 || m !in 0..59) return null
        h * 60 + m
    } catch (_: Exception) {
        // The exception is not used, so it's conventional to name it "_"
        null
    }
}

/**
 * Calculates the total parking cost based on start and end times and a rounding mode.
 * @return A [CalculationResult] containing detailed usage and totals, or null on error.
 */
fun calculate(startStr: String, endStr: String, mode: RoundingMode): CalculationResult? {
    val startMin = parseTime(startStr) ?: return null
    val endMin = parseTime(endStr) ?: return null

    // A parking session can wrap around midnight (e.g., 22:00 to 02:00)
    val isWrapping = endMin <= startMin
    val periods = if (!isWrapping) {
        listOf(startMin to endMin)
    } else {
        listOf(startMin to 1440, 0 to endMin) // Split into two periods: before and after midnight
    }

    val allUsages = mutableListOf<SlotUsage>()
    var totalMinutesOverall = 0
    for ((pStart, pEnd) in periods) {
        for (slot in slots) {
            val overlapMinutes = max(0, min(pEnd, slot.endMin) - max(pStart, slot.startMin))
            if (overlapMinutes > 0) {
                totalMinutesOverall += overlapMinutes
                val hours = if (mode == RoundingMode.Proportionnel) {
                    overlapMinutes / 60.0
                } else {
                    // Correctly round up to the nearest full hour
                    ceil(overlapMinutes / 60.0)
                }
                val cost = hours * slot.rate
                // The overlap is already an Int, so .toInt() is redundant
                allUsages.add(SlotUsage(slot.label, overlapMinutes, cost))
            }
        }
    }
    val totalCost = allUsages.sumOf { it.cost }
    return CalculationResult(allUsages, totalMinutesOverall, totalCost)
}