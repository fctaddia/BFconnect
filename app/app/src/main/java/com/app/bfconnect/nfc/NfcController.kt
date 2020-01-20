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
import android.nfc.NfcAdapter
import android.util.Log

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class NfcController(private val mAppContext: Context) {

    fun checkNfcPowerStatus(): Boolean {
        return checkNfcPowerStatus(mAppContext)
    }

    fun enableNfcPower(isEnable: Boolean): Boolean {
        Log.d(TAG, "enableNfcPower($isEnable)")

        return powerNfc(isEnable, mAppContext)
    }

    companion object {
        private const val TAG = "NfcController"

        fun checkNfcPowerStatus(context: Context): Boolean {
            val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
            var enabled = false

            if (nfcAdapter != null) {
                enabled = nfcAdapter.isEnabled
            }

            return enabled
        }

        fun powerNfc(isOn: Boolean, context: Context): Boolean {
            var success = false
            val nfcAdapter = NfcAdapter.getDefaultAdapter(context)

            if (nfcAdapter != null) {
                val NfcManagerClass: Class<*>
                val setNfcEnabled: Method

                try {
                    NfcManagerClass = Class.forName(nfcAdapter.javaClass.name)
                    setNfcEnabled = NfcManagerClass.getDeclaredMethod(
                        if (isOn)
                            "enable"
                        else
                            "disable"
                    )
                    setNfcEnabled.isAccessible = true
                    success = setNfcEnabled.invoke(nfcAdapter) as Boolean
                } catch (e: ClassNotFoundException) {
                    Log.e(TAG, Log.getStackTraceString(e))
                } catch (e: NoSuchMethodException) {
                    Log.e(TAG, Log.getStackTraceString(e))
                } catch (e: IllegalArgumentException) {
                    Log.e(TAG, Log.getStackTraceString(e))
                } catch (e: IllegalAccessException) {
                    Log.e(TAG, Log.getStackTraceString(e))
                } catch (e: InvocationTargetException) {
                    Log.e(TAG, Log.getStackTraceString(e))
                }

            }

            return success
        }
    }
}