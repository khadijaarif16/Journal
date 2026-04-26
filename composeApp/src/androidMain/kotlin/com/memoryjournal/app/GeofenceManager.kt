package com.memoryjournal.app
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.PendingIntent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

//context - reference to the app - used to access systemt services
class GeofenceManager (private val context: Context){
    private val client = LocationServices.getGeofencingClient(context)
    @SuppressLint("MissingPermission") //skip permissions
    fun add (entryId: String, lat: Double, lng: Double){
        //everytime a new entry is created - save the geofence coordinates
        val geofence= Geofence.Builder()
            .setRequestId(entryId) //to know which id to trigger later
            .setCircularRegion(lat, lng, 200f)//200m radius around the location
            .setExpirationDuration(Geofence.NEVER_EXPIRE)//watch forever
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)//trigger when inside teh region
            .build()
        //request created
        val request = GeofencingRequest.Builder()
            .addGeofence(geofence)
            .build()
        //speaks to the receiver
        val intent = Intent(context, GeofenceReceiver::class.java)
        //when geofence triggered, do this
        val pendingIntent = PendingIntent.getBroadcast(context,0,intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
        client.addGeofences(request,pendingIntent) //registered and android is watching even when app closed
    }
}

//called when user is inside
// BroadcastReceiver - wakes your app up in the background
//when user inside the geofence, it sends a broadcast and our receiver(onReceive) picks it up
class GeofenceReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        android.util.Log.d("GeofenceReceiver", "onReceive called!")
        val cts= context ?: return //return if context is null
        val safeIntent = intent ?: return
        //intent and context cannot be null
        val event = GeofencingEvent.fromIntent(safeIntent)


        // if no geofencing event (test broadcast) show test notification
        if (event == null || event.hasError() || event.triggeringGeofences.isNullOrEmpty()) {
            val entryId = safeIntent.getStringExtra("entryId")?: "test_entry"
            android.util.Log.d("GeofenceReceiver", "Test broadcast - showing test notification")
            notify(cts, entryId)
            return
        }
        //notifications for triggered events - could trigger multiple entries in the same place
        event?.triggeringGeofences?.forEach { geofence ->
            android.util.Log.d("GeofenceReceiver", "Triggered: ${geofence.requestId}")
            notify(cts, geofence.requestId) }
    }
    private fun notify(context: Context, entryId: String){
        //intent to open the app when tppaed on notif
        val intent = Intent(
            Intent.ACTION_VIEW,
            android.net.Uri.parse("memoryjournal://entry/$entryId")
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent= PendingIntent.getActivity(context,entryId.hashCode(),intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        //channel to push notifications
        val channel = NotificationChannel(
            "memories", "Memories", NotificationManager.IMPORTANCE_HIGH
        )
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        try {
            NotificationManagerCompat.from(context).notify(
                entryId.hashCode(), NotificationCompat.Builder(context, "memories")
                    .setSmallIcon(android.R.drawable.ic_dialog_map)
                    .setContentTitle("You have a memory here!")
                    .setContentText("Tap to relive this memory")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)//open app when tapped
                    .build()
            )
            android.util.Log.d("GeofenceReceiver", "Notification sent!")
        }catch (e: SecurityException){android.util.Log.e("GeofenceReceiver", "Security exception: ${e.message}")}
        catch (e: Exception) {
            android.util.Log.e("GeofenceReceiver", "Error: ${e.message}")
        }
        }
    }


