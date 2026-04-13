package com.memoryjournal.app
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class AppViewModel: ViewModel() {
    //firebase repo
    private val repository = FirebaseRepository()
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
                repository.saveEntry(entry)
            }catch (e: Exception){_error.value= e.message}
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
