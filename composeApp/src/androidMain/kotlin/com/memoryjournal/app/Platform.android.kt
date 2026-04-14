package com.memoryjournal.app

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

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
actual fun rememberSaveEntry(): (String, String,Double?, Double?) -> Unit {
    val viewModel: AppViewModel = viewModel()

    val scope= rememberCoroutineScope ()
    return{ title,text,lat,long->
        //doesnt block UI
        scope.launch {
            viewModel.saveEntry(title = title, text=text, latitude =lat, longitude = long)
        }
        }
    }


//finds entry by entryid

@Composable
actual fun rememberEntry(entryId: String): State<JournalEntry?> {
    val viewModel: AppViewModel = viewModel()
    //find in the esisting list
    val entries = viewModel.entries.collectAsState()
    return remember(entries.value){
        mutableStateOf(entries.value.find{it.id ==entryId})
    }
}

@Composable
actual fun LocationPicker(): LocationResult{
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    //intialize locationa s null
    val locationState = remember { mutableStateOf<Pair<Double, Double>?>(null) }
    //get permission
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        granted -> if(granted){
            //retrieve coordinates
            scope.launch {
                val location = Location(context).getCurrentLocation()
                locationState.value = location
            }
        }
    }

    return LocationResult(location = locationState,
                            reqLocation = {launcher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)})
}