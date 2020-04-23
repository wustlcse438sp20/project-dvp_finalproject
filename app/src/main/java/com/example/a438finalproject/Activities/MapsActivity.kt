package com.example.a438finalproject.Activities

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.a438finalproject.Data.GeojsonCenter
import com.example.a438finalproject.Data.LocationData
import com.example.a438finalproject.Network.ApiClient
import com.example.a438finalproject.R
import com.example.a438finalproject.Util.LocationHelper
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
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteFragment
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var firebase: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var mMap: GoogleMap

    private lateinit var placeAutoComplete: AutocompleteFragment

    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var homeLat: Double? = null
    private var homeLong: Double? = null

    private lateinit var placesClient: PlacesClient

    val service = ApiClient.makeRetroFitService()
    var locationData: LocationData? = LocationData(listOf(), listOf(), listOf())
    val locationHelper = LocationHelper()
    val filter = HashMap<String, String>()
    var testLatitude = "38.6488"
    var testLongitude = "-90.3108"
    var limit = 10
    var cleanedData: LocationData? = LocationData(listOf(), listOf(), listOf())

    var currentAddress: String? = null
    var currentLong: Double? = null
    var currentLat: Double? = null
    var currentName: String? = null
    var currentID: String? = null

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

        if (intent.getStringExtra("origin") == "favorites") {
            favSearch()
        }

        firebase = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near WashU.
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

        // Add a marker in St. Louis and move the camera
        val washU = LatLng(38.6488, -90.3108)
        mMap.addMarker(MarkerOptions().position(washU).title("Washington University in St. Louis"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(washU, 15f))

        searchLocation(testLatitude, testLongitude)
    }

    fun searchLocation(latitude: String, longitude: String) {

        filter["latitude"] = latitude
        filter["longitude"] = longitude
        val paramLimit = limit.toString()

        getLocationByPoint(filter, paramLimit)
    }

    fun getLocationByPoint(filter: HashMap<String, String>, limit: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getLocByPoint(filter, limit)

            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        println("response.body: " + response.body())
                        locationData = response.body()

                        cleanedData = locationHelper.garbageCleanup(locationData)
                        println("cleanedData: $cleanedData")

                        val coordList = locationHelper.getCoordinates(cleanedData)
                        println("coordList: $coordList")

                        for (pairs in coordList) {
                            val pos = pairs.first
                            val name = pairs.second
                            mMap.addMarker(
                                MarkerOptions().position(pos).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                                ).title(name)
                            )
                        }

                    }
                } catch (e: HttpException) {
                    println("Http error")
                }
            }
        }
    }

    private fun setupPlacesAutoComplete() {

        val placeFields = mutableListOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)
        Places.initialize(applicationContext, getString(R.string.google_api_key))
        placesClient = Places.createClient(applicationContext)

        val autoCompleteFragment =
            this.supportFragmentManager.findFragmentById(R.id.place_autocomplete) as? AutocompleteSupportFragment

        autoCompleteFragment?.setPlaceFields(placeFields)

        autoCompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                //Toast.makeText(applicationContext, ""+place.address, Toast.LENGTH_SHORT).show()

                //set the location and zoom variables
                val geocoder = Geocoder(applicationContext)
                val address = place.address
                val geoad = geocoder.getFromLocationName(address as String, 1)
                println("geoad: $geoad")
                val placelat = geoad[0].latitude
                val placelng = geoad[0].longitude
                println(placelat)

                currentName = place.name
                currentLong = placelng
                currentLat = placelat
                currentAddress = address
                currentID = place.id

                val placelatlng = LatLng(placelat, placelng)

                println("Place latllng: $placelatlng")
                println("Place address: ${place.address}")
                val zoomLevel = 15f

                //move the camera and set a marker
                mMap.clear()
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placelatlng, zoomLevel))
                mMap.addMarker(MarkerOptions().position(placelatlng).title(place.name))

                searchLocation(placelat.toString(), placelng.toString())

            }

            override fun onError(status: Status) {
                Toast.makeText(applicationContext, "" + status.statusMessage, Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

    //returns if the needed permissions are available
    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    //sets the home location
    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            mMap.isMyLocationEnabled = true

            //set the location to the home location
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    //store the location values
                    homeLat = location?.latitude as Double?
                    homeLong = location?.longitude as Double?

                    //set the location and zoom variables
                    val homeLatLng = LatLng(homeLat as Double, homeLong as Double)
                    val zoomLevel = 15f

                    //move the camera and set a marker
                }

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
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
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }


    companion object {
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }

    fun logout(view: View) {
        val homeIntent = Intent(this, MainActivity::class.java)
        //still needs to logout user and toast to let them know
        firebase.signOut()
        startActivity(homeIntent)
    }

    fun favorites(view: View) {
        val Intent = Intent(this, Favorites::class.java)
        startActivity(Intent)
    }

    fun addFavorites(view: View) {
        var user = firebase.currentUser
        if (currentAddress == null) {
            Toast.makeText(this, "Please Search for a Location", Toast.LENGTH_SHORT).show()
        } else {
            var data = hashMapOf(
                "address" to currentAddress,
                "name" to currentName,
                "lat" to currentLat,
                "long" to currentLong,
                "id" to currentID
            )

            db.collection(user!!.uid).document(currentID!!).set(data)
            Toast.makeText(this, "Location Added to Favorites", Toast.LENGTH_SHORT).show()

        }
    }

    fun favSearch() {
        val id = intent.getStringExtra("id")
        val placeFields = mutableListOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)
        placesClient = Places.createClient(applicationContext)
        val request: FetchPlaceRequest = FetchPlaceRequest.newInstance(id, placeFields)
        placesClient.fetchPlace(request).addOnSuccessListener {
            val place: Place = it.place

            val geocoder = Geocoder(applicationContext)
            val address = place.address
            val geoad = geocoder.getFromLocationName(address as String, 1)
            println("geoad: $geoad")
            val placelat = geoad[0].latitude
            val placelng = geoad[0].longitude
            println(placelat)

            currentName = place.name
            currentLong = placelng
            currentLat = placelat
            currentAddress = address
            currentID = place.id

            val placelatlng = LatLng(placelat, placelng)

            val zoomLevel = 15f

            //move the camera and set a marker
            mMap.clear()
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placelatlng, zoomLevel))
            mMap.addMarker(MarkerOptions().position(placelatlng).title(place.name))

            searchLocation(placelat.toString(), placelng.toString())
        }
    }
}
