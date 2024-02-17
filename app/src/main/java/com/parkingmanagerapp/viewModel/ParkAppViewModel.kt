package com.parkingmanagerapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parkingmanagerapp.model.ParkingSlot
import com.parkingmanagerapp.repository.ParkingSlotRepository

class ParkAppViewModel : ViewModel() {

    private val repository = ParkingSlotRepository()

    private val _parkingSlots = MutableLiveData<List<ParkingSlot>>()

    suspend fun fetchParkingSpaces() {
        _parkingSlots.value = repository.getAllParkingSlots()
    }
}