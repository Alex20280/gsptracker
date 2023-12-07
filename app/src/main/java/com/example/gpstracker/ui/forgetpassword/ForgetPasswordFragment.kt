package com.example.gpstracker.ui.forgetpassword

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gpstracker.R
import com.example.gpstracker.app.App
import com.example.gpstracker.base.extentions.checkFieldsForButtonColor
import com.example.gpstracker.base.extentions.openScreen
import com.example.gpstracker.base.extentions.viewBinding
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
        editTextChangeListener()
        resetButtonClick()
        observePasswordResetResult()
    }

    private fun editTextChangeListener() {
        binding.resetPasswordEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                checkFieldsForButtonColor(binding.resetPasswordEt, binding.resetPasswordEt, binding.submitBtn)
            }
        })

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