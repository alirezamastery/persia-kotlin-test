package com.persia.test.ui.panel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.firebase.messaging.FirebaseMessaging
import com.persia.test.R
import com.persia.test.data.firebase.NotificationData
import com.persia.test.data.firebase.PushNotification
import com.persia.test.data.network.services.google.FirebaseService
import com.persia.test.data.network.services.google.NotificationApiClient
import com.persia.test.databinding.ActivityPanelBinding
import com.persia.test.databinding.NavPanelViewHeaderBinding
import com.persia.test.ui.NavDrawerHeaderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

const val TOPIC = "myTopic"

@AndroidEntryPoint
class PanelActivity : AppCompatActivity() {

    @Inject
    lateinit var notificationApiClient: NotificationApiClient

    private lateinit var binding: ActivityPanelBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val drawerViewModel: NavDrawerHeaderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_panel)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        setupNavDrawer()


        val firebaseMessaging =  FirebaseMessaging.getInstance()
        Timber.i("**************************** fire instanceId: $firebaseMessaging")
        firebaseMessaging.token.addOnSuccessListener {
            Timber.i("**************************** firebase token: ${it}")
            FirebaseService.token = it
        }
        firebaseMessaging.token.addOnFailureListener {
            Timber.e("**************************** firebase token failed: $it")
        }

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        sendNotification()
    }

    private fun setupNavDrawer() {
        drawerLayout = binding.panelDrawerLayout
        val navController = this.findNavController(R.id.panelNavHostFragment)
        setSupportActionBar(binding.panelActionBar)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        // prevent nav gesture if not on start destination
        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, bundle: Bundle? ->
            if (nd.id == nc.graph.startDestinationId) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }

        NavigationUI.setupWithNavController(binding.navigationView, navController)

        val headerView = binding.navigationView.getHeaderView(0)
        val navViewHeaderBinding: NavPanelViewHeaderBinding =
            NavPanelViewHeaderBinding.bind(headerView)
        // val drawerViewModel = ViewModelProvider(this).get(NavPanelDra::class.java)
        navViewHeaderBinding.navHeaderViewModel = drawerViewModel
        navViewHeaderBinding.lifecycleOwner = this

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.panelNavHostFragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    private fun sendNotification() {
        val notification = PushNotification(
            data = NotificationData("test notif", "chaikin"),
            to = TOPIC
        )
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = notificationApiClient.postNotification(notification)
                if (response.isSuccessful) {
                    Timber.i("firebase response: $response")
                } else {
                    Timber.e("firebase error: $response")
                }
            } catch (e: Exception) {
                Timber.e("firebase request error: $e")
            }
        }
    }
}