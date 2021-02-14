package com.example.maps.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.maps.Dao.meDao
import com.example.maps.User

@Database(entities = [User::class],version = 5)
abstract class Database:RoomDatabase() {
    abstract fun Dao() : meDao
}

