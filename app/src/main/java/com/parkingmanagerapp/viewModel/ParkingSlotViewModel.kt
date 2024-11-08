package com.parkingmanagerapp.viewModel

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

    private val _isViewingAsAdmin = MutableStateFlow(false)
    val isViewingAsAdmin: StateFlow<Boolean> = _isViewingAsAdmin.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        fetchParkingSlots()
    }

    fun setViewingContextAsAdmin(isAdmin: Boolean) {
        _isViewingAsAdmin.value = isAdmin
    }

    private fun fetchParkingSlots() {
        viewModelScope.launch {
            val result = repository.getAllParkingSlots()
            result.fold(
                onSuccess = { slots ->
                    _parkingSlots.value = slots
                },
                onFailure = { error ->
                    _errorMessage.value = "Failed to fetch parking slots: ${error.localizedMessage}"
                }
            )
        }
    }

    fun addNewParkingSlot(parkingSlot: ParkingSlot) {
        viewModelScope.launch {
            val result = repository.addParkingSlot(parkingSlot)
            result.fold(
                onSuccess = {
                    fetchParkingSlots()
                },
                onFailure = { error ->
                    _errorMessage.value = "Failed to add parking slot: ${error.localizedMessage}"
                }
            )
        }
    }

    fun modifyParkingSlot(slotID: String, updatedSlot: ParkingSlot) {
        viewModelScope.launch {
            val result = repository.updateParkingSlot(slotID, updatedSlot)
            result.fold(
                onSuccess = {
                    fetchParkingSlots()
                },
                onFailure = { error ->
                    _errorMessage.value = "Failed to modify parking slot: ${error.localizedMessage}"
                }
            )
        }
    }

    fun removeParkingSlot(slotID: String) {
        viewModelScope.launch {
            val result = repository.deleteParkingSlot(slotID)
            result.fold(
                onSuccess = {
                    fetchParkingSlots()
                },
                onFailure = { error ->
                    _errorMessage.value = "Failed to remove parking slot: ${error.localizedMessage}"
                }
            )
        }
    }
}