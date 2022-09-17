package com.persia.test.ui.panel.accounting.income

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.persia.test.data.repository.IncomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@ExperimentalPagingApi
@HiltViewModel
class IncomeListViewModel @Inject constructor(
    val repository: IncomeRepository,
) : ViewModel() {

    val incomeFlow = repository.getAllIncomes()

    // fun fetchIncomes() {
    //     viewModelScope.launch {
    //         repository.refreshIncomes()
    //     }
    // }
}