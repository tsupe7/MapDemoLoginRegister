package com.tushar.map.ui.dashboard.profile

import android.view.View
import androidx.fragment.app.activityViewModels
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
            tvUserName.text = viewModel.userInfo.value?.data?.displayName
            tvUserEmail.text = viewModel.userInfo.value?.data?.email
            btLogout.setOnClickListener { viewModel.logout() }
        }
    }
}