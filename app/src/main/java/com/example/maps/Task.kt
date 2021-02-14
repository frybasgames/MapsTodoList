package com.example.maps

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task (

                  @ColumnInfo(name = "title")
                  var title:String,
                  @ColumnInfo(name = "description")
                  var description:String,
                  @ColumnInfo(name = "latitude")
                  var latitude:Double,
                  @ColumnInfo(name = "longitude")
                  var longitude:Double,
                  @ColumnInfo(name = "userId")
                  var userId:Int)

{
    @PrimaryKey(autoGenerate = true)
    var Id: Int = 0


}