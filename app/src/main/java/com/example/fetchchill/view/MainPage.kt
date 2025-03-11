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

    // Declare member variables for the frames
    private lateinit var checkUpFrame: FrameLayout
    private lateinit var groomingFrame: FrameLayout
    private lateinit var vaccineFrame: FrameLayout
    private lateinit var trainingFrame: FrameLayout
    private lateinit var nav_settings: ImageButton
    private lateinit var nav_schedule: ImageButton
    private lateinit var nav_home: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_page)

        // Set up window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the frame layouts
        checkUpFrame = findViewById(R.id.checkUpFrame)
        groomingFrame = findViewById(R.id.groomingFrame)
        vaccineFrame = findViewById(R.id.vaccineFrame)
        trainingFrame = findViewById(R.id.trainingFrame)
        nav_settings = findViewById(R.id.nav_settings)
        nav_schedule = findViewById(R.id.nav_schedule)
        nav_home = findViewById(R.id.nav_home)

        // Set click listeners for each service
        checkUpFrame.setOnClickListener { replaceFragment(FragmentCheckup()) }
        groomingFrame.setOnClickListener { replaceFragment(GroomingFragment()) }
        vaccineFrame.setOnClickListener { replaceFragment(FragmentVaccine()) }
        trainingFrame.setOnClickListener { replaceFragment(FragmentTraining()) }
        nav_settings.setOnClickListener { replaceFragment(FragmentSetting()) }
        nav_schedule.setOnClickListener { replaceFragment(FragmentSchedule()) }

        // Set click listener for nav_home to start a new activity
        nav_home.setOnClickListener {
            val intent = Intent(this, MainPage::class.java) // Replace HomeActivity with your target activity
            startActivity(intent)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        // Disable frames before navigating to another fragment
        disableFrames()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    // Function to disable the clickable frames
    private fun disableFrames() {
        checkUpFrame.isClickable = false
        checkUpFrame.isFocusable = false

        groomingFrame.isClickable = false
        groomingFrame.isFocusable = false

        vaccineFrame.isClickable = false
        vaccineFrame.isFocusable = false

        trainingFrame.isClickable = false
        trainingFrame.isFocusable = false
    }

    // Function to enable the clickable frames (when navigating back to the main page)
    private fun enableFrames() {
        // Only enable frames that are not already disabled
        if (!checkUpFrame.isClickable) {
            checkUpFrame.isClickable = true
            checkUpFrame.isFocusable = true
        }

        if (!groomingFrame.isClickable) {
            groomingFrame.isClickable = true
            groomingFrame.isFocusable = true
        }

        if (!vaccineFrame.isClickable) {
            vaccineFrame.isClickable = true
            vaccineFrame.isFocusable = true
        }

        if (!trainingFrame.isClickable) {
            trainingFrame.isClickable = true
            trainingFrame.isFocusable = true
        }
    }

    // Function to set up click listeners for frames
    private fun setupFrameClickListeners() {
        checkUpFrame.setOnClickListener {
            // Disable the frame after clicking
            enableFrames()
            // Perform your action here
        }

        groomingFrame.setOnClickListener {
            // Disable the frame after clicking
            enableFrames()
            // Perform your action here
        }

        vaccineFrame.setOnClickListener {
            // Disable the frame after clicking
            enableFrames()
            // Perform your action here
        }

        trainingFrame.setOnClickListener {
            // Disable the frame after clicking
            enableFrames()
            // Perform your action here
        }
    }

    // Call this function in your onCreate or initialization method
    private fun initialize() {
        setupFrameClickListeners()
        enableFrames() // Initially enable frames
    }

    // Optionally, handle the back button press to enable frames when returning to the home screen
    override fun onBackPressed() {
        super.onBackPressed()
        enableFrames() // Re-enable the frames when navigating back
    }
}
