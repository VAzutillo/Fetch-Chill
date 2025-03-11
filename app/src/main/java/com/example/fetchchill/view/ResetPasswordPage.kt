package com.example.fetchchill.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fetchchill.R

class ResetPasswordPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reset_password_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val first = findViewById<Button>(R.id.resetToLoginPage)
        first.setOnClickListener {
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
        }
        val first1 = findViewById<ImageView>(R.id.backToForgotPass)
        first1.setOnClickListener {
            val intent = Intent(this, ForgotPasswordPage::class.java)
            startActivity(intent)
        }
    }
}