package com.vasilyev.gdshackathon.domain.repository

import com.vasilyev.gdshackathon.domain.entity.Route

interface MapRepository {
    suspend fun getRoute(startLocation: String, endLocation: String, mode: String): Result<Route>
}