package com.persia.test.domain.models

data class Variant(
    val id: Long,
    val dkpc: Long,
    val priceMin: Long,
    val stopLoss: Long,
    val isActive: Boolean,
    val hasCompetition: Boolean,
    val product: Product,
    // val selector: VariantSelector,
    // val actualProduct: ActualProduct
    // val product: Long,
    val selector: Long,
    val actualProduct: Long?,
) {

    data class Product(
        val id: Long,
        val dkp: String,
        val title: String,
        val isActive: Boolean,
        // val type: ProductType
        val type: Long
    )

    data class ProductType(
        val id: Long,
        val title: String,
        val selectorTypeId: Long
    )

    data class VariantSelector(
        val id: Long,
        val digikalaId: Long,
        val value: String,
        val extraInfo: String,
        val type: VariantSelectorType
    )

    data class VariantSelectorType(
        val id: Long,
        val title: String
    )
}