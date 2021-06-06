package com.tushar.map.ui.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.tushar.map.MainActivity
import com.tushar.map.databinding.BaseLayoutBinding
import com.tushar.map.utils.ViewUtil.hideView
import com.tushar.map.utils.ViewUtil.showView
import com.tushar.map.utils.extension.showLongToast

abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding> : Fragment() {

    abstract val viewModel: VM

    private var _fragmentBinding: VB? = null
    private var _binding: BaseLayoutBinding? = null
    private val binding get() = _binding!!
    protected val fragmentBinding get() = _fragmentBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BaseLayoutBinding.inflate(inflater, container, false)
        _fragmentBinding = getLayoutBinding()
        binding.mainViewContainer.addView(_fragmentBinding?.root)
        return binding.root
    }

    protected fun showProgressbar() {
        hideView(binding.mainViewContainer, binding.progressText, binding.errorText)
        showView(binding.progressbar)
    }

    protected fun hideProgressbarOrError() {
        showView(binding.mainViewContainer)
        hideView(binding.progressText, binding.errorText, binding.progressbar)
    }

    protected fun showProgressbarWithText(text: String) {
        binding.progressText.text = text
        hideView(binding.mainViewContainer)
        showView(binding.progressText, binding.progressbar)
    }

    protected fun showErrorText(text: String) {
//        binding.errorText.text = text
//        showView(binding.errorText)
//        hideView(binding.mainViewContainer, binding.progressText, binding.progressbar)
        hideProgressbarOrError()
        requireContext().showLongToast(text)
    }

    protected open fun setupObservers() {
        viewModel.messageString.observe(this, Observer {
            it.data?.run { showMessage(this) }
        })

        viewModel.messageStringId.observe(this, Observer {
            it.data?.run { showMessage(this) }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        handleLogout()
        setupView(view)
    }

    protected fun handleLogout() {
        viewModel.logOut.observe(viewLifecycleOwner, Observer{
            requireContext().showLongToast("Session End: Please login again to continue!")
            //TODO : Navigate back to splash activity with clear backstack

            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()
        })
    }

    override fun onDestroyView() {
        _fragmentBinding = null
        _binding = null
        super.onDestroyView()
    }

    fun showMessage(message: String) =
        context?.let { Toast.makeText(it, message, Toast.LENGTH_SHORT).show() }

    fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))

    protected abstract fun getLayoutBinding(): VB

    protected abstract fun setupView(view: View)

}