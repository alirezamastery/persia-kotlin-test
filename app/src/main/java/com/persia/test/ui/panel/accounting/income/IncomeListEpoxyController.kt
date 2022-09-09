package com.persia.test.ui.panel.accounting.income

import com.airbnb.epoxy.TypedEpoxyController
import com.persia.test.data.domain.models.Income
import timber.log.Timber

class IncomeListEpoxyController : TypedEpoxyController<List<Income>>() {

    override fun buildModels(data: List<Income>?) {
        if (data == null || data.isEmpty()) {
            return
        }
        data.forEach { income ->
            IncomeEpoxyModel(income, ::incomeClickListener).id(income.id).addTo(this)
        }
    }

    // we can set click listener on the IncomeEpoxyModel
    private fun incomeClickListener(id: Long) {
        Timber.i("clicked on income: $id")
    }
}