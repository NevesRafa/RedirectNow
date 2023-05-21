package com.kansha.redirectNow.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.kansha.redirectNow.databinding.ActivitySplashBinding
import com.kansha.redirectNow.presentation.tutorial.TutorialActivity

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
            val intent = Intent(this@SplashActivity, TutorialActivity::class.java)
            startActivity(intent)
            finish()
        }, splashScreenDuration)
    }
}