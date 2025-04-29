package com.ddroy.tapcounter.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavInflater
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.ddroy.tapcounter.R

object Navigation {

    //this method helps in fragment to fragment navigation
    fun navigate(fragment: Fragment, from: Int? = null, to: Int, bundle: Bundle? = null) {
        val navController = fragment.findNavController()
        val navOptionsBuilder = NavOptions.Builder()

        if (from != null) {
            navOptionsBuilder.setPopUpTo(from, true)
        }

        val navOptions = navOptionsBuilder.build()
        navController.navigate(to, bundle, navOptions)
    }
}
