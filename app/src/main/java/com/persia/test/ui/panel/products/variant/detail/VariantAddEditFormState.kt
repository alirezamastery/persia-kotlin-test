package com.persia.test.ui.panel.products.variant.detail

import com.persia.test.domain.models.Variant

data class VariantAddEditFormState(
    val product: Variant.Product? = null,
    val dkpc: Long = 0L,
    val priceMin: Long = 0L
)