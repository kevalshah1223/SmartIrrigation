package com.cmpica.irrigate_smartirrigationapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_automatic_setting.*

class AutomaticSettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_automatic_setting)
        supportActionBar!!.hide()

        txtAutoCurrentHum.text = "100%"
        val arrayStartLevel = ArrayList<String>()
        spnAutoStartLevel.prompt = "Levels"
        for(i in 0..90){
            if(i%5 == 0){
                arrayStartLevel.add(i.toString())
            }
        }
        spnAutoStartLevel.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayStartLevel)

        spnAutoStartLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val arrayStopParLevel = ArrayList<String>()
                spnAutoParStopLevel.prompt = "Levels"
                for(i in 0..95){
                    if(i%5 == 0){
                        if(spnAutoStartLevel.selectedItem.toString().toInt() < i)
                            arrayStopParLevel.add(i.toString())
                    }
                }
                spnAutoParStopLevel.adapter = ArrayAdapter(this@AutomaticSettingActivity,android.R.layout.simple_spinner_item, arrayStopParLevel)
            }
        }

        spnAutoParStopLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val arrayStopTotLevel = ArrayList<String>()
                spnAutoStopTotLevel.prompt = "Levels"
                for(i in 0..100){
                    if(i%5 == 0){
                        if(spnAutoParStopLevel.selectedItem.toString().toInt() < i)
                            arrayStopTotLevel.add(i.toString())
                    }
                }
                spnAutoStopTotLevel.adapter = ArrayAdapter(this@AutomaticSettingActivity,android.R.layout.simple_spinner_item, arrayStopTotLevel)
            }
        }


        btnSetField.setOnClickListener {
            if(MainClass().isOnline(this)){
                Toast.makeText(applicationContext, "Clicked", Toast.LENGTH_SHORT).show()
            }else{
                Snackbar.make(it,"No Internet Connection, Try Again", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}