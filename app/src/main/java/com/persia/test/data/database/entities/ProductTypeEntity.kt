package com.persia.test.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "product_type",
    foreignKeys = [ForeignKey(
        entity = VariantSelectorTypeEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("selectorTypeId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class ProductTypeEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,

    val title: String,
    val selectorTypeId: Long
)
