package com.example.fetchchill.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
import com.example.fetchchill.model.SignUpResponse
import com.example.fetchchill.model.User
import com.example.fetchchill.viewmodel.AuthenticationViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpPage : AppCompatActivity() {

    private val apiService = RetrofitClient.apiService
    private lateinit var binding: ActivitySignUpPageBinding
    private val viewModel = AuthenticationViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.SignUpButton.setOnClickListener {
            signup()
        }

        val first1 = findViewById<ImageView>(R.id.signUpToSplashScreen)
        first1.setOnClickListener {
            val intent = Intent(this, SplashScreen::class.java)
            startActivity(intent)
        }

        val sec = findViewById<TextView>(R.id.toLoginPage)
        sec.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
    }


    private fun signup() {
        val username = binding.usernameTxt.text.toString()
        val email = binding.emailTxt.text.toString()
        val password =binding.passwordTxt.text.toString()
        val confirmPassword = binding.confirmPasswordTxt.text.toString()

        val user = User(name = username, email = email, password = password)

        apiService.signUp(user).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {
                if (response.isSuccessful) {
                    val userRegistration = response.body()?.message
                    Toast.makeText(this@SignUpPage, userRegistration.toString(), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SignUpPage, LoginPage::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@SignUpPage, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Toast.makeText(this@SignUpPage, "Registration failed", Toast.LENGTH_SHORT).show()
            }

        })

//        viewModel.login(user)
    }
}

