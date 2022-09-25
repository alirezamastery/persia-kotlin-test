package com.persia.test.domain.models

data class Product(
    val id: Long,
    val dkp: String,
    val title: String,
    val is_active: Boolean,
    val type: ProductType
)
