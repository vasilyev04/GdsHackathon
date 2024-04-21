package com.vasilyev.gdshackathon.domain.repository

interface AiRepository {
    suspend fun generateContent(prompt: String): String
}