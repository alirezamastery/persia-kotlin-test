package com.persia.test.data.network.services.persiaatlas

import com.persia.test.data.network.services.persiaatlas.responses.*
import com.persia.test.data.network.services.persiaatlas.responses.actual_product.ActualProductResponse
import com.persia.test.data.network.services.persiaatlas.responses.product.ProductResponse
import com.persia.test.data.network.services.persiaatlas.responses.variant.VariantResponse
import com.persia.test.data.network.services.persiaatlas.responses.variant_selector.VariantSelectorResponse
import kotlinx.coroutines.Deferred
import org.json.JSONObject
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

    // #################### Accounting API ####################
    // ********** Income **********
    @GET("/api/accounting/incomes/")
    fun getIncomesAsync(): Deferred<PaginatedResponse<IncomeResponse>>

    @GET("/api/accounting/incomes/")
    suspend fun getIncomeList(
        @Query("page") pageIndex: Int,
        @Query("page_size") pageSize: Int
    ): Response<PaginatedResponse<IncomeResponse>>

    @GET("/api/accounting/incomes/{income-id}/")
    suspend fun getIncomeById(@Path("income-id") incomeId: Long): Response<IncomeResponse>

    // #################### Products API ####################
    // ********** Variant **********
    @GET("/api/products/variants/")
    suspend fun getVariantList(
        @Query("page") pageNumber: Int,
        @Query("page_size") pageSize: Int
    ): Response<PaginatedResponse<VariantResponse>>

    @GET("/api/products/variants/{variant-id}/")
    suspend fun getVariantById(@Path("variant-id") variantId: Long): Response<VariantResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/products/variants/")
    suspend fun createVariant(@Body data: JSONObject): Response<JSONObject>

    @Headers("Content-Type: application/json")
    @PATCH("/api/products/variants/{variant-id}")
    suspend fun updateVariant(@Body data: JSONObject): Response<JSONObject>

    // ********** Product **********
    @GET("/api/products/products/")
    suspend fun getProductList(
        @Query("page") pageNumber: Int?,
        @Query("page_size") pageSize: Int?,
        @Query("search") searchPhrase: String?,
        @Query("is_active") isActive: Boolean?,
        @Query("price_step") priceStep: Long?,
    ): Response<PaginatedResponse<ProductResponse>>

    @GET("/api/products/products/{product-id}/")
    suspend fun getProductById(@Path("product-id") productId: Long): Response<ProductResponse>

    // ********** Actual Product **********
    @GET("/api/products/actual-products/")
    suspend fun getActualProductList(
        @Query("page") pageNumber: Int?,
        @Query("page_size") pageSize: Int?,
        @Query("search") searchPhrase: String?,
    ): Response<PaginatedResponse<ActualProductResponse>>

    @GET("/api/products/actual-products/{actual-product-id}/")
    suspend fun getActualProductById(@Path("actual-product-id") actualProductId: Long): Response<ActualProductResponse>

    // ********** Selector **********
    @GET("/api/products/variant-selectors/")
    suspend fun getVariantSelectorList(
        @Query("page") pageNumber: Int?,
        @Query("page_size") pageSize: Int?,
        @Query("search") searchPhrase: String?,
        @Query("o") orderBy: String?,
    ): Response<PaginatedResponse<VariantSelectorResponse>>

    @GET("/api/products/variant-selectors/{selector-id}/")
    suspend fun getVariantSelectorById(@Path("selector-id") selectorId: Long): Response<VariantSelectorResponse>
}