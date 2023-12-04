package com.example.gpstracker.ui.forgetpassword

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.gpstracker.R
import com.example.gpstracker.app.App
import com.example.gpstracker.base.extentions.openScreen
import com.example.gpstracker.databinding.FragmentForgetPasswordBinding
import com.example.gpstracker.network.RequestResult
import com.example.gpstracker.ui.forgetpassword.viewmodel.ForgetPasswordViewModel
import javax.inject.Inject

class ForgetPasswordFragment : Fragment(R.layout.fragment_forget_password) {

    private val binding by viewBinding(FragmentForgetPasswordBinding::bind)

    @Inject
    lateinit var forgetPasswordViewModel: ForgetPasswordViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelInstantiation()
        resetButtonClick()
        observePasswordResetResult()
    }

    private fun viewModelInstantiation() {
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