package com.example.fetchchill.view.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fetchchill.R
import com.example.fetchchill.utils.AuthManager
import com.example.fetchchill.view.fragments.ConfirmationDialogFragment
import com.example.fetchchill.view.MainPage
import java.util.*
import com.example.fetchchill.view.BaseAppointmentFragment

class FragmentCheckup : BaseAppointmentFragment() {

    private lateinit var etDate: EditText
    private lateinit var etTime: EditText
    private lateinit var btnSubmit: Button
    private lateinit var backButton: ImageView
    private var selectedDate: Date? = null
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize AuthManager using the static instance
        authManager = AuthManager(requireActivity().getSharedPreferences("MyAppPrefs", 0))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_checkup, container, false)

        // Initialize views
        etDate = view.findViewById(R.id.etDate)
        etTime = view.findViewById(R.id.etTime)
        btnSubmit = view.findViewById(R.id.SubmitBtnCheckUp)
        backButton = view.findViewById(R.id.backButtonCheckup)

        // Set click listeners
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

            // Get the logged-in user's ID
            val userId = authManager.userId()
            if (userId == -1) {
                showToast("Please login first.")
                return@setOnClickListener
            }

            // Show the confirmation dialog
            val confirmationDialog = ConfirmationDialogFragment.newInstance(
                "We've got you confirmed for your appointment.",
                "${etTime.text}",
                selectedDate ?: Date()
            )

            confirmationDialog.setOnConfirmListener {
                // If the user confirms, proceed to create the appointment
                val serviceType = "Checkup"
                createAppointment(userId, serviceType, appointmentDate, appointmentTime)
            }

            confirmationDialog.show(parentFragmentManager, "confirmationDialog")
        }

        // Set click listener for the back button
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainPage)?.disableFrames()
    }

    override fun onPause() {
        super.onPause()
        (activity as? MainPage)?.enableFrames()
    }

    @SuppressLint("DefaultLocale")
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            selectedDate = Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedYear)
                set(Calendar.MONTH, selectedMonth)
                set(Calendar.DAY_OF_MONTH, selectedDay)
            }.time

            etDate.setText(
                String.format(
                    "%d-%02d-%02d",
                    selectedYear,
                    selectedMonth + 1,
                    selectedDay
                )
            )
        }, year, month, day)

        datePicker.datePicker.minDate = System.currentTimeMillis()
        datePicker.show()
    }

    @SuppressLint("DefaultLocale")
    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            if (selectedHour < 7 || selectedHour > 17) {
                showToast("Please select a time between 7 AM and 5 PM.")
                return@TimePickerDialog
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