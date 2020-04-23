package com.example.a438finalproject.Data

data class LocationData (
    val on_street : List<OnStreet>,
    val off_street : List<OffStreet>,
    val off_street_parkedge : List<OffStreetParkedge>
)


data class OnStreet (
    val uid : String, //string
    val name : String, //string
    val kind : String, //string
    val min_hourly_rate : String, //int
//    val max_hourly_rate : Double, //int
//    val max_duration_mins : Double, //int
//    val consumer_parking_allowed : Boolean, //boolean
//    val until : String, //string (a date/time)
//    val next_disallowed_type : String, //string
//    val availability_state : String, //string
//    val capacity : Double, //int
//    val estimated_spaces : String, //string
//    val distance_from_center : Double //int
    val geojson_center : GeojsonCenter
)

data class OffStreet (
    val uid : String, //string
    val name : String, //string
    val kind : String, //string
    val min_hourly_rate : String, //int
//    val max_hourly_rate : Double, //int
//    val max_duration_mins : Double, //int
//    val consumer_parking_allowed : Boolean, //boolean
//    val until : String, //string (a date/time)
//    val next_disallowed_type : String, //string
//    val availability_state : String, //string
//    val capacity : Double, //int
//    val estimated_spaces : String, //string
//    val distance_from_center : Double //int
   val geojson_center : GeojsonCenter
)

data class OffStreetParkedge (

    val parkedge_id : Int,
    val name : String, //string
//    val kind : String, //string
//    val available_spaces : Int, //int
//    val availability_state : String, //string
//    val capacity : Int, //int
//    val distance_from_center : Double, //double
    val geojson_center : GeojsonCenter
)

data class GeojsonCenter (
    val type : String,
    val coordinates : List<Double>
    //within a coordinate list named coord (for example),
    // coord[0] = longitude and coord[1] = latitude
)

data class GeojsonGeometry (
    val type : String,
    val coordinates : List<Int>
)

data class adapterData(
    val address: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val id:String
)