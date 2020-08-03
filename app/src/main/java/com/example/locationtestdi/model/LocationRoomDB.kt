package com.example.locationtestdi.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.locationtestdi.R

@Database(entities = [LocationEntity::class], version = 1, exportSchema = false)
abstract class LocationRoomDB: RoomDatabase() { // The actual Room DB is created here...

    abstract fun dao(): LocationDao

    companion object {
        @Volatile
        var INSTANCE: LocationRoomDB? = null

        fun getInstance(context: Context): LocationRoomDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance
            val instance: LocationRoomDB = Room.databaseBuilder(
                context, LocationRoomDB::class.java,
                context.getString(R.string.db_name)
            )
//                .createFromAsset("database/testData.db") // To prepopulate the [Custom]RoomDB
                .build()
            INSTANCE = instance
            return instance
        }
    }
}