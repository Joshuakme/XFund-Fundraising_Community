package com.example.xfund.screens.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ELEMENTS
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)

        // LAYOUTS
        bottomNav?.visibility = View.GONE

        // EVENT LISTENERS
        view.findViewById<ImageButton>(R.id.fragmentAboutBackButton).setOnClickListener {
            findNavController().navigateUp()
            bottomNav?.visibility = View.VISIBLE
        }
    }
}