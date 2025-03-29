package com.example.fetchchill

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Handle arguments if needed
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

        // Get current user ID
        val userId = AuthManager.getUserId()

        // Call the API to get appointments for the user
        RetrofitClient.apiService.getAppointmentsForUser (userId).enqueue(object :
            Callback<List<Appointment>> {
            override fun onResponse(call: Call<List<Appointment>>, response: Response<List<Appointment>>) {
                binding.progressBarAppointments.visibility = View.GONE
                if (response.isSuccessful) {
                    val appointments = response.body() ?: emptyList()
                    scheduleAdapter.updateAppointments(appointments)
                } else {
                    // Handle error response
                    binding.progressBarAppointments.visibility = View.GONE
                    // Show error message (e.g., Toast or Snackbar)
                }
            }

            override fun onFailure(call: Call<List<Appointment>>, t: Throwable) {
                binding.progressBarAppointments.visibility = View.GONE
                // Handle failure (e.g., show a Toast message)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainPage)?.disableFrames()
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
        fun newInstance(param1: String, param2: String) =
            FragmentSchedule().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}