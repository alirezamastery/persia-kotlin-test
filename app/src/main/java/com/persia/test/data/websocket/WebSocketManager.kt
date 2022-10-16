package com.persia.test.data.websocket

import com.persia.test.data.websocket.payloads.FetchPayload
import com.persia.test.data.websocket.payloads.StopRobotPayload
import com.persia.test.data.websocket.responses.*
import com.persia.test.global.AppPreferences
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit
import com.persia.test.global.Constants
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import timber.log.Timber
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.thread


object WebSocketManager {


    @Volatile
    private var isConnected = false

    private var connectNum = 0
    private const val MAX_NORMAL_RETRY = 5
    private const val RECONNECT_INTERVAL = 2000
    private lateinit var mWebSocket: WebSocket

    // private val ResponseTypeMap = HashMap<String, Class<out WebSocketBaseResponse>>()
    private val ResponseTypeMap = HashMap<String, Class<*>>()
    private val PayloadTypeMap = HashMap<Int, Class<*>>()
    private val sentCommands: MutableList<String> = mutableListOf()
    private val commandQueue: MutableList<String> = mutableListOf()

    private var client: OkHttpClient = OkHttpClient.Builder()
        .writeTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .build()

    private var request: Request = Request
        .Builder()
        .addHeader("token", AppPreferences.accessToken!!)
        .addHeader("clienttype", "android")
        .url(Constants.WEBSOCKET_URL).build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    init {
        ResponseTypeMap["fetch_response"] = FetchData::class.java
        ResponseTypeMap["robot_stopped"] = RobotStoppedData::class.java

        PayloadTypeMap[WebSocketCommands.FETCH] = FetchPayload::class.java
        PayloadTypeMap[WebSocketCommands.STOP_ROBOT] = StopRobotPayload::class.java
    }

    fun connect() {
        if (isConnected()) {
            Timber.i("WS is already connected")
            return
        }
        client.newWebSocket(request, createListener())
    }

    fun reconnect() {
        if (connectNum <= MAX_NORMAL_RETRY) {
            try {
                thread(start = true) {
                    Thread.sleep(RECONNECT_INTERVAL.toLong())
                    connect()
                    connectNum++
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        } else {
            Timber.i("reconnect over $MAX_NORMAL_RETRY, please check url or network")
        }
    }

    fun isConnected(): Boolean {
        return isConnected
    }

    private fun sendMessage(text: String): Boolean {
        return mWebSocket.send(text)
    }

    private fun sendMessage(byteString: ByteString): Boolean {
        return mWebSocket.send(byteString)
    }

    fun close() {
        if (isConnected()) {
            mWebSocket.cancel()
            mWebSocket.close(1001, "The client actively closes the connection ")
        }
    }

    fun sendCommand(command: Int, payload: Any) {
        val requestKey = UUID.randomUUID().toString()
        val request = WebSocketRequest(
            command = command,
            requestKey = requestKey,
            payload = payload
        )
        val types = Types.newParameterizedType(
            WebSocketRequest::class.java,
            PayloadTypeMap[command]
        )
        val adapter = moshi.adapter<WebSocketRequest<*>>(types)
        val json = adapter.toJson(request)
        Timber.i("send command: $json")
        if (!isConnected) {
            commandQueue.add(json)
            reconnect()
        } else {
            sendMessage(json)
            sentCommands.add(requestKey)
        }
    }

    private fun handleCommandQueue() {
        commandQueue.forEach { json ->
            sendMessage(json)
        }
        commandQueue.clear()
    }


    private fun createListener(): WebSocketListener {
        return object : WebSocketListener() {
            override fun onOpen(
                webSocket: WebSocket,
                response: Response
            ) {
                super.onOpen(webSocket, response)
                Timber.i("WS open: $response")
                connectNum = 0
                mWebSocket = webSocket
                if (response.code == 101) {
                    isConnected = true
                    Timber.i("WS connect success")
                    handleCommandQueue()
                    sendCommand(
                        command = WebSocketCommands.FETCH,
                        payload = FetchPayload(msg_id = 7)
                    )
                } else {
                    reconnect()
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Timber.i("WS message text: $text")

                val response = moshi.adapter(WebSocketResponseInfo::class.java).fromJson(text)
                Timber.i("response type: $response")
                if (response == null) {
                    Timber.e("WS response is null")
                    return
                }
                if (response.requestKey !in sentCommands) {
                    Timber.i("no command with this key: ${response.requestKey}")
                    return
                }
                val types = Types.newParameterizedType(
                    WebSocketResponse::class.java,
                    ResponseTypeMap[response.type]
                )
                val adapter = moshi.adapter<WebSocketResponse<*>>(types)
                val res = adapter.fromJson(text)
                Timber.i("parsed response: $res")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                Timber.i("we message byte")
            }

            override fun onClosing(
                webSocket: WebSocket,
                code: Int,
                reason: String
            ) {
                super.onClosing(webSocket, code, reason)
                isConnected = false
            }

            override fun onClosed(
                webSocket: WebSocket,
                code: Int,
                reason: String
            ) {
                super.onClosed(webSocket, code, reason)
                isConnected = false
            }

            override fun onFailure(
                webSocket: WebSocket,
                t: Throwable,
                response: Response?
            ) {
                super.onFailure(webSocket, t, response)
                isConnected = false
                if (response != null) {
                    Timber.i("WS failed：${response.message}")
                }
                Timber.i("connect failed throwable：${t.message}")
                reconnect()
            }
        }
    }

    data class WebSocketResponseInfo(
        val type: String,
        @Json(name = "req_key") val requestKey: String
    )

}