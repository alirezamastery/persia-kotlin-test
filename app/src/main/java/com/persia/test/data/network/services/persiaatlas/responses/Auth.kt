package com.persia.test.data.network.services.persiaatlas.responses

data class LoginRequest(
    var mobile: String,
    var password: String
)

data class LoginResponse(
    var refresh: String,
    var access: String
)

data class RefreshToken(
    var refresh: String
)

