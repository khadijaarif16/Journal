package com.memoryjournal.app
import kotlinx.serialization.Serializable
import kotlin.time.Clock

//converting data to json to make it compatible with firebase and weather api calls etc
@Serializable
data class JournalEntry(
    val id: String ="", //firestore id
    val title: String = "", //title of the entry
    val text: String= "", //main text of the entry
    val photoPath: String?= null,
    //only saving path to firestore to avoid storage costs, photo on device's local storage
    //null cause the photo is optional
    val latitude: Double?= null,val longitude: Double?= null,//location
    val locationName: String?=null,
    val weather: String?=null,
    val createdAt:Long = 0L//timestamp

)