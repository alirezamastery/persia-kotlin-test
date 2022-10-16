package com.persia.test.data.websocket.responses

import com.squareup.moshi.Json

data class RobotStoppedData(
    @Json(name = "robot_is_on") val robotIsOn: Boolean
)