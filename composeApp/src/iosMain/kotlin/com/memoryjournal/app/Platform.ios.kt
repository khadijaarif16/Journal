package com.memoryjournal.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel

// iOS stub — returns empty data
@Composable
actual fun rememberEntries(): State<List<JournalEntry>> {
    return remember { mutableStateOf(emptyList()) }
}

@Composable
actual fun rememberError(): State<String?> {
    return remember { mutableStateOf(null) }
}

@Composable
actual fun rememberSaveEntry(): (String, String, Double?, Double?) -> Unit {
    return{ _,_,_,_-> }
}

@Composable
actual fun rememberEntry(entryId: String): State<JournalEntry?> {
    return remember{mutableStateOf(null)}
}

@Composable
actual fun LocationPicker(): LocationResult{
    return LocationResult(
        location = remember { mutableStateOf(null)},
            reqLocation = {}
    )
}
