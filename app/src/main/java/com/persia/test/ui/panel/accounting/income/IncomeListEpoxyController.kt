package com.persia.test.ui.panel.accounting.income

import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.TypedEpoxyController
import com.persia.test.R
import com.persia.test.data.domain.models.Income
import com.persia.test.databinding.ListItemIncomeBinding
import com.persia.test.epoxy.ViewBindingKotlinModel
import timber.log.Timber

class IncomeListEpoxyController : TypedEpoxyController<List<Income>>() {

    override fun buildModels(data: List<Income>?) {
        if (data == null || data.isEmpty()) {
            return
        }
        data.forEach { income ->
            IncomeEpoxyModel(income).id(income.id).addTo(this)
        }
    }

    data class IncomeEpoxyModel(
        val income: Income
    ) : ViewBindingKotlinModel<ListItemIncomeBinding>(R.layout.list_item_income) {

        override fun ListItemIncomeBinding.bind() {
            incomeTitleTextView.text = income.createdAt
        }
    }
}