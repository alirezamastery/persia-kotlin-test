package com.persia.test.data.network.services.persiaatlas.responses.product

data class ProductResponse(
    val id: Long,
    val dkp: String,
    val title: String,
    val is_active: Boolean,
    val type: Type
)