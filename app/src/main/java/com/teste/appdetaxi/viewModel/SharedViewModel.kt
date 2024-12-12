package com.teste.appdetaxi.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teste.appdetaxi.model.Driver
import com.teste.appdetaxi.model.Location
import com.teste.appdetaxi.model.Option

class SharedViewModel : ViewModel() {

    private val _userId = MutableLiveData<String?>()
    val userId: MutableLiveData<String?> = _userId

    private val _originCoordinate = MutableLiveData<Location?>()
    val originCoordinate: MutableLiveData<Location?> = _originCoordinate

    private val _destinationCoordinate = MutableLiveData<Location?>()
    val destinationCoordinate: MutableLiveData<Location?> = _destinationCoordinate

    private val _origin = MutableLiveData<String?>()
    val origin: MutableLiveData<String?> = _origin

    private val _destination = MutableLiveData<String?>()
    val destination: MutableLiveData<String?> = _destination

    private val _distance = MutableLiveData<Double?>()
    val distance: MutableLiveData<Double?> = _distance

    private val _duration = MutableLiveData<String?>()
    val duration: MutableLiveData<String?> = _duration

    private val _routeResponse = MutableLiveData<Any?>()
    val routeResponse: MutableLiveData<Any?> = _routeResponse

    private val _option = MutableLiveData<List<Option>?>()
    val option: MutableLiveData<List<Option>?> = _option

    private val _driver = MutableLiveData<Driver?>()
    val driver: MutableLiveData<Driver?> = _driver

    private val _value = MutableLiveData<Double?>()
    val value: MutableLiveData<Double?> = _value

    fun setUserId(userId: String?) {
        _userId.value = userId
    }

    fun setDestinationCoordinate(destination: Location?) {
        _destinationCoordinate.value = destination
    }

    fun setOriginCoordinate(origin: Location?) {
        _originCoordinate.value = origin
    }

    fun setDestination(destination: String?) {
        _destination.value = destination
    }

    fun setOrigin(origin: String?) {
        _origin.value = origin
    }

    fun setDistance(distance: Double?) {
        _distance.value = distance
    }

    fun setDuration(duration: String?) {
        _duration.value = duration
    }

    fun setRouteResponse(any: Any?) {
        _routeResponse.value = any
    }

    fun setOption(list: List<Option>?) {
        _option.value = list
    }

    fun setDriver(driver: Driver?) {
        _driver.value = driver
    }

    fun setValue(value: Double?) {
        _value.value = value
    }

}