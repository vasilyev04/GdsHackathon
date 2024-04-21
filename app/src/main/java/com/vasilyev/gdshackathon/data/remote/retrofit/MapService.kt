package com.vasilyev.gdshackathon.data.remote.retrofit

import com.vasilyev.gdshackathon.BuildConfig
import com.vasilyev.gdshackathon.data.remote.dto.RouteDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MapService {

    @GET("/maps/api/directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String = API_KEY
    ): Result<RouteDto>

    companion object{
        private const val API_KEY = BuildConfig.MAPS_API_KEY
    }
}