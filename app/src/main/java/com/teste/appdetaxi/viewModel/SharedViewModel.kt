package com.teste.appdetaxi.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teste.appdetaxi.model.Location
import com.teste.appdetaxi.model.Option

class SharedViewModel : ViewModel() {

    private val _userId = MutableLiveData<String?>()
    val userId: MutableLiveData<String?> = _userId

    private val _origin = MutableLiveData<Location?>()
    val origin: MutableLiveData<Location?> = _origin

    private val _destination = MutableLiveData<Location?>()
    val destination: MutableLiveData<Location?> = _destination

    private val _distance = MutableLiveData<Double?>()
    val distance: MutableLiveData<Double?> = _distance

    private val _duration = MutableLiveData<String?>()
    val duration: MutableLiveData<String?> = _duration

    private val _options = MutableLiveData<List<Option>?>()
    val options: MutableLiveData<List<Option>?> = _options

    private val _routeResponse = MutableLiveData<Any?>()
    val routeResponse: MutableLiveData<Any?> = _routeResponse

    fun setUserId(userId: String?) {
        _userId.value = userId
    }

    fun setDestination(destination: Location?) {
        _destination.value = destination
    }

    fun setOrigin(origin: Location?) {
        _origin.value = origin
    }

    fun setDistance(distance: Double?) {
        _distance.value = distance
    }

    fun setDuration(duration: String?) {
        _duration.value = duration
    }

    fun setOptions(list: List<Option>?) {
        _options.value = list
    }

    fun setRouteResponse(any: Any?) {
        _routeResponse.value = any
    }

}