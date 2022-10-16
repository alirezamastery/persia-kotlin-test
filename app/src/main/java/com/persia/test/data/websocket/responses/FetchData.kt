package com.persia.test.data.websocket.responses

import com.squareup.moshi.Json


data class FetchData(
    @Json(name = "robot_running") val robotRunning: Boolean,
    @Json(name = "robot_is_on") val robotIsOn: Boolean,
)
