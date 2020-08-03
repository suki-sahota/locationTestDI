package com.example.locationtestdi.view

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.locationtestdi.LocationApp
import com.example.locationtestdi.R
import com.example.locationtestdi.model.LocatorManager
import com.example.locationtestdi.viewmodel.LocationViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(),
    OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    @Inject lateinit var locatorManager: LocatorManager
    @Inject lateinit var viewModelProvider: LocationViewModelProvider

//    val sf = LatLng(37.7749, -122.4194) // San Francisco
//    val ny = LatLng(40.73, -73.99)  // New York City
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LocationApp.component.inject(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapView = findViewById<MapView>(R.id.maps)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)
    }

    // This callback is triggered when the map is ready to be used
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
//        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID // Hybrid
//        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE // Satellite
        mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN // Terrain

        setUpMap()
    }

    // Request permission and set current location on map
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return // Exits function if ACCESS_FINE_LOCATION is not permitted
        }

        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known user location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLng) // Add current position on map
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
    }

    // Callback for when user responds to permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {

                    setUpMap()
                    }
            }
        }
    }

    // To place customized parker on map
    private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location)
        markerOptions.icon(
            BitmapDescriptorFactory.fromBitmap(
            BitmapFactory.decodeResource(resources, R.mipmap.ic_user_location)))
        mMap.addMarker(markerOptions)
    }

    // Drop pin and navigate to new location
    fun goNewLocation(view: View) {
        val latitude: Double = et_lat.text.toString().toDoubleOrNull() ?: 37.7749
        val longitude: Double = et_lon.text.toString().toDoubleOrNull() ?: -122.4194
        if (latNotValid(latitude) || lonNotValid(longitude)) {
            Toast.makeText(this,
                "Please enter valid Coordinates", Toast.LENGTH_SHORT).show()
        } else {
            val newLocation = LatLng(latitude, longitude)
            mMap.addMarker(MarkerOptions().position(newLocation).title("Location Marker"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation))
        }
        et_lat.text.clear()
        et_lon.text.clear()
    }

    private fun latNotValid(num: Double): Boolean {
        if (num < -90 || num > 90) return true
        return false
    }

    private fun lonNotValid(num: Double): Boolean {
        if (num < -180 || num > 180) return true
        return false
    }

    override fun onMarkerClick(p0: Marker?): Boolean = false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 9274 // Random Integer

    }
}