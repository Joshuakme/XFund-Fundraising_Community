package com.example.xfund.screens.navigation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.example.xfund.R
import com.example.xfund.adapter.ImageSliderAdapter
import com.example.xfund.databinding.FragmentHomeBinding
import com.example.xfund.model.NewsModel
import com.example.xfund.util.FirebaseHelper
import com.example.xfund.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), ImageSliderAdapter.OnItemClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var newsList: List<NewsModel>
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var currentUserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container,false)

        // ELEMENTS IN FRAGMENT
        sharedPreferences =
            requireActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        // User Element
        currentUserViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]



        // SharedPreference - Login
        val isFirstTime = sharedPreferences?.getBoolean("IsFirstTime", true)
        val editor = sharedPreferences?.edit()


        if (isFirstTime == true) {
            // set login status to false if first time use app
            editor?.putBoolean("IsFirstTime", false)
            editor?.apply()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ELEMENTS IN FRAGMENT
        // Image Slider Elements
        val newsViewPager: ViewPager = binding.NewsImageSlider
        val dotsIndicator = binding.DotsIndicator

        // Image Slider
        // Create a list of NewsModel objects
        newsList = initNewsList()

        val adapter = ImageSliderAdapter(requireContext(), newsList)
        adapter.setOnItemClickListener(this) // Set the click listener
        newsViewPager.adapter = adapter
        dotsIndicator.attachTo(newsViewPager)


        // Welcome Text
        // Displaying User detail
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            binding.WelcomeUserTxt.text = "Hi, " + user.displayName

        } else {
            // User is not signed in
            binding.WelcomeUserTxt.text = "Welcome"
        }

    }

    override fun onItemClick(position: Int) {
        val url = newsList[position].newsUrl // Replace with the URL you want to open
        openWebPage(url)
    }

    private fun openWebPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)

        if(URLUtil.isValidUrl(url)) {
            startActivity(intent)
        } else {
            // Handle the case of an invalid URL
            Toast.makeText(requireContext(), "Invalid URL.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initNewsList():List<NewsModel> {
        return listOf(
            NewsModel("Minister: Drone fundraising campaign raises \$1 million within hourss",
                "Ukraine's \"Operation Unity\" drone fundraising campaign, led by Digital Transformation Minister Mykhailo Fedorov, achieved an impressive milestone by raising over \$1 million within hours of its launch on August 14th. The campaign aims to gather a total of \$6.36 million to procure 10,000 drones for the Ukrainian armed forces, with attractive prizes for donors, including platinum cards and meetings with campaign ambassadors.",
                "https://firebasestorage.googleapis.com/v0/b/xfund-89b19.appspot.com/o/news%2Fnews1.jpg?alt=media&token=bc2ff48c-8f75-4736-b833-21e810008a98",
                "https://news.yahoo.com/minister-drone-fundraising-campaign-raises-143115749.html"),
            NewsModel("Man raises money for Wildfish with seven marathons",
                "Man raises money for Wildfish with seven marathons",
                "https://ichef.bbci.co.uk/news/976/cpsprodpb/834C/production/_131221633_capture.jpg2.jpg.webp",
                "https://www.bbc.com/news/uk-england-bristol-66881357"),
            NewsModel("title 3", "desc 3", "https://firebasestorage.googleapis.com/v0/b/xfund-89b19.appspot.com/o/news%2Fnews1.jpg?alt=media&token=bc2ff48c-8f75-4736-b833-21e810008a98", ""),
            NewsModel("title 4", "desc 4", "https://firebasestorage.googleapis.com/v0/b/xfund-89b19.appspot.com/o/news%2Fnews1.jpg?alt=media&token=bc2ff48c-8f75-4736-b833-21e810008a98", ""))
    }
}