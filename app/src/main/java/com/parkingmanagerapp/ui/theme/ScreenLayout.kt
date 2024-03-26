package com.parkingmanagerapp.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.parkingmanagerapp.model.ParkingSlot

@Composable
fun StandardScreenLayout(
    title: String,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        content(paddingValues)
    }
}

// Specialised layout for the lists within the application
@Composable
fun ListScreenLayout(
    modifier: Modifier = Modifier,
    listItems: List<ParkingSlot>,
    itemContent: @Composable (ParkingSlot) -> Unit
) {
    ParkingManagerAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier.fillMaxSize()
            ) {
                items(listItems.size) { index ->
                    itemContent(listItems[index])
                }
            }
        }
    }
}