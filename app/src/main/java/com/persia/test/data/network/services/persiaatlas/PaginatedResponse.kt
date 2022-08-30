package com.persia.test.data.network.services.persiaatlas

data class PaginatedResponse<T>(
    val next: String?,
    val previous: String?,
    val count: Int,
    val page_count: Int,
    val extra_data: Any?,
    val items: List<T>,
)

