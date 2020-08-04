package com.example.locationtestdi

import android.app.Application
import com.example.locationtestdi.di.component.DaggerLocationComponent
import com.example.locationtestdi.di.component.LocationComponent
import com.example.locationtestdi.di.module.LocationAppModule
import com.example.locationtestdi.di.module.LocationRoomDBModule
import com.example.locationtestdi.di.module.LocationViewModelModule
import com.example.locationtestdi.di.module.LocatorManagerModule

class LocationApp: Application() {

    override fun onCreate() {
        super.onCreate()

        component = DaggerLocationComponent.builder()
            .locatorManagerModule(LocatorManagerModule())
            .locationViewModelModule(LocationViewModelModule())
            .locationAppModule(LocationAppModule(this))
            .locationRoomDBModule(LocationRoomDBModule())
            .build()
    }

    companion object {
        lateinit var component: LocationComponent
    }
}