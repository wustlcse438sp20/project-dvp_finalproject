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

    fun getCoordinates(locationData: LocationData?): MutableList<Triple<LatLng,String,String>> {
        val list : MutableList<Triple<LatLng,String,String>> = mutableListOf()
        for (onStreet in locationData!!.on_street) {
            val longitude = onStreet.geojson_center.coordinates[0]
            val latitude = onStreet.geojson_center.coordinates[1]
            val latlng = LatLng(latitude,longitude)
            val title = onStreet.name
            val price = onStreet.min_hourly_rate
            val info = Triple(latlng,title,price)
            list.add(info)
        }
        for (offStreet in locationData!!.off_street) {
            val longitude = offStreet.geojson_center.coordinates[0]
            val latitude = offStreet.geojson_center.coordinates[1]
            val latlng = LatLng(latitude,longitude)
            val title = offStreet.name
            val price = offStreet.min_hourly_rate
            val info = Triple(latlng,title,price)
            list.add(info)
        }
        for (edge in locationData!!.off_street_parkedge) {
            val longitude = edge.geojson_center.coordinates[0]
            val latitude = edge.geojson_center.coordinates[1]
            val latlng = LatLng(latitude,longitude)
            val title = edge.name
            val price = "No Data"
            val info = Triple(latlng,title,price)
            list.add(info)
        }
        return list
    }

}
