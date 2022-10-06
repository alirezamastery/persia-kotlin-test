package com.persia.test.ui.panel.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.persia.test.data.network.PersiaAtlasApiClient
import com.persia.test.data.network.services.persiaatlas.responses.UserProfileResponse
import com.persia.test.global.AppPreferences
import com.persia.test.global.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val apiClient: PersiaAtlasApiClient
) : ViewModel() {

    private val _userProfile = MutableLiveData<UserProfileResponse>()
    val userProfile: LiveData<UserProfileResponse>
        get() = _userProfile

    private val _navigateToIncomeList = MutableLiveData<Boolean>(false)
    val navigateToIncomeList: LiveData<Boolean>
        get() = _navigateToIncomeList

    fun displayIncomes() {
        _navigateToIncomeList.value = true
    }

    fun displayIncomesCompleted() {
        _navigateToIncomeList.value = false
    }

    fun refreshUserProfile() {
        Timber.i("get profile".repeat(100))
        viewModelScope.launch {
            val response = apiClient.getUserProfile()
            if (response.isSuccessful) {
                val data = response.body
                data.avatar = Constants.SERVER_BASE_URL + data.avatar
                _userProfile.postValue(data)
                Timber.i("avatar profile: ${_userProfile.value?.avatar}")
            }
        }
    }

}