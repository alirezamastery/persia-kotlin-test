package com.persia.test.data.network.websocket.responses

import com.squareup.moshi.Json

abstract class WebSocketResponse {

    abstract val type: String
    abstract val data: Any
}

data class WebSocketResponseType(val type: String)

data class FetchData(
    @Json(name = "robot_running") val robotRunning: Boolean,
    @Json(name = "robot_is_on") val robotIsOn: Boolean,
)

data class FetchResponse(
    override val type: String,
    override val data: FetchData
) : WebSocketResponse()


