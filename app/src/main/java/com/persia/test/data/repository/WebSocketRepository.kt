package com.persia.test.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import com.persia.test.data.database.PersiaAtlasDatabase
import com.persia.test.data.websocket.WebSocketResponse
import com.persia.test.data.websocket.responses.FetchData
import timber.log.Timber
import javax.inject.Inject

@ExperimentalPagingApi
class WebSocketRepository @Inject constructor(
    private val database: PersiaAtlasDatabase,
) {

    private val _robotIsOn = MutableLiveData(false)
    val robotIsOn get() = _robotIsOn


    private val _robotIsRunning = MutableLiveData(false)
    val robotIsRunning get() = _robotIsRunning


    inline fun <reified T : Any> handleWebSocketResponse(response: WebSocketResponse<T>) {
        when (T::class.java) {
            FetchData::class.java -> {
                Timber.i("it is fetch data")
            }
            else -> {
                Timber.i("it is wrong data")
            }
        }
    }
}