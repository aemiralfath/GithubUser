package com.aemiralfath.githubuser.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aemiralfath.githubuser.R
import com.aemiralfath.githubuser.databinding.ItemRowUserBinding
import com.aemiralfath.githubuser.model.network.response.UsersItemResponse
import com.aemiralfath.githubuser.model.network.response.UsersResponse
import com.bumptech.glide.Glide

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    var listUsers = UsersResponse()
        set(listUsers) {
            field = listUsers
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
        holder.bind(listUsers.itemResponses?.get(position) ?: UsersItemResponse())
    }

    override fun getItemCount(): Int = listUsers.itemResponses?.size ?: 0

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowUserBinding.bind(itemView)

        fun bind(user: UsersItemResponse?) {
            val username = "@${user?.login}"

            Glide.with(itemView.context).load(user?.avatarUrl).into(binding.imgItemAvatar)
            binding.tvItemName.text = username
            binding.tvItemPage.text = user?.htmlUrl

            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(user)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UsersItemResponse?)
    }

}