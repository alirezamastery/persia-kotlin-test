package com.persia.test.data.database.entities

import androidx.room.PrimaryKey


data class ActualProductEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,

    val title: String,
    val priceStep: Long,

    val brand: BrandEntity
)