package com.example.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DriverProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DriverProfileRepository

    init {
        val dao = AppDatabase.getDatabase(application).driverProfileDao()
        repository = DriverProfileRepository(dao)
        
        viewModelScope.launch {
            val currentProfiles = repository.allProfiles.firstOrNull()
            if (currentProfiles.isNullOrEmpty()) {
                repository.insert(DriverProfile(name = "Aditya", isActive = true, seatPosition = 65f, mirrorTilt = 40f, climateTemp = 74f))
                repository.insert(DriverProfile(name = "Guest", seatPosition = 50f, mirrorTilt = 50f, climateTemp = 72f))
            }
        }
    }

    val profiles: StateFlow<List<DriverProfile>> = repository.allProfiles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        
    val activeProfile: StateFlow<DriverProfile?> = repository.activeProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun switchProfile(id: Int) = viewModelScope.launch {
        repository.switchProfile(id)
    }
    
    fun updateProfile(profile: DriverProfile) = viewModelScope.launch {
        repository.update(profile)
    }

    fun addProfile(name: String, seatPosition: Float, mirrorTilt: Float, climateTemp: Float) = viewModelScope.launch {
        repository.insert(
            DriverProfile(
                name = name,
                isActive = false,
                seatPosition = seatPosition,
                mirrorTilt = mirrorTilt,
                climateTemp = climateTemp
            )
        )
    }

    fun deleteProfile(id: Int) = viewModelScope.launch {
        repository.delete(id)
    }
}
