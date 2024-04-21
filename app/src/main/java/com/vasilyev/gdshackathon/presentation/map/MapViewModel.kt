package com.vasilyev.gdshackathon.presentation.map

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.vasilyev.gdshackathon.data.repository.MapRepositoryImpl
import com.vasilyev.gdshackathon.data.repository.PlaceRepositoryImpl
import com.vasilyev.gdshackathon.domain.entity.Place
import kotlinx.coroutines.launch

class MapViewModel(
    application: Application
): ViewModel() {
    private val _mapState = MutableLiveData<MapState>()
    val mapState: LiveData<MapState> get() = _mapState

    private val placeRepository = PlaceRepositoryImpl(application)
    private val mapRepository = MapRepositoryImpl()

    fun getPlaceById(id: Int){
        viewModelScope.launch {
            val place = placeRepository.getPlace(id)
            _mapState.value = MapState.RequestedPlace(place)
        }
    }

    fun getRoute(location: String, place: Place){
        viewModelScope.launch {
            val route = mapRepository.getRoute(location, place.address)

            route.onSuccess {
                _mapState.postValue(MapState.Route(decodePoly(it.route)))
            }
        }
    }

    //decoding polylines from string to list of latlng coordinates
    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5,
                lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }

}