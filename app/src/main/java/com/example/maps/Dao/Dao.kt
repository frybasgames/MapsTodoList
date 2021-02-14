package com.example.maps.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.maps.User

@Dao
interface meDao {
    @Query("SELECT *FROM users")
    fun getAllUser(): List<User>

    @Insert
    fun userAdd(vararg user: User)
}