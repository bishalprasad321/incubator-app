package com.bishal.incubator.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bishal.incubator.R
import com.bishal.incubator.models.ImageItem

class ImageAdaptor(private val images: ArrayList<ImageItem>) : RecyclerView.Adapter<ImageAdaptor.ImageViewHolder>() {

    private var onImageItemClickListener: OnImageItemClickListener? = null

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.itemImageView)
    }

    // Interface for sending the selected image path to the parent activity
    interface OnImageItemClickListener {
        fun onImageItemClick(imagePath: String)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item_layout, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageItem = images[position]
        holder.imageView.load(imageItem.imagePath) {
            placeholder(R.drawable.splash)
        }
        holder.imageView.setOnClickListener {
            onImageItemClickListener?.onImageItemClick(imageItem.imagePath)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun setOnImageItemClickListener(listener: OnImageItemClickListener) {
        this.onImageItemClickListener = listener
    }
}