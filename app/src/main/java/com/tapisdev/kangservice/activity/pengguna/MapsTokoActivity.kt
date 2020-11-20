package com.tapisdev.kangservice.activity.pengguna

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R

class MapsTokoActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var i : Intent
    lateinit var latlon : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_toko)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        i = intent
        latlon = i.getStringExtra("latlon") as String
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        Log.d("latlon",latlon)
        val index   = latlon.indexOf(",")
        val lon     = latlon.substring(index+1)
        val lat     = latlon.substring(0,index)
        Log.d("latlon","lat : "+lat + "| lon : "+lon)

        val lokasi = LatLng(lat.toDouble(), lon.toDouble())
        val zoomLevel = 16.0f //This goes up to 21
        mMap.addMarker(MarkerOptions().position(lokasi).title("Lokasi Toko"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasi,zoomLevel))
    }
}
