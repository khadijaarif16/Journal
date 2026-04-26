package com.memoryjournal.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

// each platform provides its own implementation of this
//retrieves all entries
@Composable
expect fun rememberEntries(): State<List<JournalEntry>>


@Composable
expect fun rememberError(): State<String?>

//return func to save new entry
@Composable
expect fun rememberSaveEntry(): (String, String,String?,Double?, Double?) -> Unit

//read from firebase with a specific entryid
@Composable
expect fun rememberEntry(entryId:String): State<JournalEntry?>

@Composable
expect fun rememberDeleteEntry(): (String) -> Unit
data class LocationResult(
    val location: State<Pair<Double,Double>?>, //lat,lang pair or null
    val reqLocation: ()-> Unit //function to req loc - permission
)

//null if perm denied, otherwise location
@Composable
expect fun LocationPicker(): LocationResult

//select phot from gallery
data class PhotoResults(
    val photoPath: State<String?>,
    val gallery: () -> Unit,
    val camera: () -> Unit
)
@Composable
expect fun pickPhoto(): PhotoResults

