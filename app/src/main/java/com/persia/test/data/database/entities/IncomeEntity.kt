package com.persia.test.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.persia.test.domain.models.Income


@Entity(tableName = "income")
data class IncomeEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,

    val amount: Int,
    val date: String,
    val description: String,

    val createdAt: String,
    val modifiedAt: String,
)

fun List<IncomeEntity>.asDomainModel(): List<Income> {
    return map {
        Income(
            id = it.id,
            amount = it.amount,
            date = it.date,
            description = it.description,
            createdAt = it.createdAt,
            modifiedAt = it.modifiedAt
        )
    }
}