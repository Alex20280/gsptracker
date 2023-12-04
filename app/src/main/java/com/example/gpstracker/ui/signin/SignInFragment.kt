package com.example.gpstracker.ui.signin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gpstracker.R
import com.example.gpstracker.app.App
import com.example.gpstracker.base.extentions.hideKeyboard
import com.example.gpstracker.base.extentions.openScreen
import com.example.gpstracker.base.extentions.viewBinding
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
        initSubmitClickListener()
        initSignInResultListener()
        initSignUpClickListener()
    }

    private fun viewModelInstantiation() {
        (requireContext().applicationContext as App).appComponent.inject(this)
    }

    private fun initSubmitClickListener() {
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

    private fun initSignInResultListener() {
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

    private fun initSignUpClickListener() {
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

}