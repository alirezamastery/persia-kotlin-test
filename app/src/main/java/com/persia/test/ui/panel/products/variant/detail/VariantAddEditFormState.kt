package com.persia.test.ui.panel.products.variant.detail


data class VariantAddEditFormState(
    val productId: Long? = null,
    val productIdError: String? = null,

    val actualProductId: Long? = null,
    val actualProductIdError: String? = null,

    val variantSelectorId: Long? = null,
    val variantSelectorIdError: String? = null,

    val dkpc: String = "",
    val dkpcError: String? = null,

    val priceMin: String = "",
    val priceMinError: String? = null,
)