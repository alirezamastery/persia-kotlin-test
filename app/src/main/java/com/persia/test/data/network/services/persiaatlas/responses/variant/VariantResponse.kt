package com.persia.test.data.network.services.persiaatlas.responses.variant

import com.persia.test.data.database.entities.VariantEntity
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
    val actual_product: ActualProduct,
) {

    fun asDomainModel(): Variant {
        return Variant(
            id = id,
            dkpc = dkpc,
            priceMin = price_min,
            stopLoss = stop_loss,
            isActive = is_active,
            hasCompetition = has_competition,
            product = product.id,
            selector = selector.id,
            actualProduct = actual_product.id
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
            product = product.id,
            selector = selector.id,
            actualProduct = actual_product.id
        )
    }
}