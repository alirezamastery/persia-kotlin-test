package com.persia.test.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class VariantSelectorTypeEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,

    val title: String
)