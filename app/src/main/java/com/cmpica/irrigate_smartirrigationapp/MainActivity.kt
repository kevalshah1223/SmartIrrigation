package com.cmpica.irrigate_smartirrigationapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    lateinit var screenPager: ViewPager
    lateinit var introViewPagerAdapter: IntroViewPagerAdapter
    lateinit var tabIndicator: TabLayout
    lateinit var btnNext: CardView
    var position = 0
    lateinit var btnGetStarted: CardView
    lateinit var btnAnim: Animation
    lateinit var tvSkip: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        if (restorePrefData()) {
            val mainActivity = Intent(applicationContext, SplashActivity::class.java)
            startActivity(mainActivity)
            finish()
        }

        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        btnNext = findViewById(R.id.btn_next)
        btnGetStarted = findViewById(R.id.btn_get_started)
        tabIndicator = findViewById(R.id.tab_indicator)
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation)
        tvSkip = findViewById(R.id.tv_skip)


        val mList = ArrayList<ScreenItem>()
        mList.add(ScreenItem("Access Your Farm Atmosphere","All the Details of the Farm will be Online",R.drawable.stepper_img_1))
        mList.add(ScreenItem("Control Your Farm","Control Water Flow and Other Data Online",R.drawable.stepper_img_2))
        mList.add(ScreenItem("A Smart Farm is Secure","Secure the Farm from Anywhere",R.drawable.stepper_img_3))

        screenPager = findViewById(R.id.screen_viewpager)
        introViewPagerAdapter = IntroViewPagerAdapter(this, mList)
        screenPager.adapter = introViewPagerAdapter

        tabIndicator.setupWithViewPager(screenPager);

        btnNext.setOnClickListener {
            position = screenPager.currentItem
            if (position < mList.size) {
                position++
                screenPager.currentItem = position
            }
            if (position == mList.size - 1) { // when we rech to the last screen

                // TODO : show the GETSTARTED Button and hide the indicator and the next button
                loaddLastScreen()
            }
        }

        tabIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!!.position == mList.size -1) {
                    loaddLastScreen();
                }
            }
        })

        btnGetStarted.setOnClickListener { //open main activity
            val mainActivity = Intent(applicationContext, SplashActivity::class.java)
            startActivity(mainActivity)
            // also we need to save a boolean value to storage so next time when the user run the app
            // we could know that he is already checked the intro screen activity
            // i'm going to use shared preferences to that process
            savePrefsData()
            finish()
        }



        tvSkip.setOnClickListener { screenPager.currentItem = mList.size }
    }
    private fun restorePrefData(): Boolean {
        val pref = applicationContext.getSharedPreferences(
            "myPrefs",
            Context.MODE_PRIVATE
        )
        return pref.getBoolean("isIntroOpnend", false)
    }

    private fun savePrefsData() {
        val pref = applicationContext.getSharedPreferences(
            "myPrefs",
            Context.MODE_PRIVATE
        )
        val editor = pref.edit()
        editor.putBoolean("isIntroOpnend", true)
        editor.apply()
    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private fun loaddLastScreen() {
        btnNext.visibility = View.INVISIBLE
        btnGetStarted.visibility = View.VISIBLE
        tvSkip.visibility = View.INVISIBLE
        tabIndicator.visibility = View.INVISIBLE
        // TODO : ADD an animation the getstarted button
        // setup animation
        btnGetStarted.animation = btnAnim
    }
}