package com.example.fetchchill.view

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fetchchill.R
import androidx.fragment.app.Fragment
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

        // Set click listeners for each service
        checkUpFrame.setOnClickListener { replaceFragment(FragmentCheckup()) }
        groomingFrame.setOnClickListener { replaceFragment(GroomingFragment()) }
        vaccineFrame.setOnClickListener { replaceFragment(FragmentVaccine()) }
        trainingFrame.setOnClickListener { replaceFragment(FragmentTraining()) }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}