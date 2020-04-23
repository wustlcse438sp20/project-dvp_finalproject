package com.example.a438finalproject.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.a438finalproject.Fragments.Login
import com.example.a438finalproject.Fragments.Signup
import com.example.a438finalproject.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentAdapter =
            MyPagerAdapter(
                supportFragmentManager
            )
        viewpager_main.adapter = fragmentAdapter

        tabs_main.setupWithViewPager(viewpager_main)
    }
}

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                Login()
            }
            else -> Signup()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Login"
            else -> "Sign Up"
        }
    }
}

