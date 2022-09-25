package com.persia.test.data.network.services.persiaatlas.responses.product

import com.persia.test.domain.models.Product
import com.persia.test.domain.models.ProductType

data class ProductResponse(
    val id: Long,
    val dkp: String,
    val title: String,
    val is_active: Boolean,
    val type: Type
) {
    fun asDomainModel():Product{
        return Product(
            id=id,
            dkp=dkp,
            title=title,
            is_active = is_active,
            type = ProductType(
                id = type.id,
                title=type.title,
                selectorTypeId = type.selector_type
            )
        )
    }
}