package com.vasilyev.gdshackathon.domain.entity

data class Place(
    val id: Int,
    val name: String,
    val address: String,
    val busses: String,
    val workSchedule: String,
    val description: String,
    val contacts: String,
    val image: String
)