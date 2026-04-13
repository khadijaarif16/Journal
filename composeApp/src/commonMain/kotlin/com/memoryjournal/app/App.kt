package com.memoryjournal.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable

fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        Scaffold(
            //only visible on 3 screens - nav bar
            bottomBar = {
                if(currentRoute in listOf("home","map","settings")){
                    NavigationBar {
                        //home
                        NavigationBarItem(
                            icon = {Icon(Icons.Filled.Home, contentDescription = "Home") },
                            label = {Text("Home")},
                            selected = currentRoute=="home",
                            onClick = {navController.navigate("home")}
                        )
                        //map
                        NavigationBarItem(
                            icon = {Icon(Icons.Filled.LocationOn, contentDescription = "Map") },
                            label = {Text("Map")},
                            selected = currentRoute=="map",
                            onClick = {navController.navigate("map")}
                        )
                        //settings
                        NavigationBarItem(
                            icon = {Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                            label = {Text("Settings")},
                            selected = currentRoute=="settings",
                            onClick = {navController.navigate("settings")}
                        )
                    }
                }
            },
            //new entry button
            floatingActionButton = {
                if(currentRoute =="home"){
                    FloatingActionButton(
                        onClick = {navController.navigate("new_entry")}
                    ){Icon(Icons.Filled.Add, contentDescription = "New Entry") }
                }


                }

        ){innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            )
            {
                composable("home"){
                    val entries by rememberEntries()
                    val error by rememberError()
                    HomeScreen(entries = entries, error=error, onEntryClick = {id -> navController.navigate("entry_detail/$id")})}
                composable("new_entry"){NewEntryScreen(navController)}
                composable("entry_detail/{entryId}"){
                    backStackEntry-> val entryId = backStackEntry.arguments?.getString("entryId")?:""
                    EntryDetailScreen(entryId,navController = navController)
                }
                composable("map") { MapScreen(navController) }
                composable("settings") {SettingsScreen() }
            }
        }
    }
}