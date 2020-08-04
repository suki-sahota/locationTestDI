package com.example.locationtestdi.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.locationtestdi.model.LocatorManager
import com.google.android.gms.maps.model.LatLng

/*
 * Data container for lat and long
 * Future -> Favorite Places
 */
class LocationViewModel(val context: Context, val locatorManager: LocatorManager): ViewModel() {

    private val dataSet = MutableLiveData<AppState>()

    sealed class AppState { // Holds the current application state
        data class LocationResponse(val currentLocation: List<LatLng>): AppState() // Current Location
        data class SearchedResponse(val favoritePlaces: List<Pair<LatLng, String>>): AppState() // Some Searched Locations
        data class InfoMessage(val infoMessage: String): AppState() // Confirmation for Room insertion
        data class ErrorMessage(val errorMessage: String): AppState() // Invalid Coordinates
    }

    fun getDataSet(): LiveData<AppState> { // dataSet contains one AppState
        return dataSet
    }

    fun requestPermission(activity: Activity) {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions( // Request user for permission
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            dataSet.value = AppState.InfoMessage("Location permission is turned off")
            return // Exits function if ACCESS_FINE_LOCATION is not permitted
        }
        getLocationFromUI()
    }

    fun permissionGranted() {
        getLocationFromUI()
        dataSet.value = AppState.InfoMessage("Location permission is turned on")
    }

    private fun getLocationFromUI() { // Callback from LocatorManager (Model)
        locatorManager.getLastLocation { location ->
            dataSet.value = AppState.LocationResponse(listOf(location)) // .value triggers an onChange in Activity
        }
    }

    fun isValidDouble(latitude: String, longitude: String, name: String) {
        val lat: Double? = latitude.toDoubleOrNull()
        val lng: Double? = longitude.toDoubleOrNull()
        if (lat == null || lng == null) {
            dataSet.value = AppState.ErrorMessage("Enter a valid number for latitude and/or longitude please")
        } else if (latNotValid(lat)) {
            dataSet.value = AppState.ErrorMessage("The value you entered for latitude is out of bounds!")
        } else if (lngNotValid(lng)) {
            dataSet.value = AppState.ErrorMessage("The value you entered for longitude is out of bounds!")
        } else {
            getLocationFromUI(lat, lng, name) // Smart cast because of if statement
        }
    }

    private fun latNotValid(num: Double): Boolean {
        if (num < -90 || num > 90) return true // Not valid
        return false // Valid
    }

    private fun lngNotValid(num: Double): Boolean {
        if (num < -180 || num > 180) return true // Not valid
        return false // Valid
    }

    /*
     * Create a LatLng from Maps EditText
     */
    private fun getLocationFromUI(lat: Double, lng: Double, name: String) {
        val pair: Pair<LatLng, String> = Pair(LatLng(lat, lng), name)
        // Option 1
        with (pair) {
            dataSet.value = AppState.SearchedResponse(listOf(this)) // .value triggers an onChange in Activity
        }
//        // Option 2
//        pair.apply {
//            dataSet.value = AppState.SearchedResponse(listOf(this))
//        }
        saveFavoritePlaces(pair)
    }

    /*
     * Store Pair<LatLng, String> in LocationRoomDB
     */
    private fun saveFavoritePlaces(pair: Pair<LatLng, String>) {
        // TODO (put in LocationRoomDB)
    }

    /*
     * Query Room to get Pair<LatLng, String>
     */
    private fun getAllFavoritesPlaces() {
        // TODO
    }

    fun getDataFromRepository(dataSet: List<Pair<LatLng, String>>) {
        this.dataSet.value = AppState.SearchedResponse(dataSet) // .value triggers an onChange in Activity
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 9274 // Random Integer
    }
}