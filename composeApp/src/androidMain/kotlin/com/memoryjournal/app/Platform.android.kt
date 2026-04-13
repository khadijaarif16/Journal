package com.memoryjournal.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

// Android — gets real data from Firebase via ViewModel
@Composable
actual fun rememberEntries(): State<List<JournalEntry>> {
    val viewModel: AppViewModel = viewModel()
    return viewModel.entries.collectAsState()
}

@Composable
actual fun rememberError(): State<String?> {
    val viewModel: AppViewModel = viewModel()
    return viewModel.error.collectAsState()
}

@Composable
actual fun rememberSaveEntry(): (String, String) -> Unit {
    val viewModel: AppViewModel = viewModel()
    return{ title,text-> viewModel.saveEntry(title,text)}
}

@Composable
actual fun rememberEntry(entryId: String): State<JournalEntry?> {
    val viewModel: AppViewModel = viewModel()
    //find in the esisting list
    val entries = viewModel.entries.collectAsState()
    return remember(entries.value){
        mutableStateOf(entries.value.find{it.id ==entryId})
    }
}

