package com.memoryjournal.app
import android.app.Application
import androidx.lifecycle.AndroidViewModel

//import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
// get access to app context safely
class AppViewModel(application: Application): AndroidViewModel(application) {
    //firebase repo
    private val repository = FirebaseRepository()
    private val geofenceManager = GeofenceManager(application)
    private val _entries = MutableStateFlow<List<JournalEntry>>(emptyList())
    val entries: StateFlow<List<JournalEntry>> = _entries.asStateFlow() //for the ui

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init{
        //load entries as soon as viewmodel created
        loadEntries()
    }

    private fun loadEntries(){
        viewModelScope.launch {
            try{
                repository.getEntries().collect { entries ->
                _entries.value = entries
                }
            } catch(e:Exception){
                _error.value = e.message
            }
        }
    }
    //save to firebase
    fun saveEntry(
        title: String, text:String, photoPath:String?=null,
        latitude:Double?=null,longitude:Double?=null,locationName:String?=null,
        weather:String?=null
    ){
        viewModelScope.launch {
            try{
                val entry = JournalEntry(
                    title=title,text=text,photoPath=photoPath,
                    latitude = latitude, longitude = longitude,
                    locationName = locationName, weather = weather
                )
                //save to firestore but also get entryId for geofencinh
                val entryId=repository.saveEntry(entry)
                android.util.Log.d("AppViewModel", "Entry saved with id: $entryId")

                //add geofence if location is available
                if(latitude!= null && longitude != null){geofenceManager.add(entryId, latitude, longitude)}
            }catch (e: Exception) {
                _error.value = e.message
                android.util.Log.e("AppViewModel", "Save failed: ${e.message}")
            }
        }
    }

    fun deleteEntry(entryId:String){
        viewModelScope.launch {
            try{
                repository.deleteEntry(entryId)
            }catch (e: Exception){_error.value = e.message}
        }
    }

}
