package com.example.gpstracker.ui.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gpstracker.R
import com.example.gpstracker.app.App
import com.example.gpstracker.base.extentions.hideKeyboard
import com.example.gpstracker.base.extentions.openScreen
import com.example.gpstracker.base.extentions.viewBinding
import com.example.gpstracker.databinding.FragmentSignUpBinding
import com.example.gpstracker.network.RequestResult
import com.example.gpstracker.ui.signup.viewmodel.SignUpViewModel
import javax.inject.Inject

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val binding by viewBinding(FragmentSignUpBinding::bind)

    @Inject
    lateinit var signUpViewModel: SignUpViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelInstantiation()
        registerUser()
        observeRegistration()
    }

    private fun observeRegistration() {
        signUpViewModel.getSignUpResultLiveData().observe(viewLifecycleOwner) { result ->
            when(result){
                is RequestResult.Success -> {
                    openScreen(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
                }
                is RequestResult.Error -> {
                    Toast.makeText(context, "", Toast.LENGTH_LONG).show()
                }
                is RequestResult.Loading -> Unit
            }
        }
    }

    private fun viewModelInstantiation() {
        (requireContext().applicationContext as App).appComponent.inject(this)
    }

    private fun registerUser() {
        binding.submitBtn.setOnClickListener {
            hideKeyboard()
            val email: String = binding.emailEt.text?.toString() ?: ""
            val password: String = binding.passwordEt.text?.toString() ?: ""

            if (signUpViewModel.isValidEmail(email) && signUpViewModel.isValidPassword(password)) {
                signUpViewModel.registerUser(email, password)
            } else {
                // Show error messages or UI feedback for invalid input
                if (signUpViewModel.isValidEmail(email)) {
                    binding.emailEt.error = getString(R.string.invalid_email_warning)
                }
                if (!signUpViewModel.isValidPassword(password)) {
                    binding.passwordEt.error = getString(R.string.invalid_password_warning)
                }
            }
        }
    }

}