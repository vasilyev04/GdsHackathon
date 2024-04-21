package com.vasilyev.gdshackathon.presentation.main

import com.vasilyev.gdshackathon.domain.entity.Place

sealed class MainState {
    class Places(val list: List<Place>): MainState()

    class AiAnswer(val answer: String): MainState()

    class PlaceReceived(val place: Place): MainState()

    object Loading : MainState()
}