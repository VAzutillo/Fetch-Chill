package com.example.fetchchill.view.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fetchchill.R
import com.example.fetchchill.view.BaseAppointmentFragment
import com.example.fetchchill.view.MainPage
import java.util.*

class GroomingFragment : BaseAppointmentFragment() {

    private lateinit var etDate: EditText
    private lateinit var etTime: EditText
    private lateinit var btnSubmit: Button
    private lateinit var backButton: ImageView // Declare the back button
    private var selectedDate: Date? = null // Variable to hold the selected date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Handle any arguments if needed
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_grooming, container, false)

        // Initialize EditText fields
        etDate = view.findViewById(R.id.etDate)
        etTime = view.findViewById(R.id.etTime)
        btnSubmit = view.findViewById(R.id.SubmitBtnGrooming) // Initialize the submit button
        backButton = view.findViewById(R.id.backButtonGrooming) // Initialize the back button

        // Set click listeners for date and time EditTexts
        etDate.setOnClickListener { showDatePicker() }
        etTime.setOnClickListener { showTimePicker() }

        // Set up window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set click listener for the submit button
        btnSubmit.setOnClickListener {
            // Validate input fields
            val appointmentDate = etDate.text.toString()
            val appointmentTime = etTime.text.toString()

            if (appointmentDate.isEmpty() || appointmentTime.isEmpty()) {
                showToast("Please select both date and time.")
                return@setOnClickListener
            }

            // Show the confirmation dialog when the button is clicked
            val confirmationDialog = ConfirmationDialogFragment.newInstance(
                "We’ve got you confirmed for your appointment.",
                "${etTime.text} | Dr. Smith", // Customize this as needed
                selectedDate ?: Date() // Pass the selected date or current date as default
            )

            confirmationDialog.setOnConfirmListener {
                // If the user confirms, proceed to create the appointment
                val userId = 1 // Replace with actual user ID
                val serviceType = "Checkup" // Define the service type

                // Call the createAppointment method from BaseAppointmentFragment
                createAppointment(userId, serviceType, appointmentDate, appointmentTime)
            }

            confirmationDialog.show(parentFragmentManager, "confirmationDialog")
        }

        // Set click listener for the back button
        backButton.setOnClickListener {
            // Pop the current fragment from the back stack
            parentFragmentManager.popBackStack()
        }

        return view
    }
    override fun onResume() {
        super.onResume()
        // Disable frames when this fragment is visible
        (activity as? MainPage)?.disableFrames()
    }

    override fun onPause() {
        super.onPause()
        // Re-enable frames when navigating away from this fragment
        (activity as? MainPage)?.enableFrames()
    }

    @SuppressLint("DefaultLocale")
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a DatePickerDialog
        val datePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            selectedDate = Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedYear)
                set(Calendar.MONTH, selectedMonth)
                set(Calendar.DAY_OF_MONTH, selectedDay)
            }.time // Store the selected date

            etDate.setText(
                String.format(
                    "%d-%02d-%02d",
                    selectedYear,
                    selectedMonth + 1,
                    selectedDay
                )
            )
        }, year, month, day)

        // Set the minimum date to today
        datePicker.datePicker.minDate = System.currentTimeMillis() // Disable past dates

        datePicker.show()
    }

    @SuppressLint("DefaultLocale")
    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            // Check if the selected time is within the allowed range
            if (selectedHour < 7 || selectedHour > 17) {
                showToast("Please select a time between 7 AM and 5 PM.")
                return@TimePickerDialog // Exit if the time is not valid
            }

            etTime.setText(
                String.format(
                    "%02d:%02d %s",
                    if (selectedHour > 12) selectedHour - 12 else selectedHour,
                    selectedMinute,
                    if (selectedHour >= 12) "PM" else "AM"
                )
            )
        }, hour, minute, false)

        timePicker.show()
    }
}