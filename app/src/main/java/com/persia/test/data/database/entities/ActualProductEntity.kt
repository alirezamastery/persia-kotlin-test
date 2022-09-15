package com.persia.test.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "actual_product",
    foreignKeys = [ForeignKey(
        entity = BrandEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("brandId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class ActualProductEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,

    val title: String,
    val priceStep: Long,

    val brandId: Long
)