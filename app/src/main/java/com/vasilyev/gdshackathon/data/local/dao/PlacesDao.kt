package com.vasilyev.gdshackathon.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vasilyev.gdshackathon.data.local.entity.PlaceEntity

@Dao
interface PlacesDao {
    @Query("SELECT * FROM places WHERE id IN (:ids)")
    suspend fun getPlaces(ids: List<Int>): List<PlaceEntity>

    @Query("SELECT * FROM places WHERE id=:id")
    suspend fun getPlace(id: Int): PlaceEntity
}