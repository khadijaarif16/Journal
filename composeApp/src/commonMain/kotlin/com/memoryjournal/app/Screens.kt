package com.memoryjournal.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
expect fun NewEntryScreen(navController: NavController)

@Composable
expect fun EntryDetailScreen(entryId: String, navController: NavController)