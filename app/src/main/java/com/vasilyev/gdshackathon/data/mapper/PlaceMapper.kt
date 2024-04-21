package com.vasilyev.gdshackathon.data.mapper

import com.vasilyev.gdshackathon.data.local.entity.PlaceEntity
import com.vasilyev.gdshackathon.domain.entity.Place

class PlaceMapper {
    fun mapDbEntityToModel(entity: PlaceEntity): Place {
        return Place(
            id = entity.id,
            name = entity.name,
            address = entity.address,
            busses = entity.buses,
            workSchedule = entity.workSchedule,
            description = entity.description,
            contacts = entity.contacts,
            image = entity.image
        )
    }

    fun mapDbEntitiesToModels(entityList: List<PlaceEntity>): List<Place>{
        return entityList.map{
            mapDbEntityToModel(it)
        }.toList()
    }
}