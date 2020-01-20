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

package com.app.bfconnect.nfc

import android.content.Context
import android.content.Intent
import android.nfc.FormatException
import android.nfc.tech.Ndef
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.app.bfconnect.R
import com.app.bfconnect.helper.LabActivity
import com.app.bfconnect.helper.NfcActivity
import java.io.IOException

class NfcReadFragment : DialogFragment() {

    private var mTvMessage: TextView? = null
    private var mListener: Listener? = null
    private var message : String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_read, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) { mTvMessage = view.findViewById<View>(R.id.tv_message) as TextView }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as NfcActivity
        mListener!!.onDialogDisplayed()
    }

    override fun onDetach() {
        super.onDetach()
        mListener!!.onDialogDismissed()
    }

    fun onNfcDetected(ndef: Ndef) { readFromNFC(ndef) }

    private fun readFromNFC(ndef: Ndef) {
        try {
            ndef.connect()
            val ndefMessage = ndef.ndefMessage
            message = String(ndefMessage.records[0].payload)
            Log.d(TAG, "readFromNFC: $message")
            mTvMessage!!.text = "TAG letto correttamente"
            openRoom(message!!)
            ndef.close()
        } catch (e: IOException) { e.printStackTrace() } catch (e: FormatException) { e.printStackTrace() }
    }

    private fun openRoom(message : String) {
        id_room = message
        mode = true
        startActivity(Intent(activity, LabActivity::class.java))
    }

    companion object {
        val TAG = NfcReadFragment::class.java.simpleName
        fun newInstance(): NfcReadFragment { return NfcReadFragment() }
        var id_room : String = ""
        var mode : Boolean = false
    }
}