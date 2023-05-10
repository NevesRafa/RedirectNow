package com.kansha.phone2whats.presentation.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kansha.phone2whats.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}