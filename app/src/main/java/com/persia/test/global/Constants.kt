package com.persia.test

class Constants {
    companion object {

        const val SHARED_PREF_USER_DATA = "user_data"
        const val SERVER_BASE_URL = "https://persia-atlas.com"
        const val DATABASE_NAME = "persia_atlas_database"
        const val PAGE_SIZE = 40
        const val PREFETCH_DISTANCE = PAGE_SIZE * 2
    }

    enum class RequestState { LOADING, ERROR, DONE }
}