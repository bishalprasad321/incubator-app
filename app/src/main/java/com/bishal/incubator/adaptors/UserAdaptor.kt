package com.bishal.incubator.adaptors

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.bishal.incubator.R
import com.bishal.incubator.models.Users
import com.bishal.incubator.utils.FOLLOWER_NODE
import com.bishal.incubator.utils.FOLLOWING_NODE
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

class UserAdaptor(
    private var mContext: Context,
    private var mUser: MutableList<Users>
) :
    RecyclerView.Adapter<UserAdaptor.ViewHolder>() {

    interface OnUserItemClickListener {
        fun onUserItemClicked(userId: String)
    }

    private var clickListener: OnUserItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout, parent,false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchedUser = mUser[position]
        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        holder.userName.text = "@${searchedUser.username}"

        holder.profileName.text = searchedUser.name
        Glide.with(mContext).load(searchedUser.image).placeholder(R.drawable.user_filled).into(holder.profileImage)

        // Get references to the Firestore collections
        val followingRef = Firebase.firestore.collection(FOLLOWING_NODE).document(currentUser)

        // Check if the current user is following the searched user
        followingRef.get().addOnSuccessListener { followingDocument ->
            val followingData = followingDocument.data
            if (followingData != null && searchedUser.id.toString() in followingData["following"] as List<*>) {
                // The current user is following the searched user
                holder.followButton.text = "Following"
                holder.followButton.setTextColor(getColor(mContext, R.color.dark_background))
                holder.followButton.setBackgroundResource(R.drawable.image_button_bg)
            } else {
                // The current user is not following the searched user
                holder.followButton.text = "Follow"
                holder.followButton.setTextColor(getColor(mContext, R.color.white))
                holder.followButton.setBackgroundResource(R.drawable.gradient_button_bg)
            }
        }

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

        // start user profile fragment to show that user's details
        holder.profileImage.setOnClickListener {
            clickListener?.onUserItemClicked(searchedUser.id.toString())
        }

        holder.profileName.setOnClickListener {
            clickListener?.onUserItemClicked(searchedUser.id.toString())
        }

        holder.userCard.setOnClickListener{
            clickListener?.onUserItemClicked(searchedUser.id.toString())
        }
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userCard: ConstraintLayout = itemView.findViewById(R.id.searchedUserDetails)
        var userName: TextView = itemView.findViewById(R.id.profileUserNameTextView)
        var profileName: TextView = itemView.findViewById(R.id.profileNameTextView)
        var profileImage: ImageView = itemView.findViewById(R.id.profileImageView)
        var followButton: Button = itemView.findViewById(R.id.followButton)
    }

    fun setOnUserItemClickListener(listener: OnUserItemClickListener) {
        this.clickListener = listener
    }

}