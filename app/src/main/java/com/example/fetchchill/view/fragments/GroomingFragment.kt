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
import androidx.fragment.app.Fragment
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fetchchill.R
import java.util.*

class GroomingFragment : Fragment() {

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
            // Show the confirmation dialog when the button is clicked
            val confirmationDialog = ConfirmationDialogFragment.newInstance(
                "We’ve got you confirmed for your appointment.",
                "${etTime.text} | Dr. Smith", // Customize this as needed
                selectedDate ?: Date() // Pass the selected date or current date as default
            )
            confirmationDialog.show(parentFragmentManager, "confirmationDialog")
        }

        // Set click listener for the back button
        backButton.setOnClickListener {
            // Pop the current fragment from the back stack
            parentFragmentManager.popBackStack()
        }

        return view
    }

    @SuppressLint("DefaultLocale")
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = Calendar.getInstance().apply {
                    set(Calendar.YEAR, selectedYear)
                    set(Calendar.MONTH, selectedMonth)
                    set(Calendar.DAY_OF_MONTH, selectedDay)
                }.time // Store the selected date

                etDate.setText(
                    String.format(
                        "%02d/%02d/%d",
                        selectedMonth + 1,
                        selectedDay,
                        selectedYear
                    )
                )
            }, year, month, day)

        datePicker.show()
    }

    @SuppressLint("DefaultLocale")
    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
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