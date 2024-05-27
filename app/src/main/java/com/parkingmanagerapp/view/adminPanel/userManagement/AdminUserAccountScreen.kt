package com.parkingmanagerapp.view.adminPanel.userManagement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.parkingmanagerapp.model.User
import com.parkingmanagerapp.ui.theme.StandardScreenLayout
import com.parkingmanagerapp.viewModel.AuthViewModel

@Composable
fun AdminUserAccountScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState()
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchAllUsers()
    }

    StandardScreenLayout(
        title = "Manage User Accounts",
        snackbarHostState = snackbarHostState
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { viewModel.sortUsersByName() }) {
                    Text("Sort by Name")
                }
                Button(onClick = { viewModel.sortUsersBySurname() }) {
                    Text("Sort by Surname")
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(users) { user ->
                    UserItem(
                        user = user,
                        onEdit = {
                            selectedUser = user
                            showEditDialog = true
                        },
                        onDelete = {
                            selectedUser = user
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }

        // Edit User Dialog
        if (showEditDialog && selectedUser != null) {
            EditUserDialog(
                user = selectedUser!!,
                onDismiss = { showEditDialog = false },
                onSave = { updatedUser ->
                    viewModel.updateUserProfile(
                        updatedUser.name,
                        updatedUser.surname,
                        updatedUser.phoneNumber,
                        updatedUser.email
                    )
                    showEditDialog = false
                }
            )
        }

        // Delete Confirmation Dialog
        if (showDeleteDialog && selectedUser != null) {
            DeleteUserConfirmationDialog(
                user = selectedUser!!,
                onDismiss = { showDeleteDialog = false },
                onConfirm = {
                    viewModel.deleteUser(selectedUser!!.uID)
                    showDeleteDialog = false
                }
            )
        }
    }
}

@Composable
fun UserItem(
    user: User,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${user.name} ${user.surname}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
                Text(text = user.role.toString(), style = MaterialTheme.typography.bodyMedium)
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}