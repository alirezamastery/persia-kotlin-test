package com.persia.test.data.database

import android.content.Context
import androidx.room.*
import com.persia.test.Constants.Companion.DATABASE_NAME
import com.persia.test.data.database.dao.IncomeRemoteKeysDao
import com.persia.test.data.database.entities.IncomeEntity
import com.persia.test.data.database.entities.IncomeRemoteKey


@Database(entities = [IncomeEntity::class, IncomeRemoteKey::class], version = 1)
@TypeConverters(Converters::class)
abstract class PersiaAtlasDatabase : RoomDatabase() {

    // abstract val persiaAtlasDao: PersiaAtlasDao

    abstract fun providePersiaAtlasDao(): PersiaAtlasDao
    abstract fun provideIncomeRemoteKeysDao(): IncomeRemoteKeysDao

    companion object {

        @Volatile
        private var INSTANCE: PersiaAtlasDatabase? = null

        fun getInstance(context: Context): PersiaAtlasDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PersiaAtlasDatabase::class.java,
                        DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                // Return instance; smart cast to be non-null.
                return instance
            }
        }
    }
}