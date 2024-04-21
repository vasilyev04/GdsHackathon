package com.vasilyev.gdshackathon.data.repository

import android.util.Log
import com.vasilyev.gdshackathon.data.mapper.RouteMapper
import com.vasilyev.gdshackathon.data.remote.retrofit.RetrofitInstance
import com.vasilyev.gdshackathon.domain.entity.Route
import com.vasilyev.gdshackathon.domain.repository.MapRepository

class MapRepositoryImpl: MapRepository {
    private val mapApi = RetrofitInstance.mapApi
    private val routeMapper = RouteMapper()

    override suspend fun getRoute(startLocation: String, endLocation: String, mode: String): Result<Route> {
        val result = mapApi.getDirections(startLocation, endLocation, mode)

        if(result.isSuccess){
            val response = result.getOrNull()
                ?: return Result.failure(RuntimeException("Response body is null"))
            Log.d("MyTag", response.toString())
            val route = routeMapper.mapResponseToRoute(response)

            return Result.success(route)
        }else{
            return Result.failure(result.exceptionOrNull() ?: RuntimeException("Unknown error"))
        }
    }
}