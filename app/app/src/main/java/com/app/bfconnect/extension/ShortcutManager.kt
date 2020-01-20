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

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import com.app.bfconnect.R


object SimpleShortcutManager {

    private val IDS = arrayOf("nfc", "qrcode", "prenota")

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun configShortcut(context: Context) {
        val shortcutManager = context.getSystemService(ShortcutManager::class.java)

        val nfc = ShortcutInfo.Builder(context, IDS[0])
            .setShortLabel(context.getString(R.string.shortcut_nfc_s))
            .setIcon(Icon.createWithResource(context, R.drawable.ic_rileva))
            .setIntent(Intent("NfcActivity"))
            .build()

        val qrcode = ShortcutInfo.Builder(context, IDS[1])
            .setShortLabel(context.getString(R.string.shortcut_qr_s))
            .setIcon(Icon.createWithResource(context, R.drawable.ic_qr))
            .setIntent(Intent("QrActivity"))
            .build()

        /*val prenota = ShortcutInfo.Builder(context, IDS[2])
            .setShortLabel(context.getString(R.string.shortcut_pr_s))
            .setIcon(Icon.createWithResource(context, R.drawable.ic_prenota))
            .setIntent(Intent("BookingActivity"))
            .build()*/

        shortcutManager?.dynamicShortcuts = arrayListOf(qrcode,nfc)
    }

}