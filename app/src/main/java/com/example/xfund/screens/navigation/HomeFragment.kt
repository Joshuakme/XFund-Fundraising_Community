package com.example.xfund.screens.navigation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.xfund.R
import com.example.xfund.adapter.ImageSliderAdapter
import com.example.xfund.databinding.FragmentHomeBinding
import com.example.xfund.model.NewsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date


class HomeFragment : Fragment(), ImageSliderAdapter.OnItemClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var databaseReference: DatabaseReference
    private val db = FirebaseFirestore.getInstance()
    private lateinit var newsList: List<NewsModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )

        // ELEMENTS IN FRAGMENT
        // Search Elements
        val searchBar : SearchView = binding.SearchBar
        val txtSearch = searchBar.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        val searchIcon: ImageView = searchBar.findViewById(androidx.appcompat.R.id.search_mag_icon)
        // Image Slider Elements
        val newsViewPager: ViewPager = binding.NewsImageSlider
        val dotsIndicator = binding.DotsIndicator
        databaseReference = FirebaseDatabase.getInstance().getReference("news") // Initialize Firebase and reference to your news items node
        databaseReference.child("news").get()


        // Search Bar
        searchIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray_300),android.graphics.PorterDuff.Mode.SRC_IN)
        txtSearch.setHint(R.string.search_query_hint)
        txtSearch.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.gray_300))
        //txtSearch.textSize = R.dimen.search_txt_size.toFloat()

        // Image Slider
        // Create a list of NewsModel objects
        newsList = listOf(
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

        //val adapter = ImageSliderAdapter(requireContext(), emptyList())
        val adapter = ImageSliderAdapter(requireContext(), newsList)
        adapter.setOnItemClickListener(this) // Set the click listener
        newsViewPager.adapter = adapter
        dotsIndicator.attachTo(newsViewPager)


        //val newsDB = db.collection("news")


        // Set up a ValueEventListener to listen for changes in the database
//        databaseReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val newsList = mutableListOf<NewsModel>()
//
//                for (postSnapshot in dataSnapshot.children) {
//                    val newsItem = postSnapshot.getValue(NewsModel::class.java)
//                    newsItem?.let { newsList.add(it) }
//
//                    if (newsItem != null) {
//                        Log.d("News Title: ", newsItem.title)
//                    }
//                    if (newsItem != null) {
//                        Log.d("News Desc: ", newsItem.description)
//                    }
//                    if (newsItem != null) {
//                        Log.d("News ImgUrl: ", newsItem.imageUrl)
//                    }
//                    if (newsItem != null) {
//                        Log.d("News NewsUrl: ", newsItem.newsUrl)
//                    }
//                }
//
//                // Update the adapter with the new data
//                imageSliderAdapter.updateData(newsList)
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle error
//                // Getting Post failed, log a message
//                Log.w("loadPost:onCancelled", databaseError.toException())
//            }
//        })

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onItemClick(position: Int) {
        val url = newsList[position].newsUrl // Replace with the URL you want to open
        openWebPage(url)
    }

    private fun openWebPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            // Handle the case where a web browser app is not available
            Toast.makeText(requireContext(), "Web browser not found.", Toast.LENGTH_SHORT).show()
        }
    }
}