package com.bishal.incubator.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bishal.incubator.R
import com.bishal.incubator.models.Users
import com.bishal.incubator.utils.generateDisplayUsername
import com.squareup.picasso.Picasso

class UserAdaptor(private var mContext: Context,
                  private var mUser: MutableList<Users>, private var isFragment: Boolean = false) :
    RecyclerView.Adapter<UserAdaptor.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mUser[position]
        holder.userName.text = generateDisplayUsername(user.email)
        holder.profileName.text = user.name
        Picasso.get().load(user.image).placeholder(R.drawable.user_filled).into(holder.profileImage)
        holder.followButton.setOnClickListener{
            // TODO
        }
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userName: TextView = itemView.findViewById(R.id.profileUserNameTextView)
        var profileName: TextView = itemView.findViewById(R.id.profileNameTextView)
        var profileImage: ImageView = itemView.findViewById(R.id.profileImageView)
        var followButton: Button = itemView.findViewById(R.id.followButton)
    }

}