package com.example.fetchchill.view.fragments

import ProfileResponse
import ProfileSaveResponse
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.fetchchill.R
import com.example.fetchchill.api.ApiService
import com.example.fetchchill.utils.AuthManager
import com.google.android.material.button.MaterialButton
import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class FragmentEditProfile : Fragment() {
    private lateinit var etOwner: EditText
    private lateinit var etPetName: EditText
    private lateinit var etBreed: EditText
    private lateinit var etAge: EditText
    private lateinit var btnSave: MaterialButton
    private lateinit var backToSetting: ImageView
    private lateinit var imgProfile: ImageView
    private var currentImagePath: String? = null
    private lateinit var apiService: ApiService
    private lateinit var sharedPref: SharedPreferences
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        // Initialize UI components
        etOwner = view.findViewById(R.id.etOwner)
        etPetName = view.findViewById(R.id.etPetName)
        etBreed = view.findViewById(R.id.etBreed)
        etAge = view.findViewById(R.id.etAge)
        btnSave = view.findViewById(R.id.btnSave)
        imgProfile = view.findViewById(R.id.imgProfile)
        backToSetting = view.findViewById(R.id.backToSetting)

        // Initialize SharedPreferences
        sharedPref = requireActivity().getSharedPreferences("MyAppPrefs", Activity.MODE_PRIVATE)

        // Retrieve userId
        val authManager = AuthManager(sharedPref)
        userId = authManager.userId()
        Log.d("FragmentEditProfile", "Retrieved userId: $userId")

        if (userId == -1) {
            Toast.makeText(requireContext(), "User ID not found. Please log in again.", Toast.LENGTH_LONG).show()
            requireActivity().supportFragmentManager.popBackStack()
            return view
        }

        // Initialize Retrofit
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.18/fetch_chill/") // Base URL for your API
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        apiService = retrofit.create(ApiService::class.java)

        // Load profile data
        loadProfileData()

        // Set click listeners
        imgProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Add click listener for backToSetting
        backToSetting.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        btnSave.setOnClickListener {
            val ownerName = etOwner.text.toString().trim()
            val petName = etPetName.text.toString().trim()
            val breed = etBreed.text.toString().trim()
            val age = etAge.text.toString().trim()

            if (ownerName.isEmpty() || petName.isEmpty() || breed.isEmpty() || age.isEmpty()) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveProfile(ownerName, petName, breed, age, currentImagePath)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            imageUri?.let {
                currentImagePath = getImageFilePath(it)
                Glide.with(this)
                    .load(it)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(imgProfile)
            }
        }
    }

    private fun getImageFilePath(uri: Uri): String {
        val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
        val file = File(requireContext().cacheDir, "selected_image.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file.absolutePath
    }

    private fun saveProfile(ownerName: String, petName: String, breed: String, age: String, imagePath: String?) {
        // Log the data being sent
        Log.d("FragmentEditProfile", "Saving profile with: ownerName=$ownerName, petName=$petName, breed=$breed, age=$age, imagePath=$imagePath, userId=$userId")

        // Create RequestBody instances for the text fields - FIXED ORDER TO MATCH API INTERFACE
        val userIdBody = RequestBody.create("text/plain".toMediaTypeOrNull(), userId.toString())
        val ownerNameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), ownerName)
        val petNameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), petName)
        val breedBody = RequestBody.create("text/plain".toMediaTypeOrNull(), breed)
        val ageBody = RequestBody.create("text/plain".toMediaTypeOrNull(), age)

        // Prepare the image part if an image path is provided
        val imagePart = imagePath?.let {
            val file = File(it)
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData("image", file.name, requestFile)
        }

        // Show a loading message
        Toast.makeText(requireContext(), "Saving profile...", Toast.LENGTH_SHORT).show()

        // Call the API to save the profile - FIXED ORDER TO MATCH API INTERFACE
        val call = apiService.saveProfile(userIdBody, ownerNameBody, petNameBody, breedBody, ageBody, imagePart)

        // Add detailed debugging logs
        Log.d("API_DEBUG", "Making API call with userId: $userId")

        call.enqueue(object : Callback<ProfileSaveResponse> {
            override fun onResponse(call: Call<ProfileSaveResponse>, response: Response<ProfileSaveResponse>) {
                val responseCode = response.code()
                Log.d("API_DEBUG", "Response code: $responseCode")

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("API_DEBUG", "Response successful: ${responseBody?.message}")

                    if (responseBody?.success == true) {
                        // Important: Immediately load the newly uploaded image
                        loadProfileImage(responseBody.image_url)

                        // Save to SharedPreferences
                        saveToSharedPreferences(ownerName, petName, breed, age, responseBody.image_url)

                        Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        requireActivity().supportFragmentManager.popBackStack()
                    } else {
                        val errorMsg = responseBody?.message ?: "Unknown error"
                        Log.e("API Error", errorMsg)
                        Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("API Error", "Response not successful: $errorBody")
                    Toast.makeText(requireContext(), "Failed to save profile: Response code $responseCode", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfileSaveResponse>, t: Throwable) {
                Log.e("API Error", "Request failed: ${t.message}")
                t.printStackTrace() // Print full stack trace
                Toast.makeText(requireContext(), "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadProfileData() {
        // First try to load from SharedPreferences
        populateFieldsFromSharedPref()

        // Then try to load from API
        if (userId > 0) {
            val call = apiService.getProfile(userId)
            call.enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                    if (response.isSuccessful && response.body()?.status == "success") {
                        val profile = response.body()?.profile
                        if (profile != null) {
                            // Update UI with profile data
                            etOwner.setText(profile.owner_name)
                            etPetName.setText(profile.pet_name)
                            etBreed.setText(profile.breed)
                            etAge.setText(profile.age.toString())

                            // Prefer image_url if available, fallback to profile_image
                            val imageToLoad = profile.image_url ?: profile.profile_image

                            // Load profile image with error handling
                            loadProfileImage(imageToLoad)

                            // Save to SharedPreferences for next time
                            saveToSharedPreferences(
                                profile.owner_name,
                                profile.pet_name,
                                profile.breed,
                                profile.age.toString(),
                                imageToLoad
                            )
                        }
                    } else {
                        Log.w("API Warning", "Failed to load profile from API: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Log.e("API Error", "Failed to load profile: ${t.message}")
                }
            })
        }
    }

    private fun loadProfileImage(imageUrl: String?) {
        imageUrl?.let { url ->
            Glide.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .fallback(R.drawable.placeholder)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        imgProfile.setImageDrawable(resource)
                        Log.d("Image Loading", "Image loaded successfully: $url")
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // This is called when the image loading is cancelled or cleared
                        imgProfile.setImageDrawable(placeholder)
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        Log.e("Image Loading", "Failed to load image: $url")
                        imgProfile.setImageDrawable(errorDrawable ?: resources.getDrawable(R.drawable.error, null))
                    }
                })
        } ?: run {
            // If no image URL is provided, set to placeholder
            imgProfile.setImageResource(R.drawable.placeholder)
        }
    }

    private fun populateFieldsFromSharedPref() {
        // Load data from SharedPreferences
        val ownerName = sharedPref.getString("owner_name", "") ?: ""
        val petName = sharedPref.getString("pet_name", "") ?: ""
        val breed = sharedPref.getString("breed", "") ?: ""
        val age = sharedPref.getInt("age", 0)
        val imageUrl = sharedPref.getString("image_url", "") ?: ""

        // Only populate if we have data
        if (ownerName.isNotEmpty()) etOwner.setText(ownerName)
        if (petName.isNotEmpty()) etPetName.setText(petName)
        if (breed.isNotEmpty()) etBreed.setText(breed)
        if (age > 0) etAge.setText(age.toString())

        // Load profile image with improved method
        loadProfileImage(imageUrl)
    }

    private fun saveToSharedPreferences(ownerName: String, petName: String, breed: String, age: String, imageUrl: String?) {
        with(sharedPref.edit()) {
            putString("owner_name", ownerName)
            putString("pet_name", petName)
            putString("breed", breed)
            putInt("age", age.toIntOrNull() ?: 0)
            putString("image_url", imageUrl)
            apply()
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}