package com.example.application.Model

import android.app.Application

class UserDataApplication: Application() {
    private val database by lazy { UserItemDatabase.getDatabase(this) }
    val repository by lazy { UserItemRepository(database.userItemDao()) }
}