package com.memoryjournal.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

// each platform provides its own implementation of this
@Composable
expect fun rememberEntries(): State<List<JournalEntry>>

@Composable
expect fun rememberError(): State<String?>

//return func to save new entry
@Composable
expect fun rememberSaveEntry(): (String, String) -> Unit

//read from firebase
@Composable
expect fun rememberEntry(entryId:String): State<JournalEntry?>



//null if perm denied, otherwise location
@Composable
expect fun LocationPicker(): Pair<State<Pair<Double, Double>?>, () -> Unit>