package com.example.a438finalproject.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a438finalproject.Adapter.viewFavoritesAdapter
import com.example.a438finalproject.Data.adapterData
import com.example.a438finalproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_favorites.*

class Favorites : AppCompatActivity() {

    private var adapterData : ArrayList<adapterData> = ArrayList()
    private lateinit var firebase: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)


        firebase = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        var adapter = viewFavoritesAdapter(adapterData)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        var user = firebase.currentUser

        val docref = db.collection(user!!.uid)
        docref.get().addOnSuccessListener { documents ->
            for(document in documents){
                val location = adapterData(document.get("address").toString(), document.get("name").toString(), document.get("lat").toString().toDouble(), document.get("long").toString().toDouble(), document.get("id").toString())
                adapterData.add(location)
            }
            adapter.notifyDataSetChanged()
        }
    }

    fun back (view: View){
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    fun searchLocation(view : View){
        val id = view.tag
        val Intent = Intent(this, MapsActivity::class.java)
        Intent.putExtra("origin", "favorites")
        Intent.putExtra("id", id.toString())
        startActivity(Intent)
    }

}
