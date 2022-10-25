package com.persia.test.data.websocket.responses

import com.squareup.moshi.Json

data class RobotRunningData(
    @Json(name = "robot_running") val robotRunning: Boolean
)
