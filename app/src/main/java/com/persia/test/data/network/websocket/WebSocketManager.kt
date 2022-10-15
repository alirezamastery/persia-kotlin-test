package com.persia.test.data.network.websocket

import com.persia.test.data.network.websocket.payloads.FetchPayload
import com.persia.test.data.network.websocket.responses.FetchResponse
import com.persia.test.data.network.websocket.responses.WebSocketResponse
import com.persia.test.data.network.websocket.responses.WebSocketResponseType
import com.persia.test.global.AppPreferences
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit
import com.persia.test.global.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import timber.log.Timber
import kotlin.concurrent.thread


object WebSocketManager {

    private const val MAX_NORMAL_RETRY = 5
    private const val RECONNECT_INTERVAL = 2000
    private lateinit var mWebSocket: WebSocket
    private var isConnected = false
    private var connectNum = 0
    private val ResponseTypeMap = HashMap<String, Class<out WebSocketResponse>>()

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
        ResponseTypeMap["fetch_response"] = FetchResponse::class.java
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

    fun sendMessage(text: String): Boolean {
        return if (!isConnected()) false else mWebSocket.send(text)
    }

    fun sendMessage(byteString: ByteString): Boolean {
        return if (!isConnected()) false else mWebSocket.send(byteString)
    }

    fun close() {
        if (isConnected()) {
            mWebSocket.cancel()
            mWebSocket.close(1001, "The client actively closes the connection ")
        }
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

                    // val command = HashMap<String, Any>()
                    // command["command"] = 1
                    // sendMessage(JSONObject(command as Map<String, Any>).toString())

                    val fetchPayload = FetchPayload(msg_id = 7)
                    // val command = WebSocketCommands.Fetch(payload = fetchPayload, req_key = "sd;ikjfosi")
                    // val request = moshi.adapter(WebSocketCommands.Fetch::class.java).toJson(command)

                    val fetchRequest = FetchRequest(reqKey = "gfgfg", payload = fetchPayload, command = 1)
                    val request = moshi.adapter(FetchRequest::class.java).toJson(fetchRequest)

                    sendMessage(request)
                } else {
                    reconnect()
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Timber.i("we message text: $text")
                val response = moshi.adapter(WebSocketResponseType::class.java).fromJson(text)
                Timber.i("response type: $response")
                val res = moshi.adapter(ResponseTypeMap[response!!.type]).fromJson(text)
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
                if (response != null) {
                    Timber.i("WS failed：${response.message}")
                }
                Timber.i("connect failed throwable：${t.message}")
                isConnected = false
                reconnect()
            }
        }
    }
}