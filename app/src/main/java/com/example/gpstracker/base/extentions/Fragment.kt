package com.example.gpstracker.base.extentions

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.example.gpstracker.R
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun Fragment.hideKeyboard() {
    val activity = requireActivity()
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = activity.currentFocus
    if (view != null) {
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun <T : ViewBinding> Fragment.viewBinding(bind: (View) -> T): ReadOnlyProperty<Fragment, T> =
    object : ReadOnlyProperty<Fragment, T> {
        private var binding: T? = null

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
            binding?.let { return it }

            val view = thisRef.requireView()
            return bind(view).also {
                thisRef.viewLifecycleOwnerLiveData.observe(thisRef) { lifecycleOwner ->
                    lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
                        override fun onStateChanged(
                            source: LifecycleOwner,
                            event: Lifecycle.Event
                        ) {
                            if (event == Lifecycle.Event.ON_DESTROY) {
                                binding = null
                            }
                        }
                    })
                }
                binding = it
            }
        }
    }

fun checkFieldsForButtonColor(
    emailEditText: EditText,
    passwordEditText: EditText,
    button: Button
) {
    val email = emailEditText.text.toString().trim()
    val password = passwordEditText.text.toString().trim()

    val isEmailValid =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.endsWith(".com")
    val isPasswordValid = password.isNotEmpty()

    val isDataValid = isEmailValid && isPasswordValid

    if (isDataValid) {
        button.setBackgroundColor(button.getContext().getColor(R.color.colorAccent))
        enableButton(button)
    } else {
        button.setBackgroundColor(button.getContext().getColor(R.color.grey))
        disableButton(button)
    }
}

private fun enableButton(button: Button) {
    button.isEnabled = true
}

private fun disableButton(button: Button) {
    button.isEnabled = false
}