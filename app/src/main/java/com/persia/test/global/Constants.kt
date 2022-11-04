package com.persia.test.global

class Constants {
    companion object {

        const val SHARED_PREF_USER_DATA = "user_data"
        const val SERVER_BASE_URL = "https://persia-atlas.com"
        // const val SERVER_BASE_URL = "http://192.168.1.122:8000"
        const val WEBSOCKET_URL = "wss://persia-atlas.com/ws/"
        const val DATABASE_NAME = "persia_atlas_database"
        const val PAGE_SIZE = 40
        const val PREFETCH_DISTANCE = PAGE_SIZE * 2

        const val FIREBASE_BASE_URL = "https://fcm.googleapis.com"
        const val FIREBASE_SERVER_kEY = "AAAAHelpreQ:APA91bF-KoaB3ruXgUbLXYSrN_FpD1siIwnOv7nfktxXlJY4RU91ZUt9BDVeBAxKi6eIsc2RWM9MNf9K6z9X11fgmkwt6ZKMMsCIdJKhVwMOrQxipoj86Pg6n7TODuljopu3lYgWATBM "
        const val FIREBASE_CONTENT_TYPE = "application/json"
    }

    enum class RequestState { LOADING, ERROR, DONE }
}