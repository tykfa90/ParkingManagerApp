package com.parkingmanagerapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parkingmanagerapp.model.ParkingSlot
import com.parkingmanagerapp.model.Reservation
import com.parkingmanagerapp.repository.ParkingSlotRepository
import com.parkingmanagerapp.repository.ReservationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(
    private val reservationRepository: ReservationsRepository,
    private val parkingSlotRepository: ParkingSlotRepository
) : ViewModel() {

    private val _parkingSlots = MutableStateFlow<List<ParkingSlot>>(emptyList())
    val parkingSlots: StateFlow<List<ParkingSlot>> = _parkingSlots

    private val _reservations = MutableStateFlow<List<Reservation>>(emptyList())
    private val _userReservations = MutableStateFlow<List<Reservation>>(emptyList())
    val userReservations: StateFlow<List<Reservation>> = _userReservations

    private val _parkingSlotLabels = MutableStateFlow<Map<String, String>>(emptyMap())
    val parkingSlotLabels: StateFlow<Map<String, String>> = _parkingSlotLabels

    init {
        fetchParkingSlots()
        fetchReservations()
    }

    private fun fetchParkingSlots() {
        viewModelScope.launch {
            val result = parkingSlotRepository.getAllParkingSlots()
            if (result.isSuccess) {
                val slots = result.getOrNull() ?: emptyList()
                _parkingSlots.value = slots
                _parkingSlotLabels.value = slots.associate { it.parkingSlotID to it.parkingSlotLabel }
            } else {
                // Log the error or handle it
                println("Error fetching parking slots: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    private fun fetchReservations() {
        viewModelScope.launch {
            val result = reservationRepository.getAllReservations()
            if (result.isSuccess) {
                _reservations.value = result.getOrNull() ?: emptyList()
            } else {
                // Log the error or handle it
                println("Error fetching reservations: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun fetchUserReservations(userID: String) {
        viewModelScope.launch {
            val result = reservationRepository.getUserReservations(userID)
            if (result.isSuccess) {
                _userReservations.value = result.getOrNull()?.sortedBy { it.reservationStart } ?: emptyList()
            } else {
                // Log the error or handle it
                println("Error fetching user reservations: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun filterAvailableSlots(startDate: Date, endDate: Date, slots: List<ParkingSlot>) {
        val availableSlots = slots.filter { slot ->
            _reservations.value.none { res ->
                res.parkingSlotID == slot.parkingSlotID &&
                        ((startDate in res.reservationStart..res.reservationEnd) ||
                                (endDate in res.reservationStart..res.reservationEnd) ||
                                (res.reservationStart in startDate..endDate) ||
                                (res.reservationEnd in startDate..endDate))
            }
        }
        _parkingSlots.value = availableSlots
    }

    fun createReservation(reservation: Reservation): Boolean {
        val overlappingReservation = _reservations.value.any { res ->
            res.parkingSlotID == reservation.parkingSlotID &&
                    ((reservation.reservationStart in res.reservationStart..res.reservationEnd) ||
                            (reservation.reservationEnd in res.reservationStart..res.reservationEnd) ||
                            (res.reservationStart in reservation.reservationStart..reservation.reservationEnd) ||
                            (res.reservationEnd in reservation.reservationStart..reservation.reservationEnd))
        }

        return if (!overlappingReservation) {
            viewModelScope.launch {
                val result = reservationRepository.addReservation(reservation)
                if (result.isSuccess) {
                    fetchReservations() // Refresh reservations on success
                } else {
                    println("Error creating reservation: ${result.exceptionOrNull()?.message}")
                }
            }
            true
        } else {
            false
        }
    }

    fun cancelReservation(reservationID: String) {
        viewModelScope.launch {
            val result = reservationRepository.deleteReservation(reservationID)
            if (result.isSuccess) {
                fetchReservations() // Refresh reservations on success
            } else {
                println("Error canceling reservation: ${result.exceptionOrNull()?.message}")
            }
        }
    }
}
