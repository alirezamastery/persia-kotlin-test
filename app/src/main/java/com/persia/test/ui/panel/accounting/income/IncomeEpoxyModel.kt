package com.persia.test.ui.panel.accounting.income

import android.os.Build
import androidx.annotation.RequiresApi
import calendar.PersianDate
import com.persia.test.R
import com.persia.test.data.domain.models.Income
import com.persia.test.databinding.ListItemIncomeBinding
import com.persia.test.epoxy.ViewBindingKotlinModel
import java.time.OffsetDateTime


data class IncomeEpoxyModel(
    val income: Income,
    val onIncomeClicked: (Long) -> Unit
) : ViewBindingKotlinModel<ListItemIncomeBinding>(R.layout.list_item_income) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun ListItemIncomeBinding.bind() {
        val localDate = OffsetDateTime.parse(income.createdAt)
        val p = PersianDate(localDate.year,localDate.monthValue,localDate.dayOfMonth)
        val x = p.toStringInPersian()
        incomeTitleTextView.text = x
        incomeAmountTextView.text = income.amount.toString()
        root.setOnClickListener { onIncomeClicked(income.id) }
    }
}
