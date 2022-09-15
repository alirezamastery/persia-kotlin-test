package com.persia.test.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.persia.test.data.database.entities.IncomeEntity
import com.persia.test.data.domain.models.Income

@Dao
interface PersiaAtlasDao {

    @Query("select * from income")
    fun getIncomes(): PagingSource<Int, Income>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIncome(vararg incomes: IncomeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIncomeList(incomes: List<IncomeEntity>)

    @Query("delete from income")
    fun deleteAllIncomes(): Int
}