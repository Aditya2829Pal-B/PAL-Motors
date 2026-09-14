package com.example.data

import kotlinx.coroutines.flow.Flow

class DriverProfileRepository(private val dao: DriverProfileDao) {
    val allProfiles: Flow<List<DriverProfile>> = dao.getAllProfiles()
    val activeProfile: Flow<DriverProfile?> = dao.getActiveProfile()

    suspend fun insert(profile: DriverProfile) {
        dao.insertProfile(profile)
    }

    suspend fun update(profile: DriverProfile) {
        dao.updateProfile(profile)
    }

    suspend fun switchProfile(profileId: Int) {
        dao.deactivateAll()
        dao.setActiveProfile(profileId)
    }
}
