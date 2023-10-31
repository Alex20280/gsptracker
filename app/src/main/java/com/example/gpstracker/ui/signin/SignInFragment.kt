package com.example.gpstracker.ui.signin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gpstracker.R
import com.example.gpstracker.app.App
import com.example.gpstracker.base.BaseFragment
import com.example.gpstracker.base.extentions.openScreen
import com.example.gpstracker.databinding.FragmentSignInBinding
import com.example.gpstracker.databinding.FragmentTrackBinding
import com.example.gpstracker.di.ViewModelFactory
import com.example.gpstracker.network.RequestResult
import com.example.gpstracker.ui.signin.viewmodel.SignInViewModel
import com.example.gpstracker.ui.signup.SignUpFragmentDirections
import com.example.gpstracker.ui.signup.viewmodel.SignUpViewModel
import javax.inject.Inject

class SignInFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    @JvmField
    @Inject
    var signInViewModel: SignInViewModel? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)

        viewModelInstanciation()

        navigateToForgetPasswordScreen()
        observeSubmitClick()
        observeLogin()
        navigateToSignUpPage()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun viewModelInstanciation() {
        (requireContext().applicationContext as App).appComponent.inject(this)
        signInViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(SignInViewModel::class.java)

    }

    private fun observeSubmitClick() {
        binding.button.setOnClickListener {
            hideKeyboard()
            val email: String = binding.editTextEmail.text?.toString() ?: ""
            val password: String = binding.editTextPassword.text?.toString() ?: ""

            if (signInViewModel!!.isValidEmail(email) && signInViewModel!!.isValidPassword(password)) {
                signInViewModel?.loginUser(email, password)
            } else {
                // Show error messages or UI feedback for invalid input
                if (signInViewModel!!.isValidEmail(email)) {
                    binding.editTextEmail.error = getString(R.string.invalid_email_warning)
                }
                if (!signInViewModel!!.isValidPassword(password)) {
                    binding.editTextPassword.error = getString(R.string.invalid_password_warning)
                }
            }

        }
    }

    private fun observeLogin() {
        signInViewModel?.getSignInResultLiveData()?.observe(viewLifecycleOwner) { result ->
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