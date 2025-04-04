package com.example.fetchchill.view

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fetchchill.R
import com.example.fetchchill.utils.AuthManager

class LoginPage : AppCompatActivity() {

    private lateinit var loginEmailTxt: EditText
    private lateinit var loginPasswordTxt: EditText
    private lateinit var loginBtn: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var authManager: AuthManager
    private lateinit var progressDialog: ProgressDialog
    private lateinit var toForgotPassword: TextView
    private lateinit var toSignUpPage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)



        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        // Initialize AuthManager
        AuthManager.initialize(sharedPreferences)
        authManager = AuthManager(sharedPreferences)

//        if (AuthManager.getUserId() == null) {
//            val intent = Intent(this, MainPage::class.java)
//            startActivity(intent)
//            finish()
//        }

        // Initialize views
        loginEmailTxt = findViewById(R.id.loginUsernameTxt)
        loginPasswordTxt = findViewById(R.id.loginPasswordTxt)
        loginBtn = findViewById(R.id.LoginBtn)
        toForgotPassword = findViewById(R.id.toForgotPassword)
        toSignUpPage = findViewById(R.id.toSignupPage)

        // Initialize progress dialog
        progressDialog = ProgressDialog(this).apply {
            setMessage("Logging in...")
            setCancelable(false)
        }

        // Set click listener for login button
        loginBtn.setOnClickListener {
            login()
        }

        // Set click listener for forgot password
        toForgotPassword.setOnClickListener {
            // Navigate to Forgot Password Page
            val intent = Intent(this, ForgotPasswordPage::class.java)
            startActivity(intent)
        }
        toSignUpPage.setOnClickListener {
            // Navigate to Sign Up Page
            val intent = Intent(this, SignUpPage::class.java)
            startActivity(intent)
        }

    }

    private fun login() {
        val email = loginEmailTxt.text.toString().trim()
        val password = loginPasswordTxt.text.toString().trim()

        // Validate Input
        if (email.isEmpty()) {
            loginEmailTxt.error = "Email is required"
            loginEmailTxt.requestFocus()
            return
        }

        if (!isValidEmail(email)) {
            loginEmailTxt.error = "Please enter a valid email address"
            loginEmailTxt.requestFocus()
            return
        }

        if (password.isEmpty()) {
            loginPasswordTxt.error = "Password is required"
            loginPasswordTxt.requestFocus()
            return
        }

        // Show progress dialog
        progressDialog.show()

        // Call the AuthManager to log in
        authManager.login(email, password,
            onSuccess = { loginResponse ->
                progressDialog.dismiss() // Hide progress dialog
                Toast.makeText(this, loginResponse.message, Toast.LENGTH_SHORT).show()

                // Check if user ID was saved properly
                val userId = authManager.userId()
                Log.d("LoginPage", "User ID retrieved: $userId")

                if (userId != -1) {
                    // Navigate to MainPage
                    val intent = Intent(this, MainPage::class.java)
                    intent.putExtra("USER_EMAIL", email) // Pass the email to MainPage
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("LoginPage", "User ID not saved correctly")
                    Toast.makeText(this, "Login error: User data not saved", Toast.LENGTH_SHORT).show()
                }
            },
            onError = { errorMessage ->
                progressDialog.dismiss() // Hide progress dialog
                Log.e("LoginPage", "Login failed: $errorMessage")
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}