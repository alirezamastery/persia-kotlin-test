package com.persia.test.data.store

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class Store<T>(initialState: T) {

    private val _stateFlow = MutableStateFlow(initialState)
    val stateFlow get() = _stateFlow

    private val mutex = Mutex()

    suspend fun update(updateBlock: (T) -> T) = mutex.withLock {
        val newStore = updateBlock(_stateFlow.value)
        _stateFlow.value = newStore
    }

    suspend fun read(readBlock: (T) -> Unit) = mutex.withLock {
        readBlock(_stateFlow.value)
    }
}