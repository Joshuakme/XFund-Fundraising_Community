package com.example.xfund

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.adapter.CommunityItemAdapter
import com.example.xfund.data.CommunityDatasource
import com.example.xfund.screens.navigation.CommunityFragment
import com.example.xfund.screens.navigation.HomeFragment
import com.example.xfund.screens.navigation.ProfileFragment
import com.example.xfund.screens.navigation.ProjectsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.xfund.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
//    private lateinit var database: DatabaseReference
    lateinit var bottomNav : BottomNavigationView
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


//        // Initialize data.
//        val myDataset = CommunityDatasource().loadCommunities()
//
//        val recyclerView = findViewById<RecyclerView>(R.id.CommunityRecycleView)
//        recyclerView.adapter = CommunityItemAdapter(this, myDataset)
//
//        // Use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
//        recyclerView.setHasFixedSize(true)

        // First Load Screen
        loadFragment(HomeFragment())
        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // Initialize Bottom Nav Item Badges
        setNavItemBadge(R.id.profile)
        setNavItemBadge(R.id.community)

        // Event Listener -> BottomNav Items
        bottomNav.setOnItemSelectedListener {item ->
            when (item.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    removeNavItemBadgeOnClick(item.itemId)
                    true
                }

                R.id.projects -> {
                    loadFragment(ProjectsFragment())
                    removeNavItemBadgeOnClick(item.itemId)
                    true
                }

                R.id.community -> {
                    loadFragment(CommunityFragment())

                    removeNavItemBadgeOnClick(item.itemId)

                    true
                }

                R.id.profile -> {
                    loadFragment(ProfileFragment())
                    removeNavItemBadgeOnClick(item.itemId)
                    true
                }

                else -> {false}
            }
        }




    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }

    private fun setNavItemBadge(menuItemId : Int) {
        var badge = bottomNav.getOrCreateBadge(menuItemId)
        badge.isVisible = true
        // An icon only badge will be displayed unless a number is set:
        //badge.number = 99
    }

    private fun removeNavItemBadgeOnClick(menuItemId : Int) {
        // Item Badge
        val homeBadgeDrawable = bottomNav.getBadge(menuItemId)
        if (homeBadgeDrawable != null) {
            homeBadgeDrawable.isVisible = false
            homeBadgeDrawable.clearNumber()
        }
    }

}
