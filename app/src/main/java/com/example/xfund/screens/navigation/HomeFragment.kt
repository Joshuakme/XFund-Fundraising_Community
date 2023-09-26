package com.example.xfund.screens.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.xfund.R
import com.example.xfund.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

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
        val searchBar: SearchView = binding.SearchBar
        val txtSearch = searchBar.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        val searchIcon: ImageView = searchBar.findViewById(androidx.appcompat.R.id.search_mag_icon)
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)

        searchIcon.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.gray_300),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        txtSearch.setHint(R.string.search_query_hint)
        txtSearch.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.gray_300))
        //txtSearch.textSize = R.dimen.search_txt_size.toFloat()



        if (bottomNav != null) {
            if(bottomNav.visibility == View.INVISIBLE)  {
                bottomNav.visibility = View.VISIBLE
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

}