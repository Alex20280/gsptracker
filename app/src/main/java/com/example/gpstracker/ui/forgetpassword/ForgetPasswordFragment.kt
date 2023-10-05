package com.example.gpstracker.ui.forgetpassword

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
import com.example.gpstracker.databinding.FragmentSignInBinding
import com.example.gpstracker.di.ViewModelFactory
import com.example.gpstracker.ui.forgetpassword.viewmodel.ForgetPasswordViewModel
import com.example.gpstracker.ui.signin.viewmodel.SignInViewModel
import javax.inject.Inject

class ForgetPasswordFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentForgetPasswordBinding

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
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)

        return binding.root
    }
}