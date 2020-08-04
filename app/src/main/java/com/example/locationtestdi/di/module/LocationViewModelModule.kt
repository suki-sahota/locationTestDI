package com.example.locationtestdi.di.module

import android.content.Context
import com.example.locationtestdi.model.LocatorManager
import com.example.locationtestdi.viewmodel.LocationViewModelProvider
import dagger.Module
import dagger.Provides

@Module
class LocationViewModelModule { // This is actually the "LocationViewModelProviderModule"
    @Provides
    fun providesLocationViewModelProvider(context: Context, locatorManager: LocatorManager): LocationViewModelProvider =
        LocationViewModelProvider(context, locatorManager)
}