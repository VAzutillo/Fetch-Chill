package com.example.fetchchill.view.fragments

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
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.fetchchill.R
import com.example.fetchchill.api.ApiService
import com.example.fetchchill.model.ProfileSaveResponse
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
        sharedPref = requireActivity().getSharedPreferences("User  Profile", Activity.MODE_PRIVATE)

        backToSetting.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // Initialize Retrofit
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.18/fetch_chill/") // Replace with your actual server IP
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        apiService = retrofit.create(ApiService::class.java)

        // Load existing profile data
        loadProfileData()

        imgProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
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
                Glide.with(this).load(it).into(imgProfile)
            }
        }
    }

    private fun getImageFilePath(uri: Uri): String {
        val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
        val file = File(requireContext().cacheDir, "selected_image.jpg")
        val outputStream =

            FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file.absolutePath
    }

    private fun saveProfile(ownerName: String, petName: String, breed: String, age: String, imagePath: String?) {
        val ownerNameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), ownerName)
        val petNameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), petName)
        val breedBody = RequestBody.create("text/plain".toMediaTypeOrNull(), breed)
        val ageBody = RequestBody.create("text/plain".toMediaTypeOrNull(), age)

        val imagePart = imagePath?.let {
            val file = File(it)
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData("profile_image", file.name, requestFile)
        }

        val call = apiService.saveProfile(ownerNameBody, petNameBody, breedBody, ageBody, imagePart)
        call.enqueue(object : Callback<ProfileSaveResponse> {
            override fun onResponse(call: Call<ProfileSaveResponse>, response: Response<ProfileSaveResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()

                    // Save to SharedPreferences
                    saveToSharedPreferences(ownerName, petName, breed, age, response.body()?.image_url)

                    // Optionally, navigate back or refresh UI
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    Log.e("API Error", "Failed response: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfileSaveResponse>, t: Throwable) {
                Log.e("API Error", "Request failed: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadProfileData() {
        // Retrieve profile data from SharedPreferences
        val ownerName = sharedPref.getString("owner_name", "N/A") ?: "N/A"
        val petName = sharedPref.getString("pet_name", "N/A") ?: "N/A"
        val breed = sharedPref.getString("breed", "N/A") ?: "N/A"
        val age = sharedPref.getInt("age", 0)
        val imageUrl = sharedPref.getString("image_url", "") ?: ""

        // Populate EditText fields with profile data
        etOwner.setText(ownerName)
        etPetName.setText(petName)
        etBreed.setText(breed)
        etAge.setText(if (age > 0) age.toString() else "N/A") // Display "N/A" if age is 0

        // Load profile image using Glide
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder) // Placeholder while loading
                .error(R.drawable.error) // Error image if loading fails
                .into(imgProfile)
        } else {
            imgProfile.setImageResource(R.drawable.placeholder) // Set a default placeholder if no image URL is available
        }
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