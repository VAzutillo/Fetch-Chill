package com.example.fetchchill

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fetchchill.adapter.RecyclerViewScheduleAdapter
import com.example.fetchchill.databinding.FragmentScheduleBinding
import com.example.fetchchill.model.Appointment
import com.example.fetchchill.api.RetrofitClient
import com.example.fetchchill.utils.AuthManager
import com.example.fetchchill.view.MainPage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentSchedule.newInstance] factory method to
 * create an instance of this fragment.
 */

class FragmentSchedule : Fragment() {
    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!
    private lateinit var scheduleAdapter: RecyclerViewScheduleAdapter
    private var isAppointmentJustCreated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isAppointmentJustCreated = it.getBoolean("appointment_created", false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadAppointments()

        // Show a message if coming from appointment creation
//        if (isAppointmentJustCreated) {
//            Toast.makeText(requireContext(), "Your appointment has been scheduled successfully!", Toast.LENGTH_LONG).show()
//        }
    }

    private fun setupRecyclerView() {
        scheduleAdapter = RecyclerViewScheduleAdapter(emptyList())
        binding.recyclerViewAppointments.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = scheduleAdapter
        }
    }

    private fun loadAppointments() {
        binding.progressBarAppointments.visibility = View.VISIBLE
        val userId = AuthManager.getUserId()

        RetrofitClient.apiService.getAppointmentsForUser(userId).enqueue(object :
            Callback<List<Appointment>> {
            override fun onResponse(call: Call<List<Appointment>>, response: Response<List<Appointment>>) {
                binding.progressBarAppointments.visibility = View.GONE
                if (response.isSuccessful) {
                    val appointments = response.body() ?: emptyList()
                    val sortedAppointments = sortAppointments(appointments) // Use the sorting function
                    if (sortedAppointments.isEmpty()) {
                        Toast.makeText(requireContext(), "You don't have any appointments yet", Toast.LENGTH_SHORT).show()
                    }
                    scheduleAdapter.updateAppointments(sortedAppointments)
                } else {
                    Toast.makeText(requireContext(), "Failed to load appointments: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Appointment>>, t: Throwable) {
                binding.progressBarAppointments.visibility = View.GONE
                Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Add the sorting function here
    private fun sortAppointments(appointments: List<Appointment>): List<Appointment> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        return appointments.sortedWith(compareByDescending<Appointment> {
            dateFormat.parse(it.appointment_date ?: "") ?: Date(0)
        }.thenByDescending {
            timeFormat.parse(it.appointment_time ?: "00:00") ?: Date(0)
        })
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainPage)?.disableFrames()

        // Refresh appointments when returning to this fragment
        loadAppointments()
    }

    override fun onPause() {
        super.onPause()
        (activity as? MainPage)?.enableFrames()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(appointmentCreated: Boolean = false) =
            FragmentSchedule().apply {
                arguments = Bundle().apply {
                    putBoolean("appointment_created", appointmentCreated)
                }
            }
    }
}