package com.memoryjournal.app
import android.content.ContentValues
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import android.net.Uri
import androidx.compose.runtime.MutableState
import kotlin.contracts.contract

@Composable
actual fun pickPhoto() : PhotoResults{
    val context = LocalContext.current
    val photoPath = remember { mutableStateOf<String?>(null) }
    val cameraURI= remember { mutableStateOf<Uri?>(null) }
    //launch the gallery and pick a photo
    val galleryLaunch = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        uri -> uri?.let{ photoPath.value = it.toString()}
    }

    //launch camera, returns true if photo is taken
    val cameraLaunch = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if(success){
            //save photo
            photoPath.value =cameraURI.value?.toString()
        }
    }

    val camPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted){
            //media store in androids library - stores photo locally
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.TITLE, "memory_${System.currentTimeMillis()}")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            }
            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
            )
            cameraURI.value = uri // store so that it can be accessed later
            uri?.let{cameraLaunch.launch(it)}//open camera
        }
    }

    return PhotoResults(
        photoPath = photoPath,
        gallery= {galleryLaunch.launch("image/*")}, //tells device to show images
        camera ={
            //request permission first then open camera
            camPermission.launch(android.Manifest.permission.CAMERA)
        }
    )
}