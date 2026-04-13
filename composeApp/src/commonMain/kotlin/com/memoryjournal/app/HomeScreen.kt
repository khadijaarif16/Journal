package com.memoryjournal.app
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun HomeScreen(entries: List<JournalEntry>,
               error: String?,
               onEntryClick: (String) -> Unit){
    //display error

    error?.let{
        Text(text = "Error:$it", color = MaterialTheme.colorScheme.error )
    }

    if(entries.isEmpty()){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
        {
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = "No memories yet", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Tap + to create your first entry", style = MaterialTheme.typography.bodyMedium)

            }
        }
    }

    else{
        //view all entries and can scroll through them
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)

        ){
            items(entries){
                entry ->
                EntryCard(entry=entry, onClick = {
                    //open the entry to view using entryid
                    onEntryClick(entry.id)
                })

            }
        }
    }
}

//entrycard
@Composable
fun EntryCard(entry: JournalEntry, onClick:() -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = entry.title.ifEmpty{"Untitled"},
                style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = entry.text, style = MaterialTheme.typography.bodySmall, maxLines = 2)
            if (entry.locationName != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "${entry.locationName}", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}