//package com.example.fetchchill.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.fetchchill.R
//import com.example.fetchchill.model.Appointment
//
//class AppointmentAdapter(private val appointments: List<Appointment>) :
//    RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {
//
//    class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvAppointmentDetails: TextView = itemView.findViewById(R.id.tvAppointmentDetails)
//        val tvAppointmentDate: TextView = itemView.findViewById(R.id.tvAppointmentDate)
//        val tvAppointmentService: TextView = itemView.findViewById(R.id.tvAppointmentService)
//        val imgAppointmentService: ImageView = itemView.findViewById(R.id.imgAppointmentService)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_schedule, parent, false)
//        return AppointmentViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
//        val appointment = appointments[position]
//
//        // Set appointment time
//        holder.tvAppointmentDetails.text = appointment.appointment_time
//
//        // Set appointment date
//        holder.tvAppointmentDate.text = appointment.appointment_date
//
//        // Set service type
//        holder.tvAppointmentService.text = appointment.service_type
//
//        // Set service image based on service type (you can expand this)
//        when (appointment.service_type.lowercase()) {
//            "grooming" -> holder.imgAppointmentService.setImageResource(R.drawable.vaccineimg)
//            // Add more service types as needed
//            else -> holder.imgAppointmentService.setImageResource(R.drawable.vaccineimg)
//        }
//    }
//
//    override fun getItemCount() = appointments.size
//}