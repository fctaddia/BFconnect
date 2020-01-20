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

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.bfconnect.R
import com.app.bfconnect.helper.LabActivity
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.fragment_qrcode.*
import org.json.JSONException

class QrcodeFragment : Fragment() {

    private lateinit var btnScan: Button
    private lateinit var qrScanIntegrator: IntentIntegrator
    private lateinit var imgQr : ImageView
    private lateinit var txtQr : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_qrcode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initObj()
        initListners()
        refreshCamera()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(activity, R.string.result_not_found, Toast.LENGTH_LONG).show()
            } else {
                try {
                    openRoom(result.contents)
                } catch (e: JSONException) { e.printStackTrace() }
            }
        } else { super.onActivityResult(requestCode, resultCode, data) }
    }

    private fun initObj() {
        btnScan = view!!.findViewById(R.id.btnScan)
        imgQr = view!!.findViewById(R.id.imgCam)
        txtQr = view!!.findViewById(R.id.txtCam)
        qrScanIntegrator = IntentIntegrator.forSupportFragment(this)
        qrScanIntegrator.setOrientationLocked(false)
        qrScanIntegrator.setPrompt(getString(R.string.scan_a_code))
        qrScanIntegrator.setCameraId(0)
        qrScanIntegrator.setBeepEnabled(false)
        qrScanIntegrator.setBarcodeImageEnabled(true)
    }

    private fun initListners() {
        btnScan.setOnClickListener { performAction() }
    }

    private fun performAction() {
        qrScanIntegrator.initiateScan()
    }

    private fun checkCamera() : Boolean{
        return context!!.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    private fun refreshCamera(){
        if (!checkCamera()) {
            imgQr.setImageResource(R.drawable.nfc_not_av)
            txtQr.setText(R.string.camera_na)
        } else {
            imgQr.setImageResource(R.drawable.nfc_on)
            txtQr.setText(R.string.camera_av)
        }
    }

    private fun openRoom(message : String) {
        id_roomQ = message
        startActivity(Intent(activity, LabActivity::class.java))
    }

    companion object{
        var id_roomQ : String = ""
    }
}