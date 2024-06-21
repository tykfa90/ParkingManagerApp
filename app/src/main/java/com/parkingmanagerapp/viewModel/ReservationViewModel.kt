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
    private val reservationRepository: ReservationsRepository
) : ViewModel() {
    private val _reservations = MutableStateFlow<List<Reservation>>(emptyList())
    private val _userReservations = MutableStateFlow<List<Reservation>>(emptyList())
    val userReservations: StateFlow<List<Reservation>> = _userReservations

    init {
        fetchReservations()
    }

    private fun fetchReservations() {
        viewModelScope.launch {
            _reservations.value = reservationRepository.getAllReservations()
        }
    }

    fun loadUserReservations(userID: String) {
        viewModelScope.launch {
            _userReservations.value = reservationRepository.getUserReservations(userID)
        }
    }

    fun filterAvailableSlots(startDate: Date, endDate: Date, parkingSlots: List<ParkingSlot>): List<ParkingSlot> {
        return parkingSlots.filter { slot ->
            _reservations.value.none { res ->
                res.parkingSlotID == slot.parkingSlotID &&
                        ((startDate in res.reservationStart..res.reservationEnd) ||
                                (endDate in res.reservationStart..res.reservationEnd) ||
                                (res.reservationStart in startDate..endDate) ||
                                (res.reservationEnd in startDate..endDate))
            }
        }
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
                reservationRepository.addReservation(reservation)
                fetchReservations() // Refresh reservations
            }
            true
        } else {
            false
        }
    }

    fun cancelReservation(reservationID: String) {
        viewModelScope.launch {
            reservationRepository.deleteReservation(reservationID)
            fetchReservations() // Refresh reservations
            loadUserReservations(_userReservations.value.firstOrNull()?.userID ?: "")
        }
    }
}