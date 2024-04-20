package com.vasilyev.gdshackathon.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RouteDto (
    @SerializedName("geocoded_waypoints")
    val geocodedWaypoints: List<GeocodedWaypoint>,
    val routes: List<Route>,
    val status: String
)