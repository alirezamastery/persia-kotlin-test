package com.persia.test.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "variant_selector",
    foreignKeys = [ForeignKey(
        entity = VariantSelectorTypeEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("selectorTypeId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class VariantSelectorEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,

    val digikalaId: Int,
    val value: String,
    val extraInfo: String,
    val selectorTypeId: Long
)