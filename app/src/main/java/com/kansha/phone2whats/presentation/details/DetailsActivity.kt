package com.kansha.phone2whats.presentation.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kansha.phone2whats.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PHONE_DETAILS = "extra_phone_details"
    }

    lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}