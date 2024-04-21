package com.vasilyev.gdshackathon.presentation.map

import com.google.android.gms.maps.model.LatLng
import com.vasilyev.gdshackathon.domain.entity.Place

sealed class MapState {
    class RequestedPlace(val place: Place): MapState()

    class Route(val route: List<LatLng>): MapState()

    object Loading : MapState()
}