package com.parkingmanagerapp.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Generic layout for lists within the application
@Composable
fun <T> ListScreenLayout(
    listItems: List<T>,
    isAdminContext: Boolean,
    onEdit: (T) -> Unit,
    onDelete: (T) -> Unit,
    itemContent: @Composable (T, Boolean, (T) -> Unit, (T) -> Unit, Modifier) -> Unit,
    onItemClick: ((T) -> Unit)? = null, // Optional onItemClick lambda
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(listItems) { item ->
            val modifier = Modifier
                .clickable { onItemClick?.invoke(item) }

            itemContent(
                item,
                isAdminContext,
                onEdit,
                onDelete,
                modifier
            )
        }
    }
}