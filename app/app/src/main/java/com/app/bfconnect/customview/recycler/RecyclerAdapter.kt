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

package com.app.bfconnect.customview.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.bfconnect.AppConstants
import com.app.bfconnect.ui.HomeFragment
import com.app.bfconnect.R

class RecyclerAdapter(listDataModel: ArrayList<DataModel>) :
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    private var listDataModel = ArrayList<DataModel>()

    init { this.listDataModel = listDataModel }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return if (HomeFragment.modeAdrPrj) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false)
            MyViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_imgadr, parent, false)
            MyViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val objDataModel = listDataModel[position]

        if (HomeFragment.modeAdrPrj) {
            holder.image.setImageResource(objDataModel.getImgSrc()!!)
        } else {
            holder.txtTitle.text = objDataModel.getRestaurantName()
            holder.txtDesc.text = objDataModel.getDesc()
            holder.image.setImageResource(objDataModel.getImgSrc()!!)
        }
    }

    override fun getItemCount(): Int { return listDataModel.size }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        internal lateinit var txtTitle: TextView
        internal lateinit var txtDesc: TextView
        var image: ImageView

        init {
            if (HomeFragment.modeAdrPrj) {
                image = view.findViewById<View>(R.id.imgRestaurant) as ImageView
            } else {
                txtTitle = view.findViewById<View>(R.id.txtRestaurantName) as TextView
                txtDesc = view.findViewById<View>(R.id.txtDesc) as TextView
                image = view.findViewById<View>(R.id.imgRestaurant) as ImageView
            }
        }
    }


    class CustomRA(listDataModel: ArrayList<DataModel>) :
        RecyclerView.Adapter<CustomRA.MyViewHolder>() {

        private var listDataModel = ArrayList<DataModel>()

        init { this.listDataModel = listDataModel }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

            return if (AppConstants.SharedPreferences.modeLabPro) {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_home, parent, false)
                MyViewHolder(view)
            } else {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_template1, parent, false)
                MyViewHolder(view)
            }
        }

        override fun getItemCount(): Int { return listDataModel.size }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val objDataModel = listDataModel[position]
            holder.image.setImageResource(objDataModel.getImgSrc()!!)
        }

        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            var image: ImageView = if (AppConstants.SharedPreferences.modeLabPro) {
                view.findViewById<View>(R.id.imgRestaurant) as ImageView
            } else {
                view.findViewById<View>(R.id.ciao) as ImageView
            }

        }
    }
}