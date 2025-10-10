package com.solicodet.parkingsystem.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
//import java.math.RoundingMode


@Composable
fun ParkingFeeScreen() {
    var startText by remember { mutableStateOf("") }
    var endText by remember { mutableStateOf("") }
    var selectedMode by remember { mutableStateOf(RoundingMode.Proportionnel) }
    var currentResult by remember { mutableStateOf<CalculationResult?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        Text(
            "Put your time to calcule",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // Input for start time
        Row(verticalAlignment = Alignment.CenterVertically) {
//            Text("Début : ", style = MaterialTheme.typography.bodyLarge)
            OutlinedTextField(
                value = startText,
                onValueChange = { startText = it },
                label = { Text("Start HH:MM") },
                modifier = Modifier.weight(1f)
            )
        }

        // Input for end time
        Row(verticalAlignment = Alignment.CenterVertically) {
//            Text("Fin : ", style = MaterialTheme.typography.bodyLarge)
            OutlinedTextField(
                value = endText,
                onValueChange = { endText = it },
                label = { Text("End HH:MM") },
                modifier = Modifier.weight(1f)
            )
        }

        // Mode selection
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(" Mode : ", style = MaterialTheme.typography.bodyLarge)
            RoundingMode.values().forEach { mode ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedMode == mode,
                        onClick = { selectedMode = mode }
                    )
                    Text(
                        when (mode) {
                            RoundingMode.Proportionnel -> "Proportionl"
                            RoundingMode.HeureEntamee -> "Heurs"
                        }
                    )
                }
            }
        }

        // Calculate button
        Button(
            onClick = {
                val result = calculate(startText, endText, selectedMode)
                if (result != null) {
                    currentResult = result
                    val item = HistoryItem(startText, endText, selectedMode, result.totalCost)
                    history.add(item)
                    if (history.size > 3) {
                        history.removeAt(0)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Get Total")
        }

        // Current result
        currentResult?.let { result ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Durée totale: ${result.totalMinutes / 60} h ${result.totalMinutes % 60} min",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    result.usages.forEach { usage ->
                        val formattedCost = "%.2f".format(usage.cost).replace('.', ',')
                        Text(
                            "${usage.slotLabel}: ${usage.minutes} min → $formattedCost MAD"
                        )
                    }
                    val formattedTotal = "%.2f".format(result.totalCost).replace('.', ',')
                    Text(
                        "Total: $formattedTotal MAD",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        // History
        if (history.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Derniers calculs:",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    history.reversed().forEachIndexed { index, item ->
                        val modeDisplay = when (item.mode) {
                            RoundingMode.Proportionnel -> "Proportionnel"
                            RoundingMode.HeureEntamee -> "Heure entamée"
                        }
                        val formattedTotal = "%.2f".format(item.total).replace('.', ',')
                        Text(
                            "${index + 1}. ${item.startTime} - ${item.endTime} ($modeDisplay) : $formattedTotal MAD"
                        )
                    }
                }
            }
        }
    }
}