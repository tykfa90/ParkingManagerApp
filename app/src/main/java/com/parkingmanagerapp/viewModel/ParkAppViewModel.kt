package com.parkingmanagerapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parkingmanagerapp.model.ParkingSpace
import com.parkingmanagerapp.repository.Repository

class ParkAppViewModel : ViewModel() {

    private val repository = Repository()

    val parkingSpaces = MutableLiveData<List<ParkingSpace>>()

    fun fetchParkingSpaces() {
        parkingSpaces.value = repository.getAllParkingSpaces()
    }
}