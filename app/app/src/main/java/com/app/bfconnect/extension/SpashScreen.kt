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

package com.app.bfconnect.extension

//Sfruttarla per precaricare dati

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.app.bfconnect.AppConstants
import com.app.bfconnect.R
import com.app.bfconnect.intro.IntroActivity
import com.app.bfconnect.ui.MainActivity
import net.grandcentrix.tray.AppPreferences

class SpashScreen : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 200 // 0,2 sec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spash_screen)
        timeSplash()
    }

    private fun timeSplash() {
        Handler().postDelayed({
            val wizardHelp: Boolean = AppPreferences(this).getBoolean(AppConstants.SharedPreferences.wizardHelp,false)
            when {
                wizardHelp -> startActivity(Intent(this, MainActivity::class.java))
                else -> startActivity(Intent(this, IntroActivity::class.java))
            }
            finish()
        }, SPLASH_TIME_OUT)
    }
}
