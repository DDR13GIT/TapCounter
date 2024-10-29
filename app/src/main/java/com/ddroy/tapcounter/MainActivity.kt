package com.ddroy.tapcounter

import android.os.Bundle
import com.ddroy.tapcounter.databinding.ActivityMainBinding

class MainActivity : BaseActivity(){

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}