package com.bishal.incubator.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bishal.incubator.R
import com.bishal.incubator.models.Photo
import com.bishal.incubator.utils.PROFILE_FRAGMENT_NUMBER
import com.bishal.incubator.utils.calculateSizeOfView

class MyPostsImageAdapter(private val photos : List<Photo>) : RecyclerView.Adapter<MyPostsImageAdapter.ViewHolder>() {

    private var onPostItemClickListener : OnPostItemClickListener? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.itemImageView)
    }

    interface OnPostItemClickListener {
        fun onItemClick(photo : Photo, fragmentNumber : Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item_layout, parent, false)
        val size = calculateSizeOfView(parent.context)
        val margin = 8 * 3 // any vertical spacing margin = your_margin * column_count
        val layoutParams = GridLayout.LayoutParams(ViewGroup.LayoutParams(size - margin, size)) // width and height

        layoutParams.bottomMargin = 8 // horizontal spacing if needed

        view.layoutParams = layoutParams
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = photos[position]
        if (!photo.imagePath.isNullOrEmpty()) {
            holder.imageView.load(photo.imagePath) {
                placeholder(R.drawable.splash)
                error(R.drawable.splash)
            }
        }

        holder.imageView.setOnClickListener {
            onPostItemClickListener?.onItemClick(photo, PROFILE_FRAGMENT_NUMBER)
        }
    }

    fun setOnPostItemClickListener(listener: OnPostItemClickListener) {
        this.onPostItemClickListener = listener
    }
}