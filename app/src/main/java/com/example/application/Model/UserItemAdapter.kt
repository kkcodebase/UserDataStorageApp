package com.example.application.Model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application.databinding.UserItemCellBinding

class UserItemAdapter(
    private val userItem: List<UserItem>,
    private val clickListener: UserItemClickListener
): RecyclerView.Adapter<UserItemViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = UserItemCellBinding.inflate(from, parent, false)
        return UserItemViewHolder(parent.context, binding, clickListener)
    }

    override fun getItemCount(): Int = userItem.size

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        holder.bindUserItem(userItem[position])
    }
}