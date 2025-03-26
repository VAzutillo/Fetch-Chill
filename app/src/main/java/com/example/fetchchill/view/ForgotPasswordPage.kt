package com.example.fetchchill.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.fetchchill.R
import com.example.fetchchill.api.RetrofitClient
import com.example.fetchchill.model.ForgotPasswordRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ForgotPasswordPage : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var forgotPasswordBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password_page)

        // Edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText)
        newPasswordEditText = findViewById(R.id.newPasswordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        forgotPasswordBtn = findViewById(R.id.Forgotpassbtn)

        // Back to Login Page
        val backToLoginPage = findViewById<ImageView>(R.id.backToLoginPage)
        backToLoginPage.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
            finish()
        }

        // Forgot Password Button
        forgotPasswordBtn.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val newPassword = newPasswordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            // Validate inputs
            if (validateInputs(email, newPassword, confirmPassword)) {
                resetPassword(email, newPassword, confirmPassword)
            }
        }
    }

    private fun validateInputs(email: String, newPassword: String, confirmPassword: String): Boolean {
        var isValid = true

        // Email validation
        if (email.isEmpty()) {
            emailEditText.error = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Invalid email format"
            isValid = false
        }

        // Password validation
        if (newPassword.isEmpty()) {
            newPasswordEditText.error = "New password is required"
            isValid = false
        } else if (newPassword.length < 8) {
            newPasswordEditText.error = "Password must be at least 8 characters"
            isValid = false
        }

        // Confirm password validation
        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.error = "Confirm password is required"
            isValid = false
        } else if (newPassword != confirmPassword) {
            confirmPasswordEditText.error = "Passwords do not match"
            isValid = false
        }

        return isValid
    }

    private fun resetPassword(email: String, newPassword: String, confirmPassword: String) {
        lifecycleScope.launch {
            try {
                val apiService = RetrofitClient.apiService
                val response = apiService.forgotPassword(
                    ForgotPasswordRequest(
                        email = email,
                        newPassword = newPassword,
                        confirmPassword = confirmPassword
                    )
                )

                if (response.isSuccessful) {
                    val result = response.body()
                    if (result?.success == true) {
                        // Password reset successful
                        Toast.makeText(this@ForgotPasswordPage, result.message, Toast.LENGTH_SHORT).show()

                        // Navigate to Login Page
                        val intent = Intent(this@ForgotPasswordPage, LoginPage::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Server returned success as false
                        Toast.makeText(this@ForgotPasswordPage, result?.message ?: "Password reset failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // HTTP error
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@ForgotPasswordPage, "Error: $errorBody", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                // Network or server error
                Toast.makeText(this@ForgotPasswordPage, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                // Other exceptions
                Toast.makeText(this@ForgotPasswordPage, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}