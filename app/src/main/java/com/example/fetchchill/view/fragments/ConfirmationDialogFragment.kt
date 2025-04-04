package com.example.fetchchill.view.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import android.view.View
import androidx.appcompat.app.AlertDialog
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import com.example.fetchchill.FragmentSchedule
import com.example.fetchchill.R
import java.text.SimpleDateFormat
import java.util.*

class ConfirmationDialogFragment : DialogFragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var appointmentDate: Date? = null // Variable for the appointment date

    private var onConfirmListener: (() -> Unit)? = null // Listener for confirmation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            appointmentDate = it.getSerializable(ARG_DATE) as? Date // Get the date from arguments
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.fragment_confirmation_dialog, null)

        // Access the views using their IDs
        val imgCheckmark: ImageView = view.findViewById(R.id.imgCheckmark)
        val tvConfirmationMessage: TextView = view.findViewById(R.id.tvConfirmationMessage)
        val tvAppointmentDetails: TextView = view.findViewById(R.id.tvAppointmentDetails)
        val tvAppointmentDate: TextView = view.findViewById(R.id.tvAppointmentDate) // New TextView for date
        val btnAppointments: Button = view.findViewById(R.id.btnAppointments)

        // Set the confirmation message and appointment details
        tvConfirmationMessage.text = param1 ?: "Default confirmation message"
        tvAppointmentDetails.text = param2 ?: "Default appointment details"

        // Format the date and set it to the TextView
        appointmentDate?.let {
            val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
            tvAppointmentDate.text = dateFormat.format(it)
        }

        // Set a click listener on the dialog view to confirm the appointment
        view.setOnClickListener {
            onConfirmListener?.invoke() // Call the listener
            dismiss() // Close the dialog
        }

        imgCheckmark.setOnClickListener {
            onConfirmListener?.invoke() // Call the listener
            dismiss() // Close the dialog
        }

        // Set click listener for the Appointments button
        btnAppointments.setOnClickListener {
//            Log.d("navigate","working")
            // First call the confirm listener to save the data
            onConfirmListener?.invoke()

            // Close the dialog (important to do this first) dismiss()
//            navigateToSchedule()
            // Short delay to ensure the appointment is saved before navigating
            view.postDelayed({
                Log.d("navigate","working")
                // Navigate to FragmentSchedule
                navigateToSchedule()
                dismiss()
            }, 300) // Small delay to ensure API call completes
        }

        builder.setView(view)
        return builder.create()
    }

    // Function to navigate to FragmentSchedule
    private fun navigateToSchedule() {
        Log.d("navigate","working")
        // Create FragmentSchedule with flag indicating an appointment was just created
        val fragmentSchedule = FragmentSchedule.newInstance(appointmentCreated = true)
        val transaction = parentFragmentManager.beginTransaction()

        // Add the transition animation if desired
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

        // Replace the current fragment with FragmentSchedule
        // Use the main container ID from your app (commonly fragment_container or main_container)
        transaction.replace(R.id.fragment_container, fragmentSchedule)

        // Clear the back stack to prevent returning to the appointment creation screen
        transaction.addToBackStack(null)

        // Commit the transaction
        transaction.commitAllowingStateLoss()
    }

    fun setOnConfirmListener(listener: () -> Unit) {
        onConfirmListener = listener
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val ARG_DATE = "appointment_date" // New constant for date

        @JvmStatic
        fun newInstance(param1: String, param2: String, date: Date) =
            ConfirmationDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putSerializable(ARG_DATE, date) // Pass the date as a Serializable
                }
            }
    }
}