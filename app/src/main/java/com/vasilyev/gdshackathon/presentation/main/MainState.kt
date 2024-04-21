package com.vasilyev.gdshackathon.presentation.main

import com.vasilyev.gdshackathon.domain.entity.Place

sealed class MainState {
    class PlacesReceived(list: List<Place>): MainState()
}