package com.example.application.Model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserItemDao {
    @Query("SELECT * FROM user_item_table ORDER BY id")
    fun allUserItems(): Flow<List<UserItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserItem(userItem: UserItem)

    @Update
    suspend fun updateUserItem(userItem: UserItem)

    @Delete
    suspend fun deleteUserItem(userItem: UserItem)
}