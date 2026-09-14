package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DriverProfileDao {
    @Query("SELECT * FROM driver_profiles ORDER BY id ASC")
    fun getAllProfiles(): Flow<List<DriverProfile>>

    @Query("SELECT * FROM driver_profiles WHERE isActive = 1 LIMIT 1")
    fun getActiveProfile(): Flow<DriverProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: DriverProfile)

    @Update
    suspend fun updateProfile(profile: DriverProfile)

    @Query("DELETE FROM driver_profiles WHERE id = :id")
    suspend fun deleteProfileById(id: Int)

    @Query("UPDATE driver_profiles SET isActive = 0")
    suspend fun deactivateAll()
    
    @Query("UPDATE driver_profiles SET isActive = 1 WHERE id = :id")
    suspend fun setActiveProfile(id: Int)
}
