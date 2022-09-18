package com.persia.test.data.network.services.persiaatlas.responses.variant

import com.persia.test.data.database.entities.ActualProductEntity
import com.persia.test.data.database.entities.ProductEntity
import com.persia.test.data.database.entities.VariantEntity
import com.persia.test.data.database.entities.VariantSelectorEntity
import com.persia.test.data.domain.models.Variant


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
            // product = product.id,
            product = Variant.Product(
                id = product.id,
                dkp = product.dkp,
                title = product.title,
                isActive = product.is_active,
                type = product.type.id
            ),
            selector = selector.id,
            actualProduct = actual_product?.id
        )
    }

    fun asDatabaseModel(): VariantEntity {
        var actualProduct: ActualProductEntity? = null
        if (actual_product != null) {
            actualProduct = ActualProductEntity(
                id = actual_product.id,
                title = actual_product.title,
                priceStep = actual_product.price_step,
                brandId = actual_product.brand.id
            )
        }
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
            actualProduct = actualProduct
        )
    }
}