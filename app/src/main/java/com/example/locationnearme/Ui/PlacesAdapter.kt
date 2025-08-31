package com.example.locationnearme.Ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide // Import Glide
import com.example.locationnearme.R
import com.example.locationnearme.data.Place
import com.bumptech.glide.load.engine.DiskCacheStrategy

class PlacesAdapter(private var places: List<Place>) :
    RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder>() {

    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.placeImageView) // Find ImageView
        val nameTextView: TextView = itemView.findViewById(R.id.placeNameTextView)
        val addressTextView: TextView = itemView.findViewById(R.id.placeAddressTextView)
        val ratingTextView: TextView = itemView.findViewById(R.id.placeRatingTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_place, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        holder.nameTextView.text = place.name ?: "N/A"
        holder.addressTextView.text = place.address ?: "No address available"
        holder.ratingTextView.text = place.rating?.toString() ?: "-"

        // Use Glide to load the image
        Glide.with(holder.itemView.context)
            .load(place.photoUrl)
            .override(200,200)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .into(holder.imageView)
    }

    override fun getItemCount() = places.size

    fun updatePlaces(newPlaces: List<Place>) {
        places = newPlaces
        notifyDataSetChanged()
    }
}