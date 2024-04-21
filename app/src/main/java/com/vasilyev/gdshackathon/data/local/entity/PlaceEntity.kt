package com.vasilyev.gdshackathon.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val address: String,
    val buses: String,
    @ColumnInfo("work_schedule")
    val workSchedule: String,
    val description: String,
    val contacts: String,
    val image: String
)