package com.vasilyev.gdshackathon.presentation.map

import com.vasilyev.gdshackathon.domain.entity.Place

sealed class MapState {
    class RequestedPlace(place: Place): MapState()
}