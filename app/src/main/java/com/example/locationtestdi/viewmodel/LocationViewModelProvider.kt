package com.example.locationtestdi.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.locationtestdi.model.LocatorManager

class LocationViewModelProvider(val context: Context, val locatorManager: LocatorManager): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return LocationViewModel(context = context, locatorManager = locatorManager) as T
    }
}