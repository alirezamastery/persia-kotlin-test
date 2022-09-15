package com.persia.test.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "income_remote_keys")
data class IncomeRemoteKey(
    @PrimaryKey(autoGenerate = false)
    val incomeId: Long,
    val prevPage: Int?,
    val nextPage: Int?
)