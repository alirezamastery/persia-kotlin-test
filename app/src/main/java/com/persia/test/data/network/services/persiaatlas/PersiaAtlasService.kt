package com.persia.test.data.network.services.persiaatlas

import com.persia.test.data.network.services.persiaatlas.responses.*
import com.persia.test.data.network.services.persiaatlas.responses.variant.VariantResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

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
    suspend fun getIncomeList(
        @Query("page") pageIndex: Int,
        @Query("page_size") pageSize: Int
    ): Response<PaginatedResponse<IncomeResponse>>

    @GET("/api/accounting/incomes/{income-id}/")
    suspend fun getIncomeById(@Path("income-id") incomeId: Long): Response<IncomeResponse>


    @GET("/api/products/variants/")
    suspend fun getVariantList(
        @Query("page") pageNumber: Int,
        @Query("page_size") pageSize: Int
    ): Response<PaginatedResponse<VariantResponse>>

    @GET("/api/products/variants/{variant-id}/")
    suspend fun getVariantById(@Path("variant-id") variantId: Long): Response<VariantResponse>
}