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

package com.app.bfconnect.ui

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import com.app.bfconnect.AppConstants
import com.app.bfconnect.R
import com.app.bfconnect.extension.active
import com.app.bfconnect.extension.attach
import com.app.bfconnect.extension.detach
import com.app.bfconnect.helper.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.app.bfconnect.extension.SimpleShortcutManager
import io.karn.notify.Notify
import net.grandcentrix.tray.AppPreferences
import kotlin.math.roundToInt

private const val KEY_POSITION = "keyPosition"
private const val tQuit = "Esci"
private const val tCancel = "Annulla"
private const val tTitle = "Vuoi uscire?"
private const val nTitle = "Benvenuto"
private const val nText = "Scopri tutte le informazioni sull'IIS Belluzzi Fioravanti"

class MainActivity : AppCompatActivity() {

    private var navPosition: BottomNavigationPosition = BottomNavigationPosition.DASHBOARD
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var nestedScrollView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restoreSaveInstanceState(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Toolbar>(R.id.toolbar).apply {
            setSupportActionBar(this)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            setLogo(R.drawable.ic_bfconnect_horizontal)
        }

        createShortcut()
        showNotify()
        setBottomSheet()
        initBottomSheet(bottomSheetBehavior,nestedScrollView)
        initFragment(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_POSITION, navPosition.id)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val appPackageName = packageName
        when (item.itemId) {
            R.id.action_rate_app -> {
                try { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))) }
                catch (e:android.content.ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                }
            }
            R.id.action_quit -> showAlertDialogQuit()
            R.id.action_share -> {
                ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setChooserTitle("Scarica BFconnect")
                    .setText("https://play.google.com/store/apps/details?id=$appPackageName")
                    .startChooser()
            }
        } ; return super.onOptionsItemSelected(item)
    }

    /**
     * Management Fragment
     */
    private fun initFragment(savedInstanceState: Bundle?) { savedInstanceState ?: switchFragment(BottomNavigationPosition.DASHBOARD) }

    private fun switchFragment(navPosition: BottomNavigationPosition): Boolean {
        return findFragment(navPosition).let {
            if (it.isAdded) return false
            supportFragmentManager.detach()
            supportFragmentManager.attach(it, navPosition.getTag())
            supportFragmentManager.executePendingTransactions()
        }
    }

    private fun findFragment(position: BottomNavigationPosition): Fragment {
        return supportFragmentManager.findFragmentByTag(position.getTag()) ?: position.createFragment()
    }

    private fun createShortcut() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) { SimpleShortcutManager.configShortcut(this) }
        } else { Log.d("Fail","Shortcut creation") }
    }

    private fun restoreSaveInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.getInt(KEY_POSITION, BottomNavigationPosition.DASHBOARD.id)?.also { navPosition = findNavigationPositionById(it) }
    }

    /**
     * Management BottomSheet
     */
    private fun initBottomSheet(bottomSheetBehavior: BottomSheetBehavior<View>,nestedScrollView: View) {

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.peekHeight = TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 110f, resources.displayMetrics ).roundToInt()
        nestedScrollView.visibility = View.VISIBLE

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun hideBottomSheet(navPosition: BottomNavigationPosition, nestedScrollView: View) {
        if (navPosition == BottomNavigationPosition.HOME || navPosition == BottomNavigationPosition.NOTIFICATIONS || navPosition == BottomNavigationPosition.PROFILE) {
            nestedScrollView.visibility = View.GONE
        } else { nestedScrollView.visibility = View.VISIBLE }
    }

    private fun setBottomSheet() {
        nestedScrollView = findViewById(R.id.nestedScrollView)
        bottomSheetBehavior = BottomSheetBehavior.from(nestedScrollView)
        findViewById<BottomNavigationView>(R.id.bottom_navigation).apply {
            active(navPosition.position)
            setOnNavigationItemSelectedListener { item ->
                navPosition = findNavigationPositionById(item.itemId)
                hideBottomSheet(navPosition,nestedScrollView)
                switchFragment(navPosition)
            }
        }
    }

    /**
     * Management Dialog
     */
    private fun showAlertDialogQuit() {
        val dialogBuilder = AlertDialog.Builder(this, R.style.CustomDialogTheme)
            .setCancelable(false)
            .setPositiveButton(tQuit) { dialog, _ -> finish() ; dialog.dismiss() }
            .setNegativeButton(tCancel) { dialog, _ -> dialog.cancel() }
        val alert = dialogBuilder.create()
        alert.setTitle(tTitle)
        alert.show()
    }

    /**
     * Management Notification
     */
    private fun showNotify() {
        val notifyMode: Boolean = AppPreferences(this).getBoolean(AppConstants.SharedPreferences.notifyMode,true)
        when {
            notifyMode -> {
                Notify
                    .with(this)
                    .header { icon = R.drawable.ic_notify }
                    .meta {
                        clickIntent = PendingIntent.getActivity(
                            this@MainActivity,
                            0,
                            Intent(this@MainActivity, MainActivity::class.java),
                            0)
                    }
                    .content {
                        title = nTitle
                        text = nText
                    }
                    .show()
                AppPreferences(this).put(AppConstants.SharedPreferences.notifyMode, false)
            }
            else -> Log.d("Notify","Influent Notify")
        }
    }
}