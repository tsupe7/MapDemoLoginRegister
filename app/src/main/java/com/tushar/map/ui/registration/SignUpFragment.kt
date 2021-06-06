package com.tushar.map.ui.registration

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
import com.tushar.map.R
import com.tushar.map.databinding.FragmentSignupBinding
import com.tushar.map.ui.base.BaseFragment
import com.tushar.map.ui.dashboard.DashboardActivity
import com.tushar.map.ui.registration.model.request.RegisterUserRequest
import com.tushar.map.ui.registration.viewmodel.SignUpViewModel
import com.tushar.map.utils.Status
import com.tushar.map.utils.ViewState
import com.tushar.map.utils.extension.clickWithDebounce
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Matcher
import java.util.regex.Pattern


@AndroidEntryPoint
class SignUpFragment : BaseFragment<SignUpViewModel, FragmentSignupBinding>() {

    private var isValidEmail: Boolean = false
    private var isValidFullName: Boolean = false
    private var isValidPassword: Boolean = false
    private var isValidConfirmPassword: Boolean = false
    override val viewModel: SignUpViewModel by viewModels()

    override fun getLayoutBinding()= FragmentSignupBinding.inflate(layoutInflater)

    override fun setupView(view: View) {
        setupListeners()
    }
    private fun setupListeners() {
        fragmentBinding.run {
            etEmail.setOnFocusChangeListener { _, hasFocus -> checkEmailValidation(hasFocus) }
            etFullname.setOnFocusChangeListener { _, hasFocus -> checkFullNameValidation(hasFocus) }
            etPassword.setOnFocusChangeListener { _, hasFocus -> checkPasswordValidation(hasFocus) }
            etConfirmPassword.setOnFocusChangeListener { _, hasFocus -> checkConfirmPasswordValidation(hasFocus) }
            etEmail.addTextChangedListener(textWatcher)
            etFullname.addTextChangedListener(textWatcher)
            etPassword.addTextChangedListener(textWatcher)
            etConfirmPassword.addTextChangedListener(textWatcher)
            btSignUp.clickWithDebounce({ registerUser() })
            etConfirmPassword.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
                // Identifier of the action. This will be either the identifier you supplied,
                // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event.action == KeyEvent.ACTION_DOWN
                    && event.keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    etConfirmPassword.clearFocus()
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

    private fun registerUser() {
        fragmentBinding.etPassword.clearFocus()
        viewModel.registerUserState.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                Status.LOADING -> showProgressbar()
                Status.SUCCESS -> showSuccess()
                Status.ERROR  -> showErrorText(it.message!!)
                Status.NO_INTERNET -> showErrorText(getString(R.string.network_connection_error))
            }
        })

        val request =
            RegisterUserRequest(
                fragmentBinding.etFullname.text.toString(),
                fragmentBinding.etEmail.text.toString(),
                fragmentBinding.etPassword.text.toString()
            )
        viewModel.registerUser(request)
    }

    private fun showSuccess() {
        startActivity(Intent(requireContext(), DashboardActivity::class.java))
    }

    private fun FragmentSignupBinding.checkPasswordValidation(hasFocus: Boolean) {
        if (!hasFocus) {
            isValidPassword = (etPassword.text.toString().length<=10 && isValidPassword(etPassword.text.toString()))
            if (!isValidPassword) {
                fragmentBinding.tvPasswordError.text = getString(R.string.password_field_invalid)
            } else {
                fragmentBinding.tvPasswordError.text = null
            }
        }
    }

    private fun FragmentSignupBinding.checkConfirmPasswordValidation(hasFocus: Boolean) {
        if (!hasFocus) {
            isValidConfirmPassword = (etConfirmPassword.text.toString().length<=10 && isValidPassword(etConfirmPassword.text.toString())) && etConfirmPassword.text.toString().equals(etPassword.text.toString())

            if(!etConfirmPassword.text.toString().isNullOrEmpty() && !etConfirmPassword.text.toString().equals(etPassword.text.toString())){
                fragmentBinding.tvConfirmPasswordError.text = getString(R.string.confirm_password_same_as_password)
            }
            else if (!isValidConfirmPassword) {
                fragmentBinding.tvConfirmPasswordError.text = getString(R.string.confirm_password_field_invalid)
            } else {
                fragmentBinding.tvConfirmPasswordError.text = null
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
    private fun FragmentSignupBinding.checkEmailValidation(hasFocus: Boolean) {
        if (!hasFocus) {
            isValidEmail = Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString().orEmpty()).matches()
            if (!isValidEmail) {
                fragmentBinding.tvEmailError.text = getString(R.string.email_field_invalid)
            } else {
                fragmentBinding.tvEmailError.text = null
            }
        }
    }

    private fun FragmentSignupBinding.checkFullNameValidation(hasFocus: Boolean) {
        if (!hasFocus) {
            isValidFullName = !etEmail.text.isNullOrEmpty()
            if (!isValidFullName) {
                fragmentBinding.tvFullnameError.text = getString(R.string.full_name_field_invalid)
            } else {
                fragmentBinding.tvFullnameError.text = null
            }
        }
    }

    private fun enableButton() {
        fragmentBinding.run {
            btSignUp.isEnabled =
                !etEmail.text.isNullOrBlank() && !etPassword.text
                    .isNullOrBlank() && isValidEmail && isValidFullName && isValidPassword && isValidConfirmPassword
        }
    }
}