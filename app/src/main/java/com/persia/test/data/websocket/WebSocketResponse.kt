package com.persia.test.data.websocket

import com.squareup.moshi.Json

data class WebSocketResponse<T>(
    val type: String,
    @Json(name = "req_key") val requestKey: String,
    val data: T
)
