package com.example.gpstracker.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gpstracker.R
import com.example.gpstracker.app.App
import com.example.gpstracker.base.BaseFragment
import com.example.gpstracker.databinding.FragmentForgetPasswordBinding
import com.example.gpstracker.databinding.FragmentSignUpBinding
import com.example.gpstracker.di.ViewModelFactory
import com.example.gpstracker.ui.forgetpassword.viewmodel.ForgetPasswordViewModel
import com.example.gpstracker.ui.signup.viewmodel.SignUpViewModel
import javax.inject.Inject

class SignUpFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentSignUpBinding

    @JvmField
    @Inject
    var signUpViewModel: SignUpViewModel? = null

    override fun init() {
        (requireContext().applicationContext as App).appComponent.inject(this)
        signUpViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(SignUpViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        return binding.root
    }


}