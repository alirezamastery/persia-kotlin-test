package com.persia.test.data.network.services.persiaatlas.responses

import com.persia.test.data.database.entities.IncomeEntity
import com.persia.test.domain.models.Income


data class IncomeResponse(
    val id: Long,
    val amount: Int,
    val date: String,
    val description: String,
    val created_at: String,
    val modified_at: String
)

fun IncomeResponse.asDomainModel(): Income {
    return Income(
        id = id,
        createdAt = created_at,
        modifiedAt = modified_at,
        amount = amount,
        date = date,
        description = description
    )
}

fun IncomeResponse.asDatabaseModel(): IncomeEntity {
    return IncomeEntity(
        id = id,
        createdAt = created_at,
        modifiedAt = modified_at,
        amount = amount,
        date = date,
        description = description
    )
}

