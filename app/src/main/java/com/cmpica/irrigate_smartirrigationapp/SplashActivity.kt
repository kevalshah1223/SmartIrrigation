package com.cmpica.irrigate_smartirrigationapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()



        android.os.Handler().postDelayed({
            val pref = getSharedPreferences("LOGIN",Context.MODE_PRIVATE)
            if(null == pref.getString("userId", null)){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        },3000)
    }
}