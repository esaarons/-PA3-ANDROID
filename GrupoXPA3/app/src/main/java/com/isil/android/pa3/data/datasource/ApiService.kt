package com.isil.android.pa3.data.datasource

import com.isil.android.pa3.data.model.AuthResponse
import com.isil.android.pa3.data.model.LoginRequest
import com.isil.android.pa3.data.model.VehiculoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Definición de los endpoints de la API propia (Node.js + Express)
interface ApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @GET("api/vehiculos")
    suspend fun getVehiculos(): List<VehiculoDto>

    @POST("api/vehiculos")
    suspend fun createVehiculo(@Body vehiculo: VehiculoDto): VehiculoDto

    @PUT("api/vehiculos/{id}")
    suspend fun updateVehiculo(@Path("id") id: Int, @Body vehiculo: VehiculoDto): VehiculoDto

    @DELETE("api/vehiculos/{id}")
    suspend fun deleteVehiculo(@Path("id") id: Int): Response<Unit>
}
