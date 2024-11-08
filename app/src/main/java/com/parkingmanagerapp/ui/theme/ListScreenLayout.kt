package com.parkingmanagerapp.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> ListScreenLayout(
    listItems: List<T>,
    isAdminContext: Boolean,
    onEdit: (T) -> Unit,
    onDelete: (T) -> Unit,
    itemContent: @Composable (T, Boolean, (T) -> Unit, (T) -> Unit, Modifier) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(listItems) { item ->
            itemContent(item, isAdminContext, onEdit, onDelete, Modifier)
        }
    }
}