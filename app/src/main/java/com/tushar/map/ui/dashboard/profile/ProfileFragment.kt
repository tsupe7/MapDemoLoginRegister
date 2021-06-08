package com.tushar.map.ui.dashboard.profile

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.tushar.map.databinding.FragmentProfileBinding
import com.tushar.map.ui.base.BaseFragment
import com.tushar.map.ui.dashboard.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment: BaseFragment<DashboardViewModel, FragmentProfileBinding>(){

    override val viewModel: DashboardViewModel by activityViewModels()
    override fun getLayoutBinding()= FragmentProfileBinding.inflate(layoutInflater)

    override fun setupView(view: View) {
        fragmentBinding.run {
            viewModel.retrieveData()
            viewModel.displayName.observe(viewLifecycleOwner, Observer {displayName ->
                tvUserName.text = displayName
            })
            viewModel.emailId.observe(viewLifecycleOwner, Observer {emailId ->
                tvUserEmail.text = emailId
            })
            btLogout.setOnClickListener { viewModel.logout() }
        }
    }
}