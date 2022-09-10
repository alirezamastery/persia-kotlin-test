package com.persia.test.ui.panel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.persia.test.R
import com.persia.test.databinding.ActivityPanelBinding
import com.persia.test.databinding.NavPanelViewHeaderBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PanelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPanelBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_panel)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        setupNavDrawer()
    }

    private fun setupNavDrawer() {
        drawerLayout = binding.panelDrawerLayout
        val navController = this.findNavController(R.id.panelNavHostFragment)
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
        val navViewHeaderBinding: NavPanelViewHeaderBinding = NavPanelViewHeaderBinding.bind(headerView)
        // val drawerViewModel = ViewModelProvider(this).get(NavPanelDra::class.java)
        // navViewHeaderBinding.navHeaderViewModel = drawerViewModel
        // navViewHeaderBinding.lifecycleOwner = this

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.panelNavHostFragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }
}