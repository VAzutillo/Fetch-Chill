package com.example.fetchchill.view

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fetchchill.R
import androidx.fragment.app.Fragment
import com.example.fetchchill.FragmentSchedule
import com.example.fetchchill.FragmentSetting
import com.example.fetchchill.view.fragments.FragmentCheckup
import com.example.fetchchill.view.fragments.FragmentTraining
import com.example.fetchchill.view.fragments.FragmentVaccine
import com.example.fetchchill.view.fragments.GroomingFragment

class MainPage : AppCompatActivity() {

    private lateinit var checkUpFrame: FrameLayout
    private lateinit var groomingFrame: FrameLayout
    private lateinit var vaccineFrame: FrameLayout
    private lateinit var trainingFrame: FrameLayout
    private lateinit var nav_settings: ImageButton
    private lateinit var nav_schedule: ImageButton
    private lateinit var nav_home: ImageButton

    // Add a property to store the email
    var userEmail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_page)

        // Get the email from the intent
        userEmail = intent.getStringExtra("USER_EMAIL") ?: ""

        // Initialize the frame layouts
        checkUpFrame = findViewById(R.id.checkUpFrame)
        groomingFrame = findViewById(R.id.groomingFrame)
        vaccineFrame = findViewById(R.id.vaccineFrame)
        trainingFrame = findViewById(R.id.trainingFrame)
        nav_settings = findViewById(R.id.nav_settings)
        nav_schedule = findViewById(R.id.nav_schedule)
        nav_home = findViewById(R.id.nav_home)

        // Set up click listeners for each service
        setupFrameClickListeners()

        // Set click listener for nav_home to start a new activity
        nav_home.setOnClickListener {
            val intent = Intent(this, MainPage::class.java)
            // Make sure to pass the email to the new instance of MainPage
            intent.putExtra("USER_EMAIL", userEmail)
            startActivity(intent)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        disableFrames() // Disable frames when navigating to a fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupFrameClickListeners() {
        checkUpFrame.setOnClickListener { replaceFragment(FragmentCheckup()) }
        groomingFrame.setOnClickListener { replaceFragment(GroomingFragment()) }
        vaccineFrame.setOnClickListener { replaceFragment(FragmentVaccine()) }
        trainingFrame.setOnClickListener { replaceFragment(FragmentTraining()) }
        nav_settings.setOnClickListener { replaceFragment(FragmentSetting()) }
        nav_schedule.setOnClickListener { replaceFragment(FragmentSchedule()) }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Re-enable frames when navigating back to the main page
        if (supportFragmentManager.backStackEntryCount == 0) {
            enableFrames()
        }
    }

    // Change visibility from private to internal or public
    internal fun disableFrames() {
        checkUpFrame.isClickable = false
        groomingFrame.isClickable = false
        vaccineFrame.isClickable = false
        trainingFrame.isClickable = false
    }

    internal fun enableFrames() {
        checkUpFrame.isClickable = true
        groomingFrame.isClickable = true
        vaccineFrame.isClickable = true
        trainingFrame.isClickable = true
    }
}