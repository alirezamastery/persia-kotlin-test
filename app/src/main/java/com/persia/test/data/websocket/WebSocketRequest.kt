package com.persia.test.data.websocket

import com.squareup.moshi.Json

data class WebSocketRequest<T>(
    val command: Int,
    @Json(name = "req_key") val requestKey: String,
    val payload: T
)
