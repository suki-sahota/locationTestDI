package com.example.locationtestdi.di.module

import android.content.Context
import dagger.Module
import dagger.Provides

/*
 * Assisted Injection
 */
@Module
class LocationAppModule(val context: Context) {
    @Provides
    fun providesAppContext(): Context = context
}