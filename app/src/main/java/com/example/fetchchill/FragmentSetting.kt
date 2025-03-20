package com.example.fetchchill

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.fetchchill.view.MainActivity
import com.example.fetchchill.view.MainPage
import com.example.fetchchill.view.fragments.FragmentEditProfile
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException

class FragmentSetting : Fragment() {

    private lateinit var editFrame: FrameLayout
    private lateinit var accInfoFrame: FrameLayout
    private lateinit var faqFrame: FrameLayout
    private lateinit var aboutUsFrame: FrameLayout
    private lateinit var logoutFrame: FrameLayout

    private lateinit var imgProfile: ImageView
    private lateinit var tvOwnerName: TextView
    private lateinit var tvPetName: TextView
    private lateinit var tvBreed: TextView
    private lateinit var tvAge: TextView
    private lateinit var sharedPref: SharedPreferences

    private val client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        // Initialize UI components
        editFrame = view.findViewById(R.id.EditFrame)
        accInfoFrame = view.findViewById(R.id.AccInfoFrame)
        faqFrame = view.findViewById(R.id.FAQFrame)
        aboutUsFrame = view.findViewById(R.id.AboutUsFrame)
        logoutFrame = view.findViewById(R.id.LogoutFrame)
        imgProfile = view.findViewById(R.id.imgProfile)
        tvOwnerName = view.findViewById(R.id.tvOwnerName)
        tvPetName = view.findViewById(R.id.tvPetName)
        tvBreed = view.findViewById(R.id.tvBreed)
        tvAge = view.findViewById(R.id.tvAge)

        // Initialize SharedPreferences
        sharedPref = requireActivity().getSharedPreferences("User  Profile", Activity.MODE_PRIVATE)

        // Load and display profile data
        loadProfileData()

        // Set click listeners for frames
        editFrame.setOnClickListener { replaceFragment(FragmentEditProfile()) }
        accInfoFrame.setOnClickListener { replaceFragment(FragmentAccountInformation()) }
        faqFrame.setOnClickListener { replaceFragment(FragmentFaq()) }
        aboutUsFrame.setOnClickListener { replaceFragment(FragmentAboutUs()) }
        logoutFrame.setOnClickListener { logout() }

        return view
    }

    override fun onResume() {
        super.onResume()
        loadProfileData() // Reload profile data when returning to the settings screen
        (activity as? MainPage)?.disableFrames() // Disable frames when this fragment is visible
    }

    override fun onPause() {
        super.onPause()
        (activity as? MainPage)?.enableFrames() // Re-enable frames when navigating away from this fragment
    }

    private fun loadProfileData() {
        val ownerName = sharedPref.getString("owner_name", "N/A") ?: "N/A"
        val petName = sharedPref.getString("pet_name", "N/A") ?: "N/A"
        val breed = sharedPref.getString("breed", "N/A") ?: "N/A"
        val age = sharedPref.getInt("age", 0)
        val imageUrl = sharedPref.getString("image_url", "") ?: ""

        // Update TextViews with profile data
        tvOwnerName.text = ownerName
        tvPetName.text = petName
        tvBreed.text = breed
        tvAge.text = if (age > 0) age.toString() else "N/A"

        // Load profile image using Glide
        if (imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder) // Placeholder while loading
                .error(R.drawable.error) // Error image if loading fails
                .into(imgProfile)
        } else {
            imgProfile.setImageResource(R.drawable.placeholder) // Set a placeholder if no image URL
        }
    }

    private fun logout() {
        // Create the logout request
        val request = Request.Builder()
            .url("http://192.168.100.18/fetch_chill/logout.php") // Replace with your actual logout URL
            .post(RequestBody.create("application/json".toMediaType(), "{}")) // Sending an empty JSON body
            .build()

        // Execute the request
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), "Logout failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                activity?.runOnUiThread {
                    if (response.isSuccessful) {
                        // Clear SharedPreferences
                        sharedPref.edit().clear().apply()

                        // Show logout success message
                        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()

                        // Navigate to MainActivity and clear the activity stack
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        requireActivity().finish() // Finish the current activity
                    } else {
                        Toast.makeText(requireContext(), "Logout failed: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container, fragment)
            ?.addToBackStack(null)
            ?.commit()
    }
}