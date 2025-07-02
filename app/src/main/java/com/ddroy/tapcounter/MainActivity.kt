package com.ddroy.tapcounter

import android.os.Bundle
import androidx.core.view.WindowCompat
import com.ddroy.tapcounter.databinding.ActivityMainBinding

class MainActivity : BaseActivity(){

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}