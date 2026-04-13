package com.memoryjournal.app
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.ui.unit.dp

@Composable
fun MapScreen(navController: NavController) {
    //show all journal entries as pins on a map
    Box(modifier= Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Text( text ="Map view", style =MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your memories will appear as pins.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}