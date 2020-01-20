/*
 * MIT License
 *
 * Copyright (c) 2020 Francesco Taddia
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.app.bfconnect.intro

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.app.bfconnect.AppConstants
import com.app.bfconnect.ui.MainActivity
import com.google.android.material.tabs.TabLayout
import net.grandcentrix.tray.AppPreferences
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.Toast
import com.app.bfconnect.R

private const val PERMISSION_CODE = 123

class IntroActivity : AppCompatActivity() {

    private var position = 0
    private lateinit var screenPager: ViewPager
    private lateinit var introViewPagerAdapter: IntroViewPagerAdapter
    private lateinit var tabIndicator: TabLayout
    private lateinit var btnNext: Button
    private lateinit var btnAnim: Animation
    private lateinit var btnGetStarted: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN )
        setTheme(R.style.AppTheme_NoActionBar)
        setContentView(R.layout.activity_intro)

        // ini views
        btnNext = findViewById(R.id.btn_next)
        tabIndicator = findViewById(R.id.tab_indicator)
        btnAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.button_animation)
        btnGetStarted = findViewById(R.id.btn_start)
        // fill list screen
        val mList = ArrayList<ScreenItem>()
        mList.add(
            ScreenItem(
                "Benvenuto",
                "BFconnect è un'app che rende le informazioni di una scuola a portata di tutti",
                R.drawable.ic_bf
            )
        )
        mList.add(
            ScreenItem(
                "Abbiamo finito",
                "Seleziona Entra e accetta i permessi per poter utilizzare BFconnect",
                R.drawable.ic_bf
            )
        )

        // setup viewpager
        this.screenPager = findViewById(R.id.screen_viewpager)
        introViewPagerAdapter = IntroViewPagerAdapter(this, mList)
        screenPager.adapter = introViewPagerAdapter
        // setup tablayout with viewpager
        tabIndicator.setupWithViewPager(screenPager)

        // next button click Listner
        btnNext.setOnClickListener {
            position = screenPager.currentItem
            if (position < mList.size) {
                position++
                screenPager.currentItem = position
            }
            if (position == mList.size - 1) {switchOne()} else {switchTwo()}
        }

        // tablayout add change listener
        tabIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == mList.size - 1)  switchOne() else switchTwo()
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        btnGetStarted.setOnClickListener { checkPermission() }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this@IntroActivity, Manifest.permission.CAMERA) and
            ContextCompat.checkSelfPermission(this@IntroActivity, Manifest.permission.READ_EXTERNAL_STORAGE) and
            ContextCompat.checkSelfPermission(this@IntroActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat
                .requestPermissions(
                    this@IntroActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA), PERMISSION_CODE)
        } else {
            Toast.makeText(this@IntroActivity, "Permessi concessi", Toast.LENGTH_SHORT).show()
            AppPreferences(this).put(AppConstants.SharedPreferences.wizardHelp, true)
            startActivity(Intent(this@IntroActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_CODE -> {
                if ((grantResults.isNotEmpty()) and (grantResults[0] and grantResults[1] and grantResults[2] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this@IntroActivity, "Permessi concessi", Toast.LENGTH_SHORT).show()
                    AppPreferences(this@IntroActivity).put(AppConstants.SharedPreferences.wizardHelp, true)
                    startActivity(Intent(this@IntroActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@IntroActivity, "Permessi negati", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun switchOne() {
        btnNext.visibility = View.INVISIBLE
        tabIndicator.visibility = View.VISIBLE
        btnGetStarted.visibility = View.VISIBLE
        btnGetStarted.animation = btnAnim
    }

    private fun switchTwo() {
        btnNext.visibility = View.VISIBLE
        tabIndicator.visibility = View.VISIBLE
        btnGetStarted.visibility = View.INVISIBLE
        btnGetStarted.animation = btnAnim
    }
}