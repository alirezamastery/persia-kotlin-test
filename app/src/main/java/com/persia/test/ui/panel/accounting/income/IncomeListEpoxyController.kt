package com.persia.test.ui.panel.accounting.income

import android.os.Build
import androidx.annotation.RequiresApi
import calendar.PersianDate
import com.airbnb.epoxy.TypedEpoxyController
import com.persia.test.R
import com.persia.test.data.domain.models.Income
import com.persia.test.databinding.IncomeListHeaderBinding
import com.persia.test.databinding.ListItemIncomeBinding
import com.persia.test.epoxy.ViewBindingKotlinModel
import timber.log.Timber
import java.time.OffsetDateTime

class IncomeListEpoxyController(
    private val incomeClickListener: (Long) -> Unit
) : TypedEpoxyController<List<Income>>() {

    override fun buildModels(data: List<Income>?) {
        if (data == null || data.isEmpty()) {
            return
        }
        IncomeHeaderEpoxyModel().id("header").addTo(this)
        data.forEach { income ->
            IncomeEpoxyModel(
                income = income,
                onIncomeClicked = incomeClickListener
            ).id(income.id).addTo(this)
        }
    }

    // // we can set click listener on the IncomeEpoxyModel
    // private fun incomeClickListener(id: Long) {
    //     Timber.i("clicked on income: $id")
    // }

    // --------------------- Models ---------------------
    class IncomeHeaderEpoxyModel :
        ViewBindingKotlinModel<IncomeListHeaderBinding>(R.layout.income_list_header) {

        override fun IncomeListHeaderBinding.bind() {
            // can do what we want here
        }
    }

    data class IncomeEpoxyModel(
        val income: Income,
        val onIncomeClicked: (Long) -> Unit
    ) : ViewBindingKotlinModel<ListItemIncomeBinding>(R.layout.list_item_income) {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun ListItemIncomeBinding.bind() {
            val localDate = OffsetDateTime.parse(income.createdAt)
            val persianDate =
                PersianDate(localDate.year, localDate.monthValue, localDate.dayOfMonth)
            incomeTitleTextView.text = persianDate.toStringInPersian()
            incomeAmountTextView.text = "%,d".format(income.amount)
            root.setOnClickListener { onIncomeClicked(income.id) }
        }
    }
}