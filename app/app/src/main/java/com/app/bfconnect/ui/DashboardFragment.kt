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
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ResourceCursorAdapter
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.app.bfconnect.R
import com.app.bfconnect.helper.AboutActivity
import com.app.bfconnect.helper.BookingActivity
import com.app.bfconnect.helper.NfcActivity
import com.app.bfconnect.helper.QrActivity
import kotlin.math.roundToInt


class DashboardFragment : Fragment() {

    private lateinit var cvNFC : CardView
    private lateinit var cvQR : CardView
    private lateinit var cvPR : CardView
    private lateinit var cvAB : CardView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
    }

    private fun initViews() {
        cvNFC = view!!.findViewById(R.id.cv_nfc)
        cvQR = view!!.findViewById(R.id.cv_qr)
        cvPR = view!!.findViewById(R.id.cv_prenota)
        cvAB = view!!.findViewById(R.id.cv_about)
    }

    private fun initListeners() {
        cvNFC.setOnClickListener { startActivity(Intent(activity,NfcActivity::class.java)) }
        cvQR.setOnClickListener { startActivity(Intent(activity,QrActivity::class.java)) }
        cvAB.setOnClickListener { startActivity(Intent(activity,AboutActivity::class.java)) }
        cvPR.setOnClickListener { toastBook() }
    }

    private fun toastBook() {
        val r = resources
        val dip = TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 130f, r.displayMetrics).roundToInt()
        val toast : Toast = Toast.makeText(activity, "Pagina non disponibile", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, dip)
        toast.show()
        //startActivity(Intent(activity, BookingActivity::class.java))
    }

    companion object {
        val TAG: String = DashboardFragment::class.java.simpleName
        fun newInstance() = DashboardFragment()
    }
}