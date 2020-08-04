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

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
//    lateinit var callback: (position: LatLng) -> Unit

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