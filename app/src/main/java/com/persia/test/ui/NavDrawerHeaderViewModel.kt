package com.persia.test.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.persia.test.data.network.PersiaAtlasApiClient
import com.persia.test.data.network.services.persiaatlas.responses.UserProfileResponse
import com.persia.test.data.network.websocket.WebSocketManager
import com.persia.test.global.AppPreferences
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
        requestUserProfile()
        Timber.i("WS: ${WebSocketManager.isConnected()}")
        WebSocketManager.connect()
    }

    private fun requestUserProfile() {
        viewModelScope.launch {
            val response = apiClient.getUserProfile()
            Timber.i("profile res: $response")
            if (response.isSuccessful) {
                val data = response.body
                Timber.i("profile: $data")
                _userProfile.postValue(data)
                _fullName.postValue("${data.first_name} ${data.last_name}")
                _username.postValue(AppPreferences.username)
            }
        }
    }
}