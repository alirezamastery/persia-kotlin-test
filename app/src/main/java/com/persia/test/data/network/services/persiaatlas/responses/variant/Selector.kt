package com.persia.test.data.network.services.persiaatlas.responses.variant

data class Selector(
    val id: Long,
    val digikala_id: Long,
    val value: String,
    val extra_info: String,
    val selector_type: SelectorType,
)