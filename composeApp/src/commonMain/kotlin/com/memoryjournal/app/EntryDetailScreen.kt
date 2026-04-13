package com.memoryjournal.app
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryDetailScreen(entryId: String, navController: NavController) {
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
                    Text("Placeholder photot")
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
                //timestamp
                Text(text = "${entry.createdAt}", style = MaterialTheme.typography.labelSmall)

            }
        }
    }
}

