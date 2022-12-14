package com.persia.test.modules

import android.app.Application
import com.persia.test.data.store.RobotState
import com.persia.test.data.store.Store
import com.persia.test.data.websocket.WebSocketManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {

    @Singleton
    @Provides
    fun provideWebSocketInstance(robotStore: Store<RobotState>): WebSocketManager {
        return WebSocketManager(robotStore)
    }
}
