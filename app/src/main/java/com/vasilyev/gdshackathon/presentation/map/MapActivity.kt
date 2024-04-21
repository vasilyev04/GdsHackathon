package com.vasilyev.gdshackathon.presentation.map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.vasilyev.gdshackathon.R
import com.vasilyev.gdshackathon.databinding.ActivityMapBinding
import com.vasilyev.gdshackathon.domain.entity.Place

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private var _binding: ActivityMapBinding? = null
    private val binding: ActivityMapBinding get() = requireNotNull(_binding)

    private val viewModel: MapViewModel by viewModels(){
        MapViewModelFactory(application)
    }

    private lateinit var googleMap: GoogleMap
    private lateinit var place: Place

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        parseIntent()
        initMap()
    }

    private fun parseIntent(){
        if(!intent.hasExtra(EXTRA_PLACE_ID)) throw RuntimeException("EXTRA_ORDER_ID is absent")

        val placeId = intent.getIntExtra(EXTRA_PLACE_ID, 0)
        viewModel.getPlaceById(placeId)
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
    }

    companion object{
        private const val EXTRA_PLACE_ID = "extra_place_id"

        fun newIntent(context: Context, placeId: Int): Intent{
            return Intent(context, MapActivity::class.java).apply {
                putExtra(EXTRA_PLACE_ID, placeId)
            }
        }
    }

}