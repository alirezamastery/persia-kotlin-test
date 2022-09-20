package com.persia.test.domain.models

data class Income(
    val id: Long,
    val amount: Int,
    val date: String,
    val description: String,
    val createdAt: String,
    val modifiedAt: String,
)