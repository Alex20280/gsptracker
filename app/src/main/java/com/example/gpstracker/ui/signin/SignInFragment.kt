package com.example.gpstracker.ui.signin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gpstracker.R
import com.example.gpstracker.app.App
import com.example.gpstracker.base.BaseFragment
import com.example.gpstracker.base.extentions.openScreen
import com.example.gpstracker.databinding.FragmentSignInBinding
import com.example.gpstracker.databinding.FragmentTrackBinding
import com.example.gpstracker.di.ViewModelFactory
import com.example.gpstracker.ui.signin.viewmodel.SignInViewModel
import com.example.gpstracker.ui.signup.viewmodel.SignUpViewModel
import javax.inject.Inject

class SignInFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentSignInBinding

    @JvmField
    @Inject
    var signInViewModel: SignInViewModel? = null

    override fun init() {
        (requireContext().applicationContext as App).appComponent.inject(this)
        signInViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(SignInViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        navigateToForgetPasswordScreen()
        navigateToTrackPage()
        navigateToSignUpPage()

        return binding.root
    }

    private fun navigateToSignUpPage() {
        binding.textViewSignUp.setOnClickListener{
            openScreen(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }
    }

    private fun navigateToTrackPage() {
        binding.button.setOnClickListener {
            openScreen(SignInFragmentDirections.actionSignInFragmentToTrackFragment())
        }
    }

    private fun navigateToForgetPasswordScreen() {
        binding.textViewForgotPassword.setOnClickListener {
            openScreen(SignInFragmentDirections.actionSignInFragmentToForgetPasswordFragment())
        }
    }


}