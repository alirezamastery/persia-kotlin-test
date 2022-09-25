package com.persia.test.ui.panel.products.variant.detail

sealed class VariantAddEditFormEvent {

    data class ProductSearchChanged(val searchPhrase: String) : VariantAddEditFormEvent()
    data class ProductSelected(val index: Int) : VariantAddEditFormEvent()

    data class DkpcChanged(val dkpc: Long) : VariantAddEditFormEvent()
    data class PriceMinChanged(val priceMin: Long) : VariantAddEditFormEvent()

    object Submit : VariantAddEditFormEvent()
}