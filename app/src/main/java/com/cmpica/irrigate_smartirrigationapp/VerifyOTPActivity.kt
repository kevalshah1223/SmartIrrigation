package com.cmpica.irrigate_smartirrigationapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_verify_o_t_p.*

class VerifyOTPActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_o_t_p)
        supportActionBar!!.hide()

        val userOTP = intent.getStringExtra("userOTP")
        val userId = intent.getStringExtra("userId")
        val userNumber = intent.getStringExtra("userMobile")
        txtSendOTP.text = "Your OTP is send on $userNumber"
        btnVerifyOTP.setOnClickListener {
            if(MainClass().isOnline(this)){
                if(userOTP == pinOTP.text.toString()){
                    pinOTP.setLineColor(Color.GREEN)
                    val intent = Intent(this,HomeActivity::class.java)
                    val pref = getSharedPreferences("LOGIN",Context.MODE_PRIVATE)
                    val edit = pref.edit()
                    edit.putString("userId", userId)
                    edit.apply()
                    startActivity(intent)
                }else{
                    pinOTP.setLineColor(Color.RED)
                    pinOTP.text!!.clear()
                }
            }else{
                Snackbar.make(it,"No Internet Connection, Try Again", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}