package com.parkingmanagerapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parkingmanagerapp.model.ParkingSlot
import com.parkingmanagerapp.repository.ParkingSlotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParkingSlotViewModel @Inject constructor(private val repository: ParkingSlotRepository) :
    ViewModel() {
    private val _parkingSlots = MutableStateFlow<List<ParkingSlot>>(emptyList())
    val parkingSlots: StateFlow<List<ParkingSlot>> = _parkingSlots.asStateFlow()

    init {
        fetchParkingSlots()
    }

    private fun fetchParkingSlots() {
        viewModelScope.launch {
            _parkingSlots.value = repository.getAllParkingSlots()
        }
    }

    fun addNewParkingSlot(parkingSlot: ParkingSlot) {
        viewModelScope.launch {
            val result = repository.addParkingSlot(parkingSlot)
            if (result.isSuccess) {
                fetchParkingSlots() // Reload the list to include the new slot
            } else {
                Log.e("ParkingSlotVM", "Error during adding new parking slot.")
            }
        }
    }

    fun modifyParkingSlot(slotID: String, updatedSlot: ParkingSlot) {
        viewModelScope.launch {
            val result = repository.updateParkingSlot(slotID, updatedSlot)
            if (result.isSuccess) {
                fetchParkingSlots() // Reload the list to reflect the update
            } else {
                Log.e("ParkingSlotVM", "Error during modifying a parking slot.")
            }
        }
    }

    fun removeParkingSlot(slotID: String) {
        viewModelScope.launch {
            val result = repository.deleteParkingSlot(slotID)
            if (result.isSuccess) {
                fetchParkingSlots() // Reload the list to reflect the deletion
            } else {
                Log.e("ParkingSlotVM", "Error during removing a parking slot.")
            }
        }
    }
}