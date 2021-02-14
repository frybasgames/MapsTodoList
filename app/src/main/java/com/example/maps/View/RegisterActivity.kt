package com.example.maps.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.room.Room
import com.example.maps.Database.Database
import com.example.maps.MapsActivity
import com.example.maps.R
import com.example.maps.User
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var registeredUser = 0;
        val getAllUser:List<User>
        val db: Database = Room.databaseBuilder(applicationContext,
            Database::class.java,"users")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

        registertvRegisterBtn.setOnClickListener {
            onBackPressed()
        }
        getAllUser=db.Dao().getAllUser()
            registerRegisterbtn.setOnClickListener {
                if(tvName.text.toString().trim().isNotEmpty() && registerEmail.text.toString().trim().isNotEmpty() && registerPassword.text.toString().trim().isNotEmpty()) {
                    Log.e("üyelik tiklandi", "pozitif")
                    registeredUser = 1;
                    val user: User =
                        User(
                            tvName.text.toString(),
                            registerEmail.text.toString(),
                            registerPassword.text.toString(),
                            registeredUser.toInt()
                        )
                    db.Dao().userAdd(user)
                    Toast.makeText(this, "added users", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MapsActivity::class.java)
                    intent.putExtra("info","new")
                    getAllUser.forEach{
                        intent.putExtra("userTask",it.Id)
                    }
                    startActivity(intent)
                    finish()
                }
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(
            R.anim.slide_from_left,
            R.anim.slide_to_right
        )
    }
}