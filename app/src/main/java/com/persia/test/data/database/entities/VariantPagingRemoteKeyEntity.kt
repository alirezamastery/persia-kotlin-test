package com.persia.test.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "variant_paging_remote_keys")
data class VariantPagingRemoteKeyEntity(
    @PrimaryKey(autoGenerate = false)
    val variantId: Long,

    val prevPage: Int?,
    val nextPage: Int?
)