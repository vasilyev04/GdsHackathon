package com.vasilyev.gdshackathon.data.mapper

import com.vasilyev.gdshackathon.data.remote.dto.EndLocation
import com.vasilyev.gdshackathon.data.remote.dto.RouteDto
import com.vasilyev.gdshackathon.data.remote.dto.StartLocation
import com.vasilyev.gdshackathon.domain.entity.Location
import com.vasilyev.gdshackathon.domain.entity.Route

class RouteMapper {
    fun mapResponseToRoute(entity: RouteDto): Route {
        val startLocation = mapDtoStartLocationToModel(entity.routes[0].legs[0].start_location)
        val endLocation = mapDtoEndLocationToModel(entity.routes[0].legs[0].end_location)
        val route = entity.routes[0].overview_polyline.points

        return Route(startLocation, endLocation, route)
    }

    private fun mapDtoStartLocationToModel(start: StartLocation): Location {
        return Location(
            lat = start.lat,
            lng = start.lng
        )
    }

    private fun mapDtoEndLocationToModel(end: EndLocation): Location{
        return Location(
            lat = end.lat,
            lng = end.lng
        )
    }
}