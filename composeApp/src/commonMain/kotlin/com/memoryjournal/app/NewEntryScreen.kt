package com.memoryjournal.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEntryScreen(navController: NavController) {
    //record changes in title and text of an entry
    var title by remember { mutableStateOf("") }
    var text by remember {mutableStateOf("")}
    val saveEntry = rememberSaveEntry()
    val locationPicker = LocationPicker()
    val location by locationPicker.location

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
                onClick = {},modifier = Modifier.fillMaxWidth()
            ){Text("Add Photo")}
            OutlinedButton(
                onClick = {locationPicker.reqLocation()},modifier = Modifier.fillMaxWidth()
            ){Text(if (location!= null)"Location captured" else "Add Location")}


            //save button
            Button(
                onClick={
                    saveEntry(title, text,location?.first,location?.second)
                    navController.popBackStack() //go to home
                }, modifier = Modifier.fillMaxWidth()
            ){Text("Save Memory")}
        }
    }
}