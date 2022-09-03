package com.persia.test.ui.panel.accounting.income

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.persia.test.data.repository.IncomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class IncomeListViewModel @Inject constructor(
    val repository: IncomeRepository
) : ViewModel() {


    fun fetchIncomes() {
        viewModelScope.launch {
            repository.refreshIncomes()
        }
    }
}