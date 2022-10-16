package com.persia.test.data.websocket

interface MessageListener {

    fun onConnectSuccess()
    fun onConnectFailed()
    fun onClose()
    fun onMessage(text: String?)
}