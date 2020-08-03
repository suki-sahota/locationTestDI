package com.example.locationtestdi.di.module

import android.content.Context
import com.example.locationtestdi.model.LocatorManager
import dagger.Module
import dagger.Provides

@Module
class LocatorManagerModule {
    @Provides
    fun providesLocatorManager(context: Context): LocatorManager {
        return LocatorManager(context)
    }
}