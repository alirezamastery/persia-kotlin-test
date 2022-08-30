package com.persia.test.data.network.services.persiaatlas

import com.persia.test.data.network.services.persiaatlas.responses.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface PersiaAtlasService {

    @Headers("Content-Type: application/json")
    @POST("/api/token/")
    suspend fun login(@Body loginCredentials: LoginRequest): Response<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/token/refresh/")
    suspend fun refreshToken(@Body refreshToken: RefreshToken): Response<LoginResponse>

    @GET("/api/users/profile/")
    suspend fun getUserProfile(): Response<UserProfileResponse>

    @GET("/api/accounting/incomes/")
    fun getIncomesAsync(): Deferred<PaginatedResponse<IncomeResponse>>

    @GET("/api/accounting/incomes/")
    suspend fun getIncomes(): Response<PaginatedResponse<IncomeResponse>>
}