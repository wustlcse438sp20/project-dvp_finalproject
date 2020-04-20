package com.example.a438finalproject.Network

import com.example.a438finalproject.Data.LocationData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap


interface LocationInterface {

    //goal: https://guidance.streetline.com/v3/guidance/by-point?latitude=38.907192&longitude=-77.036873&limit=1
    //hashmap: latitude = int, longitude = int, limit = something
    //add a path var as the first parameter before the query map if you want to change the by-point to by-area or anything like that
    @GET("https://guidance.streetline.com/v3/guidance/by-point")
    suspend fun getLocByPoint(@QueryMap filter: HashMap<String, String>, @Query("limit")limit : String) : Response<LocationData>

}