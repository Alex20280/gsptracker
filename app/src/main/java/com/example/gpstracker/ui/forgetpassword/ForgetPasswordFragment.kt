package com.example.gpstracker.ui.forgetpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.gpstracker.app.App
import com.example.gpstracker.base.BaseFragment
import com.example.gpstracker.base.extentions.openScreen
import com.example.gpstracker.databinding.FragmentForgetPasswordBinding
import com.example.gpstracker.di.ViewModelFactory
import com.example.gpstracker.network.RequestResult
import com.example.gpstracker.ui.forgetpassword.viewmodel.ForgetPasswordViewModel
import javax.inject.Inject

class ForgetPasswordFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!

    @JvmField
    @Inject
    var forgetPasswordViewModel: ForgetPasswordViewModel? = null

    override fun init() {
        (requireContext().applicationContext as App).appComponent.inject(this)
        forgetPasswordViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(ForgetPasswordViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)

        resetButtonClick()
        observePasswordResetResult()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observePasswordResetResult() {
        forgetPasswordViewModel?.getResetPasswordResultLiveData()?.observe(viewLifecycleOwner) { result ->
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
            forgetPasswordViewModel?.resetPassword(binding.resetPasswordEt.text.toString())

        }
    }
}