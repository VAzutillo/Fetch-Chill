package com.example.fetchchill.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fetchchill.R
import com.example.fetchchill.api.RetrofitClient
import com.example.fetchchill.databinding.ActivityLoginPageBinding
import com.example.fetchchill.model.LoginResponse
import com.example.fetchchill.model.LoginUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPage : AppCompatActivity() {
    private val apiService = RetrofitClient.apiService
    private lateinit var binding: ActivityLoginPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWindowInsets()
        setupClickListeners()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupClickListeners() {
        binding.LoginBtn.setOnClickListener {
            login()
        }

        binding.toSignupPage.setOnClickListener {
            val intent = Intent(this, SignUpPage::class.java)
            startActivity(intent)
        }
        binding.toForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordPage::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        val email = binding.loginUsernameTxt.text.toString()
        val password = binding.loginPasswordTxt.text.toString()

        // Create a LoginUser  object with the email and password
        val user = LoginUser(email = email, password = password)

        // Call the API with the LoginUser  object
        apiService.userLogin(user).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.success) {
                        Toast.makeText(this@LoginPage, "Login successful!", Toast.LENGTH_SHORT)
                            .show()
                        // Navigate to the main page and pass the email
                        val intent = Intent(this@LoginPage, MainPage::class.java)
                        intent.putExtra("USER_EMAIL", email) // Pass the email
                        startActivity(intent)
                        finish() // Close the LoginPage
                    } else {
                        Toast.makeText(
                            this@LoginPage,
                            "Login failed: ${loginResponse?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@LoginPage,
                        "Login failed: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginPage, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}