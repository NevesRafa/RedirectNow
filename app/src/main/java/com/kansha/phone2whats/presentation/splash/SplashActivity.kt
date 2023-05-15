package com.kansha.phone2whats.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.kansha.phone2whats.databinding.ActivitySplashBinding
import com.kansha.phone2whats.presentation.home.HomeActivity

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splashScreenDuration()
    }

    private fun splashScreenDuration() {
        val splashScreenDuration = 3000L // 3 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, splashScreenDuration)
    }
}