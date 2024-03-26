package com.parkingmanagerapp.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.parkingmanagerapp.model.ParkingSlot

@Composable
fun StandardScreenLayout(
    modifier: Modifier = Modifier,
    buttons: List<Pair<String, () -> Unit>>
) {
    ParkingManagerAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    buttons.forEach { (text, action) ->
                        Button(onClick = action) {
                            Text(text)
                        }
                    }
                }
            }
        }
    }
}

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