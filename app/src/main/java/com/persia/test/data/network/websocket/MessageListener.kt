package com.persia.test.data.network.websocket

interface MessageListener {

    fun onConnectSuccess()
    fun onConnectFailed()
    fun onClose()
    fun onMessage(text: String?)
}