package com.example.application.Model

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class UserItemRepository (private val userItemDao: UserItemDao){

    val allUserItems: Flow<List<UserItem>> = userItemDao.allUserItems()

    @WorkerThread
    suspend fun insertUserItem(userItem: UserItem){
        userItemDao.insertUserItem(userItem)
    }

    @WorkerThread
    suspend fun updateUserItem(userItem: UserItem){
        userItemDao.updateUserItem(userItem)
    }
}