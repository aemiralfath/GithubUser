package com.aemiralfath.githubuser.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aemiralfath.githubuser.R
import com.aemiralfath.githubuser.databinding.ItemRowUserBinding
import com.aemiralfath.githubuser.model.entity.FollowResponse
import com.bumptech.glide.Glide

class UserFollowAdapter : RecyclerView.Adapter<UserFollowAdapter.UserViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    var listUsersFollow = arrayListOf<FollowResponse>()
        set(listUsersFollow) {
            field = listUsersFollow
            notifyDataSetChanged()
        }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listUsersFollow[position])
    }

    override fun getItemCount(): Int = listUsersFollow.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowUserBinding.bind(itemView)

        fun bind(user: FollowResponse) {
            val username = "@${user.login}"

            Glide.with(itemView.context).load(user.avatarUrl).into(binding.imgItemAvatar)
            binding.tvItemName.text = username
            binding.tvItemPage.text = user.htmlUrl

            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(user)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FollowResponse)
    }

}