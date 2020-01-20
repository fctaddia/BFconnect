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

import android.os.AsyncTask
import android.util.Log

class KillerTask<T>(val task: () -> T, val onSuccess: (T) -> Any = {}, val onFailed: (Exception?) -> Any = {}) : AsyncTask<Void, Void, T>() {

    private var exception: Exception? = null

    companion object {
        private val TAG = "KillerTask"
    }

    /**
     * Override AsyncTask's function doInBackground
     */
    override fun doInBackground(vararg params: Void): T? {
        try {
            Log.wtf(TAG, "Enter to doInBackground")
            return run { task() }
        } catch (e: Exception) {
            Log.wtf(TAG, "Error in background task")
            exception = e
            return null
        }
    }

    /**
     * Override AsyncTask's function onPostExecute
     */
    override fun onPostExecute(result: T) {
        Log.wtf(TAG, "Enter to onPostExecute")
        if (!isCancelled) { // task not cancelled
            if (exception != null) { // fail
                Log.wtf(TAG, "Failure with Exception")
                run { onFailed(exception) }
            } else { // success
                Log.wtf(TAG, "Success")
                run { onSuccess(result) }
            }
        } else { // task cancelled
            Log.wtf(TAG, "Failure with RuntimeException caused by task cancelled")
            run { onFailed(RuntimeException("Task was cancelled")) }
        }
    }

    /**
     * Execute AsyncTask
     */
    fun go() {
        execute()
    }

    /**
     * Cancel AsyncTask
     */
    fun cancel() {
        cancel(true)
    }

}