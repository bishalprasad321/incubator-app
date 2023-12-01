package com.bishal.incubator.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bishal.incubator.R

class ImageAdapter(private val images: ArrayList<String>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private var onImageItemClickListener: OnImageItemClickListener? = null

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.itemImageView)
        val imageCard: CardView = itemView.findViewById(R.id.itemImageCard)
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
        // device parameters
        val deviceMetrics = holder.itemView.context.resources.displayMetrics
        val screenWidth = deviceMetrics.widthPixels

        // set layout params
        holder.imageCard.layoutParams = ConstraintLayout.LayoutParams(screenWidth / 4, screenWidth / 4)

        val imageItem = images[position]
        holder.imageView.load(imageItem) {
            placeholder(R.drawable.splash)
        }
        holder.imageView.setOnClickListener {
            onImageItemClickListener?.onImageItemClick(imageItem)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun setOnImageItemClickListener(listener: OnImageItemClickListener) {
        this.onImageItemClickListener = listener
    }
}