package com.persia.test.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ProductWithVariants(
    @Embedded val product: ProductEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "productId"
    )
    val variants: List<VariantEntity>
)
