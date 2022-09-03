package com.persia.test.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.persia.test.data.database.entities.IncomeEntity

@Dao
interface PersiaAtlasDao {

    @Query("select * from income")
    fun getIncomes(): LiveData<List<IncomeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIncomes(vararg incomes: IncomeEntity)
}