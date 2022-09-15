package com.persia.test.modules

import android.app.Application
import com.persia.test.data.database.PersiaAtlasDao
import com.persia.test.data.database.PersiaAtlasDatabase
import com.persia.test.data.database.dao.IncomeRemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePersiaAtlasDatabase(context: Application): PersiaAtlasDatabase {
        return PersiaAtlasDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun providePersiaAtlasDao(persiaAtlasDatabase: PersiaAtlasDatabase): PersiaAtlasDao {
        return persiaAtlasDatabase.providePersiaAtlasDao()
    }

    @Singleton
    @Provides
    fun provideIncomeRemoteKeysDao(persiaAtlasDatabase: PersiaAtlasDatabase): IncomeRemoteKeysDao {
        return persiaAtlasDatabase.provideIncomeRemoteKeysDao()
    }
}

