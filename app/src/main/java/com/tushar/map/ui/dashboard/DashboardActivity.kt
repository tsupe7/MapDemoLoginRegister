package com.tushar.map.ui.dashboard

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.viewbinding.ViewBinding
import com.tushar.map.R
import com.tushar.map.databinding.ActivityDashboardBinding
import com.tushar.map.ui.base.BaseActivity
import com.tushar.map.ui.dashboard.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity: BaseActivity<DashboardViewModel>() {

    private lateinit var navController: NavController
    private lateinit var activityDashboardBinding: ActivityDashboardBinding
    override val viewModel: DashboardViewModel by viewModels()

    override fun setupView(savedInstanceState: Bundle?) {
        setupNavigationComponent()
        activityDashboardBinding.run {
            NavigationUI.setupWithNavController(bottomNavMenu, navController)
        }
    }

    override fun getLayoutBinding(): ViewBinding =
        ActivityDashboardBinding.inflate(layoutInflater).apply {
            activityDashboardBinding = this
        }

    private fun setupNavigationComponent() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.dashboardNavHostContainer) as NavHostFragment
        navController = navHostFragment.findNavController()
    }

    // Support back arrow and navigation
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}