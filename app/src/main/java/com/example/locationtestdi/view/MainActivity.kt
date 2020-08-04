package com.example.locationtestdi.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.locationtestdi.LocationApp
import com.example.locationtestdi.R
import com.example.locationtestdi.model.LocatorManager
import com.example.locationtestdi.viewmodel.LocationViewModel
import com.example.locationtestdi.viewmodel.LocationViewModelProvider
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

//    private val TAG = "MainActivity"

    @Inject lateinit var locatorManager: LocatorManager
    @Inject lateinit var viewModelProvider: LocationViewModelProvider

//    val sf = LatLng(37.7749, -122.4194) // San Francisco, CA
//    val ny = LatLng(40.73, -73.99)  // New York City, NY
//    val berk = LatLng(37.87, -122.27) // Berkeley, CA
    private lateinit var mMap: GoogleMap
    private lateinit var viewModel: LocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LocationApp.component.inject(this)

        val mapView = findViewById<MapView>(R.id.maps)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)

        // Create [Custom]ViewModel from the [Custom]ViewModelProvider
        viewModel = viewModelProvider.create(LocationViewModel::class.java) // Lately initialize here...
        viewModel.getDataSet().observe( this,
            Observer {
                when (it) {
                    is LocationViewModel.AppState.LocationResponse -> {
                        it.currentLocation.forEach { location -> placeCurrentLocation(location) }
                    }
                    is LocationViewModel.AppState.SearchedResponse -> {
                        it.favoritePlaces.forEach { location -> dropPinOnMap(location) }
                    }
                    is LocationViewModel.AppState.InfoMessage -> {
                        Toast.makeText(this, it.infoMessage, Toast.LENGTH_SHORT).show()
                    }
                    is LocationViewModel.AppState.ErrorMessage -> {
                        Toast.makeText(this, it.errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
        viewModel.requestPermission(this)

        // To show the EditText fields
        toolbar_icon_expand.setOnClickListener {
            linear_layout.visibility = View.VISIBLE
            toolbar_icon_expand.visibility = View.GONE
        }
    }

    // This callback is triggered when the map is ready to be used
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap // Lately initialize here...
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID // Hybrid, Satellite, Terrain, etc.
    }

    // Callback for when user responds to permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LocationViewModel.LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                    @SuppressLint("MissingPermission")
                    mMap.isMyLocationEnabled = true
                    viewModel.permissionGranted()
                }
            }
        }
    }

    // Place customized marker on map; signifies user's location
    private fun placeCurrentLocation(myLocation: LatLng) {
        val markerOptions = MarkerOptions().position(myLocation)
        markerOptions.icon(
            BitmapDescriptorFactory.fromBitmap(
            BitmapFactory.decodeResource(resources, R.mipmap.ic_user_location)))
        mMap.addMarker(markerOptions)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12f))
    }

    // Drop new pin on map
    private fun dropPinOnMap(newLocation: Pair<LatLng, String>) {
        mMap.addMarker(MarkerOptions().position(newLocation.first).title(newLocation.second))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLocation.first, 12f))
    }

    // Send user input to the [Custom]ViewModel
    fun goNewLocation(view: View) {
        val lat: String = et_lat.text.toString()
        val lng: String = et_lng.text.toString()
        val name: String = et_name.text.toString()

        linear_layout.visibility = View.GONE
        toolbar_icon_expand.visibility = View.VISIBLE

        viewModel.isValidDouble(lat, lng, name)

        et_lat.text.clear()
        et_lng.text.clear()
        et_name.text.clear()
        val imm: InputMethodManager = // Hide the soft keyboard
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onMarkerClick(p0: Marker?): Boolean = false
}