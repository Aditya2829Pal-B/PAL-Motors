package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "driver_profiles")
data class DriverProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val seatPosition: Float = 50f, // 0 to 100
    val mirrorTilt: Float = 50f,   // 0 to 100
    val climateTemp: Float = 72f,  // 60 to 85
    val isActive: Boolean = false
)
