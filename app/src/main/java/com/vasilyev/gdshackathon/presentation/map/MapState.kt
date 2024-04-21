package com.vasilyev.gdshackathon.presentation.map

import com.google.android.gms.maps.model.LatLng
import com.vasilyev.gdshackathon.domain.entity.Place

sealed class MapState {
    class RequestedPlace(place: Place): MapState()

    class Route(route: List<LatLng>): MapState()
}