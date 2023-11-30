package com.example.gpstracker.ui.signin

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.gpstracker.R
import com.example.gpstracker.app.App
import com.example.gpstracker.base.extentions.openScreen
import com.example.gpstracker.databinding.FragmentSignInBinding
import com.example.gpstracker.network.RequestResult
import com.example.gpstracker.ui.signin.viewmodel.SignInViewModel
import javax.inject.Inject

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val binding by viewBinding(FragmentSignInBinding::bind)

    @Inject
    lateinit var signInViewModel: SignInViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelInstantiation()
        navigateToForgetPasswordScreen()
        observeSubmitClick()
        observeLogin()
        navigateToSignUpPage()
    }

    private fun viewModelInstantiation() {
        (requireContext().applicationContext as App).appComponent.inject(this)
    }

    private fun observeSubmitClick() {
        binding.button.setOnClickListener {
            hideKeyboard()
            val email: String = binding.editTextEmail.text?.toString() ?: ""
            val password: String = binding.editTextPassword.text?.toString() ?: ""

            if (signInViewModel.isValidEmail(email) && signInViewModel.isValidPassword(password)) {
                signInViewModel.loginUser(email, password)
            } else {
                // Show error messages or UI feedback for invalid input
                if (signInViewModel.isValidEmail(email)) {
                    binding.editTextEmail.error = getString(R.string.invalid_email_warning)
                }
                if (!signInViewModel.isValidPassword(password)) {
                    binding.editTextPassword.error = getString(R.string.invalid_password_warning)
                }
            }

        }
    }

    private fun observeLogin() {
        signInViewModel.getSignInResultLiveData().observe(viewLifecycleOwner) { result ->
            when (result) {
                is RequestResult.Success -> {
                    navigateToTrackPage()
                }

                is RequestResult.Error -> {
                    Toast.makeText(context, "Login Error", Toast.LENGTH_LONG).show()
                }

                is RequestResult.Loading -> Unit
            }
        }

    }

    private fun navigateToSignUpPage() {
        binding.textViewSignUp.setOnClickListener {
            openScreen(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }
    }

    private fun navigateToTrackPage() {
        openScreen(SignInFragmentDirections.actionSignInFragmentToTrackFragment())
    }

    private fun navigateToForgetPasswordScreen() {
        binding.textViewForgotPassword.setOnClickListener {
            openScreen(SignInFragmentDirections.actionSignInFragmentToForgetPasswordFragment())
        }
    }

    fun Fragment.hideKeyboard() {
        val activity = requireActivity()
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity.currentFocus
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}