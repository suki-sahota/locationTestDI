package com.example.locationtestdi.model

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

/*
 * Class that provides current location
 * Return lat and long
 */
class LocatorManager(val context: Context) {

    lateinit var callback: (position: LatLng) -> Unit
    private lateinit var fusedLocationClient: FusedLocationProviderClient // For illustrative purposes only

    init {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

//    fun onBind(callback: (position: LatLng) -> Unit) {
//        this.callback = callback
//    }

    @SuppressLint("MissingPermission")
    fun getLastLocation(callback: (position: LatLng) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                callback.invoke(LatLng(location.latitude, location.longitude))
            }
        }
    }
}