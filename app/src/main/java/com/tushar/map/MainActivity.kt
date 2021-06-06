package com.tushar.map

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.tushar.map.databinding.ActivityDashboardBinding
import com.tushar.map.databinding.ActivityMainBinding
import com.tushar.map.ui.base.BaseActivity
import com.tushar.map.ui.dashboard.DashboardActivity
import com.tushar.map.ui.dashboard.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel>() {

    private lateinit var navController: NavController
    override val viewModel: MainViewModel by viewModels()
    private lateinit var activityMainBinding: ActivityMainBinding

    override fun setupView(savedInstanceState: Bundle?) {
        setupNavigationComponent()
        checkLogin()
    }

    override fun getLayoutBinding(): ViewBinding =
        ActivityMainBinding.inflate(layoutInflater).apply {
            activityMainBinding = this
        }

    private fun setupNavigationComponent() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.launcherNavHostContainer) as NavHostFragment
        navController = navHostFragment.findNavController()
    }

    // Support back arrow and navigation
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun checkLogin() {
        
        with(viewModel) {
            isLoggedInState.observe(this@MainActivity, Observer { loggedIn ->
                if (loggedIn) {
                    startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
                }
            })
            isUserLoggedIn()
        }
    }
}