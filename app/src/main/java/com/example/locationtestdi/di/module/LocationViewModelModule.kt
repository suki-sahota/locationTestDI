package com.example.locationtestdi.di.module

import com.example.locationtestdi.viewmodel.LocationViewModelProvider
import dagger.Module
import dagger.Provides

@Module
class LocationViewModelModule {
    @Provides
    fun providesLocationViewModelProvider(): LocationViewModelProvider =
        LocationViewModelProvider()
}