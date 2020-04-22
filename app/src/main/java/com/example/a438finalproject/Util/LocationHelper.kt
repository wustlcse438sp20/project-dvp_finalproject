package com.example.a438finalproject.Util

import com.example.a438finalproject.Data.LocationData
import com.google.android.gms.maps.model.LatLng

class LocationHelper {

    fun garbageCleanup(locationData : LocationData?) : LocationData?{
        val cleanedOnStreets = locationData!!.on_street.filter { !it.name.isNullOrEmpty() }
        val cleanedOffStreets = locationData!!.off_street.filter { !it.name.isNullOrEmpty() }
        val cleanedOffStreetParkedge = locationData!!.off_street_parkedge.filter { !it.name.isNullOrEmpty() }

        val filteredLocData : LocationData? = LocationData(cleanedOnStreets, cleanedOffStreets, cleanedOffStreetParkedge)

        return filteredLocData
    }

    fun getCoordinates(locationData: LocationData?): MutableList<Pair<LatLng,String>> {
        val list : MutableList<Pair<LatLng,String>> = mutableListOf()
        for (onStreet in locationData!!.on_street) {
            val longitude = onStreet.geojson_center.coordinates[0]
            val latitude = onStreet.geojson_center.coordinates[1]
            val latlng = LatLng(latitude,longitude)
            val title = onStreet.name
            val pair = Pair(latlng,title)
            list.add(pair)
        }
        for (offStreet in locationData!!.off_street) {
            val longitude = offStreet.geojson_center.coordinates[0]
            val latitude = offStreet.geojson_center.coordinates[1]
            val latlng = LatLng(latitude,longitude)
            val title = offStreet.name
            val pair = Pair(latlng,title)
            list.add(pair)
        }
        for (parkedge in locationData!!.off_street_parkedge) {
            val longitude = parkedge.geojson_center.coordinates[0]
            val latitude = parkedge.geojson_center.coordinates[1]
            val latlng = LatLng(latitude,longitude)
            val title = parkedge.name
            val pair = Pair(latlng,title)
            list.add(pair)
        }
        return list
    }

}
