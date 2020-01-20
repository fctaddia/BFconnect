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

package com.app.bfconnect.customview.spinner

import android.content.Context
import android.content.res.Resources.Theme
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner

class CustomSpinner : AppCompatSpinner {

    var itemClickListener: OnItemClickListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, mode: Int) : super(context, mode)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, mode: Int) :
            super(context, attrs, defStyleAttr, mode)

    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            mode: Int,
            popupTheme: Theme?
    ) : super(context, attrs, defStyleAttr, mode, popupTheme)

    override fun setOnItemClickListener(l: OnItemClickListener?) {
        this.itemClickListener = l
    }

    override fun setSelection(position: Int) {
        super.setSelection(position)
        performItemClick()
    }

    override fun setSelection(position: Int, animate: Boolean) {
        super.setSelection(position, animate)
        if (animate) {
            performItemClick()
        }
    }

    private fun performItemClick() {
        itemClickListener?.onItemClick(this, selectedView, selectedItemPosition, selectedItemId)
    }

}