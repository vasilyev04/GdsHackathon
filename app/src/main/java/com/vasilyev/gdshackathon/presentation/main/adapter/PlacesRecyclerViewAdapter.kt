package com.vasilyev.gdshackathon.presentation.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.vasilyev.gdshackathon.R
import com.vasilyev.gdshackathon.domain.entity.Place
import com.vasilyev.gdshackathon.presentation.map.MapActivity
import org.w3c.dom.Text

class PlacesRecyclerViewAdapter: RecyclerView.Adapter<PlacesRecyclerViewAdapter.PlaceViewHolder>() {
    inner class PlaceViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image = view.findViewById<ImageView>(R.id.iv_place_image)
        val name = view.findViewById<TextView>(R.id.tv_place_name)
    }

    private var places: List<Place> = listOf()

    fun submitList(list: List<Place>){
        places = list
        notifyDataSetChanged()
    }

    var onItemClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return PlaceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return places.size
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        holder.name.text = place.name
        val requestOptions = RequestOptions()
            .transform(RoundedCorners(30))
        Glide.with(holder.itemView.context).load(place.image).apply(requestOptions).into(holder.image)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(place.id)
        }
    }


}