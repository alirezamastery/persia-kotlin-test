package com.persia.test

class Constants {
    companion object {

        const val SHARED_PREF_USER_DATA = "user_data"
        const val SERVER_BASE_URL = "https://persia-atlas.com"
    }

    enum class RequestState { LOADING, ERROR, DONE }
}