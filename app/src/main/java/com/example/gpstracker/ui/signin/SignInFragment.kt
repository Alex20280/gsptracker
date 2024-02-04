package com.example.gpstracker.ui.signin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gpstracker.R
import com.example.gpstracker.app.App
import com.example.gpstracker.extensions.hideKeyboard
import com.example.gpstracker.extensions.openScreen
import com.example.gpstracker.extensions.viewBinding
import com.example.gpstracker.databinding.FragmentSignInBinding
import com.example.gpstracker.extensions.isEmailAndPasswordValid
import com.example.gpstracker.extensions.onTextChanged
import com.example.gpstracker.network.RequestResult
import com.example.gpstracker.ui.signin.viewmodel.SignInViewModel
import javax.inject.Inject

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val binding by viewBinding(FragmentSignInBinding::bind)

    @Inject
    lateinit var signInViewModel: SignInViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        injectDependencies()
        editTextChangeListener()
        navigateToForgetPasswordScreen()
        initSubmitClickListener()
        initSignInResultListener()
        initSignUpClickListener()
    }

    private fun editTextChangeListener() {
        binding.editTextEmail.onTextChanged {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            binding.button.isEnabled = isEmailAndPasswordValid(email, password)
            if (email.isEmpty() || password.isEmpty()) {
                binding.button.isEnabled = false
            }
            updateButtonState()
        }

        binding.editTextPassword.onTextChanged {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            binding.button.isEnabled = isEmailAndPasswordValid(email, password)
            if (email.isEmpty() || password.isEmpty()) {
                binding.button.isEnabled = false
            }
            updateButtonState()
        }
    }

    private fun updateButtonState() {
        if (binding.button.isEnabled) {
            binding.button.setBackgroundColor(binding.button.context.getColor(R.color.colorAccent))
        } else {
            binding.button.setBackgroundColor(binding.button.context.getColor(R.color.grey))
        }
    }

    private fun injectDependencies() {
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