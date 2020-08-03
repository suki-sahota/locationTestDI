package com.example.locationtestdi.di.module

import android.content.Context
import com.example.locationtestdi.model.LocatorManager
import com.example.locationtestdi.viewmodel.LocationViewModelProvider
import dagger.Module
import dagger.Provides

@Module
class LocationViewModelModule {
    @Provides
    fun providesLocationViewModelProvider(context: Context, locatorManager: LocatorManager): LocationViewModelProvider =
        LocationViewModelProvider(context, locatorManager)
}