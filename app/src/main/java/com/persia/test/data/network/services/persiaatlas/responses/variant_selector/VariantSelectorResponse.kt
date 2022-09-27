package com.persia.test.data.network.services.persiaatlas.responses.variant_selector

import com.persia.test.data.database.entities.VariantSelectorEntity
import com.persia.test.domain.models.VariantSelector
import com.persia.test.domain.models.VariantSelectorType

data class VariantSelectorResponse(
    val id: Long,
    val digikala_id: Long,
    val extra_info: String?,
    val value: String,
    val selector_type: SelectorType,
) {

    fun asDomainModel(): VariantSelector {
        return VariantSelector(
            id = id,
            digikalaId = digikala_id,
            extraInfo = extra_info,
            value = value,
            type = VariantSelectorType(
                id = selector_type.id,
                title = selector_type.title
            )
        )
    }

    fun asDatabaseModel(): VariantSelectorEntity {
        return VariantSelectorEntity(
            id = id,
            digikalaId = digikala_id,
            extraInfo = extra_info,
            value = value,
            selectorTypeId = selector_type.id
        )
    }
}

// fun List<VariantSelectorResponse>.asDomainModel():List<VariantSelector>{
//
// }