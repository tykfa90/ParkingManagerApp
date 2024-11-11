package com.parkingmanagerapp.view.adminPanel.userManagement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.parkingmanagerapp.model.User

@Composable
fun UserItem(
    user: User,
    onDisableOrEnable: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = "Name: ${user.name}")
        Text(text = "Surname: ${user.surname}")
        Text(text = "Email: ${user.email}")
        Text(text = "Status: ${if (user.active) "Active" else "Disabled"}")

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Button(onClick = onDisableOrEnable) {
                Text(if (user.active) "Disable" else "Enable")
            }
        }
    }
}