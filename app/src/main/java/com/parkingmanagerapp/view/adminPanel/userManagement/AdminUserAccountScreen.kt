package com.parkingmanagerapp.view.adminPanel.userManagement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.parkingmanagerapp.model.User
import com.parkingmanagerapp.ui.theme.StandardScreenLayout
import com.parkingmanagerapp.viewModel.AuthViewModel

@Suppress("UNUSED_PARAMETER")
@Composable
fun AdminUserAccountScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState()
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var showDisableDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchAllUsers()
    }

    StandardScreenLayout(
        title = "Manage User Accounts",
        snackbarHostState = snackbarHostState
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            var isScrolledToEnd by remember { mutableStateOf(false) }

            // Sort reservations by start date
            val sortedUserAccounts = users.sortedBy { it.surname }

            // Added LazyColumn for scrollable user list with indicator arrows for better usability
            LazyColumn(
                contentPadding = PaddingValues(bottom = 48.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(sortedUserAccounts) { user ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                2.dp,
                                shape = androidx.compose.material3.MaterialTheme.shapes.medium
                            ),
                        colors = CardDefaults.cardColors(containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface)
                    ) {
                        UserItem(
                            user = user,
                            onDisableOrEnable = {
                                selectedUser = user
                                showDisableDialog = true
                            },
                            modifier = Modifier.padding(16.dp)
                        )

                    }

                }

                // Set isScrolledToEnd to true if there are more items beyond visible space
                isScrolledToEnd = if (sortedUserAccounts.size > 3) {
                    false
                } else {
                    true
                }
            }

            // Scroll indicator to inform users they can scroll down/up
            if (!isScrolledToEnd && sortedUserAccounts.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Scroll down for more",
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.Gray, CircleShape)
                            .padding(4.dp),
                        tint = Color.White
                    )
                }
            }

            // Disable Confirmation Dialog
            if (showDisableDialog && selectedUser != null) {
                DisableUserConfirmationDialog(
                    user = selectedUser!!,
                    onDismiss = { showDisableDialog = false },
                    onConfirm = {
                        if (selectedUser!!.active) {
                            viewModel.deactivateUser(selectedUser!!.uid)
                        } else {
                            viewModel.activateUser(selectedUser!!.uid)
                        }
                        showDisableDialog = false
                    }
                )
            }
        }
    }
}