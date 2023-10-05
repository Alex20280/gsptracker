package com.example.gpstracker.base

import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    abstract fun init()
}