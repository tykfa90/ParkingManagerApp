package com.parkingmanagerapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.parkingmanagerapp.model.ParkingSlot
import com.parkingmanagerapp.repository.ParkingSlotRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ParkingSlotViewModel(private val repository: ParkingSlotRepository) : ViewModel() {
    private val _parkingSlots = MutableStateFlow<List<ParkingSlot>>(emptyList())
    val parkingSlots: StateFlow<List<ParkingSlot>> = _parkingSlots.asStateFlow()

    init {
        loadParkingSlots()
    }

    private fun loadParkingSlots() {
        viewModelScope.launch {
            _parkingSlots.value = repository.getAllParkingSlots()
        }
    }

    class ParkingSlotViewModelFactory(private val repository: ParkingSlotRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ParkingSlotViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ParkingSlotViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
