package com.persia.test.ui.panel.products.variant.list

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import com.persia.test.R
import com.persia.test.data.domain.models.Variant
import com.persia.test.databinding.ListItemVariantBinding
import com.persia.test.epoxy.ViewBindingKotlinModel

class VariantListEpoxyController(
    private val clickListener: (Long) -> Unit
) : PagingDataEpoxyController<Variant>() {

    override fun buildItemModel(currentPosition: Int, item: Variant?): EpoxyModel<*> {
        return VariantListItemEpoxyModel(
            variant = item,
            onClick = { variantId -> clickListener(variantId) }
        ).id("variant_${item?.id}")
    }

    data class VariantListItemEpoxyModel(
        val variant: Variant?,
        val onClick: (Long) -> Unit
    ) : ViewBindingKotlinModel<ListItemVariantBinding>(R.layout.list_item_variant) {

        override fun ListItemVariantBinding.bind() {
            variant?.let {
                variantDKPCTextView.text = variant.id.toString()
                root.setOnClickListener { onClick(variant.id) }
            }
        }
    }
}