package com.bishal.incubator.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bishal.incubator.R
import com.bishal.incubator.models.Photo
import com.bumptech.glide.Glide

class MyPostsImageAdapter(private val photos : List<Photo>) : RecyclerView.Adapter<MyPostsImageAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.itemImageView)
        // val imageCard: CardView = itemView.findViewById(R.id.itemImageCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = photos[position]
        Glide.with(holder.itemView.context)
            .load(photo.imagePath)
            .placeholder(R.drawable.splash) // Placeholder image while loading
            .into(holder.imageView)
    }
}