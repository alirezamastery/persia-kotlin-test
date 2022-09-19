package com.persia.test.ui.panel.products.variant.list

import android.content.Context
import android.graphics.Color
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import com.persia.test.R
import com.persia.test.data.database.entities.VariantEntity
import com.persia.test.databinding.ListItemVariantBinding
import com.persia.test.epoxy.ViewBindingKotlinModel
import kotlin.coroutines.coroutineContext

class VariantListEpoxyController(
    private val context: Context,
    private val clickListener: (Long) -> Unit
) : PagingDataEpoxyController<VariantEntity>() {

    override fun buildItemModel(currentPosition: Int, item: VariantEntity?): EpoxyModel<*> {
        return VariantListItemEpoxyModel(
            context = context,
            variant = item,
            onClick = { variantId -> clickListener(variantId) }
        ).id("variant_${item?.id}")
    }

    data class VariantListItemEpoxyModel(
        val context: Context,
        val variant: VariantEntity?,
        val onClick: (Long) -> Unit
    ) : ViewBindingKotlinModel<ListItemVariantBinding>(R.layout.list_item_variant) {

        override fun ListItemVariantBinding.bind() {
            variant?.let {
                variantProductTextView.text = variant.productEntity.title
                variantDKPCTextView.text = variant.dkpc.toString()
                variantPriceMinTextView.text = "%,d".format(variant.priceMin)
                variantSelectorTextView.text = variant.selector.value
                if (variant.selector.selectorTypeId == 1L) {
                    val drawable =
                        AppCompatResources.getDrawable(context, R.drawable.ic_baseline_circle_24)
                    val unwrappedDrawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(
                        unwrappedDrawable,
                        Color.parseColor(variant.selector.extraInfo)
                    )
                    variantSelectorTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        unwrappedDrawable,
                        null,
                        null,
                        null
                    )
                }
                if (variant.hasCompetition) {
                    variantHasCompetitionTextView.setText(R.string.yes)
                } else {
                    variantHasCompetitionTextView.setText(R.string.no)
                }
                if (variant.isActive) {
                    variantIsActiveImageView.setImageResource(R.drawable.ic_baseline_check_circle_24)
                } else {
                    variantIsActiveImageView.setImageResource(R.drawable.ic_baseline_cancel_24)
                }
                root.setOnClickListener { onClick(variant.id) }
            }
        }
    }
}