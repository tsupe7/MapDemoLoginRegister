package com.tushar.map.ui.login

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tushar.map.R
import com.tushar.map.databinding.FragmentLoginBinding
import com.tushar.map.ui.base.BaseFragment
import com.tushar.map.ui.dashboard.DashboardActivity
import com.tushar.map.ui.login.model.request.LoginUserRequest
import com.tushar.map.ui.login.viewmodel.LoginViewModel
import com.tushar.map.utils.Result
import com.tushar.map.utils.Status
import com.tushar.map.utils.ViewState
import com.tushar.map.utils.extension.clickWithDebounce
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Matcher
import java.util.regex.Pattern


@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>(){
    private var isValidEmail: Boolean = false
    private var isValidPassword: Boolean = false
    override val viewModel: LoginViewModel by viewModels()

    override fun getLayoutBinding()= FragmentLoginBinding.inflate(layoutInflater)

    override fun setupView(view: View) {
        setupListeners()
    }

    private fun setupListeners() {
        fragmentBinding.run {
            etEmail.setOnFocusChangeListener { _, hasFocus -> checkEmailValidation(hasFocus) }
            etPassword.setOnFocusChangeListener { _, hasFocus -> checkPasswordValidation(hasFocus) }
            etEmail.addTextChangedListener(textWatcher)
            etPassword.addTextChangedListener(textWatcher)
            btLogin.clickWithDebounce({ loginUser() })
            tvSignUp.clickWithDebounce({
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
                )
            })

            etPassword.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
                // Identifier of the action. This will be either the identifier you supplied,
                // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event.action == KeyEvent.ACTION_DOWN
                    && event.keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    etPassword.clearFocus()
                    enableButton()
                }
                false
            })
        }
    }

    val textWatcher = object : TextWatcher{
        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            enableButton()
        }
    }

    private fun loginUser() {
        fragmentBinding.etPassword.clearFocus()
        viewModel.loginUserState.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                Status.LOADING -> showProgressbar()
                Status.SUCCESS -> showSuccess()
                Status.ERROR -> showErrorText(it.message!!)
                Status.NO_INTERNET -> showErrorText(getString(R.string.network_connection_error))
            }
        })

        val request =
            LoginUserRequest(
                fragmentBinding.etEmail.text.toString(),
                fragmentBinding.etPassword.text.toString()
            )
        viewModel.loginUser(request)
    }

    private fun showSuccess() {
        hideProgressbarOrError()
        startActivity(Intent(requireContext(), DashboardActivity::class.java))
    }

    private fun FragmentLoginBinding.checkPasswordValidation(hasFocus: Boolean) {
        if (!hasFocus) {
            isValidPassword = (etPassword.text.toString().length<=10 && isValidPassword(etPassword.text.toString()))
            if (!isValidPassword) {
                fragmentBinding.tvPasswordError.text = getString(R.string.password_field_invalid)
            } else {
                fragmentBinding.tvPasswordError.text = null
            }
        }
    }

    private fun isValidPassword(password: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }
    private fun FragmentLoginBinding.checkEmailValidation(hasFocus: Boolean) {
        if (!hasFocus) {
            isValidEmail = Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString().orEmpty()).matches()
            if (!isValidEmail) {
                fragmentBinding.tvEmailError.text = getString(R.string.email_field_invalid)
            } else {
                fragmentBinding.tvEmailError.text = null
            }
        }
    }

    private fun enableButton() {
        fragmentBinding.run {
            btLogin.isEnabled =
                !etEmail.text.isNullOrBlank() && !etPassword.text
                    .isNullOrBlank() && isValidEmail && isValidPassword
        }
    }
}