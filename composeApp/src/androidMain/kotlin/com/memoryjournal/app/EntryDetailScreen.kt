package com.memoryjournal.app

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun EntryDetailScreen(entryId: String, navController: NavController) {
    //load real data from firebase later
    //entryid from home screen when card is tapped on
    val entry by rememberEntry(entryId)

    Scaffold(
        topBar = { TopAppBar(
            title= {Text(entry?.title?.ifEmpty{"Memory"}?:"Memory")},
            //back nav
            navigationIcon = {IconButton(onClick = {navController.popBackStack()}){
                Icon(Icons.Filled.ArrowBack,  contentDescription  = "Back")
            }
            }
        )}
    ){ innerPadding ->
        //scrollable entries
        entry?.let {entry ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                //photo if exists
                if (entry.photoPath != null) {
                    //Text("Placeholder photot")
                    AsyncImage(
                        model = entry.photoPath, contentDescription = "photo",
                        modifier= Modifier.fillMaxWidth().height(250.dp),
                        contentScale =  ContentScale.Crop
                    )
                }

                Text(
                    text = entry.text, style = MaterialTheme.typography.bodyLarge
                )
                HorizontalDivider()
                //location info if availble
                if (entry.locationName != null) {
                    Text(
                        text = "${entry.locationName}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                //weather info if availble
                if (entry.weather != null) {
                    Text(text = "${entry.weather}", style = MaterialTheme.typography.bodyMedium)
                }
                //timestamp - orignally milliseconds but formatted
                val formattedDate = java.text.SimpleDateFormat("MMMM d,yyyy 'at' h:mm:a",
                java.util.Locale.getDefault()).format(java.util.Date(entry.createdAt))
                Text(text = "${formattedDate}", style = MaterialTheme.typography.labelSmall)

            }
        }
    }
}