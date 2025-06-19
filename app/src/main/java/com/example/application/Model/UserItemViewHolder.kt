package com.example.application.Model

import androidx.recyclerview.widget.RecyclerView
import com.example.application.databinding.UserItemCellBinding

class UserItemViewHolder(
    private val context: android.content.Context,
    private val binding: UserItemCellBinding,
    private val clickListener: UserItemClickListener
): RecyclerView.ViewHolder(binding.root){

    fun bindUserItem(userItem: UserItem){
        binding.name.text = userItem.name

        binding.userCellContainer.setOnClickListener {
            clickListener.editUserItem(userItem)
        }
    }
}