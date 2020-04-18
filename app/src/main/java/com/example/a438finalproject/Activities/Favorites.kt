package com.example.a438finalproject.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.a438finalproject.R

class Favorites : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
    }

    fun back (view: View){
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
}
