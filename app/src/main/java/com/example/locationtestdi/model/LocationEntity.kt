package com.example.locationtestdi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_places")
data class LocationEntity( // Used to model a SQLite DB inside the room database (like a Schema)
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    @ColumnInfo(name = "latitude")
    val lat: Double,
    @ColumnInfo(name = "longitude")
    val lng: Double,
    @ColumnInfo(name = "place_name")
    val place: String
)