package com.vasilyev.gdshackathon.presentation.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vasilyev.gdshackathon.data.repository.AiRepositoryImpl
import com.vasilyev.gdshackathon.data.repository.PlaceRepositoryImpl

class MainViewModel(application: Application): ViewModel() {

    private val _mainState = MutableLiveData<MainState>()
    val mainState: LiveData<MainState> get() = _mainState

    private val placeRepository = PlaceRepositoryImpl(application)
    private val aiRepository = AiRepositoryImpl

    suspend fun generatePrompt(prompt: String){
        aiRepository.generateContent(prompt)
    }
}