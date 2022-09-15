package com.persia.test.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.persia.test.data.database.entities.VariantPagingRemoteKeyEntity


@Dao
interface VariantPagingRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<VariantPagingRemoteKeyEntity>)

    @Query("SELECT * FROM variant_paging_remote_keys WHERE variantId = :variantId")
    suspend fun getRemoteKeysByVariantId(variantId: Long): VariantPagingRemoteKeyEntity?

    @Query("DELETE FROM variant_paging_remote_keys")
    suspend fun clearRemoteKeys()
}