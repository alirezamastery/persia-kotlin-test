package com.persia.test.data.network.services.persiaatlas

import com.persia.test.data.network.services.persiaatlas.responses.LoginResponse
import com.persia.test.data.network.services.persiaatlas.responses.RefreshToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PersiaAtlasAuthService {

    @Headers("Content-Type: application/json")
    @POST("/api/auth/refresh/")
    suspend fun getRefreshToken(@Body refreshToken: RefreshToken): Response<LoginResponse>
}