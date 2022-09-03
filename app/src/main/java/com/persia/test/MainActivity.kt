package com.persia.test

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.persia.test.global.AppPreferences
import com.persia.test.ui.auth.AuthActivity
import com.persia.test.ui.panel.PanelActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppPreferences.setup(applicationContext)
        AppPreferences.accessToken = "ttt"
        Timber.i("auth stat: ${AppPreferences.isLoggedIn}")

        setContentView(R.layout.activity_main)

        if (AppPreferences.isLoggedIn == true)
            goToActivity(this, PanelActivity::class.java)
        else
            goToActivity(this, AuthActivity::class.java)
    }

    private fun goToActivity(activity: Activity, clazz: Class<*>) {
        val intent = Intent(activity, clazz)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}