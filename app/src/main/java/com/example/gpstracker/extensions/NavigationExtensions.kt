package com.example.gpstracker.extensions

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController


fun Fragment.openScreen(navDirections: NavDirections, navOptions: NavOptions?) {
    this.findNavController().navigate(navDirections, navOptions)
}

fun Fragment.openScreen(navDirections: NavDirections) {
    this.openScreen(navDirections, null)
}

fun Fragment.openPopBackstackScreen() {
    this.findNavController().popBackStack()
}

fun Fragment.openPopBackstackScreen(destination: Int) {
    this.findNavController().popBackStack(destination, false)
}