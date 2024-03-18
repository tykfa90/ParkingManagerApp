package com.parkingmanagerapp.utility

import com.parkingmanagerapp.model.ParkingSlot

interface IParkingSlotRepository {
    suspend fun addParkingSlot(parkingSlot: ParkingSlot): Result<Unit>
    suspend fun getAllParkingSlots(): Result<List<ParkingSlot>>
}