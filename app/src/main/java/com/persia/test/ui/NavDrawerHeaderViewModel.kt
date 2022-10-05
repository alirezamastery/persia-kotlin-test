package com.persia.test.ui

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
class NavDrawerHeaderViewModel @Inject constructor(
    private val apiClient: PersiaAtlasApiClient
) : ViewModel() {

    private val _userProfile = MutableLiveData<UserProfileResponse>()
    val userProfile: LiveData<UserProfileResponse>
        get() = _userProfile

    private val _fullName = MutableLiveData("")
    val fullName get() = _fullName

    private val _username = MutableLiveData("")
    val username get() = _username

    init {
        getUserProfile()
    }

    private fun getUserProfile() {
        viewModelScope.launch {
            val response = apiClient.getUserProfile()
            if (response.isSuccessful) {
                val data = response.body
                _userProfile.value = data
                _userProfile.value!!.avatar = Constants.SERVER_BASE_URL + data.avatar
                _fullName.value = "${data.first_name} ${data.last_name}"
                _username.value = AppPreferences.username
            }
        }
    }
}