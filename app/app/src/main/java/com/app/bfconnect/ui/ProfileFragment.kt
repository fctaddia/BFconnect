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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.bfconnect.customview.recycler.DataModel
import com.app.bfconnect.customview.recycler.RecyclerAdapter
import com.app.bfconnect.database.DbColumns
import com.app.bfconnect.database.DbHandler
import com.app.bfconnect.R
import com.app.bfconnect.helper.AddressActivity
import com.app.bfconnect.helper.LabActivity
import com.app.bfconnect.nfc.NfcReadFragment
import com.app.bfconnect.ui.HomeFragment.Companion.arrayPage
import com.app.bfconnect.ui.HomeFragment.Companion.modeAdrPrj
import com.app.bfconnect.ui.HomeFragment.Companion.modeNoDes

class ProfileFragment : Fragment() {

    private lateinit var thiscontext: Context
    private lateinit var dbHandler: DbHandler
    private lateinit var rvFav: RecyclerView
    private lateinit var rvRoom: RecyclerView
    private val arrStr = Array(14) { "" }
    private val arrRoom = Array(9) { "" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        thiscontext = container!!.context
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (start) {
            initComponents()
            initRecyclerView()
            clickOnRv()
        } ; super.onViewCreated(view, savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        this.onCreate(null)
        start = false
    }

    override fun onResume() {
        super.onResume()
        this.onCreate(null)
        if (!start) {
            for (i in arrStr.indices) { arrStr[i] = null.toString() }
            for (i in arrRoom.indices) { arrRoom[i] = null.toString() }
            removeAllItemsRoom()
            removeAllItemsFav()
            createObjects()
        } ; start = false
    }

    /**
     * Management Initialize
     */
    private fun initComponents() { dbHandler = DbHandler(thiscontext, null) }

    private fun initRecyclerView() {

        rvFav = view!!.findViewById(R.id.rvFav)
        rvFav.isNestedScrollingEnabled = false
        rvFav.setHasFixedSize(true)
        rvFav.setItemViewCacheSize(20)
        val listDataModel1 = listFavCard()
        val mLayoutManager1 = LinearLayoutManager(activity)
        mLayoutManager1.orientation = LinearLayoutManager.HORIZONTAL
        rvAd = RecyclerAdapter.CustomRA(listDataModel1)
        rvFav.layoutManager = mLayoutManager1
        rvFav.adapter = rvAd

        rvRoom = view!!.findViewById(R.id.rvRoom)
        rvRoom.isNestedScrollingEnabled = false
        rvRoom.setHasFixedSize(true)
        rvRoom.setItemViewCacheSize(20)
        val listDataModel2 = addRoomRV()
        val mLayoutManager2 = LinearLayoutManager(activity)
        mLayoutManager2.orientation = LinearLayoutManager.HORIZONTAL
        rvAd1 = RecyclerAdapter.CustomRA(listDataModel2)
        rvRoom.layoutManager = mLayoutManager2
        rvRoom.adapter = rvAd1
    }

    private fun createObjects() {
        initComponents()
        initRecyclerView()
        clickOnRv()
    }


    /**
     * Functions for Refresh
     */
    private fun refreshFavourites() {
        val cursor = dbHandler.getAllFav()
        var i = 0
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val name: String = cursor.getString(cursor.getColumnIndex(DbColumns.DbFav.ADDRESS))
                arrStr[i] = name
                cursor.moveToNext()
                i++
            }
        } ; cursor.close()
    }

    private fun refreshRoom() {
        val cursor = dbHandler.getAllRooms()
        var i = 0
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val name1: String = cursor.getString(cursor.getColumnIndex(DbColumns.DbRoom.ID))
                arrRoom[i] = name1
                cursor.moveToNext()
                i++
            }
        } ; cursor.close()
    }


    /**
     * Management RecyclerView
     */

    interface OnItemClickListener { fun onItemClicked(position: Int, view: View) }

    private fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object :
            RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) { view.setOnClickListener(null) }
            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                }
            }
        })
    }

    private fun clickOnRv() {
        rvFav.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                when (listFavCard()[position].getImgSrc()) {
                    R.drawable.ic_banner_informatica -> { modeNoDes = false; arrayPage[0] = true; modeAdrPrj = false; startActivity(Intent(activity, AddressActivity::class.java))}
                    R.drawable.ic_banner_chimica -> { modeNoDes = false; arrayPage[1] = true; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java))}
                    R.drawable.ic_banner_meccanica -> { modeNoDes = false; arrayPage[2] = true; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.ic_banner_elettronica -> { modeNoDes = false; arrayPage[3] = true; modeAdrPrj = false;startActivity (Intent(activity, AddressActivity::class.java)) }
                    R.drawable.ic_desi -> { modeNoDes = false; arrayPage[7] = true ; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.ic_mast -> { modeNoDes = false; arrayPage[8] = true ; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.ic_carpigiani -> { modeNoDes = false; arrayPage[9] = true ; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.ic_filo -> { modeNoDes = false; arrayPage[10] = true ; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.ic_opus -> { modeNoDes = false; arrayPage[11] = true ; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.ic_texa -> { modeNoDes = false; arrayPage[12] = true ; modeAdrPrj = false; startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.ic_stem -> { modeNoDes = false; arrayPage[13] = true ; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.cxc -> { modeNoDes = false; arrayPage[4] = true ; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.banner_qualifiche -> { modeNoDes = false; arrayPage[6] = true ; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                    R.drawable.apparati -> { modeNoDes = false; arrayPage[5] = true ; modeAdrPrj = false;startActivity(Intent(activity, AddressActivity::class.java)) }
                }
            }
        })

        rvRoom.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                when (addRoomRV()[position].getImgSrc()) {
                    R.drawable.li1 -> {
                        NfcReadFragment.id_room = "li15"
                        NfcReadFragment.mode = true
                        startActivity(Intent(activity, LabActivity::class.java)) }
                    R.drawable.lt1 -> {
                        NfcReadFragment.id_room = "lt14"
                        NfcReadFragment.mode = true
                        startActivity(Intent(activity, LabActivity::class.java)) }
                    R.drawable.ls1 -> {
                        NfcReadFragment.id_room = "ls16"
                        NfcReadFragment.mode = true
                        startActivity(Intent(activity, LabActivity::class.java)) }
                    R.drawable.lc1 -> {
                        NfcReadFragment.id_room = "lc1"
                        NfcReadFragment.mode = true
                        startActivity(Intent(activity, LabActivity::class.java)) }
                    R.drawable.lch1 -> {
                        NfcReadFragment.id_room = "lc2"
                        NfcReadFragment.mode = true
                        startActivity(Intent(activity, LabActivity::class.java)) }
                    R.drawable.lchi1 -> {
                        NfcReadFragment.id_room = "lc3"
                        NfcReadFragment.mode = true
                        startActivity(Intent(activity, LabActivity::class.java)) }
                    R.drawable.camera1 -> {
                        NfcReadFragment.id_room = "lca"
                        NfcReadFragment.mode = true
                        startActivity(Intent(activity, LabActivity::class.java)) }
                    R.drawable.elet1 -> {
                        NfcReadFragment.id_room = "le1"
                        NfcReadFragment.mode = true
                        startActivity(Intent(activity, LabActivity::class.java)) }
                    R.drawable.elet21 -> {
                        NfcReadFragment.id_room = "le2"
                        NfcReadFragment.mode = true
                        startActivity(Intent(activity, LabActivity::class.java)) }
                }
            }
        })
    }

    private fun listFavCard(): ArrayList<DataModel> {
        var dm: DataModel?
        refreshFavourites()
        var num = 0
        for (i in arrStr.indices) { if (arrStr[i].isNotEmpty()) { num++ } }

        for (i in 0 until num) {
            when (arrStr[i]) {
                "informatica" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.ic_banner_informatica)
                    objListFav.add(dm)
                }
                "chimica" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.ic_banner_chimica)
                    objListFav.add(dm)
                }
                "meccanica" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.ic_banner_meccanica)
                    objListFav.add(dm)
                }
                "elettronica" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.ic_banner_elettronica)
                    objListFav.add(dm)
                }
                "manutenzione" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.cxc)
                    objListFav.add(dm)
                }
                "meccanico" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.banner_qualifiche)
                    objListFav.add(dm)
                }
                "elettronico" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.apparati)
                    objListFav.add(dm)
                }
                "desi" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.ic_desi)
                    objListFav.add(dm)
                }
                "mast" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.ic_mast)
                    objListFav.add(dm)
                }
                "carpigiani" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.ic_carpigiani)
                    objListFav.add(dm)
                }
                "filosofia" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.ic_filo)
                    objListFav.add(dm)
                }
                "opusfacere" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.ic_opus)
                    objListFav.add(dm)
                }
                "texa" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.ic_texa)
                    objListFav.add(dm)
                }
                "stem" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.ic_stem)
                    objListFav.add(dm)
                }
            }
        } ; return objListFav
    }

    private fun addRoomRV(): ArrayList<DataModel> {
        var dm: DataModel?
        refreshRoom()
        var num = 0
        for (i in arrRoom.indices) { if (arrRoom[i].isNotEmpty()) { num ++ } }
        for (i in 0 until num) {
            when (arrRoom[i]) {
                "lt14" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.lt1)
                    objListRoom.add(dm)
                }
                "li15" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.li1)
                    objListRoom.add(dm)
                }
                "ls16" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.ls1)
                    objListRoom.add(dm)
                }
                "lc1" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.lc1)
                    objListRoom.add(dm)
                }
                "lc2" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.lch1)
                    objListRoom.add(dm)
                }
                "lc3" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.lchi1)
                    objListRoom.add(dm)
                }
                "lca" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.camera1)
                    objListRoom.add(dm)
                }
                "le1" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.elet1)
                    objListRoom.add(dm)
                }
                "le2" -> {
                    dm = DataModel()
                    dm.setImgSrc(R.drawable.elet21)
                    objListRoom.add(dm)
                }
            }
        } ; return objListRoom
    }

    companion object {
        val TAG: String = ProfileFragment::class.java.simpleName
        fun newInstance() = ProfileFragment()
        private lateinit var rvAd: RecyclerAdapter.CustomRA
        private lateinit var rvAd1: RecyclerAdapter.CustomRA
        private val objListFav = ArrayList<DataModel>()
        private val objListRoom = ArrayList<DataModel>()
        var start : Boolean = true
        fun removeAllItemsFav() { objListFav.clear() ; rvAd.notifyDataSetChanged() }
        fun removeAllItemsRoom() { objListRoom.clear() ; rvAd1.notifyDataSetChanged() }
    }
}


