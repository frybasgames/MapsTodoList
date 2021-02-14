package com.example.maps

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(

    @ColumnInfo(name = "name")
    var name:String,
    @ColumnInfo(name = "email")
    var email:String,
    @ColumnInfo(name = "password")
    var password:String,
    @ColumnInfo(name = "registeredUser")
    var registeredUser:Int = 0)

    {
        @PrimaryKey(autoGenerate = true)
        var Id: Int = 0


}


