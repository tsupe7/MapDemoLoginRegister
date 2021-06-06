package com.tushar.map.ui.base

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.tushar.map.databinding.BaseLayoutBinding
import com.tushar.map.utils.ViewUtil.hideView
import com.tushar.map.utils.ViewUtil.showView

/**
 * Base Activity for all activity.
 *
 * Handles common operations.
 * Observes message live data from base view model to display toast messages.
 *
 */
abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity() {

    abstract val viewModel: T

    lateinit var baseLayoutBinding: BaseLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseLayoutBinding = BaseLayoutBinding.inflate(layoutInflater)
        setContentView(baseLayoutBinding.root)
        baseLayoutBinding.mainViewContainer.addView(getLayoutBinding().root)
        initObserver()
        setupView(savedInstanceState)
    }

    /**
     * Observes the message changes from base view model.
     */
    private fun initObserver() {
        viewModel.messageString.observe(this, Observer {
            it.data?.run { showMessage(this) }
        })

        viewModel.messageStringId.observe(this, Observer {
            it.data?.run { showMessage(this) }
        })
    }

    /**
     * Shows the fill screen progress.
     */
    protected fun showProgressbar() {
        hideView(baseLayoutBinding.mainViewContainer, baseLayoutBinding.progressText)
        showView(baseLayoutBinding.progressbar)
    }

    /**
     * Hides the progress.
     */
    protected fun hideProgressbar() {
        showView(baseLayoutBinding.mainViewContainer)
        hideView(baseLayoutBinding.progressText, baseLayoutBinding.progressbar)
    }

    /**
     * Shows the fill screen progress.
     *
     * @param text - display over progress
     */
    protected fun showProgressbarWithText(text: String) {
        baseLayoutBinding.progressText.text = text
        showView(baseLayoutBinding.progressText, baseLayoutBinding.progressbar)
        hideView(baseLayoutBinding.mainViewContainer)
    }

    protected fun showNoInternetError() {
        showView(baseLayoutBinding.errorText)
        hideView(
            baseLayoutBinding.mainViewContainer,
            baseLayoutBinding.progressText,
            baseLayoutBinding.progressbar
        )
    }

    fun hideKeyboard() {
        val inputMethodManager: InputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    fun showMessage(message: String) =
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))

    protected abstract fun setupView(savedInstanceState: Bundle?)

    protected abstract fun getLayoutBinding(): ViewBinding

}