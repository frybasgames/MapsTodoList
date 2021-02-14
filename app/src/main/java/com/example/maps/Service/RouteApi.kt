package com.example.maps.Service

import com.example.maps.Model.RouteModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RouteApi {
    @Headers("Authorization: 5b3ce3597851110001cf624848c28baf5f6f4c9ba480b1ee44c1a7e0")
    @POST("v2/directions/driving-car/json")
    fun createRoute(@Body data: RouteModel): Call<List<RouteModel>>
}