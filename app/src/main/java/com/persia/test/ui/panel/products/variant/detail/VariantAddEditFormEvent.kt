package com.persia.test.ui.panel.products.variant.detail

sealed class VariantAddEditFormEvent {

    data class ProductChanged(val searchPhrase: String) : VariantAddEditFormEvent()
    data class DkpcChanged(val dkpc: Long) : VariantAddEditFormEvent()
    data class PriceMinChanged(val priceMin: Long) : VariantAddEditFormEvent()

    object Submit : VariantAddEditFormEvent()
}