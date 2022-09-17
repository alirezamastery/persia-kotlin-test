package com.persia.test.data.domain.models

data class Variant(
    val id: Long,
    val dkpc: Long,
    val priceMin: Long,
    val stopLoss: Long,
    val isActive: Boolean,
    val hasCompetition: Boolean,
    // val product: Product,
    // val selector: VariantSelector,
    // val actualProduct: ActualProduct
    val product: Long,
    val selector: Long,
    val actualProduct: Long?
)
