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
import androidx.viewpager.widget.ViewPager
import com.example.xfund.R
import com.example.xfund.adapter.ImageSliderAdapter
import com.example.xfund.databinding.FragmentHomeBinding
import com.example.xfund.model.NewsModel
import com.example.xfund.viewModel.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

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

        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav?.visibility = View.VISIBLE

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
            if(user.displayName == "") {
                binding.WelcomeUserTxt.text = "Hi, " + "Anonymous"
            } else {
                binding.WelcomeUserTxt.text = "Hi, " + user.displayName
            }

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
            NewsModel("Join this Fun Run to raise funds for special-needs kids",
                "A charity fun run organized by Hua Xia International School in December aims to raise funds for the Orlin Special Needs School, supporting special-needs children. ",
                "https://media.freemalaysiatoday.com/wp-content/uploads/2023/09/79fd7c7a-kids.jpg",
                "https://www.freemalaysiatoday.com/category/leisure/2023/10/01/join-this-fun-run-to-raise-funds-for-special-needs-kids/"),
            NewsModel("S’pore charity to raise funds for Libya flood victims",
                "Local charity Rahmatan Lil Alamin Foundation (RLAF), in collaboration with the United Nations High Commissioner for Refugees (UNHCR), is calling for donations to support the needs of those affected by the floods in Libya.",
                "https://static1.straitstimes.com.sg/s3fs-public/styles/large30x20/public/articles/2023/10/02/dw-derna-231002.jpg",
                "https://www.straitstimes.com/singapore/s-pore-charity-to-raise-funds-for-libya-flood-victims"))
    }
}