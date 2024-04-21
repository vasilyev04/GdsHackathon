package com.vasilyev.gdshackathon.data.repository

import android.app.Application
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.vasilyev.gdshackathon.data.local.RoomInstance
import com.vasilyev.gdshackathon.data.local.entity.PlaceEntity
import com.vasilyev.gdshackathon.data.mapper.PlaceMapper
import com.vasilyev.gdshackathon.domain.entity.Place
import com.vasilyev.gdshackathon.domain.repository.PlaceRepository

class PlaceRepositoryImpl(
    application: Application
): PlaceRepository {
    private val placeDao = RoomInstance.getInstance(application).placesDao()
    private val placeMapper = PlaceMapper()

    override suspend fun getPlaces(listOfIds: List<Int>): List<Place> {
        return placeMapper.mapDbEntitiesToModels(placeDao.getPlaces(listOfIds))
    }

    override suspend fun getPlace(id: Int): Place {
        return placeMapper.mapDbEntityToModel(placeDao.getPlace(id))
    }
}