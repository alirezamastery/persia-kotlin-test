package com.persia.test.data.database

import android.content.Context
import androidx.room.*
import com.persia.test.Constants.Companion.DATABASE_NAME
import com.persia.test.data.database.dao.IncomeRemoteKeysDao
import com.persia.test.data.database.dao.VariantDao
import com.persia.test.data.database.dao.VariantPagingRemoteKeyDao
import com.persia.test.data.database.entities.IncomeEntity
import com.persia.test.data.database.entities.IncomeRemoteKey
import com.persia.test.data.database.entities.VariantEntity
import com.persia.test.data.database.entities.VariantPagingRemoteKeyEntity


@Database(
    entities = [
        IncomeEntity::class,
        IncomeRemoteKey::class,
        VariantEntity::class,
        VariantPagingRemoteKeyEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class PersiaAtlasDatabase : RoomDatabase() {

    // abstract val persiaAtlasDao: PersiaAtlasDao

    abstract fun providePersiaAtlasDao(): PersiaAtlasDao
    abstract fun provideIncomeRemoteKeysDao(): IncomeRemoteKeysDao
    abstract fun variantDao(): VariantDao
    abstract fun variantRemoteKeysDao(): VariantPagingRemoteKeyDao

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