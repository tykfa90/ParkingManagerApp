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

    // State to indicate if the user is viewing as admin
    private val _isViewingAsAdmin = MutableStateFlow(false)
    val isViewingAsAdmin: StateFlow<Boolean> = _isViewingAsAdmin.asStateFlow()

    init {
        fetchParkingSlots()
    }

    // Set viewing context
    fun setViewingContextAsAdmin(isAdmin: Boolean) {
        _isViewingAsAdmin.value = isAdmin
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
                fetchParkingSlots()
            } else {
                Log.e("ParkingSlotVM", "Error during adding new parking slot.")
            }
        }
    }

    fun modifyParkingSlot(slotID: String, updatedSlot: ParkingSlot) {
        viewModelScope.launch {
            val result = repository.updateParkingSlot(slotID, updatedSlot)
            if (result.isSuccess) {
                fetchParkingSlots()
            } else {
                Log.e("ParkingSlotVM", "Error during modifying a parking slot.")
            }
        }
    }

    fun removeParkingSlot(slotID: String) {
        viewModelScope.launch {
            val result = repository.deleteParkingSlot(slotID)
            if (result.isSuccess) {
                fetchParkingSlots()
            } else {
                Log.e("ParkingSlotVM", "Error during removing a parking slot.")
            }
        }
    }
}