package com.example.fetchchill.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fetchchill.R
import com.example.fetchchill.model.Appointment
import java.text.SimpleDateFormat
import java.util.*

class RecyclerViewScheduleAdapter(
    private var appointments: List<Appointment>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private companion object {
        const val TYPE_EMPTY = 0
        const val TYPE_APPOINTMENT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (appointments.isEmpty()) TYPE_EMPTY else TYPE_APPOINTMENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_APPOINTMENT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_schedule, parent, false)
                AppointmentViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_empty_schedule, parent, false)
                EmptyViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AppointmentViewHolder) {
            val appointment = appointments[position]
            holder.bind(appointment)
        }
    }

    override fun getItemCount(): Int {
        return if (appointments.isEmpty()) 1 else appointments.size
    }

    fun updateAppointments(newAppointments: List<Appointment>) {
        appointments = newAppointments
        notifyDataSetChanged()
    }

    inner class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDay: TextView = itemView.findViewById(R.id.tvDay)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvAppointmentStatus: TextView = itemView.findViewById(R.id.tvAppointmentStatus)
        private val tvAppointmentService: TextView = itemView.findViewById(R.id.tvAppointmentService)
        private val imgAppointmentService: ImageView = itemView.findViewById(R.id.imgAppointmentService)

        fun bind(appointment: Appointment) {
            // Handle null values with default text
            tvDay.text = appointment.day ?: getDefaultDayText(appointment.appointment_date)
            tvTime.text = formatTime(appointment.appointment_time ?: "")
            tvDate.text = formatDate(appointment.appointment_date ?: "")

            // Handle status with null safety
            val status = appointment.status?.lowercase(Locale.getDefault()) ?: "pending"
            tvAppointmentStatus.text = status.replaceFirstChar { it.uppercase() }

            // Set status color
            when (status) {
                "pending" -> tvAppointmentStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.orange))
                "confirmed" -> tvAppointmentStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
                "cancelled" -> tvAppointmentStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
                else -> tvAppointmentStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            }

            // Get service type from service_type field (not service field)
            val serviceType = appointment.service_type?.lowercase(Locale.getDefault()) ?: "unknown"
            tvAppointmentService.text = when (serviceType) {
                "grooming" -> "Grooming"
                "vaccination" -> "Vaccination"
                "checkup" -> "Checkup"
                "training" -> "Training"
                else -> serviceType.replaceFirstChar { it.uppercase() }
            }

            // Set appropriate image based on service type
            val serviceImage = when (serviceType) {
                "grooming" -> R.drawable.groomingimg
                "vaccination" -> R.drawable.vaccineimg
                "checkup" -> R.drawable.checkupimg
                "training" -> R.drawable.trainingimg
                else -> R.drawable.error
            }
            imgAppointmentService.setImageResource(serviceImage)
        }

        private fun getDefaultDayText(date: String?): String {
            return if (date.isNullOrEmpty()) "Today at" else {
                // You can add logic to determine "Today"/"Tomorrow" based on date
                "Today at"
            }
        }

        private fun formatTime(timeString: String): String {
            return try {
                if (timeString.length >= 5) {
                    val timeParts = timeString.split(":")
                    val hour = timeParts[0].toInt()
                    val minute = timeParts[1]
                    val period = if (hour >= 12) "AM" else "PM"
                    val displayHour = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
                    "$displayHour:$minute $period"
                } else {
                    timeString
                }
            } catch (e: Exception) {
                timeString
            }
        }

        private fun formatDate(dateString: String): String {
            return try {
                if (dateString == "0000-00-00") return "Date not set"
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)?.let {
                    SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(it)
                } ?: dateString
            } catch (e: Exception) {
                dateString
            }
        }
    }

    inner class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}