package com.persia.test.data.domain.models

data class Product(
    val id: Long,
    val dkp: Long,
    val title: String,
    val isActive: Boolean,
    val type: ProductType
)
