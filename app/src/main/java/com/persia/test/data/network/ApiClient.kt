package com.persia.test.data.network

import com.persia.test.data.network.services.persiaatlas.PaginatedResponse
import com.persia.test.data.network.services.persiaatlas.PersiaAtlasService
import com.persia.test.data.network.services.persiaatlas.responses.*
import retrofit2.Response

class ApiClient(
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

    suspend fun getIncomes(): CustomResponse<PaginatedResponse<IncomeResponse>> {
        return safeApiCall { persiaAtlasService.getIncomes() }
    }
}