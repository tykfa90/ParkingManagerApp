package com.parkingmanagerapp.viewModel

import androidx.lifecycle.ViewModel
import com.parkingmanagerapp.utility.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    val networkMonitor: NetworkMonitor
) : ViewModel()