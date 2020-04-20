package com.example.a438finalproject.Util

import com.example.a438finalproject.Data.LocationData

class LocationHelper {

    fun garbageCleanup(locationData : LocationData?) : LocationData?{
        val cleanedOnStreets = locationData!!.on_street.filter { !it.name.isNullOrEmpty() }
        val cleanedOffStreets = locationData!!.off_street.filter { !it.name.isNullOrEmpty() }
        val cleanedOffStreetParkedge = locationData!!.off_street_parkedge.filter { !it.name.isNullOrEmpty() }

        val filteredLocData : LocationData? = LocationData(cleanedOnStreets, cleanedOffStreets, cleanedOffStreetParkedge)
        print("breakpoint")
        return filteredLocData
    }

    fun getCoordinates(locationData: LocationData?) {
        for (onStreet in locationData!!.on_street) {
            var longitude = onStreet.geojson_center.coordinates[0]
            var latitude = onStreet.geojson_center.coordinates[1]
            //display data (longitude, latitude, onStreet)
        }
    }
}
