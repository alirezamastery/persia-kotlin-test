package com.persia.test.ui.panel.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.persia.test.data.network.PersiaAtlasApiClient
import com.persia.test.data.network.services.persiaatlas.responses.UserProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import timber.log.Timber
import java.io.File
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val apiClient: PersiaAtlasApiClient
) : ViewModel() {

    private val _userProfile = MutableLiveData<UserProfileResponse>()
    val userProfile get() = _userProfile

    private val _apiError = MutableLiveData<String?>(null)
    val apiError get() = _apiError

    private val _isLoading = MutableLiveData(false)
    val isLoading get() = _isLoading

    private var _formState = MutableLiveData(ProfileEditFormState())
    val formState get() = _formState

    private val _navigateToIncomeList = MutableLiveData<Boolean>(false)
    val navigateToIncomeList get() = _navigateToIncomeList

    fun displayIncomes() {
        _navigateToIncomeList.value = true
    }

    fun displayIncomesCompleted() {
        _navigateToIncomeList.value = false
    }

    fun onEvent(event: ProfileEditFormEvent) {
        when (event) {
            is ProfileEditFormEvent.FirstnameChanged -> {
                _formState.postValue(formState.value?.copy(firstname = event.firstname))
            }
            is ProfileEditFormEvent.LastnameChanged -> {
                _formState.postValue(formState.value?.copy(lastname = event.lastname))
            }
            ProfileEditFormEvent.Submit -> {
                submitProfileData()
            }
        }
    }

    fun refreshUserProfile() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val response = apiClient.getUserProfile()
            if (response.isSuccessful) {
                val data = response.body
                Timber.i("avatar in profile: ${data.avatar}")
                _userProfile.postValue(data)
            }
            _isLoading.postValue(false)
        }
    }

    fun uploadAvatarImage(path: String) {
        Timber.i("file path: $path")
        val file = File(path) // get file from uri
        val photoBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val partPhoto = MultipartBody.Part.createFormData("avatar", file.name, photoBody)
        Timber.i("part image: $partPhoto")
        viewModelScope.launch {
            val response = apiClient.updateUserProfileAvatar(partPhoto)
            if (response.isSuccessful) {
                Timber.i("avatar response: ${response.body}")
                _userProfile.postValue(userProfile.value?.copy(avatar = response.body.avatar))
            } else {
                Timber.i("error in avatar: ${response.exception}")
                Timber.i("error in avatar: ${response.status}")
                _apiError.postValue("${response.exception}")
            }
        }
    }

    private fun submitProfileData() {
        // This form requires no validation so we send the form
        // state directly to the server
        val data = HashMap<String, String>()
        data["first_name"] = formState.value!!.firstname
        data["last_name"] = formState.value!!.lastname
        viewModelScope.launch {
            _isLoading.postValue(true)
            // cast as Map important for empty string
            val payloadData = JSONObject(data as Map<String, String>)
            val response = apiClient.updateUserProfileInfo(payloadData)
            if (!response.isSuccessful) {
                _apiError.postValue(response.exception.toString())
            }
            _isLoading.postValue(false)
        }

    }

    fun updateFormState() {
        _formState.postValue(
            formState.value?.copy(
                firstname = userProfile.value!!.first_name,
                lastname = userProfile.value!!.last_name
            )
        )
    }
}