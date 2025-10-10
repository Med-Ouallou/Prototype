package com.solicodet.parkingsystem

import android.os.Bundle
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import com.solicodet.parkingsystem.ui.ParkingFeeScreen
import androidx.compose.material3.Surface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Surface(
                modifier = Modifier.padding(top = 60.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                MaterialTheme {
                    ParkingFeeScreen()
                }
            }
        }
    }
}
