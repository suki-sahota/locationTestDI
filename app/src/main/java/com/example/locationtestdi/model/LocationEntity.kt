package com.example.locationtestdi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_table")
data class LocationEntity( // Used to model a SQLite DB inside the room database (like a Schema)
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    @ColumnInfo(name = "longitude")
    val lat: String,
    @ColumnInfo(name = "latitude")
    val lng: String
)