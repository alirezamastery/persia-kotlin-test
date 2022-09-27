package com.persia.test.ui.panel.products.variant.detail

sealed class VariantAddEditFormEvent {

    data class ProductSearchChanged(val searchPhrase: String) : VariantAddEditFormEvent()
    data class ProductSelected(val index: Int) : VariantAddEditFormEvent()

    data class ActualProductSearchChanged(val searchPhrase: String) : VariantAddEditFormEvent()
    data class ActualProductSelected(val index: Int) : VariantAddEditFormEvent()

    data class SelectorSearchChanged(val searchPhrase: String) : VariantAddEditFormEvent()
    data class SelectorSelected(val index: Int) : VariantAddEditFormEvent()

    data class DkpcChanged(val dkpc: String) : VariantAddEditFormEvent()

    data class PriceMinChanged(val priceMin: String) : VariantAddEditFormEvent()

    object Submit : VariantAddEditFormEvent()
}