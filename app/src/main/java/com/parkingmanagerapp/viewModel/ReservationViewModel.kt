package com.parkingmanagerapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.parkingmanagerapp.model.ParkingSlot
import com.parkingmanagerapp.model.Reservation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(
    private val db: FirebaseFirestore
) : ViewModel() {
    private val _parkingSlots = MutableStateFlow<List<ParkingSlot>>(emptyList())
    val parkingSlots: StateFlow<List<ParkingSlot>> = _parkingSlots

    private val _reservations = MutableStateFlow<List<Reservation>>(emptyList())

    init {
        fetchParkingSlots()
        fetchReservations()
    }

    private fun fetchParkingSlots() {
        viewModelScope.launch {
            val snapshot = db.collection("parkingSlots").get().await()
            val slots = snapshot.documents.map { doc ->
                doc.toObject(ParkingSlot::class.java)!!
            }
            _parkingSlots.value = slots
        }
    }

    private fun fetchReservations() {
        viewModelScope.launch {
            val snapshot = db.collection("reservations").get().await()
            val res = snapshot.documents.map { doc ->
                doc.toObject(Reservation::class.java)!!
            }
            _reservations.value = res
        }
    }

    fun filterAvailableSlots(startDate: Date, endDate: Date) {
        val availableSlots = _parkingSlots.value.filter { slot ->
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
                db.collection("reservations").add(reservation).await()
                fetchReservations() // Refresh reservations
            }
            true
        } else {
            false
        }
    }
}