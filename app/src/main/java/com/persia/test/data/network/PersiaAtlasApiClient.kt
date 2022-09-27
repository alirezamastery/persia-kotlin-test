package com.persia.test.data.network

import com.persia.test.data.network.services.persiaatlas.PaginatedResponse
import com.persia.test.data.network.services.persiaatlas.PersiaAtlasService
import com.persia.test.data.network.services.persiaatlas.responses.*
import com.persia.test.data.network.services.persiaatlas.responses.actual_product.ActualProductResponse
import com.persia.test.data.network.services.persiaatlas.responses.product.ProductResponse
import com.persia.test.data.network.services.persiaatlas.responses.variant.VariantResponse
import com.persia.test.data.network.services.persiaatlas.responses.variant_selector.VariantSelectorResponse
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Query

class PersiaAtlasApiClient(
    private val persiaAtlasService: PersiaAtlasService
) {

    private inline fun <T> safeApiCall(apiCall: () -> Response<T>): CustomResponse<T> {
        return try {
            CustomResponse.success(apiCall.invoke())
        } catch (e: Exception) {
            CustomResponse.failure(e)
        }
    }

    // api calls

    suspend fun login(loginCredentials: LoginRequest): CustomResponse<LoginResponse> {
        return safeApiCall { persiaAtlasService.login(loginCredentials) }
    }

    suspend fun refreshToken(refreshToken: RefreshToken): CustomResponse<LoginResponse> {
        return safeApiCall { persiaAtlasService.refreshToken(refreshToken) }
    }

    suspend fun getUserProfile(): CustomResponse<UserProfileResponse> {
        return safeApiCall { persiaAtlasService.getUserProfile() }
    }

    // #################### Accounting ####################
    // ********** Income **********
    suspend fun getIncomeList(
        pageIndex: Int,
        pageSize: Int
    ): CustomResponse<PaginatedResponse<IncomeResponse>> {
        return safeApiCall { persiaAtlasService.getIncomeList(pageIndex, pageSize) }
    }

    suspend fun getIncomeById(incomeId: Long): CustomResponse<IncomeResponse> {
        return safeApiCall { persiaAtlasService.getIncomeById(incomeId) }
    }

    // #################### Products ####################
    // ********** Variant **********
    suspend fun getVariantList(
        pageNumber: Int,
        pageSize: Int
    ): CustomResponse<PaginatedResponse<VariantResponse>> {
        return safeApiCall { persiaAtlasService.getVariantList(pageNumber, pageSize) }
    }

    suspend fun getVariantById(variantId: Long): CustomResponse<VariantResponse> {
        return safeApiCall { persiaAtlasService.getVariantById(variantId) }
    }

    suspend fun createVariant(data: JSONObject): CustomResponse<JSONObject> {
        return safeApiCall { persiaAtlasService.createVariant(data) }
    }

    suspend fun updateVariant(data: JSONObject): CustomResponse<JSONObject> {
        return safeApiCall { persiaAtlasService.updateVariant(data) }
    }

    // ********** Product **********
    suspend fun getProductList(
        pageNumber: Int? = null,
        pageSize: Int? = null,
        searchPhrase: String? = null,
        isActive: Boolean? = null,
        priceStep: Long? = null,
    ): CustomResponse<PaginatedResponse<ProductResponse>> {
        return safeApiCall {
            persiaAtlasService.getProductList(
                pageNumber, pageSize, searchPhrase, isActive, priceStep
            )
        }
    }

    suspend fun getProductById(productId: Long): CustomResponse<ProductResponse> {
        return safeApiCall { persiaAtlasService.getProductById(productId) }
    }

    // ********** Actual Product **********
    suspend fun getActualProductList(
        pageNumber: Int? = null,
        pageSize: Int? = null,
        searchPhrase: String? = null,
    ): CustomResponse<PaginatedResponse<ActualProductResponse>> {
        return safeApiCall {
            persiaAtlasService.getActualProductList(
                pageNumber = pageNumber,
                pageSize = pageSize,
                searchPhrase = searchPhrase
            )
        }
    }

    suspend fun getActualProductById(actualProductId: Long): CustomResponse<ActualProductResponse> {
        return safeApiCall { persiaAtlasService.getActualProductById(actualProductId) }
    }

    // ********** Selector **********
    suspend fun getVariantSelectorList(
        pageNumber: Int? = null,
        pageSize: Int? = null,
        searchPhrase: String? = null,
        orderBy: String? = null
    ): CustomResponse<PaginatedResponse<VariantSelectorResponse>> {
        return safeApiCall {
            persiaAtlasService.getVariantSelectorList(
                pageNumber = pageNumber,
                pageSize = pageSize,
                searchPhrase = searchPhrase,
                orderBy = orderBy
            )
        }
    }

    suspend fun getVariantSelectorById(selectorId: Long): CustomResponse<VariantSelectorResponse> {
        return safeApiCall {
            persiaAtlasService.getVariantSelectorById(selectorId)
        }
    }
}