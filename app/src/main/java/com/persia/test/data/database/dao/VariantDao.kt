package com.persia.test.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.persia.test.data.database.entities.VariantEntity
import com.persia.test.data.domain.models.Variant


@Dao
interface VariantDao {
    // @Query("SELECT * FROM variant")
    // fun getAllVariants(): PagingSource<Int, VariantEntity>
    @Query("SELECT * FROM variant ORDER BY id DESC")
    fun getAllVariants(): PagingSource<Int, Variant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVariant(vararg variants: VariantEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVariantList(variants: List<VariantEntity>)

    @Query("DELETE FROM variant")
    fun deleteAllVariants(): Int
}