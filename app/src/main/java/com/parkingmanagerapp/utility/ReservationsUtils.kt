package com.parkingmanagerapp.utility

import java.util.Date

//Helper class for reservation overlap checking.
object ReservationUtils {

    fun isOverlapping(start1: Date, end1: Date, start2: Date, end2: Date): Boolean {
        return (start1 <= end2 && end1 >= start2)
    }
}