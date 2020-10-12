package com.cmpica.irrigate_smartirrigationapp

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.random.Random


class LoginActivity : AppCompatActivity() {

    companion object{
        var permisson_flag = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()

        val permission = object : PermissionListener {
            override fun onPermissionGranted() {
                permisson_flag = true
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                permisson_flag = false
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permission)
            .setPermissions(android.Manifest.permission.SEND_SMS)
            .check()

        btnLogin.setOnClickListener {
            val progressDialog = Dialog(this)
            progressDialog.setContentView(R.layout.progress_dailog)
            progressDialog.setCancelable(false)
            progressDialog.show()

            if(MainClass().isOnline(this)) {
                if(permisson_flag) {
                    progressDialog.show()
                    val database = FirebaseDatabase.getInstance()
                    val fetchUserData = database.getReference("login_master")
                    var flag = false
                    fetchUserData.addValueEventListener(object : ValueEventListener {
                        var userID = ""
                        override fun onCancelled(error: DatabaseError) {
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (snap in snapshot.children) {
                                val value = snap.getValue(DataClassUser::class.java)
                                val userMobileNum = value!!.user_phone
                                if (userMobileNum == cpCountryCode.selectedCountryCodeWithPlus.toString() + edtUserMobileNo.text.toString()) {
                                    userID = value.user_id
                                    flag = true
                                    break
                                }
                            }
                            if (flag) {
                                val intent =
                                    Intent(this@LoginActivity, VerifyOTPActivity::class.java)
                                intent.putExtra(
                                    "userMobile",
                                    "${cpCountryCode.selectedCountryCodeWithPlus.toString()}${edtUserMobileNo.text.toString()}"
                                )
                                intent.putExtra("userId", userID)
                                val OTP = Random.nextInt(1000,9999)
                                SmsManager.getDefault().sendTextMessage("${cpCountryCode.selectedCountryCodeWithPlus.toString()}${edtUserMobileNo.text.toString()}",
                                    null,OTP.toString(),null,null)

                                intent.putExtra("userOTP", OTP.toString())
                                startActivity(intent)
                            } else {
                                progressDialog.dismiss()
                                Toast.makeText(
                                    applicationContext,
                                    "Invalid Username or Password, Try Again!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
                }else{
                    Toast.makeText(applicationContext,"Permission Not Granted for SMS, Try Again", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(applicationContext,"No Internet Connection, Try Again", Toast.LENGTH_SHORT).show()
            }
        }
    }
}