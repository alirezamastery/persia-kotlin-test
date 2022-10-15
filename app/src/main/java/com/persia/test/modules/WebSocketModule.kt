package com.persia.test.modules

import android.app.Application
import com.persia.test.data.database.PersiaAtlasDao
import com.persia.test.data.database.PersiaAtlasDatabase
import com.persia.test.data.database.dao.IncomeRemoteKeysDao
import com.persia.test.data.network.websocket.WebSocketManager
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
    fun provideWebSocketInstance(context: Application) = WebSocketManager
}
