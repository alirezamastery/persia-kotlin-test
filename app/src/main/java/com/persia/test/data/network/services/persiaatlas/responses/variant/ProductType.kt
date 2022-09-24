package com.persia.test.data.network.services.persiaatlas.responses.variant

import com.squareup.moshi.Json

data class ProductType(
    val id: Long,
    val title: String,
    @Json(name = "selector_type")
    val selectorTypeId: Long,
)