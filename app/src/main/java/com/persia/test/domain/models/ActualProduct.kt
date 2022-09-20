package com.persia.test.domain.models

data class ActualProduct(
    val id: Long,
    val title: String,
    val priceStep: Long,
    val brand: Brand
)
