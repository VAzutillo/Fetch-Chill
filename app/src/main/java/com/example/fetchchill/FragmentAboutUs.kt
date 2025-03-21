package com.example.fetchchill

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.fetchchill.view.MainPage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [FragmentAboutUs.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentAboutUs : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_about_us, container, false)


        // Find the back button ImageView
        val backButton: ImageView = view.findViewById(R.id.backToSetting)

        // Set an OnClickListener on the back button
        backButton.setOnClickListener {
            // Pop the current fragment from the back stack
            requireActivity().supportFragmentManager.popBackStack()
        }
        return view

    }
    override fun onResume() {
        super.onResume()
        // Disable frames when this fragment is visible
        (activity as? MainPage)?.disableFrames()
    }

    override fun onPause() {
        super.onPause()
        // Re-enable frames when navigating away from this fragment
        (activity as? MainPage)?.enableFrames()
    }



    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentAboutUs.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentAboutUs().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}