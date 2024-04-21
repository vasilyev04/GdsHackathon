package com.vasilyev.gdshackathon.presentation.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.vasilyev.gdshackathon.databinding.ActivityMainBinding
import com.vasilyev.gdshackathon.domain.entity.Message
import com.vasilyev.gdshackathon.presentation.BottomSheetInfoFragment
import com.vasilyev.gdshackathon.presentation.main.adapter.PlacesRecyclerViewAdapter
import com.vasilyev.gdshackathon.presentation.main.adapter.RecyclerViewAdapter


class MainActivity : AppCompatActivity() {
    private lateinit var locationManager: LocationManager
    private lateinit var userLocation: Location

    private val gpsLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            userLocation = location
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private val adapter = RecyclerViewAdapter()
    private val placeAdapter = PlacesRecyclerViewAdapter()


    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = requireNotNull(_binding)

    private val viewModel: MainViewModel by viewModels{
        MainViewModelFactory(application)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){ permissions ->
        var isGranted = true

        permissions.entries.forEach {
            if(it.key in REQUIRED_PERMISSIONS){
                isGranted = it.value
            }
        }

        if(isGranted){
            observeLocation()
            observeState()
        }
    }

    private fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.generateStartPrompt()

        requestPermissions()
        binding.rvPlaces.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPlaces.adapter = placeAdapter
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        placeAdapter.onItemClick = {
            val bottomSheetInfoFragment = BottomSheetInfoFragment().apply {
                val bundle = Bundle().apply {
                    putInt("EXTRA_PLACE", it)
                }
                arguments = bundle
            }
            bottomSheetInfoFragment.show(supportFragmentManager, "Bottom Sheet Info Fragment")
        }

        binding.sendMsgBtn.setOnClickListener {
            hideWelcomeText()

            val text = binding.supportSentRequestEt.text.toString().trim()
            if(text.isNotBlank()){
                val location = "\nUSER_LOCATION: ${userLocation.latitude} ${userLocation.longitude}"
                adapter.addMessage(Message("user", text))
                viewModel.generatePrompt(text + location)
                binding.supportSentRequestEt.setText("")
                hideKeyboard(this)
                binding.rvChat.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }


    private fun requestPermissions(){
        requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun observeState(){
        viewModel.mainState.observe(this){state ->
            when(state){
                is MainState.Places -> {
                    endLocation()
                    placeAdapter.submitList(state.list)
                }

                is MainState.AiAnswer -> {
                    endLocation()
                    adapter.addMessage(Message("ai", state.answer))
                }

                is MainState.PlaceReceived -> {
                    endLocation()
                }

                is MainState.Loading -> {
                    startLoading()
                }
            }
        }
    }

    private fun startLoading(){
        binding.progressBar.visibility = View.VISIBLE
        binding.progressBar.isActivated = true
    }

    private fun endLocation(){
        binding.progressBar.isActivated = false
        binding.progressBar.visibility = View.GONE
    }

    private fun observeLocation(){
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){}

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0F, gpsLocationListener)
    }


    private fun hideWelcomeText(){
        binding.llWelcome.visibility = View.GONE
        binding.rvChat.visibility = View.VISIBLE
        binding.rvChat.adapter = adapter
    }

    companion object{
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    }
}