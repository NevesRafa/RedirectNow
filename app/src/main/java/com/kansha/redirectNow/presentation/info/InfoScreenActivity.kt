package com.kansha.redirectNow.presentation.info

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kansha.redirectNow.databinding.ActivityInfoScreenBinding

class InfoScreenActivity : AppCompatActivity() {

    lateinit var binding: ActivityInfoScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}