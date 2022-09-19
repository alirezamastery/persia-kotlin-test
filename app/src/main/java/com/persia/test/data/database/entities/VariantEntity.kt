package com.persia.test.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "variant",

    )
data class VariantEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,

    val dkpc: Long,
    val priceMin: Long,
    val stopLoss: Long,
    val isActive: Boolean,
    val hasCompetition: Boolean,

    // when dealing with nested objects you can either use Embedded which
    // flattens the object and adds its fields as columns, or use TypeConverter.
    // when using TypeConverter, you should also create converters for the nested
    // object inside the object
    @Embedded(prefix = "product_")
    val productEntity: ProductEntity,
    @Embedded(prefix = "selector_")
    val selector: VariantSelectorEntity,
    val actualProduct: ActualProductEntity?,
)
// {
//
//     fun asDomainModel(): Variant {
//         return Variant(
//             id = id,
//             dkpc = dkpc,
//             priceMin = priceMin,
//             stopLoss = stopLoss,
//             isActive = isActive,
//             hasCompetition = hasCompetition,
//             product = product,
//             selector = selector,
//             actualProduct = actualProduct
//         )
//     }
// }
//
// fun List<VariantEntity>.asDomainModel(): List<Variant> {
//     return map {
//         Variant(
//             id = it.id,
//             dkpc = it.dkpc,
//             priceMin = it.priceMin,
//             stopLoss = it.stopLoss,
//             isActive = it.isActive,
//             hasCompetition = it.hasCompetition,
//             product = it.product,
//             selector = it.selector,
//             actualProduct = it.actualProduct
//         )
//     }
// }
