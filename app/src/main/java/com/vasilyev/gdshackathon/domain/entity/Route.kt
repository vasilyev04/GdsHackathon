package com.vasilyev.gdshackathon.domain.entity

data class Route(
    val startLocation: Location,
    val endLocation: Location,
    val route: String
)