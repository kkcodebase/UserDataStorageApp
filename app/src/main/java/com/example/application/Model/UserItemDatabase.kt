package com.example.application.Model


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserItem::class], version = 1, exportSchema = false)
abstract class UserItemDatabase: RoomDatabase() {

    abstract fun userItemDao(): UserItemDao

    companion object{
        @Volatile
        private var INSTANCE: UserItemDatabase? = null

        fun getDatabase(context: Context): UserItemDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserItemDatabase::class.java,
                    name = "user_item_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}