package com.example.a438finalproject

import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteFragment
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var placeAutoComplete : AutocompleteFragment

    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var homeLat : Double? = null
    private var homeLong : Double? = null

    private lateinit var placesClient: PlacesClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        Places.initialize(applicationContext, "@string/google_maps_key")

        setupPlacesAutoComplete()

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

        //set the user location
        enableMyLocation()

        //set the click longitude
        setMapLongClick(mMap)

        //add zoom controls
        val mapSettings = mMap.uiSettings
        mapSettings.isZoomControlsEnabled = true



        //set as street map
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-33.8568, 151.2153)
        mMap.addMarker(MarkerOptions().position(sydney).title("Sydney Opera House"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15f))
    }

    private fun setupPlacesAutoComplete(){

        var placeFields = mutableListOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)
        Places.initialize(applicationContext, getString(R.string.google_api_key))
        placesClient = Places.createClient(applicationContext)

        val autoCompleteFragment =
            this?.supportFragmentManager?.findFragmentById(R.id.place_autocomplete) as? AutocompleteSupportFragment

        autoCompleteFragment?.setPlaceFields(placeFields)

        autoCompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener{
            override fun onPlaceSelected(place: Place) {
                Toast.makeText(applicationContext, ""+place.address, Toast.LENGTH_SHORT).show()

                //set the location and zoom variables
                val geocoder = Geocoder(applicationContext)
                val address = place.address
                val geoad = geocoder.getFromLocationName(address as String,1)
                println("geoad: $geoad")
                val placelat = geoad[0].latitude
                val placelng = geoad[0].longitude
                println(placelat)


                val placelatlng = LatLng(placelat, placelng)

                println("Place latllng: $placelatlng")
                println("Place address: ${place.address}")
                val zoomLevel = 15f

                //move the camera and set a marker
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placelatlng, zoomLevel))
                mMap.addMarker(MarkerOptions().position(placelatlng).title(place.name))

            }

            override fun onError(status: Status) {
                Toast.makeText(applicationContext, ""+status.statusMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    //returns if the needed permissions are available
    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    //sets the home location
    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            mMap.isMyLocationEnabled = true

            //set the location to the home location
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    //store the location values
                    homeLat =  location?.latitude as Double?
                    homeLong = location?.longitude as Double?

                    //set the location and zoom variables
                    val homeLatLng = LatLng(homeLat as Double, homeLong as Double)
                    val zoomLevel = 15f

                    //move the camera and set a marker
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
//                    mMap.addMarker(MarkerOptions().position(homeLatLng))
                }

        }
        else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    //click listener for additional locations
    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
        }
    }

    //check the permissions
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }


    companion object {
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }
}
