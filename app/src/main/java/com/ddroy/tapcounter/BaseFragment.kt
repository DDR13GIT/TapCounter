package com.ddroy.tapcounter

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.ddroy.tapcounter.navigation.Navigation

abstract class BaseFragment(layoutId: Int) : Fragment(layoutId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    handleBackPress()
                }
            })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                v.paddingLeft,
                systemInsets.top,
                v.paddingRight,
                systemInsets.bottom
            )

            insets
        }
    }

    private fun handleBackPress() {
        if (getHomeFragmentId() == R.id.settingsFragment) {
            Navigation.navigate(getFragmentInstance(), R.id.settingsFragment, R.id.countingFragment)
        } else {
            requireActivity().finish()
        }
    }

    abstract fun getHomeFragmentId(): Int
    abstract fun getFragmentInstance(): Fragment
}