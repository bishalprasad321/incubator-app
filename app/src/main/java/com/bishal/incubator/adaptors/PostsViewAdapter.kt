package com.bishal.incubator.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bishal.incubator.R
import com.bishal.incubator.models.Photo
import com.bishal.incubator.models.User
import com.google.android.material.imageview.ShapeableImageView
import de.hdodenhof.circleimageview.CircleImageView

class PostsViewAdapter(private val allPosts : List<Photo>, private val user: User) : RecyclerView.Adapter<PostsViewAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView : CircleImageView = itemView.findViewById(R.id.userProfileImage)
        val userName : TextView = itemView.findViewById(R.id.userNameTextView)
        val moreOptions : Button = itemView.findViewById(R.id.moreOptionsButton)
        val postViewImage : ShapeableImageView = itemView.findViewById(R.id.postViewImage)


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.posts_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = allPosts[position]

        holder.userName.text = user.username

//        if (!user.image.isNullOrEmpty()) {
//            holder.profileImageView.load(user.image) {
//                placeholder(R.drawable.user_filled)
//            }
//        }
        holder.moreOptions.setOnClickListener{

        }
//        if (!post.imagePath.isNullOrEmpty()) {
//            holder.postViewImage.load(post.imagePath) {
//                placeholder(R.drawable.splash)
//            }
//        }
    }

    override fun getItemCount(): Int {
        return allPosts.size
    }
}