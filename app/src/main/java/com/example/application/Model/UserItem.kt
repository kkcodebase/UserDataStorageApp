package com.example.application.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "User_Item_Table")
class UserItem(
    @ColumnInfo(name = "Name") var name: String,
    @ColumnInfo(name = "Email") var email: String,
    @ColumnInfo(name = "Phone") var phone: String,
    @ColumnInfo(name = "Age") var age: String,
    @ColumnInfo(name = "Profile") var profile: String,
    @PrimaryKey(autoGenerate = true) var id: Int = 0) {

}