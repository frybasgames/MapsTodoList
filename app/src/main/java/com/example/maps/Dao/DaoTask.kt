package com.example.maps.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.maps.Task

@Dao
interface DaoTask {
    @Query("SELECT *FROM tasks")
    fun getAllTask(): List<Task>

    @Query("SELECT *FROM tasks WHERE userId=:userTaskId")
    fun userTask(userTaskId:Int): List<Task>

    @Insert
    fun taskAdd(vararg task: Task)
}