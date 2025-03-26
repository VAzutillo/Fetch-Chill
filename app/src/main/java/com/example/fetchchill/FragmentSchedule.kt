//package com.example.fetchchill
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ProgressBar
//import android.widget.TextView
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.fetchchill.adapter.AppointmentAdapter
//import com.example.fetchchill.view.MainPage
//import com.example.fetchchill.viewmodel.AppointmentViewModel
//
//class FragmentSchedule : Fragment() {
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var progressBar: ProgressBar
//    private lateinit var tvError: TextView
//    private lateinit var appointmentAdapter: AppointmentAdapter
//    private lateinit var appointmentViewModel: AppointmentViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        // Initialize ViewModel
//        appointmentViewModel = AppointmentViewModel()
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_schedule, container, false)
//
//        // Find views
//        recyclerView = view.findViewById(R.id.recyclerViewAppointments)
//        progressBar = view.findViewById(R.id.progressBarAppointments)
//        tvError = view.findViewById(R.id.tvAppointmentError)
//
//        // Set up RecyclerView
//        recyclerView.layoutManager = LinearLayoutManager(context)
//
//        // Observe appointments
//        appointmentViewModel.appointments.observe(viewLifecycleOwner) { appointments ->
//            // Update RecyclerView with appointments
//            appointmentAdapter = AppointmentAdapter(appointments)
//            recyclerView.adapter = appointmentAdapter
//
//            // Show/hide RecyclerView based on appointments
//            recyclerView.visibility = if (appointments.isNotEmpty()) View.VISIBLE else View.GONE
//            tvError.visibility = if (appointments.isEmpty()) View.VISIBLE else View.GONE
//            tvError.text = if (appointments.isEmpty()) "No appointments found" else ""
//        }
//
//        // Observe loading state
//        appointmentViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
//            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//            recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
//        }
//
//        // Observe error state
//        appointmentViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
//            errorMessage?.let {
//                tvError.visibility = View.VISIBLE
//                tvError.text = it
//                recyclerView.visibility = View.GONE
//            }
//        }
//
//        return view
//    }
//
//    override fun onResume() {
//        super.onResume()
//        // Disable frames when this fragment is visible
//        (activity as? MainPage)?.disableFrames()
//
//        // Fetch appointments when the fragment resumes
//        appointmentViewModel.fetchAppointments()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        // Re-enable frames when navigating away from this fragment
//        (activity as? MainPage)?.enableFrames()
//    }
//
//    companion object {
//        @JvmStatic
//        fun newInstance() = FragmentSchedule()
//    }
//}