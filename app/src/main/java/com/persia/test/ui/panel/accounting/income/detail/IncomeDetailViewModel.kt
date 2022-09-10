package com.persia.test.ui.panel.accounting.income.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.persia.test.data.domain.models.Income
import com.persia.test.data.network.PersiaAtlasApiClient
import com.persia.test.data.network.services.persiaatlas.responses.asDomainModel
import com.persia.test.data.repository.IncomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class IncomeDetailViewModel @Inject constructor(
    private val api: PersiaAtlasApiClient
) : ViewModel() {

    private val _incomeDetail = MutableLiveData<Income?>()
    val incomeDetail: LiveData<Income?>
        get() = _incomeDetail


    fun getIncomeDetail(incomeId: Long) {
        viewModelScope.launch {
            Timber.i("$incomeId")
            val res = api.getIncomeById(incomeId)
            _incomeDetail.postValue(res.body.asDomainModel())
        }
    }
}