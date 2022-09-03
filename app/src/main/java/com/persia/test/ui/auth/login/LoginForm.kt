package com.persia.test.ui.auth.login

enum class LoginRequestStatus { LOADING, ERROR, DONE }

data class LoginFormData(
    val mobile: String,
    val password: String
)

data class LoginFormState(
    val mobileError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)