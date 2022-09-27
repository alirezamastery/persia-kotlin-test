package com.persia.test.data.network.services.persiaatlas.responses.actual_product

import com.persia.test.data.database.entities.ActualProductEntity
import com.persia.test.data.database.entities.BrandEntity
import com.persia.test.data.network.services.persiaatlas.responses.variant.VariantResponse
import com.persia.test.domain.models.ActualProduct
import com.persia.test.domain.models.Variant
import com.persia.test.domain.models.Brand as DomainBrand

data class ActualProductResponse(
    val id: Long,
    val title: String,
    val price_step: Long,
    val brand: Brand,
    val variants: List<VariantResponse>,
) {

    fun asDomainModel(): ActualProduct {
        return ActualProduct(
            id = id,
            title = title,
            priceStep = price_step,
            brand = DomainBrand(
                id = brand.id,
                title = brand.title
            )
        )
    }

    fun asDatabaseModel(): ActualProductEntity {
        return ActualProductEntity(
            id = id,
            title = title,
            priceStep = price_step,
            brand = BrandEntity(
                id = brand.id,
                title = brand.title
            )
        )
    }

    fun getVariantsAsDomainModel(): List<Variant> {
        return variants.map { variantResponse ->
            variantResponse.asDomainModel()
        }
    }
}