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

package com.app.bfconnect.customview.tab

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.FragmentActivity

/**
 * Opens the URL on a Custom Tab if possible. Otherwise fallback
 *
 * @param activity The host activity.
 * @param uri the Uri to be opened.
 * @param fallback a CustomTabFallback to be used if Custom Tabs is not available.
 */
fun CustomTabsIntent.launchWithFallback(activity: FragmentActivity, uri: Uri, fallback: CustomTabFallback? = null) {

    val packageName = CustomTabsHelper.getPackageNameToUse(activity)

    //If we cant find a package name, it means there is no browser that supports
    //Chrome Custom Tabs installed. So, we do the fallback
    if (packageName == null) {
        fallback?.openUri(activity, uri)
    } else {
        intent.setPackage(packageName)
        launchUrl(activity, uri)
    }
}