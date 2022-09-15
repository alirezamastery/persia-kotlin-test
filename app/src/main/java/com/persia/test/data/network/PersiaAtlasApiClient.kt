package com.persia.test.data.network

import com.persia.test.data.network.services.persiaatlas.PaginatedResponse
import com.persia.test.data.network.services.persiaatlas.PersiaAtlasService
import com.persia.test.data.network.services.persiaatlas.responses.*
import com.persia.test.data.network.services.persiaatlas.responses.variant.VariantResponse
import retrofit2.Response

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

}