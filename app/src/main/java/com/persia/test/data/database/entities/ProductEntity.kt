package com.persia.test.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "product",
    foreignKeys = [ForeignKey(
        entity = ProductTypeEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("typeId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,

    val dkp: String,
    val title: String,
    val isActive: Boolean,

    val typeId: Long
)
