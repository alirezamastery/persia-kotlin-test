package com.persia.test.domain.models

data class VariantSelector(
    val id: Long,
    val digikalaId: Long,
    val value: String,
    val extraInfo: String?,
    val type: VariantSelectorType
)