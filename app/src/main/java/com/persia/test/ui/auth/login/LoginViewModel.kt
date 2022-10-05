package com.persia.test.ui.auth.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.persia.test.data.network.services.persiaatlas.responses.LoginResponse
import com.persia.test.R
import com.persia.test.data.network.NetworkLayer
import com.persia.test.data.network.services.persiaatlas.responses.LoginRequest
import com.persia.test.global.AppPreferences
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginViewModel : ViewModel() {

    private val _loginFormState = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState>
        get() = _loginFormState

    private val _loginFormData = MutableLiveData<LoginFormData>()

    private val _loginRequestStatus = MutableLiveData<LoginRequestStatus>()
    val loginRequestStatus: LiveData<LoginRequestStatus?>
        get() = _loginRequestStatus

    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?>
        get() = _loginResponse

    fun loginDataChanged(mobileNumber: String, password: String) {
        if (!isMobileValid(mobileNumber)) {
            _loginFormState.value = LoginFormState(mobileError = R.string.invalid_mobile)
        } else if (!isPasswordValid(password)) {
            _loginFormState.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginFormState.value = LoginFormState(isDataValid = true)
            _loginFormData.value = LoginFormData(
                mobile = mobileNumber,
                password = password
            )
        }
    }

    private fun isMobileValid(mobileNumber: String): Boolean {
        return Patterns.PHONE.matcher(mobileNumber).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    fun login() {
        Timber.i("login start")
        val payload = LoginRequest(
            mobile = _loginFormData.value!!.mobile,
            password = _loginFormData.value!!.password,
        )
        Timber.i("payload: $payload")

        _loginRequestStatus.value = LoginRequestStatus.LOADING

        viewModelScope.launch {
            try {
                Timber.i("trying")
                _loginRequestStatus.value = LoginRequestStatus.DONE
                val response = NetworkLayer.persiaAtlasService.login(payload)
                Timber.i("response: $response | ${response.isSuccessful} | ${response.code()}")
                if (response.isSuccessful) {
                    val data = response.body()!!
                    _loginResponse.value = data
                    AppPreferences.accessToken = data.access
                    AppPreferences.refreshToken = data.refresh
                    AppPreferences.isLoggedIn = true
                    AppPreferences.username = payload.mobile
                } else
                    Timber.e("response bad | ${response.code()}")
            } catch (e: Exception) {
                if (e is CancellationException)
                    throw e
                Timber.i("response: $e")
                _loginRequestStatus.value = LoginRequestStatus.ERROR
                _loginResponse.value = null
            }
        }
    }
}