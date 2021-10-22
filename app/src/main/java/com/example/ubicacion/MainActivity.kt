package com.example.ubicacion

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.ubicacion.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        updateLocation()
        binding.btnUpdateLocation.setOnClickListener {
            updateLocation()
        }
    }

    fun updateLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Doesn't have permission",Toast.LENGTH_SHORT).show()
            Log.d("LocationPermissions", "Doesn't have permission")
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),1)
            }
            return
        }else{
            fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                if (location != null) {
                    binding.tvCoordinates.setText("${location?.latitude.toString()}, ${location?.longitude}")
                    Log.d("LocationPermissions", "Success ${location?.latitude.toString()}, ${location?.longitude}")
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    Log.d("LocationPermissions","${address[0]}")
                    binding.tvCoordinates.setText("${address[0].locality}, ${address[0].adminArea}")
                }
            }
            Log.d("LocationPermissions", "Has permission ")
            //val square = { number: Int -> number * number}
            //val sixteen = square(4)
            //val sixteen: Int? = null
        }
    }
}