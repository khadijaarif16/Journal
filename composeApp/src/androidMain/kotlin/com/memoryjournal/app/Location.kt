package com.memoryjournal.app

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

class Location(private val context: Context){
    private val locationClient = LocationServices.getFusedLocationProviderClient(context)
    //get current location, null if unavailable
    suspend fun getCurrentLocation(): Pair<Double,Double>?{
        return try {
            val loc = locationClient.getCurrentLocation( Priority.PRIORITY_HIGH_ACCURACY, null).await()
            if(loc!= null){
                Pair(loc.latitude, loc.longitude)
            } else null


        }catch(e: Exception){null}

        }
    }

}