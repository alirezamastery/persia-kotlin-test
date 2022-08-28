package com.persia.test.ui.panel.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    private val _navigateToIncomeList = MutableLiveData<Boolean>(false)
    val navigateToIncomeList: LiveData<Boolean>
        get() = _navigateToIncomeList

    fun displayIncomes() {
        _navigateToIncomeList.value = true
    }

    fun displayIncomesCompleted() {
        _navigateToIncomeList.value = false
    }

}