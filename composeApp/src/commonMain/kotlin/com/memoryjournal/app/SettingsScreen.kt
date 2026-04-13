package com.memoryjournal.app
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    //how close you are to trigger memory
    var radius by remember {mutableStateOf(200f)}

    //enable or disable notif - enabled by default
    var notifications by remember {mutableStateOf(true)}

    Column(modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)){
        Text( text = "Settings", style = MaterialTheme.typography.headlineMedium)
        HorizontalDivider()

        //notif toggle
        Text(text = "Nearby memory notifications", style = MaterialTheme.typography.titleMedium)
        Switch(checked = notifications, onCheckedChange = {notifications=it})

        HorizontalDivider()

        //define your own radius for memories
        Text( text = "Memory trigger radius: ${radius.toInt()}m", style = MaterialTheme.typography.titleMedium)
        Slider(value =radius,
            onValueChange = {radius = it},
            valueRange = 100f..1000f//btw 100m and 1000m
        )

    }

}