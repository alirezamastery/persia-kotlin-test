package com.persia.test.modules

import com.persia.test.data.store.RobotState
import com.persia.test.data.store.Store
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RobotStoreModule {

    @Singleton
    @Provides
    fun provideRobotStore(): Store<RobotState> {
        return Store(RobotState())
    }
}