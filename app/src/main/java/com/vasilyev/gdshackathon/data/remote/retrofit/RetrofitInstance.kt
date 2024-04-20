package com.vasilyev.gdshackathon.data.remote.retrofit

import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://maps.googleapis.com/"

    private val _retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ResultCallAdapterFactory.create())
            .build()
    }

    val mapApi: MapService by lazy{
        _retrofit.create(MapService::class.java)
    }
}