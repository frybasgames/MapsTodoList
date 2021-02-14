package com.example.maps.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.maps.Dao.DaoTask
import com.example.maps.Task

@Database(entities = [Task::class],version = 3)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun DaoTask() : DaoTask
}