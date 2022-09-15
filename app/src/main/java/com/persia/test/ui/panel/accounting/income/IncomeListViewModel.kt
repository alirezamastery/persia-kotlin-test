package com.persia.test.ui.panel.accounting.income

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.persia.test.Constants
import com.persia.test.data.network.PersiaAtlasApiClient
import com.persia.test.data.repository.IncomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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