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
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.bfconnect.customview.recycler.DataModel
import com.app.bfconnect.customview.recycler.RecyclerAdapter
import com.app.bfconnect.helper.AddressActivity
import com.app.bfconnect.R
import com.app.bfconnect.customview.tab.launchWithFallback

class HomeFragment : Fragment() {

    private lateinit var rvSites: RecyclerView
    private lateinit var rvAdrB: RecyclerView
    private lateinit var rvPrjB: RecyclerView
    private lateinit var rvAdrF: RecyclerView
    private lateinit var rvPrjF: RecyclerView

    private val listSites = listArray(arrayOf(R.drawable.ic_sito, R.drawable.ic_asl, R.drawable.ic_smd, R.drawable.ic_erasmus, R.drawable.ic_legalita, R.drawable.ic_cisco),5)
    private val listAdrB = listArray(arrayOf(R.drawable.ic_banner_informatica,R.drawable.ic_banner_chimica,R.drawable.ic_banner_meccanica,R.drawable.ic_banner_elettronica),3)
    private val listPrjB = listArray(arrayOf(R.drawable.ic_desi,R.drawable.ic_mast,R.drawable.ic_carpigiani,R.drawable.ic_filo,R.drawable.ic_opus,R.drawable.ic_texa,R.drawable.ic_stem),6)
    private val listAdrF = listArray(arrayOf(R.drawable.cxc,R.drawable.banner_qualifiche,R.drawable.apparati),2)
    private val listPrjF = listArray(arrayOf(R.drawable.ic_texa,R.drawable.ic_opus),1)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setRecyclerView()
        listRecyclerView()
    }


    private fun initRecyclerView() {
        rvSites = view!!.findViewById(R.id.rv1)
        rvAdrB = view!!.findViewById(R.id.rv2)
        rvPrjB = view!!.findViewById(R.id.rv3)
        rvAdrF = view!!.findViewById(R.id.rv4)
        rvPrjF = view!!.findViewById(R.id.rv5)
        rvSites.isNestedScrollingEnabled = false
        rvAdrB.isNestedScrollingEnabled = false
        rvPrjB.isNestedScrollingEnabled = false
        rvAdrF.isNestedScrollingEnabled = false
        rvPrjF.isNestedScrollingEnabled = false
    }

    private fun setRecyclerView() {
        rvSites.setHasFixedSize(true)
        rvSites.setItemViewCacheSize(10)
        val mLayoutManager1 = LinearLayoutManager(activity)
        mLayoutManager1.orientation = LinearLayoutManager.HORIZONTAL
        rvSites.layoutManager = mLayoutManager1
        rvSites.adapter = RecyclerAdapter(listSites)

        rvAdrB.setHasFixedSize(true)
        rvAdrB.setItemViewCacheSize(10)
        val mLayoutManager2 = LinearLayoutManager(activity)
        mLayoutManager2.orientation = LinearLayoutManager.HORIZONTAL
        rvAdrB.layoutManager = mLayoutManager2
        rvAdrB.adapter = RecyclerAdapter(listAdrB)

        rvPrjB.setHasFixedSize(true)
        rvPrjB.setItemViewCacheSize(10)
        val mLayoutManager3 = LinearLayoutManager(activity)
        mLayoutManager3.orientation = LinearLayoutManager.HORIZONTAL
        rvPrjB.layoutManager = mLayoutManager3
        rvPrjB.adapter = RecyclerAdapter(listPrjB)

        rvAdrF.setHasFixedSize(true)
        rvAdrF.setItemViewCacheSize(10)
        val mLayoutManager4 = LinearLayoutManager(activity)
        mLayoutManager4.orientation = LinearLayoutManager.HORIZONTAL
        rvAdrF.layoutManager = mLayoutManager4
        rvAdrF.adapter = RecyclerAdapter(listAdrF)

        rvPrjF.setHasFixedSize(true)
        rvPrjF.setItemViewCacheSize(10)
        val mLayoutManager5 = LinearLayoutManager(activity)
        mLayoutManager5.orientation = LinearLayoutManager.HORIZONTAL
        rvPrjF.layoutManager = mLayoutManager5
        rvPrjF.adapter = RecyclerAdapter(listPrjF)
    }

    private fun listRecyclerView() {
        rvSites.addOnItemClickListener(object : OnItemClickListener {
            val customTabsIntent = CustomTabsIntent.Builder().build()
            override fun onItemClicked(position: Int, view: View) {

                when (listSites[position].getImgSrc()) {
                    R.drawable.ic_sito -> { customTabsIntent.launchWithFallback(requireActivity(), Uri.parse(getString(R.string.url_bfsite))) }
                    R.drawable.ic_asl -> { customTabsIntent.launchWithFallback(requireActivity(), Uri.parse(getString(R.string.url_asl))) }
                    R.drawable.ic_smd -> { customTabsIntent.launchWithFallback(requireActivity(), Uri.parse(getString(R.string.url_smd))) }
                    R.drawable.ic_erasmus -> { customTabsIntent.launchWithFallback(requireActivity(), Uri.parse(getString(R.string.url_erasmus))) }
                    R.drawable.ic_ele_b -> { customTabsIntent.launchWithFallback(requireActivity(), Uri.parse(getString(R.string.url_el))) }
                    R.drawable.ic_cisco -> { customTabsIntent.launchWithFallback(requireActivity(), Uri.parse(getString(R.string.url_cisco))) }
                }
            }
        })

        rvAdrB.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {

                when (listAdrB[position].getImgSrc()) {
                    R.drawable.ic_banner_informatica -> { modeNoDes = false; arrayPage[0] = true ; modeAdrPrj = false; startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.ic_banner_chimica -> { modeNoDes = false; arrayPage[1] = true ; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.ic_banner_meccanica -> { modeNoDes = false; arrayPage[2] = true ; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.ic_banner_elettronica -> { modeNoDes = false; arrayPage[3] = true ; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                }
            }
        })

        rvPrjB.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {

                when (listPrjB[position].getImgSrc()) {
                    R.drawable.ic_desi -> { modeNoDes = false; arrayPage[7] = true ; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.ic_mast -> { modeNoDes = false; arrayPage[8] = true ; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.ic_carpigiani -> { modeNoDes = false; arrayPage[9] = true ; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.ic_filo -> { modeNoDes = false; arrayPage[10] = true ; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.ic_opus -> { modeNoDes = false; arrayPage[11] = true ; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.ic_texa -> { modeNoDes = false; arrayPage[12] = true ; modeAdrPrj = false; startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.ic_stem -> { modeNoDes = false; arrayPage[13] = true ; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                }
            }
        })

        rvAdrF.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {

                when (listAdrF[position].getImgSrc()) {
                    R.drawable.cxc -> { modeNoDes = false; arrayPage[4] = true ; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.apparati -> { modeNoDes = false; arrayPage[5] = true ; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.banner_qualifiche -> { modeNoDes = false; arrayPage[6] = true ; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                }
            }
        })

        rvPrjF.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {

                when (listPrjF[position].getImgSrc()) {
                    R.drawable.ic_toyota -> { modeNoDes = false; toyota = true; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.ic_magneti -> { modeNoDes = false; magneti = true; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.ic_texa -> { modeNoDes = false; project = true; arrayPage[12] = true; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.ic_opus -> { modeNoDes = false; project = true; arrayPage[11] = true; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }

                }
            }
        })
    }

    interface OnItemClickListener { fun onItemClicked(position: Int, view: View) }


    private fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object :
            RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                view.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                }
            }
        })
    }

    private fun listArray(arrayImg: Array<Int>, num: Int): ArrayList<DataModel> {
        val objList = ArrayList<DataModel>()
        var dm: DataModel?

        for (i in 0..num) {
            dm = DataModel()
            dm.setImgSrc(arrayImg[i])
            objList.add(dm)
        }

        return objList
    }

    companion object {
        val TAG: String = HomeFragment::class.java.simpleName
        fun newInstance() = HomeFragment()
        val arrayPage : Array<Boolean> = arrayOf(false,false,false,false,false,false,false,false,false,false,false,false,false,false)
        val arrayAdr : Array<String> = arrayOf("informatica","chimica","meccanica","elettronica","manutenzione","meccanico","elettronico","desi","mast","carpigiani","filosofia","opusfacere","texa","stem")
        var modeNoDes: Boolean = true
        var modeAdrPrj: Boolean = true
        var project: Boolean = false
        var toyota: Boolean = false
        var magneti: Boolean = false
    }
}

