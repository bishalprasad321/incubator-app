package com.bishal.incubator.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bishal.incubator.R
import com.bishal.incubator.utils.calculateSizeOfView

class ImageAdapter(private val images: ArrayList<String>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

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
        val size = calculateSizeOfView(parent.context)
        val margin = 8 * 3 // any vertical spacing margin = your_margin * column_count
        val layoutParams = GridLayout.LayoutParams(ViewGroup.LayoutParams(size - margin, size)) // width and height

        layoutParams.bottomMargin = 8 // horizontal spacing if needed

        view.layoutParams = layoutParams
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
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