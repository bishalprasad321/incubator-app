package com.bishal.incubator.adaptors

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.bishal.incubator.R
import com.bishal.incubator.models.Users
import com.bishal.incubator.utils.FOLLOWER_NODE
import com.bishal.incubator.utils.FOLLOWING_NODE
import com.bishal.incubator.utils.generateDisplayUsername
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.squareup.picasso.Picasso

class UserAdaptor(
    private var mContext: Context,
    private var mUser: MutableList<Users>
) :
    RecyclerView.Adapter<UserAdaptor.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout, parent,false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchedUser = mUser[position]
        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        holder.userName.text = generateDisplayUsername(searchedUser.email)

        holder.profileName.text = searchedUser.name
        Picasso.get().load(searchedUser.image).placeholder(R.drawable.user_filled).into(holder.profileImage)

        holder.followButton.setOnClickListener{
            if (holder.followButton.text.toString() == "Follow") {
                // Follow action
                holder.followButton.text = "Following"
                holder.followButton.setTextColor(getColor(mContext, R.color.dark_background))
                holder.followButton.setBackgroundResource(R.drawable.image_button_bg)

                // Add searched user to the following list of current user's account
                Firebase.firestore.collection(FOLLOWING_NODE).document(currentUser)
                    .update("following", FieldValue.arrayUnion(searchedUser.id.toString()))

                // Add the current user to the follower list of searched user's account
                Firebase.firestore.collection(FOLLOWER_NODE).document(searchedUser.id.toString())
                    .update("followers", FieldValue.arrayUnion(currentUser))
            } else {
                // Unfollow action
                holder.followButton.text = "Follow"
                holder.followButton.setTextColor(getColor(mContext, R.color.white))
                holder.followButton.setBackgroundResource(R.drawable.gradient_button_bg)

                // Remove the searched user from the following list of the current user's account
                Firebase.firestore.collection(FOLLOWING_NODE).document(currentUser)
                    .update("following", FieldValue.arrayRemove(searchedUser.id.toString()))

                // Remove the current user from the follower's list of the searched user's account
                Firebase.firestore.collection(FOLLOWER_NODE).document(searchedUser.id.toString())
                    .update("followers", FieldValue.arrayRemove(currentUser))
            }
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