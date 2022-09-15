package com.persia.test.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "brand")
data class BrandEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,

    val title: String
)