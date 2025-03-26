package com.example.fetchchill

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.fetchchill.utils.AuthManager
import okhttp3.*
import org.json.JSONObject
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

    private lateinit var authManager: AuthManager
    private val client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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

        // Initialize AuthManager using the companion object
        authManager = AuthManager(requireContext().getSharedPreferences("MyAppPrefs", android.content.Context.MODE_PRIVATE))

        // Fetch and display profile data
        fetchProfileData()

        // Set click listeners
        editFrame.setOnClickListener { replaceFragment(FragmentEditProfile()) }
        accInfoFrame.setOnClickListener { replaceFragment(FragmentAccountInformation()) }
        faqFrame.setOnClickListener { replaceFragment(FragmentFaq()) }
        aboutUsFrame.setOnClickListener { replaceFragment(FragmentAboutUs()) }
        logoutFrame.setOnClickListener { logout() }

        return view
    }

    override fun onResume() {
        super.onResume()
        fetchProfileData() // Refresh data when returning to this fragment
        (activity as? MainPage)?.disableFrames()
    }

    override fun onPause() {
        super.onPause()
        (activity as? MainPage)?.enableFrames()
    }

    private fun fetchProfileData() {
        val userId = AuthManager.getUserId() // Use the static method from companion object
        Log.d("FragmentSetting", "Retrieved user ID: $userId")

        if (userId == -1) {
            Log.e("FragmentSetting", "Invalid user ID, redirecting to login")
            navigateToLogin()
            return
        }

        val url = "http://192.168.100.18/fetch_chill/getProfile.php?id=$userId"
        Log.d("FragmentSetting", "Fetching profile from: $url")

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    Log.e("FragmentSetting", "Network error", e)
                    Toast.makeText(requireContext(), "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        activity?.runOnUiThread {
                            Toast.makeText(requireContext(), "Server error: ${response.code}", Toast.LENGTH_SHORT).show()
                        }
                        return
                    }

                    try {
                        val responseBody = response.body?.string()
                        Log.d("FragmentSetting", "API Response: $responseBody")

                        val json = JSONObject(responseBody!!)
                        if (json.getBoolean("success")) {
                            val profile = json.getJSONObject("profile")
                            activity?.runOnUiThread {
                                updateProfileUI(
                                    ownerName = profile.getString("owner_name"),
                                    petName = profile.getString("pet_name"),
                                    breed = profile.getString("breed"),
                                    age = profile.getInt("age"),
                                    imageUrl = profile.optString("profile_image", "")
                                )
                            }
                        } else {
                            val errorMsg = json.getString("message")
                            activity?.runOnUiThread {
                                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("FragmentSetting", "JSON parsing error", e)
                        activity?.runOnUiThread {
                            Toast.makeText(requireContext(), "Data parsing error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }

    private fun updateProfileUI(ownerName: String, petName: String, breed: String, age: Int, imageUrl: String) {
        tvOwnerName.text = ownerName
        tvPetName.text = petName
        tvBreed.text = breed
        tvAge.text = age.toString()

        if (imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(imgProfile)
        } else {
            imgProfile.setImageResource(R.drawable.placeholder)
        }
    }

    private fun logout() {
        // Use networkLogout to ensure server-side logout
        authManager.networkLogout(
            onSuccess = {
                // Navigate to login screen on successful logout
                Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
                navigateToLogin()
            },
            onError = { errorMessage ->
                // Optionally handle logout error
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()

                // Even if network logout fails, still clear local auth state
                authManager.logout()
                navigateToLogin()
            }
        )
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}