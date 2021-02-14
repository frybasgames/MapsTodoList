package com.example.maps.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.room.Room
import com.example.maps.*
import com.example.maps.Database.Database
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val getAllUser:List<User>
        val db: Database = Room.databaseBuilder(this,
            Database::class.java,"users")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
        getAllUser=db.Dao().getAllUser()


        loginRegisterbtn.setOnClickListener {
            getAllUser.forEach {
                if(loginEmail.text.toString()==it.email){
                    if(loginPassword.text.toString()==it.password){
                        Toast.makeText(this,"Giris Yapıldı",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MapsActivity::class.java)
                        intent.putExtra("info","new")
                        intent.putExtra("userTask",it.Id)
                        startActivity(intent)
                        finish()
                    } else{
                        Toast.makeText(this,"Kullanıcı veya Şifre hatalı.",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        LoginRegisterBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            overridePendingTransition(
                R.anim.slide_from_right,
                R.anim.slide_to_left
            )
        }
    }
}