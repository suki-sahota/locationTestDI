package com.example.locationtestdi.di.component

import com.example.locationtestdi.view.MainActivity
import com.example.locationtestdi.di.module.LocationAppModule
import com.example.locationtestdi.di.module.LocationViewModelModule
import com.example.locationtestdi.di.module.LocatorManagerModule
import dagger.Component

@Component(
    modules = [LocationViewModelModule::class,
    LocatorManagerModule::class,
    LocationAppModule::class]
)
interface LocationComponent {
    fun inject(activity: MainActivity) // Name of function is usually inject or bind
}