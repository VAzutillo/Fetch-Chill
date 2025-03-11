package com.example.fetchchill.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fetchchill.R
import com.example.fetchchill.api.RetrofitClient
import com.example.fetchchill.databinding.ActivitySignUpPageBinding
import com.example.fetchchill.model.EmailCheckResponse
import com.example.fetchchill.model.EmailRequest
import com.example.fetchchill.model.SignUpResponse
import com.example.fetchchill.model.User
import com.example.fetchchill.model.UsernameCheckResponse
import com.example.fetchchill.model.UsernameRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpPage : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Username availability check
        binding.usernameTxt.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val username = binding.usernameTxt.text.toString()
                if (username.isEmpty()) {
                    binding.usernameHint.text = "Username is required"
                    binding.usernameHint.setTextColor(Color.RED)
                } else {
                    checkUsernameAvailability(username)
                }
            }
        }

        // Password validation
        binding.passwordTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s?.toString() ?: ""
                validatePassword(password)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Email availability check
        binding.emailTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s?.toString() ?: ""
                if (isValidEmail(email)) {
                    checkEmailAvailability(email) { isAvailable ->
                        if (isAvailable) {
                            binding.emailHint.text = "Email available"
                            binding.emailHint.setTextColor(Color.parseColor("#32CD32"))
                        } else {
                            binding.emailHint.text = "The email has already been taken."
                            binding.emailHint.setTextColor(Color.RED)
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Sign up button click listener
        binding.SignUpButton.setOnClickListener {
            val username = binding.usernameTxt.text.toString()
            val email = binding.emailTxt.text.toString()
            val password = binding.passwordTxt.text.toString()
            val confirmPassword = binding.confirmPasswordTxt.text.toString()

            if (validateFields(username, email, password, confirmPassword)) {
                // Check email availability before registering
                checkEmailAvailability(email) { isAvailable ->
                    if (isAvailable) {
                        registerUser (username, email, password)
                    } else {
                        binding.emailHint.text = "The email has already been taken."
                        binding.emailHint.setTextColor(Color.RED)
                    }
                }
            }
        }

        // Navigation to login page
        findViewById<TextView>(R.id.toLoginPage).setOnClickListener {
            startActivity(Intent(this, LoginPage::class.java))
        }

        // Back to splash screen
        findViewById<ImageView>(R.id.signUpToSplashScreen).setOnClickListener {
            startActivity(Intent(this, SplashScreen::class.java))
        }
    }

    private fun validateFields(username: String, email: String, password: String, confirmPassword: String): Boolean {
        var allFieldsValid = true

        if (username.isEmpty()) {
            binding.usernameHint.text = "Username is required"
            binding.usernameHint.setTextColor(Color.RED)
            allFieldsValid = false
        }

        if (email.isEmpty()) {
            binding.emailHint.text = "Email is required"
            binding.emailHint.setTextColor(Color.RED)
            allFieldsValid = false
        } else if (!isValidEmail(email)) {
            binding.emailHint.text = "Invalid email"
            binding.emailHint.setTextColor(Color.RED)
            allFieldsValid = false
        } else if (!isAllowedEmailDomain(email)) {
            binding.emailHint.text = "Email must be from @gmail.com or @yahoo.com"
            binding.emailHint.setTextColor(Color.RED)
            allFieldsValid = false
        }

        if (password.isEmpty()) {
            binding.passwordHint.text = "Password is required"
            binding.passwordHint.setTextColor(Color.RED)
            allFieldsValid = false
        } else if (password.length < 8 || password.length > 16) {
            binding.passwordHint.text = "Password must be between 8 and 16 characters"
            binding.passwordHint.setTextColor(Color.RED)
            allFieldsValid = false
        } else if (password.contains(" ")) {
            binding.passwordHint.text = "Password cannot contain spaces"
            binding.passwordHint.setTextColor(Color.RED)
            allFieldsValid = false
        } else if (!containsSpecialCharacter(password)) {
            binding.passwordHint.text = "Password must contain at least one special character (!@#\$&*)"
            binding.passwordHint.setTextColor(Color.RED)
            allFieldsValid = false
        }

        if (confirmPassword.isEmpty()) {
            binding.confirmPasswordHint.text = "Confirm password is required"
            binding.confirmPasswordHint.setTextColor(Color.RED)
            allFieldsValid = false
        } else if (password != confirmPassword) {
            binding.confirmPasswordHint.text = "Passwords do not match"
            binding.confirmPasswordHint.setTextColor(Color.RED)
            allFieldsValid = false
        }

        return allFieldsValid
    }

    private fun isAllowedEmailDomain(email: String): Boolean {
        val allowedDomains = listOf("gmail.com", "yahoo.com")
        val emailDomain = email.substringAfter("@")
        return emailDomain in allowedDomains
    }

    private fun validatePassword(password: String) {
        if (password.isEmpty() || password.contains(" ")) {
            binding.passwordHint.text = getString(R.string.password_cannot_be_empty_or_contain_spaces)
            binding.passwordHint.setTextColor(Color.RED)
        } else if (password.length < 8 || password.length > 16) {
            binding.passwordHint.text = getString(R.string.the_password_must_be_between_8_and_16_characters)
            binding.passwordHint.setTextColor(Color.RED)
        } else if (!containsSpecialCharacter(password)) {
            binding.passwordHint.text = getString(R.string.the_password_must_contain_at_least_one_special_character)
            binding.passwordHint.setTextColor(Color.RED)
        } else {
            binding.passwordHint.text = getString(R.string.strong_password)
            binding.passwordHint.setTextColor(Color.parseColor("#32CD32"))
        }
    }

    private fun registerUser (username: String, email: String, password: String) {
        val user = User(name = username, email = email, password = password)

        RetrofitClient.apiService.signUp(user).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@SignUpPage, response.body()?.message ?: "Signup successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SignUpPage, LoginPage::class.java))
                } else {
                    Toast.makeText(this@SignUpPage, "Signup failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Toast.makeText(this@SignUpPage, "API call failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkUsernameAvailability(username: String) {
        val request = UsernameRequest(username)

        RetrofitClient.apiService.checkUsername(request).enqueue(object : Callback<UsernameCheckResponse> {
            override fun onResponse(call: Call<UsernameCheckResponse>, response: Response<UsernameCheckResponse>) {
                try {
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()
                        if (responseBody?.exists == true) {
                            binding.usernameHint.text = "The username has already been taken."
                            binding.usernameHint.setTextColor(Color.RED)
                        } else {
                            binding.usernameHint.text = "Username available"
                            binding.usernameHint.setTextColor(Color.parseColor("#32CD32"))
                        }
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "Unknown error"
                        binding.usernameHint.text = "Server error: $errorBody"
                        binding.usernameHint.setTextColor(Color.RED)
                    }
                } catch (e: Exception) {
                    binding.usernameHint.text = "Error parsing response: ${e.message}"
                    binding.usernameHint.setTextColor(Color.RED)
                }
            }

            override fun onFailure(call: Call<UsernameCheckResponse>, t: Throwable) {
                binding.usernameHint.text = "API call failed: ${t.message}"
                binding.usernameHint.setTextColor(Color.RED)
            }
        })
    }


    private fun checkEmailAvailability(email: String, callback: (Boolean) -> Unit) {
        val request = EmailRequest(email)
        RetrofitClient.apiService.checkEmail(request).enqueue(object : Callback<EmailCheckResponse> {
            override fun onResponse(call: Call<EmailCheckResponse>, response: Response<EmailCheckResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.exists != true) // Call the callback with the availability status
                } else {
                    binding.emailHint.text = "Error checking email"
                    binding.emailHint.setTextColor(Color.RED)
                    callback(false) // Treat as not available on error
                }
            }

            override fun onFailure(call: Call<EmailCheckResponse>, t: Throwable) {
                binding.emailHint.text = "API call failed: ${t.message}"
                binding.emailHint.setTextColor(Color.RED)
                callback(false) // Treat as not available on failure
            }
        })
    }

    private fun containsSpecialCharacter(password: String): Boolean {
        val specialCharacters = "!@#\$&*"
        return password.any { it in specialCharacters }
    }

    private fun isValidEmail(email: String): Boolean {
        // A more comprehensive email validation can be implemented here
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}