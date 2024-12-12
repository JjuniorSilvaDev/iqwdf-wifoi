package com.teste.appdetaxi.api

import com.teste.appdetaxi.model.ConfirmRequest
import com.teste.appdetaxi.model.ConfirmResponse
import com.teste.appdetaxi.model.EstimateRequest
import com.teste.appdetaxi.model.EstimateResponse
import com.teste.appdetaxi.model.GetRidesResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // Endpoint POST /ride/estimate
    @POST("/ride/estimate")
    fun estimateRide(@Body request: EstimateRequest): Call<EstimateResponse>

    // Endpoint PATCH /ride/confirm
    @PATCH("/ride/confirm")
    fun confirmRide(@Body request: ConfirmRequest): Call<ConfirmResponse>

    // Endpoint GET /ride/{customer_id}
    @GET("/ride/{customer_id}")
    fun getRides(@Path("customer_id") customerId: String): Call<GetRidesResponse>
}