package com.example.fetchchill.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fetchchill.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        supportActionBar?.hide()

        // Set up a delayed task using a Handler
        Handler().postDelayed({
            // Create an Intent to start the LoginActivity
            val intent = Intent(this@MainActivity, SplashScreen::class.java)
            // Start the LoginActivity
            startActivity(intent)
            // Finish MainActivity so that the user cannot return to it using the back button
            finish()
        }, 1500) // Delay of 1500 milliseconds (1.5 seconds)
    }

}