package com.rutvi.mad_practical11_21012011123

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.OnMapReadyCallback
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.BitmapDescriptorFactory
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions


@Suppress("DEPRECATION", "CAST_NEVER_SUCCEEDS")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding:ActivityMapsBinding

    @Suppress("UNREACHABLE_CODE")
    class ActivityMapsBinding {
        companion object {
            fun inflate(): ActivityMapsBinding {

                return TODO("Provide the return value")
            }
        }

    }

    private var lat:Double = 0.0
    private var log:Double = 0.0
    private var title = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate()
        setContentView()
        val obj = intent.getSerializableExtra("Object") as Person
        lat = obj.latitude
        log = obj.longitude
        title = obj.name
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setContentView() {
        TODO("Not yet implemented")
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
        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        val sydney = LatLng(lat,  log )
        mMap.addMarker(
            MarkerOptions().position(sydney)
            .title(title)
            .snippet(title)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_1))
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,16.0f))
    }
}