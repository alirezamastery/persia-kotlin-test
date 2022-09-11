package com.persia.test.ui.panel.accounting.income

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.persia.test.Constants
import com.persia.test.data.network.PersiaAtlasApiClient
import com.persia.test.data.repository.IncomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class IncomeListViewModel @Inject constructor(
    val repository: IncomeRepository,
    val api: PersiaAtlasApiClient
) : ViewModel() {

    val incomeFlow = Pager(
        PagingConfig(
            pageSize = Constants.PAGE_SIZE,
            prefetchDistance = Constants.PREFETCH_DISTANCE,
            enablePlaceholders = false
        ),
    ) {
        IncomePagingSource(repository, api)
    }.flow.cachedIn(viewModelScope)


    // fun fetchIncomes() {
    //     viewModelScope.launch {
    //         repository.refreshIncomes()
    //     }
    // }
}