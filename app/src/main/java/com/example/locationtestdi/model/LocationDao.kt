package com.example.locationtestdi.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao // Data Access Object
interface LocationDao { // An advantage of Room is that you get access to all the joins from SQL

    @Query("SELECT * FROM location_table") // Read
    suspend fun getAllLocations(): List<LocationEntity> // Ready for multithreading with RxJava or Coroutines

    @Insert(onConflict = OnConflictStrategy.IGNORE) // Create
    suspend fun insertLocation(singleLocation: LocationEntity)

    @Query("DELETE FROM location_table") // Destroy
    suspend fun deleteAll()
}