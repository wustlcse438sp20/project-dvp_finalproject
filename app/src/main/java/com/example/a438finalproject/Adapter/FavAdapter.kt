package com.example.a438finalproject.Adapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a438finalproject.Data.adapterData
import com.example.a438finalproject.R

class viewFavoritesViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.favoritelocations, parent, false)) {
    private val name: TextView
    private val address: TextView
    private val lat: TextView
    private val long: TextView
    private val search: Button

    init {
        name = itemView.findViewById(R.id.name)
        address = itemView.findViewById(R.id.address)
        lat = itemView.findViewById(R.id.latitude)
        long = itemView.findViewById(R.id.longitude)
        search = itemView.findViewById(R.id.favSearchButton)
    }

    fun bind(favLocation: adapterData) {
        name.text = "Name: " + favLocation.name
        address.text = "Address: " + favLocation.address
        lat.text = "Latitude: " + favLocation.latitude
        long.text = "Longitude " + favLocation.longitude
        search.tag = favLocation.id
    }

}

//create the listener for the recycler view
class viewFavoritesAdapter(private val list: ArrayList<adapterData>?) :
    RecyclerView.Adapter<viewFavoritesViewHolder>() {
    private var listEvents: ArrayList<adapterData>? = list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewFavoritesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return viewFavoritesViewHolder(inflater, parent)
    }

    //bind the object
    override fun onBindViewHolder(holder: viewFavoritesViewHolder, position: Int) {
        val event: adapterData = listEvents!!.get(position)
        holder.bind(event)
    }

    //set the count
    override fun getItemCount(): Int = list!!.size

}
