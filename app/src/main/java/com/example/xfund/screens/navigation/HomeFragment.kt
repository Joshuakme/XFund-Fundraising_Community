package com.example.xfund.screens.navigation

import android.app.ActionBar
import android.content.AbstractThreadedSyncAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import com.example.xfund.News
import com.example.xfund.R
import com.example.xfund.adapter.NewsSliderAdapter
import com.example.xfund.databinding.FragmentHomeBinding
import com.example.xfund.model.NewsSliderItem


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewPager: ViewPager
    private lateinit var newsArrayList: ArrayList<NewsSliderItem>
    private lateinit var newsAdapter: NewsSliderAdapter

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

        viewPager = binding.NewsViewPager

        // Search Bar Style Configuration
        searchIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray_300),android.graphics.PorterDuff.Mode.SRC_IN)
        txtSearch.setHint(R.string.search_query_hint)
        txtSearch.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.gray_300))
        //txtSearch.textSize = R.dimen.search_txt_size.toFloat()

        // Load News ViewPage
        //loadNews()


        // EVENT LISTENERS
        // Add page Change Listener
        viewPager.addOnPageChangeListener(object: OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                TODO("Not yet implemented")
            }

            override fun onPageSelected(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Change action
                Log.d("Error", "Error")
            }
        })


        // Inflate the layout for this fragment
        return binding.root
    }

//    private fun loadNews() {
//        // Init list
//        newsArrayList = ArrayList()
//
//        // Add items to the list
//        newsArrayList.add(NewsSliderItem("https://firebasestorage.googleapis.com/v0/b/xfund-89b19.appspot.com/o/news%2Fnews1.jpg?alt=media&token=bc2ff48c-8f75-4736-b833-21e810008a98"))
//        newsArrayList.add(NewsSliderItem("https://firebasestorage.googleapis.com/v0/b/xfund-89b19.appspot.com/o/news%2Fnews2.jpg?alt=media&token=07fc04df-e4c6-44ba-ad61-cbfea0684923"))
//
//        // Setup Adapter
//        newsAdapter = NewsSliderAdapter(this.requireContext(), newsArrayList)
//
//        // Set Adapter to ViewPager
//        viewPager.adapter = newsAdapter
//        // Set default padding
//        viewPager.setPadding(100,0,100,0)
//
//    }

}