package com.example.fetchchill.view

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fetchchill.R
import com.example.fetchchill.api.RetrofitClient
import com.example.fetchchill.databinding.ActivitySignUpPageBinding
import com.example.fetchchill.model.SignUpResponse
import com.example.fetchchill.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SignUpPage : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpPageBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        // Edge-to-edge handling
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Setup progress dialog
        progressDialog = ProgressDialog(this).apply {
            setMessage("Creating your account...")
            setCancelable(false)
        }

        // Setup text watchers
        setupTextWatchers()

        // Sign up button click
        binding.SignUpButton.setOnClickListener {
            attemptSignUp()
        }

        // Navigation
        binding.toLoginPage.setOnClickListener {
            startActivity(Intent(this, LoginPage::class.java))
        }

        binding.signUpToSplashScreen.setOnClickListener {
            startActivity(Intent(this, SplashScreen::class.java))
        }
    }

    private fun setupTextWatchers() {
        binding.passwordTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = validatePassword(s?.toString() ?: "")
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.emailTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = validateEmail(s?.toString() ?: "")
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun attemptSignUp() {
        val name = binding.usernameTxt.text.toString().trim() // Using username input as name
        val email = binding.emailTxt.text.toString().trim()
        val password = binding.passwordTxt.text.toString().trim()
        val confirmPassword = binding.confirmPasswordTxt.text.toString().trim()

        if (validateAllFields(name, email, password, confirmPassword)) {
            registerUser(name, email, password)
        }
    }

    private fun validateAllFields(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        var isValid = true

        // Name validation
        if (name.isEmpty()) {
            binding.usernameHint.text = "Name is required"
            binding.usernameHint.setTextColor(Color.RED)
            isValid = false
        } else {
            binding.usernameHint.text = ""
        }

        // Email validation
        if (email.isEmpty()) {
            binding.emailHint.text = "Email is required"
            binding.emailHint.setTextColor(Color.RED)
            isValid = false
        } else if (!isValidEmail(email)) {
            binding.emailHint.text = "Invalid email format"
            binding.emailHint.setTextColor(Color.RED)
            isValid = false
        } else {
            binding.emailHint.text = ""
        }

        // Password validation
        if (!isValidPassword(password)) {
            isValid = false
        }

        // Confirm password
        if (confirmPassword.isEmpty()) {
            binding.confirmPasswordHint.text = "Please confirm password"
            binding.confirmPasswordHint.setTextColor(Color.RED)
            isValid = false
        } else if (password != confirmPassword) {
            binding.confirmPasswordHint.text = "Passwords don't match"
            binding.confirmPasswordHint.setTextColor(Color.RED)
            isValid = false
        } else {
            binding.confirmPasswordHint.text = ""
        }

        return isValid
    }

    private fun validatePassword(password: String) {
        when {
            password.isEmpty() -> {
                binding.passwordHint.text = "Password is required"
                binding.passwordHint.setTextColor(Color.RED)
            }
            password.length < 8 -> {
                binding.passwordHint.text = "Minimum 8 characters"
                binding.passwordHint.setTextColor(Color.RED)
            }
            !containsSpecialCharacter(password) -> {
                binding.passwordHint.text = "Needs special character (!@#\$&*)"
                binding.passwordHint.setTextColor(Color.RED)
            }
            else -> {
                binding.passwordHint.text = "Strong password"
                binding.passwordHint.setTextColor(Color.GREEN)
            }
        }
    }

    private fun validateEmail(email: String) {
        when {
            email.isEmpty() -> {
                binding.emailHint.text = "Email is required"
                binding.emailHint.setTextColor(Color.RED)
            }
            !isValidEmail(email) -> {
                binding.emailHint.text = "Invalid email format"
                binding.emailHint.setTextColor(Color.RED)
            }
            else -> {
                binding.emailHint.text = "Valid email"
                binding.emailHint.setTextColor(Color.GREEN)
            }
        }
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8 &&
                containsSpecialCharacter(password) &&
                !password.contains(" ")
    }

    private fun registerUser(name: String, email: String, password: String) {
        progressDialog.show()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Create User object with "name" field (matching PHP)
                val user = User(name = name, email = email, password = password)
                val response = RetrofitClient.apiService.signUp(user)

                if (response.isSuccessful) {
                    response.body()?.let { signUpResponse ->
                        if (signUpResponse.success) {
                            // Save user ID to SharedPreferences
                            signUpResponse.user?.id?.let { userId ->
                                sharedPreferences.edit().putInt("user_id", userId).apply()
                                Log.d("SignUp", "User ID saved: $userId")
                            }

                            runOnUiThread {
                                Toast.makeText(
                                    this@SignUpPage,
                                    signUpResponse.message ?: "Registration successful",
                                    Toast.LENGTH_SHORT
                                ).show()

                                startActivity(Intent(this@SignUpPage, LoginPage::class.java))
                                finish()
                            }
                        } else {
                            runOnUiThread {
                                handleSignUpError(signUpResponse.message)
                            }
                        }
                    } ?: run {
                        runOnUiThread {
                            Toast.makeText(
                                this@SignUpPage,
                                "Empty response from server",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        handleNetworkError(response.errorBody()?.string())
                    }
                }
            } catch (e: IOException) {
                runOnUiThread {
                    handleNetworkError("Network error: ${e.message}")
                }
            } catch (e: HttpException) {
                runOnUiThread {
                    handleNetworkError("HTTP error: ${e.message}")
                }
            } catch (e: Exception) {
                runOnUiThread {
                    handleNetworkError("Unexpected error: ${e.message}")
                }
            } finally {
                runOnUiThread {
                    progressDialog.dismiss()
                }
            }
        }
    }

    private fun handleSignUpError(errorMessage: String?) {
        when {
            errorMessage?.contains("Email already exists", ignoreCase = true) == true -> {
                binding.emailHint.text = "Email already registered"
                binding.emailHint.setTextColor(Color.RED)
            }
            errorMessage?.contains("Name already exists", ignoreCase = true) == true -> {
                binding.usernameHint.text = "Name already taken"
                binding.usernameHint.setTextColor(Color.RED)
            }
            else -> {
                Toast.makeText(
                    this,
                    errorMessage ?: "Registration failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun handleNetworkError(error: String?) {
        Log.e("SignUp", "Error: $error")
        Toast.makeText(
            this,
            error ?: "Network error occurred",
            Toast.LENGTH_SHORT
        ).show()
    }

    // Helper functions
    private fun containsSpecialCharacter(password: String): Boolean {
        val specialChars = "!@#\$&*"
        return password.any { it in specialChars }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}