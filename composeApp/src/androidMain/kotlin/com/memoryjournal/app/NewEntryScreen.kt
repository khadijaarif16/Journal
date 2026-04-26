package com.memoryjournal.app

import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun NewEntryScreen(navController: NavController) {
    //record changes in title and text of an entry
    var title by remember { mutableStateOf("") }
    var text by remember {mutableStateOf("")}
    val saveEntry = rememberSaveEntry()
    val locationPicker = LocationPicker()
    val location by locationPicker.location
    val photoPicker = pickPhoto()
    val photoPath by photoPicker.photoPath

    var photoDialog by remember { mutableStateOf(false)}

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("New Memory")},
                //back icon
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}){
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            //title
            OutlinedTextField(
                value = title, onValueChange = {title=it}, //updates when chnaged
                label = {Text("Title")},
                modifier = Modifier.fillMaxWidth(), singleLine = true //keep on one line
            )
            //entry text
            OutlinedTextField(
                value = text, onValueChange = {text =it},
                label = { Text("What's on your mind today?")},
                modifier = Modifier.fillMaxWidth().height(200.dp),
                maxLines =10
            )
            OutlinedButton(
                onClick = {photoDialog = true},modifier = Modifier.fillMaxWidth()
            ){Text(
                if (photoPath!= null)"Photo added!"
                else "Add photo."
            )}
            //show photo
            if(photoPath != null){
                AsyncImage(
                    model = photoPath, contentDescription = "selected photo",
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
            //when dialog pops up
            if (photoDialog){
                AlertDialog(
                    onDismissRequest = {photoDialog= false},
                    title ={Text("Add photo")},
                    text = {Text("Choose a source")},
                    confirmButton = {
                        TextButton( onClick = { photoPicker.camera()
                        photoDialog = false}){Text("camera")}

                    },
                    dismissButton = {
                        TextButton( onClick = { photoPicker.gallery()
                                                photoDialog = false}) {Text("Gallery")}

                    }
                )
            }

            OutlinedButton(
                onClick = {locationPicker.reqLocation()},modifier = Modifier.fillMaxWidth()
            ){Text(if (location!= null)"Location captured" else "Add Location")}


            //save button
            Button(
                onClick={
                    saveEntry(title, text,photoPath,location?.first,location?.second)
                    navController.popBackStack() //go to home
                }, modifier = Modifier.fillMaxWidth()
            ){Text("Save Memory")}
        }
    }
}