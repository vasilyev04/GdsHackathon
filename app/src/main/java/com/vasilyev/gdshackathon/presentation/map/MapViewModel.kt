package com.vasilyev.gdshackathon.presentation.map

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vasilyev.gdshackathon.data.repository.PlaceRepositoryImpl
import com.vasilyev.gdshackathon.domain.entity.Place
import kotlinx.coroutines.launch

class MapViewModel(
    application: Application
): ViewModel() {
    private val _mapState = MutableLiveData<MapState>()
    val mapState: LiveData<MapState> get() = _mapState

    private val placeRepository = PlaceRepositoryImpl(application)

    fun getPlaceById(id: Int){
        viewModelScope.launch {
            val place = placeRepository.getPlace(id)
            _mapState.value = MapState.RequestedPlace(place)
        }
    }

}