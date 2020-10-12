package com.cmpica.irrigate_smartirrigationapp

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xw.repo.BubbleSeekBar
import kotlinx.android.synthetic.main.activity_manual_setting.*

class ManualSettingActivity : AppCompatActivity() {

    lateinit var progressDialog : Dialog
    var statusActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_setting)

        supportActionBar!!.hide()

        showDialog()

        val pref = getSharedPreferences("LOGIN",Context.MODE_PRIVATE)

        val fieldData = FirebaseDatabase.getInstance().getReference("field_master")
        fieldData.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children){
                    val value = snap.getValue(DataClassFieldData::class.java)
                    if(pref.getString("userId",null) == value!!.user_id){
                        txtManualHumidity.text = value.field_humdity
                        if(value.field_type == "automatic"){
                            txtManualNote.visibility = View.VISIBLE
                            statusActive = false
                            imgManualWaterFlow.setImageResource(R.drawable.water_off)
                            swipeButtonManualSetField.setText("Swipe to Set Field")
                            switchManualWaterFLow.isEnabled = true
                            seekbarWaterFlow.isEnabled = true
                            switchManualWaterFLow.isChecked = false
                            seekbarWaterFlow.setProgress(0f)
                            swipeButtonManualSetField.isEnabled = false
                        }else {
                            txtManualNote.visibility = View.GONE
                            swipeButtonManualSetField.isEnabled = true
                            if (value.field_type == "manual") {
                                statusActive = true
                                imgManualWaterFlow.setImageResource(R.drawable.water_on)
                                swipeButtonManualSetField.setText("Swipe to Stop Field")
                                switchManualWaterFLow.isEnabled = false
                                seekbarWaterFlow.isEnabled = false
                                switchManualWaterFLow.isChecked = value.field_water == "ON"
                                seekbarWaterFlow.setProgress(value.field_waterflow.toFloat())
                            } else {
                                statusActive = false
                                imgManualWaterFlow.setImageResource(R.drawable.water_off)
                                swipeButtonManualSetField.setText("Swipe to Set Field")
                                switchManualWaterFLow.isEnabled = true
                                seekbarWaterFlow.isEnabled = true
                                switchManualWaterFLow.isChecked = false
                                seekbarWaterFlow.setProgress(0f)
                            }
                        }
                        progressDialog.dismiss()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        progressDialog.dismiss()
        swipeButtonManualSetField.setOnStateChangeListener {active ->
            if(active){
                if(statusActive){
                    imgManualWaterFlow.setImageResource(R.drawable.water_off)
                    swipeButtonManualSetField.setText("Swipe to Set Field")
                    switchManualWaterFLow.isEnabled = true
                    seekbarWaterFlow.isEnabled = true
                    switchManualWaterFLow.isChecked = false
                    seekbarWaterFlow.setProgress(0f)
                    Toast.makeText(applicationContext, "Irrigation Stopped!", Toast.LENGTH_SHORT).show()
                    statusActive = false
                }else{
                    if(seekbarWaterFlow.progress == 0){
                        if(!switchManualWaterFLow.isChecked){
                            Toast.makeText(applicationContext, "Set All the Data, Try Again", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        if(!switchManualWaterFLow.isChecked){
                            Toast.makeText(applicationContext, "Set All the Data, Try Again", Toast.LENGTH_SHORT).show()
                        }else {
                            imgManualWaterFlow.setImageResource(R.drawable.water_on)
                            swipeButtonManualSetField.setText("Swipe to Stop Field")
                            switchManualWaterFLow.isEnabled = false
                            seekbarWaterFlow.isEnabled = false

                            fieldData.child("field_master").child(pref.getString("userId",null).toString()).
                                child("field_type").setValue("None")

                            Toast.makeText(applicationContext, "Irrigation Started!", Toast.LENGTH_SHORT).show()
                            statusActive = true
                        }
                    }
                }
                swipeButtonManualSetField.toggleState()
            }
        }
    }

    private fun showDialog() {
        progressDialog = Dialog(this)
        progressDialog.setContentView(R.layout.progress_dailog)
        progressDialog.setCancelable(false)
        progressDialog.show()
    }
}