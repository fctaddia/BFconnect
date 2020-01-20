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
import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.tech.Ndef
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.app.bfconnect.R
import com.app.bfconnect.helper.NfcActivity
import java.io.IOException
import java.nio.charset.Charset

class NfcWriteFragment : DialogFragment() {

    private var mTvMessage: TextView? = null
    private var mListener: Listener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_write, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {

        mTvMessage = view.findViewById<View>(R.id.tv_message) as TextView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as NfcActivity
        mListener!!.onDialogDisplayed()
    }

    override fun onDetach() {
        super.onDetach()
        mListener!!.onDialogDismissed()
    }

    fun onNfcDetected(ndef: Ndef, messageToWrite: String) {
        writeToNfc(ndef, messageToWrite)
    }


    private fun writeToNfc(ndef: Ndef?, message: String) {

        mTvMessage!!.text = getString(R.string.message_write_progress)
        if (ndef != null) {

            try {
                ndef.connect()
                val mimeRecord = NdefRecord.createMime("BFconnect TAG", message.toByteArray(Charset.forName("US-ASCII")))
                ndef.writeNdefMessage(NdefMessage(mimeRecord))
                ndef.close()
                //Write Successful
                mTvMessage!!.text = getString(R.string.message_write_success)

            } catch (e: IOException) {
                e.printStackTrace()
                mTvMessage!!.text = getString(R.string.message_write_error)

            } catch (e: FormatException) {
                e.printStackTrace()
                mTvMessage!!.text = getString(R.string.message_write_error)
            }
        }
    }

    companion object {

        val TAG = NfcWriteFragment::class.java.simpleName

        fun newInstance(): NfcWriteFragment {

            return NfcWriteFragment()
        }
    }
}