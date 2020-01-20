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

package com.app.bfconnect.helper

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.app.bfconnect.R

class AboutActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var toolbar : Toolbar
    private lateinit var lTel : LinearLayout
    private lateinit var lEmail : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setToolbar()
        setMaps()
        initViews()
        initListeners()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val belluzzi = LatLng(44.489236, 11.284999599999992)
        mMap.setMinZoomPreference(16.0F)
        mMap.setMaxZoomPreference(20.0F)
        mMap.addMarker(MarkerOptions().position(belluzzi).title("IIS Belluzzi-Fioravanti").snippet("Via G. D. Cassini, 3"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(belluzzi))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_qrcode, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.close_qrcode) { onBackPressed() ; finish() }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        lTel = findViewById(R.id.telephone)
        lEmail = findViewById(R.id.email)
    }

    private fun initListeners() {
        lTel.setOnClickListener { dialContactPhone() }
        lEmail.setOnClickListener { email() }
    }

    private fun setToolbar() {
        toolbar = findViewById(R.id.toolbar_about)
        toolbar.apply {
            setSupportActionBar(this)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            setLogo(R.drawable.ic_bfconnect_horizontal)
        }
    }

    private fun setMaps() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun dialContactPhone() {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "0513519711", null)))
    }

    private fun email() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.type = "plain/text"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("bois02300g@istruzione.it"))
        startActivity(Intent.createChooser(intent, ""))
    }
}
