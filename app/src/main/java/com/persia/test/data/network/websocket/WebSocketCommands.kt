package com.persia.test.data.network.websocket

import com.persia.test.data.network.websocket.payloads.FetchPayload
import com.persia.test.data.network.websocket.payloads.StopRobotPayload

abstract class WebSocketRequest(val payload: Any) {

    abstract val command: Int
    abstract val reqKey: String
}

class FetchRequest(
    override val command: Int,
    override val reqKey: String,
    payload: Any,
) : WebSocketRequest(payload) {

}

sealed class WebSocketCommands(val command: Int) {
    class Fetch(
        private val req_key: String,
        val payload: FetchPayload
    ) : WebSocketCommands(command = 1)

    data class StopRobot(
        val payload: StopRobotPayload
    ) : WebSocketCommands(2)
}
