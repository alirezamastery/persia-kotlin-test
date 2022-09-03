package com.persia.test.modules

import android.app.Application
import android.content.Context
import com.persia.test.data.database.PersiaAtlasDao
import com.persia.test.data.database.PersiaAtlasDatabase
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
}

