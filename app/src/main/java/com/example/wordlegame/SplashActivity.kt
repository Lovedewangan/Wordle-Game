package com.example.wordlegame

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash) // Ensure this is the correct layout name

        // Start fade out animation on the root view
        fadeOutAndStartMainActivity()
    }

    private fun fadeOutAndStartMainActivity() {
        val rootView = findViewById<View>(android.R.id.content) // Get the root view of the activity
        val fadeOut = AlphaAnimation(1f, 0f) // This used to Create Fade out Animation
        fadeOut.duration = 3000


        fadeOut.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}

            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                // Start MainActivity after the animation ends
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish() // Close SplashActivity
            }

            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
        })

        rootView.startAnimation(fadeOut) // Apply animation to the root view
    }
}
