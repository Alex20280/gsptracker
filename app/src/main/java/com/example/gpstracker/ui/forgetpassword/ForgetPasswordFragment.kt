package com.example.gpstracker.ui.forgetpassword

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gpstracker.R
import com.example.gpstracker.app.App
import com.example.gpstracker.extensions.openScreen
import com.example.gpstracker.extensions.viewBinding
import com.example.gpstracker.databinding.FragmentForgetPasswordBinding
import com.example.gpstracker.extensions.isEmailAndPasswordValid
import com.example.gpstracker.extensions.onTextChanged
import com.example.gpstracker.network.RequestResult
import com.example.gpstracker.ui.forgetpassword.viewmodel.ForgetPasswordViewModel
import javax.inject.Inject

class ForgetPasswordFragment : Fragment(R.layout.fragment_forget_password) {

    private val binding by viewBinding(FragmentForgetPasswordBinding::bind)

    @Inject
    lateinit var forgetPasswordViewModel: ForgetPasswordViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        injectDependencies()
        editTextChangeListener()
        resetButtonClick()
        observePasswordResetResult()
    }

    private fun editTextChangeListener() {
        binding.resetPasswordEt.onTextChanged {
            val email= binding.resetPasswordEt.text.toString().trim()
            val password = binding.resetPasswordEt.text.toString().trim()
            if (isEmailAndPasswordValid(email, password)){
                binding.submitBtn.setBackgroundColor(binding.submitBtn.context.getColor(R.color.colorAccent))
                binding.submitBtn.isEnabled = true
            }
        }
    }

    private fun injectDependencies() {
        (requireContext().applicationContext as App).appComponent.inject(this)
    }

    private fun observePasswordResetResult() {
        forgetPasswordViewModel.getResetPasswordResultLiveData().observe(viewLifecycleOwner) { result ->
            when (result) {
                is RequestResult.Success -> {
                    navigateToSignInPage()
                }

                is RequestResult.Error -> {
                    Toast.makeText(context, "Login Error", Toast.LENGTH_LONG).show()
                }

                is RequestResult.Loading -> Unit
            }
        }
    }

    private fun navigateToSignInPage() {
        openScreen(ForgetPasswordFragmentDirections.actionForgetPasswordFragmentToSignInFragment())
    }

    private fun resetButtonClick() {
        binding.submitBtn.setOnClickListener {
            forgetPasswordViewModel.resetPassword(binding.resetPasswordEt.text.toString())

        }
    }
}