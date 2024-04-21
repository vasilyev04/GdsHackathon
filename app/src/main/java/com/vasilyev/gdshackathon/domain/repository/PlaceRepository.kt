package com.vasilyev.gdshackathon.domain.repository

import com.vasilyev.gdshackathon.domain.entity.Place

interface PlaceRepository {
    suspend fun getPlaces(listOfIds: List<Int>): List<Place>

    suspend fun getPlace(id: Int): Place
}