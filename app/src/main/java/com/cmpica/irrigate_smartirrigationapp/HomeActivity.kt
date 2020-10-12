package com.cmpica.irrigate_smartirrigationapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar!!.hide()
        val pref = getSharedPreferences("LOGIN",Context.MODE_PRIVATE)
        val database = FirebaseDatabase.getInstance()
        val user_details = database.getReference("login_master")

        user_details.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children){
                    val value = snap.getValue(DataClassUser::class.java)
                    if(pref.getString("userId",null) == value!!.user_id){
                        val userID = pref.getString("userId",null).toString()
                        val fieldData = FirebaseDatabase.getInstance().getReference("field_master")

                        fieldData.addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for(snap in snapshot.children){
                                    val values = snap.getValue(DataClassFieldData::class.java)
                                    if(values!!.user_id == userID){
                                        txtHomeHumidity.text = values.field_humdity
                                        txtHomeWaterFlow.text = values.field_waterflow
                                    }
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                            }
                        })

                        txtUserName.text = value.user_name
                        if(value.user_gen == "male"){
                            imgProfileImage.setImageResource(R.drawable.male)
                        }else{
                            imgProfileImage.setImageResource(R.drawable.female)
                        }
                    }
                }
            }
        })

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                //Call your function here
                setGreetingAndTime()
                handler.postDelayed(this, 1000)//1 sec delay
            }
        }, 0)

        ///
        cardManualSetting.setOnClickListener {
            if(MainClass().isOnline(this)) {
                startActivity(Intent(this, ManualSettingActivity::class.java))
            }else{
                Snackbar.make(it,"No Internet Connection, Try Again", Snackbar.LENGTH_SHORT).show()
            }

        }

        cardAutomaticSetting.setOnClickListener {
            if(MainClass().isOnline(this)) {
                startActivity(Intent(this, AutomaticSettingActivity::class.java))
            }else{
                Snackbar.make(it,"No Internet Connection, Try Again", Snackbar.LENGTH_SHORT).show()
            }
        }

        swipeButtonLogout.setOnStateChangeListener { active ->
            if(active){
                val edit = pref.edit()
                edit.clear()
                edit.apply()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                swipeButtonLogout.toggleState()
            }
        }
    }

    private fun setGreetingAndTime() {
        val c: Calendar = Calendar.getInstance()
        val timeOfDay: Int = c.get(Calendar.HOUR_OF_DAY)
        val timeOfMin: Int = c.get(Calendar.MINUTE)

        if(timeOfMin.toString().length == 1){
            txtTime.text = "$timeOfDay:0$timeOfMin"
        }else{
            txtTime.text = "$timeOfDay:$timeOfMin"
        }

        when (timeOfDay) {
            in 0..11 -> {
                txtGreeting.text = "Good Morning"
            }
            in 12..15 -> {
                txtGreeting.text = "Good Afternoon"
            }
            in 16..20 -> {
                txtGreeting.text = "Good Evening"
            }
            in 21..23 -> {
                txtGreeting.text = "Good Night"
            }
        }
    }
}