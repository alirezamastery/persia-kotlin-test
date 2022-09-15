package com.persia.test.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.persia.test.data.database.entities.IncomeRemoteKey

@Dao
interface IncomeRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<IncomeRemoteKey>)

    @androidx.room.Query("SELECT * FROM income_remote_keys WHERE incomeId = :incomeId")
    suspend fun getRemoteKeysByIncomeId(incomeId: Long): IncomeRemoteKey?

    @androidx.room.Query("DELETE FROM income_remote_keys")
    suspend fun clearRemoteKeys()
}