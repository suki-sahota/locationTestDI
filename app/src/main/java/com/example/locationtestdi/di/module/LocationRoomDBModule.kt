package com.example.locationtestdi.di.module

import android.content.Context
import androidx.room.Room
import com.example.locationtestdi.R
import com.example.locationtestdi.model.LocationRoomDB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocationRoomDBModule {
    @Provides
    @Singleton
    fun providesLocationRoomDB(context: Context): LocationRoomDB {
        return Room
            .databaseBuilder(context, LocationRoomDB::class.java,
                context.getString(R.string.db_name))
            .build()
    }
}