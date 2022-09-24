package com.persia.test.data.network.services.persiaatlas.responses.variant

import com.persia.test.data.database.entities.*
import com.persia.test.domain.models.Variant


data class VariantResponse(
    val id: Long,
    val dkpc: Long,
    val price_min: Long,
    val stop_loss: Long,
    val is_active: Boolean,
    val has_competition: Boolean,
    val product: Product,
    val selector: Selector,
    val actual_product: ActualProduct?,
) {

    fun asDomainModel(): Variant {
        return Variant(
            id = id,
            dkpc = dkpc,
            priceMin = price_min,
            stopLoss = stop_loss,
            isActive = is_active,
            hasCompetition = has_competition,
            product = Variant.Product(
                id = product.id,
                dkp = product.dkp,
                title = product.title,
                isActive = product.is_active,
                type = Variant.ProductType(
                    id = product.type.id,
                    title = product.type.title,
                    selectorTypeId = product.type.selectorTypeId
                )
            ),
            selector = Variant.VariantSelector(
                id = selector.id,
                digikalaId = selector.digikala_id,
                value = selector.value,
                extraInfo = selector.extra_info,
                type = Variant.VariantSelectorType(
                    id = selector.selector_type.id,
                    title = selector.selector_type.title
                )
            ),
            actualProduct = actual_product?.let { ap ->
                Variant.ActualProduct(
                    id = ap.id,
                    title = ap.title,
                    priceStep = ap.price_step,
                    brand = Variant.Brand(
                        id = ap.brand.id,
                        title = ap.brand.title
                    )
                )
            }
        )
    }

    fun asDatabaseModel(): VariantEntity {
        return VariantEntity(
            id = id,
            dkpc = dkpc,
            priceMin = price_min,
            stopLoss = stop_loss,
            isActive = is_active,
            hasCompetition = has_competition,
            // product = product.id,
            productEntity = ProductEntity(
                id = product.id,
                dkp = product.dkp,
                title = product.title,
                isActive = product.is_active,
                typeId = product.type.id
            ),
            selector = VariantSelectorEntity(
                id = selector.id,
                digikalaId = selector.digikala_id,
                value = selector.value,
                extraInfo = selector.extra_info,
                selectorTypeId = selector.selector_type.id
            ),
            actualProduct = actual_product?.let { ap ->
                ActualProductEntity(
                    id = ap.id,
                    title = ap.title,
                    priceStep = ap.price_step,
                    brand = BrandEntity(
                        id = ap.brand.id,
                        title = ap.brand.title
                    )
                )
            }
        )
    }
}