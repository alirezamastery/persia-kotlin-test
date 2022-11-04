package com.persia.test.global

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import timber.log.Timber

object AppPreferences {

    private var sharedPreferences: SharedPreferences? = null

    // TODO step 1: call `AppPreferences.setup(applicationContext)` in your MainActivity's `onCreate` method
    fun setup(context: Context) {
        // TODO step 2: set your app name here
        sharedPreferences = context.getSharedPreferences("persiaatlas.sharedprefs", MODE_PRIVATE)
    }

    // // TODO step 4: replace these example attributes with your stored values
    var accessToken: String?
        get() = Key.ACCESS_TOKEN.getString()
        set(value) = Key.ACCESS_TOKEN.setString(value)

    var refreshToken: String?
        get() = Key.REFRESH_TOKEN.getString()
        set(value) = Key.REFRESH_TOKEN.setString(value)

    var testToken: Boolean?
        get() = Key.TEST_TOKEN.getBoolean()
        set(value) = Key.TEST_TOKEN.setBoolean(value)

    var isLoggedIn: Boolean?
        get() = Key.IS_LOGGED_IN.getBoolean()
        set(value) = Key.IS_LOGGED_IN.setBoolean(value)

    var username: String?
        get() = Key.USERNAME.getString()
        set(value) = Key.USERNAME.setString(value)

    var firebaseToken: String?
        get() = Key.FIREBASE_TOKEN.getString()
        set(value) = Key.FIREBASE_TOKEN.setString(value)

    private enum class Key {
        // TODO step 3: replace these cases with your stored values keys
        TEST_TOKEN,
        ACCESS_TOKEN,
        REFRESH_TOKEN,
        IS_LOGGED_IN,
        USERNAME,
        FIREBASE_TOKEN;

        fun getBoolean(): Boolean? =
            if (sharedPreferences!!.contains(name)) sharedPreferences!!.getBoolean(
                name,
                false
            ) else null

        fun getFloat(): Float? =
            if (sharedPreferences!!.contains(name)) sharedPreferences!!.getFloat(name, 0f) else null

        fun getInt(): Int? =
            if (sharedPreferences!!.contains(name)) sharedPreferences!!.getInt(name, 0) else null

        fun getLong(): Long? =
            if (sharedPreferences!!.contains(name)) sharedPreferences!!.getLong(name, 0) else null

        fun getString(): String? =
            if (sharedPreferences!!.contains(name)) sharedPreferences!!.getString(
                name,
                ""
            ) else null

        fun setBoolean(value: Boolean?) =
            value?.let { sharedPreferences!!.edit { putBoolean(name, value) } } ?: remove()

        fun setFloat(value: Float?) =
            value?.let { sharedPreferences!!.edit { putFloat(name, value) } } ?: remove()

        fun setInt(value: Int?) =
            value?.let { sharedPreferences!!.edit { putInt(name, value) } } ?: remove()

        fun setLong(value: Long?) =
            value?.let { sharedPreferences!!.edit { putLong(name, value) } } ?: remove()

        fun setString(value: String?) =
            value?.let { sharedPreferences!!.edit { putString(name, value) } } ?: remove()

        fun exists(): Boolean = sharedPreferences!!.contains(name)
        fun remove() = sharedPreferences!!.edit { remove(name) }
    }
}