package com.vasilyev.gdshackathon.presentation.main

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.vasilyev.gdshackathon.R
import com.vasilyev.gdshackathon.data.repository.MapRepositoryImpl
import com.vasilyev.gdshackathon.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = requireNotNull(_binding)

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
            showChat()
        }else{
            Toast.makeText(this, "Вы не предоставили разрешения", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions()
    }

    private fun requestPermissions(){
        requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun showChat(){

    }

    companion object{
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}