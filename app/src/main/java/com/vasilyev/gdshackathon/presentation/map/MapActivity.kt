package com.vasilyev.gdshackathon.presentation.map

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.vasilyev.gdshackathon.R
import com.vasilyev.gdshackathon.databinding.ActivityMapBinding
import com.vasilyev.gdshackathon.domain.entity.Place

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private var _binding: ActivityMapBinding? = null
    private val binding: ActivityMapBinding get() = requireNotNull(_binding)

    private val viewModel: MapViewModel by viewModels{
        MapViewModelFactory(application)
    }
    private var userMarker: Marker? = null

    private lateinit var locationManager: LocationManager
    private lateinit var userLocation: Location

    private var isRouteShowed = false

    private val gpsLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            userLocation = location
            updateStartMarker(location.latitude, location.longitude)
            val startLoc = "${location.latitude} ${location.longitude}"

            Log.d("TestTest", "$startLoc $place")
            if(!isRouteShowed){
                viewModel.getRoute(startLoc, place, mode = "driving")
                isRouteShowed = true
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private lateinit var googleMap: GoogleMap
    private lateinit var place: Place

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        parseIntent()
        initMap()
        observeLocation()
        observeState()

        binding.fabCar.setOnClickListener {
            val startLoc = "${userLocation.latitude} ${userLocation.longitude}"
            viewModel.getRoute(startLoc, place, mode = "driving")
        }

        binding.fabWalk.setOnClickListener {
            val startLoc = "${userLocation.latitude} ${userLocation.longitude}"
            viewModel.getRoute(startLoc, place, mode = "walking")
        }
    }
    private fun observeLocation(){
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){}

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0F, gpsLocationListener)
    }

    private fun parseIntent(){
        if(!intent.hasExtra(EXTRA_PLACE_ID)) throw RuntimeException("EXTRA_ORDER_ID is absent")

        val placeId = intent.getIntExtra(EXTRA_PLACE_ID, 0)
        viewModel.getPlaceById(placeId)
    }

    private fun updateStartMarker(lat: Double, lng: Double){
        val point = LatLng(lat, lng)

        userMarker?.remove()

        userMarker = googleMap.addMarker(
            MarkerOptions()
                .position(point)
                .icon(getStartMarkerIcon())
                .anchor(MARKER_ANCHOR_VALUE, MARKER_ANCHOR_VALUE)
        )
    }

    private fun observeState(){
        viewModel.mapState.observe(this){ state ->
            when(state){
                is MapState.Route -> {
                    hideLoading()
                    displayRoute(state.route)
                    createCameraBounds(state.route)
                }

                is MapState.RequestedPlace -> {
                    place = state.place
                }

                is MapState.Loading -> {
                    showLoading()
                }
            }
        }
    }

    private var polyline: Polyline? = null
    private fun displayRoute(route: List<LatLng>){
        val options = createPolyLine(route)
        polyline?.remove()
        polyline = googleMap.addPolyline(options)
    }
    private fun createPolyLine(route: List<LatLng>): PolylineOptions {
        val polyOptions = PolylineOptions()

        for(point in route){
            polyOptions.add(point)
        }

        return polyOptions
    }

    private fun createCameraBounds(route: List<LatLng>){
        val boundsBuilder = LatLngBounds.builder()

        for (point in route) {
            boundsBuilder.include(point)
        }

        val routeBounds = boundsBuilder.build()

        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(routeBounds, CAMERA_PADDING)
        googleMap.animateCamera(cameraUpdate)
    }

    private fun getStartMarkerIcon(): BitmapDescriptor {
        val bitmapMarker = ContextCompat.getDrawable(this, R.drawable.user_marker)
            ?.toBitmap() ?: throw java.lang.RuntimeException("Unknown resource id")

        return BitmapDescriptorFactory.fromBitmap(bitmapMarker)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initMap(){
        val mapFragment = SupportMapFragment.newInstance()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.map, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        centerCameraOnCity()
    }

    private fun centerCameraOnCity(){
        val cameraPosition = CameraPosition.Builder()
            .target(cityCoordinates)
            .zoom(cityZoom)
            .build()

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    companion object{
        private const val EXTRA_PLACE_ID = "extra_place_id"

        private const val CAMERA_PADDING = 150
        private val cityCoordinates = LatLng(51.16, 71.42) // ASTANA
        private const val cityZoom = 12f
        private const val MARKER_ANCHOR_VALUE = 0.5f

        fun newIntent(context: Context, placeId: Int): Intent{
            return Intent(context, MapActivity::class.java).apply {
                putExtra(EXTRA_PLACE_ID, placeId)
            }
        }
    }

}