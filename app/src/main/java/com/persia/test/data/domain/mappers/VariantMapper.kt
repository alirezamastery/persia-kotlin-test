package com.persia.test.data.domain.mappers

import com.persia.test.data.domain.models.Variant
import com.persia.test.data.network.services.persiaatlas.responses.variant.VariantResponse

object VariantMapper {

    fun buildFrom(response: VariantResponse): Variant {
        return Variant(
            id = response.id,
            dkpc = response.dkpc,
            priceMin = response.price_min,
            stopLoss = response.stop_loss,
            isActive = response.is_active,
            hasCompetition = response.has_competition,
            // product=response.product.id,
            product = Variant.Product(
                id = response.product.id,
                dkp = response.product.dkp,
                title = response.product.title,
                isActive = response.product.is_active,
                type = response.product.type.id
            ),
            selector = response.selector.id,
            actualProduct = response.actual_product?.id,
        )
    }
}