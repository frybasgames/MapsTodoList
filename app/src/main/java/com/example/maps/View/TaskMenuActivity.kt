package com.example.maps.View

import android.content.Intent
import android.graphics.Color
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.maps.Adapter.TaskAdapter
import com.example.maps.Database.TaskDatabase
import com.example.maps.MapsActivity
import com.example.maps.Model.Place
import com.example.maps.R
import com.example.maps.Task
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_task_menu.*
import kotlinx.android.synthetic.main.fragment_form.*

class TaskMenuActivity : AppCompatActivity(){

    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var tasklocation:LatLng
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_menu)
        val tasks:List<Task>
        val db: TaskDatabase = Room.databaseBuilder(applicationContext,
            TaskDatabase::class.java,"tasks")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

        tasks=db.DaoTask().getAllTask()

        var intent = intent
        var taskId = intent.getIntExtra("userTask",1)
        var userTask=db.DaoTask().userTask(taskId)

        userTask.forEach{
            if (taskId==it.userId){
                recyclerview.layoutManager=LinearLayoutManager(this@TaskMenuActivity)
                recyclerview.adapter= TaskAdapter(userTask, this)
            }
        }

        val info = intent.getStringExtra("info")
        val id = intent.getIntExtra("id",1)
        if (info.equals("konum")){
            tasks.forEach {
                tasklocation = LatLng(it.longitude,it.latitude)
                if(id==it.Id){
                    Place(it.latitude, it.longitude)
                    val intented = Intent(this@TaskMenuActivity,
                        MapsActivity::class.java)
                    intented.putExtra("info","old")
                    intented.putExtra("maps",id)
                    intented.putExtra("mapsloc",
                        Place(it.latitude, it.longitude)
                    )
                    startActivity(intented)
                }
            }
        }
        if(info.equals("task")){
            frameLayout.visibility = View.VISIBLE
            recyclerview.visibility = View.GONE
            consLayout.setBackgroundColor(Color.BLUE)
            tasks.forEach {
                if(id==it.Id){
                    taskBox.setText(it.title)
                    descriptionBox.setText(it.description)

                    taskBox.setFocusable(false);
                    taskBox.setEnabled(false);

                    descriptionBox.setFocusable(false);
                    descriptionBox.setEnabled(false);

                    iptal.setOnClickListener{
                        frameLayout.visibility = View.GONE
                        recyclerview.visibility = View.VISIBLE
                        consLayout.setBackgroundColor(Color.WHITE)
                    }
                }

            }
        }
    }
}