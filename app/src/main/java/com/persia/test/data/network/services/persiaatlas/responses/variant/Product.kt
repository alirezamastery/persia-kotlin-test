package com.persia.test.data.network.services.persiaatlas.responses.variant

data class Product(
    val id: Long,
    val dkp: String,
    val is_active: Boolean,
    val title: String,
    val type: ProductType
)