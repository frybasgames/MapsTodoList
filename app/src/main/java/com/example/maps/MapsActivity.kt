package com.example.maps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.maps.Database.Database
import com.example.maps.Database.TaskDatabase
import com.example.maps.Model.Place
import com.example.maps.Model.RouteModel
import com.example.maps.Service.RouteApi
import com.example.maps.View.TaskMenuActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.fragment_form.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList


private val Unit.java: Unit
    get() {}

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val BASE_URL = "https://api.openrouteservice.org/"
    private var routeModels: ArrayList<RouteModel>? = null

    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    var number: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        taskList.setOnClickListener {
            val intented =intent
            val userTaskId = intented.getIntExtra("userTask",1)
            val intending = Intent(this, TaskMenuActivity::class.java)
            intending.putExtra("userTask",userTaskId)
            startActivity(intending)
            finish()

        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val db: TaskDatabase = Room.databaseBuilder(applicationContext,
            TaskDatabase::class.java,"tasks")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

        val dbUser: Database = Room.databaseBuilder(this,
            Database::class.java,"users")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
        val defaultLocation = LatLng(39.95873636646086,32.713063694536686)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f))

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(p0: Location) {


        }
    }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                2,
                2f,
                locationListener
            )

            val info = intent.getStringExtra("info")
                if (info.equals("old")){
                val selectPlace = intent.getSerializableExtra("mapsloc") as Place
                val selectLocation = LatLng(selectPlace.latitude!!,selectPlace.longitude!!)

                    loadData()
                mMap.addMarker(
                    MarkerOptions()
                        .position(selectLocation)
                        .title("Marker")
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectLocation,15f))
            }
                if (info.equals("new")){
                    val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (lastLocation != null) {
                        val lastLocationLng = LatLng(lastLocation.latitude, lastLocation.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocationLng, 15f))


                        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
                            if (number == 0) {
                                number++
                                fab.setImageResource(R.drawable.check_foreground)
                                mMap.clear()
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(lastLocationLng)
                                        .title("Marker").draggable(true)
                                )

                                mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                                    override fun onMarkerClick(marker: Marker): Boolean {
                                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                        return true
                                    }
                                })
                                mMap.setOnMarkerDragListener(object : OnMarkerDragListener {
                                    override fun onMarkerDragStart(marker: Marker) {
                                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                        Log.e("mark start","lat")
                                    }

                                    override fun onMarkerDragEnd(marker: Marker) {
                                        val lat = marker.position.latitude
                                        val lng = marker.position.longitude
                                        val newPlace = LatLng(lat,lng)
                                        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
                                            mMap.clear()

                                            mMap.addMarker(
                                                MarkerOptions()
                                                    .position(newPlace)
                                                    .title("New Task")
                                            ).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

                                            frameLayout.visibility= View.VISIBLE
                                            iptal.setOnClickListener {
                                                frameLayout.visibility= View.GONE
                                                mMap.clear()
                                                fab.setImageResource(R.drawable.add_foreground)
                                            }
                                            kaydet.setOnClickListener {
                                                val getAllUser:List<User>

                                                getAllUser=dbUser.Dao().getAllUser()
                                                getAllUser.forEach {
                                                    if(1==it.registeredUser){
                                                        val id= it.Id

                                                        val newPlaces =
                                                            Place(
                                                                lat,
                                                                lng
                                                            )
                                                        val task: Task = Task(
                                                            taskBox.text.toString(),
                                                            descriptionBox.text.toString(),
                                                            newPlaces.latitude!!,
                                                            newPlaces.longitude!!,
                                                            id)
                                                        db.DaoTask().taskAdd(task)
                                                    }
                                                }
                                                frameLayout.visibility= View.GONE
                                                mMap.clear()
                                                fab.setImageResource(R.drawable.add_foreground)
                                            }
                                        }
                                    }
                                    override fun onMarkerDrag(marker: Marker) {
                                        Log.e("mark son","lat")
                                    }
                                })
                            }
                            else {
                                number--
                                mMap.clear()
                                fab.setImageResource(R.drawable.add_foreground)
                            }
                        }
                    }
                    else{

                        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {


                            if (number == 0) {
                                number++
                                fab.setImageResource(R.drawable.check_foreground)
                                mMap.clear()


                                mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                                    override fun onMarkerClick(marker: Marker): Boolean {
                                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                        return true
                                    }
                                })
                                mMap.setOnMarkerDragListener(object : OnMarkerDragListener {
                                    override fun onMarkerDragStart(marker: Marker) {
                                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                        Log.e("mark start","lat")
                                    }

                                    override fun onMarkerDragEnd(marker: Marker) {
                                        val lat = marker.position.latitude
                                        val lng = marker.position.longitude
                                        val newPlace = LatLng(lat,lng)
                                        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
                                            mMap.clear()

                                            mMap.addMarker(
                                                MarkerOptions()
                                                    .position(newPlace)
                                                    .title("New Task")
                                            ).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

                                            frameLayout.visibility= View.VISIBLE
                                            iptal.setOnClickListener {
                                                frameLayout.visibility= View.GONE
                                                mMap.clear()
                                                fab.setImageResource(R.drawable.add_foreground)
                                            }
                                            kaydet.setOnClickListener {
                                                val getAllUser:List<User>

                                                getAllUser=dbUser.Dao().getAllUser()
                                                getAllUser.forEach {
                                                    if(1==it.registeredUser){
                                                        val id= it.Id

                                                        val newPlaces =
                                                            Place(
                                                                lat,
                                                                lng
                                                            )
                                                        val task: Task = Task(
                                                            taskBox.text.toString(),
                                                            descriptionBox.text.toString(),
                                                            newPlaces.latitude!!,
                                                            newPlaces.longitude!!,
                                                            id)
                                                        db.DaoTask().taskAdd(task)
                                                    }
                                                }
                                                frameLayout.visibility= View.GONE
                                                mMap.clear()
                                                fab.setImageResource(R.drawable.add_foreground)
                                            }
                                        }
                                    }
                                    override fun onMarkerDrag(marker: Marker) {
                                        Log.e("mark son","lat")
                                    }
                                })
                            }
                            else {
                                number--
                                mMap.clear()
                                fab.setImageResource(R.drawable.add_foreground)
                            }
                        }
                    }
                }

        }
    }


  override fun onRequestPermissionsResult(
      requestCode: Int,
      permissions: Array<out String>,
      grantResults: IntArray
  ) {
      if (requestCode == 1){
          if (grantResults.size>0){
              if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                  locationManager.requestLocationUpdates(
                      LocationManager.GPS_PROVIDER,
                      2,
                      2f,
                      locationListener
                  )
              }
          }
      }
      super.onRequestPermissionsResult(requestCode, permissions, grantResults)
  }



    private val loc = "{\"coordinates\":[[32.713063694536686,39.95873636646086],[32.713063694536686,39.95873636646086],[32.713568955659866,39.96060132874777]]}"
    private fun loadData(){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RouteApi::class.java)

        val call = service.createRoute(RouteModel(loc))

        call.enqueue(object: Callback<List<RouteModel>>{
            override fun onFailure(call: Call<List<RouteModel>>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<List<RouteModel>>,
                response: Response<List<RouteModel>>
            ) {
                if (response.isSuccessful){
                    response.body()?.let{
                        routeModels = ArrayList(it)

                        for (routeModel: RouteModel in routeModels!!){
                            println(routeModel.coordinates)
                        }
                    }
                }
            }

        })

    }

}

