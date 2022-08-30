package com.persia.test.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.persia.test.data.database.entities.IncomeEntity

@Dao
interface AccountingDao {

    @Query("select * from income")
    fun getIncomes(): LiveData<List<IncomeEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIncomes(vararg incomes: IncomeEntity)
}

@Database(entities = [IncomeEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AccountingDatabase : RoomDatabase() {

    abstract val accountingDao: AccountingDao

    companion object {

        @Volatile
        private var INSTANCE: AccountingDatabase? = null

        fun getInstance(context: Context): AccountingDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AccountingDatabase::class.java,
                        "sleep_history_database"
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