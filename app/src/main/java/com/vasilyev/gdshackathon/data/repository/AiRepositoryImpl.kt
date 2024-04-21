package com.vasilyev.gdshackathon.data.repository

import com.vasilyev.gdshackathon.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.vasilyev.gdshackathon.domain.repository.AiRepository

object AiRepositoryImpl: AiRepository {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    private val chat = generativeModel.startChat()

    override suspend fun generateContent(prompt: String): String {

        return chat.sendMessage(prompt).text.toString()
    }
}