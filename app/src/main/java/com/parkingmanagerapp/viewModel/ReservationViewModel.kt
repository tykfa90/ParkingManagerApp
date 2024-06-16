package com.parkingmanagerapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parkingmanagerapp.model.ParkingSlot
import com.parkingmanagerapp.model.Reservation
import com.parkingmanagerapp.repository.ReservationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(
    private val reservationsRepository: ReservationsRepository
) : ViewModel() {

    private val _parkingSlots = MutableStateFlow<List<ParkingSlot>>(emptyList())
    val parkingSlots: StateFlow<List<ParkingSlot>> = _parkingSlots

    fun filterAvailableSlots(startDate: Date, endDate: Date) {
        viewModelScope.launch {
            val availableSlots = reservationsRepository.getAvailableSlots(startDate, endDate)
            _parkingSlots.value = availableSlots
        }
    }

    fun createReservation(reservation: Reservation) {
        TODO("Not yet implemented")
    }
}