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

package com.app.bfconnect.helper

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.app.bfconnect.AppConstants
import com.app.bfconnect.customview.recycler.DataModel
import com.app.bfconnect.customview.recycler.RecyclerAdapter
import com.app.bfconnect.database.DbColumns
import com.app.bfconnect.database.DbHandler
import com.app.bfconnect.database.DbKeys
import com.app.bfconnect.pdf.PdfHandler
import com.app.bfconnect.ui.HomeFragment
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_address.*
import com.app.bfconnect.R

class AddressActivity : AppCompatActivity() {

    private fun toast(message: String) { Toast.makeText(this@AddressActivity, message, Toast.LENGTH_SHORT).show() }

    companion object { val arrayAdr = arrayOfNulls<String>(14) }

    private var pdf = ""
    private var idAdr = 0
    private var address: String = ""
    private val proff : String = "Professionale"
    private val prj : String = "Progetto"
    private val dbHandler : DbHandler = DbHandler(this ,null)
    private var arrayAllstr : Array<Array<String>?> = arrayOf(AppConstants.arrayAddress.infoArray,AppConstants.arrayAddress.chiArray,AppConstants.arrayAddress.mecArray,AppConstants.arrayAddress.eleArray,AppConstants.arrayAddress.manuArray,AppConstants.arrayAddress.mecaArray,AppConstants.arrayAddress.eletArray,AppConstants.arrayAddress.desiArray,AppConstants.arrayAddress.mastArray,AppConstants.arrayAddress.carpArray,AppConstants.arrayAddress.filoArray,AppConstants.arrayAddress.opusArray,AppConstants.arrayAddress.texaArray,AppConstants.arrayAddress.stemArray)
    private var arrayAllint : Array<Array<Int>?> = arrayOf(AppConstants.arrayAddress.infoImg,AppConstants.arrayAddress.chiImg,AppConstants.arrayAddress.mecImg,AppConstants.arrayAddress.eleImg,AppConstants.arrayAddress.manuImg,AppConstants.arrayAddress.mecaImg,AppConstants.arrayAddress.eletImg,AppConstants.arrayAddress.desiImg,AppConstants.arrayAddress.mastImg,AppConstants.arrayAddress.carpImg,AppConstants.arrayAddress.filoImg,AppConstants.arrayAddress.opusImg,AppConstants.arrayAddress.texaImg,AppConstants.arrayAddress.stemImg)
    private var arrayDesL : Array<Int> = arrayOf(R.string.des_adr_info2,R.string.des_adr_chi2,R.string.des_adr_mecc2,R.string.des_adr_ele2,R.string.des_manu2,R.string.des_meca2,R.string.des_elet2,R.string.des_desi2,R.string.des_mast2,R.string.carp_carp2,R.string.des_filo2,R.string.des_opus2,R.string.des_texa2,R.string.des_stem2)
    private var arrayTitleDes : Array<Int> = arrayOf(R.string.name_adr_title_info,R.string.name_adr_title_chimica,R.string.name_adr_title_meccanica,R.string.name_adr_title_ele,R.string.name_adr_title_manu,R.string.name_adr_title_meca,R.string.name_adr_title_elet,R.string.project_desi,R.string.project_mast,R.string.project_carp,R.string.project_filo,R.string.project_opus,R.string.project_texa,R.string.project_stem)
    private var arrayKeys : Array<String> = arrayOf(DbKeys.address_key.informatica,DbKeys.address_key.chimica,DbKeys.address_key.meccanica,DbKeys.address_key.elettronica,DbKeys.address_key.manutenzione,DbKeys.address_key.meccanico,DbKeys.address_key.elettronico,DbKeys.projects_key.desi,DbKeys.projects_key.mast,DbKeys.projects_key.carpigiani,DbKeys.projects_key.filosofia,DbKeys.projects_key.opusfacere,DbKeys.projects_key.texa,DbKeys.projects_key.stem)
    private lateinit var toolbar: Toolbar
    private lateinit var btnPdf: MaterialButton
    private lateinit var txtDiploma: TextView
    private lateinit var txtScuola: TextView
    private lateinit var txtTitleAdr: TextView
    private lateinit var txtTitleDes: TextView
    private lateinit var txtDesAdr: TextView
    private lateinit var viewOther: LinearLayout
    private lateinit var txtViewOther: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        setTheme(R.style.AppThemeCustom)
        /* Init Functions */
        initViews()
        initListeners()
        initToolbar()
        /* Important for to open pdf */
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        /* Refresh Functions */
        refreshFavourites()
    }

    private fun initViews() {
        btnPdf = findViewById(R.id.btnPdf)
        txtDiploma = findViewById(R.id.txtDiploma)
        txtScuola = findViewById(R.id.txtScuola)
        txtTitleAdr = findViewById(R.id.title_adr)
        txtTitleDes = findViewById(R.id.title_des)
        txtDesAdr = findViewById(R.id.text_des1)
        viewOther = findViewById(R.id.view_other)
        recyclerView = findViewById(R.id.recyclerView)
        txtViewOther = findViewById(R.id.text_view_other)
        toolbar = findViewById(R.id.toolbar_nfc)
    }

    private fun initListeners() {
        btnPdf.setOnClickListener { startDownload(pdf) }
        openViewOther()
        val listDataModel = listArray(getAdrStr(), getAdrImg())
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(20)
        val mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = mLayoutManager
        recyclerView.adapter = RecyclerAdapter(listDataModel)
    }

    private fun initToolbar() {
        toolbar.apply {
            setSupportActionBar(this)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            loadMode()
        }
    }

    /* Start management RecyclerView */

    private fun refreshContents(arrayStr: Array<String>?, arrayImg: Array<Int>?) {
        val array1 : Array<String>? = arrayAllstr[idAdr]
        val array2 : Array<Int>? = arrayAllint[idAdr]
            for (i in 0..5) { arrayStr?.set(i, array1!![i]) }
            for (x in 0..2) { arrayImg?.set(x, array2!![x]) }
    }

    private fun getAdrStr(): Array<String>? {
        when (getAddress()) {
            "informatica" -> return AppConstants.arrayAddress.infoArray?.asList()?.toTypedArray()
            "chimica" -> return AppConstants.arrayAddress.chiArray?.asList()?.toTypedArray()
            "meccanica" -> return AppConstants.arrayAddress.mecArray?.asList()?.toTypedArray()
            "elettronica" -> return AppConstants.arrayAddress.eleArray?.asList()?.toTypedArray()
            "desi" -> return AppConstants.arrayAddress.desiArray?.asList()?.toTypedArray()
            "mast" -> return AppConstants.arrayAddress.mastArray?.asList()?.toTypedArray()
            "carpigiani" -> return AppConstants.arrayAddress.carpArray?.asList()?.toTypedArray()
            "opusfacere" -> return AppConstants.arrayAddress.opusArray?.asList()?.toTypedArray()
            "texa" -> return AppConstants.arrayAddress.texaArray?.asList()?.toTypedArray()
            "filosofia" -> return AppConstants.arrayAddress.filoArray?.asList()?.toTypedArray()
            "stem" -> return AppConstants.arrayAddress.stemArray?.asList()?.toTypedArray()
        } ; return AppConstants.arrayAddress.infoArray?.asList()?.toTypedArray()
    }

    private fun getAdrImg(): Array<Int>? {
        when (getAddress()) {
            "informatica" -> return AppConstants.arrayAddress.infoImg?.asList()?.toTypedArray()
            "chimica" -> return AppConstants.arrayAddress.chiImg?.asList()?.toTypedArray()
            "meccanica" -> return AppConstants.arrayAddress.mecImg?.asList()?.toTypedArray()
            "elettronica" -> return AppConstants.arrayAddress.eleImg?.asList()?.toTypedArray()
            "manutenzione" -> return AppConstants.arrayAddress.manuImg?.asList()?.toTypedArray()
            "desi" -> return AppConstants.arrayAddress.desiImg?.asList()?.toTypedArray()
            "mast" -> return AppConstants.arrayAddress.mastImg?.asList()?.toTypedArray()
            "carpigiani" -> return AppConstants.arrayAddress.carpImg?.asList()?.toTypedArray()
            "opusfacere" -> return AppConstants.arrayAddress.opusImg?.asList()?.toTypedArray()
            "texa" -> return AppConstants.arrayAddress.texaImg?.asList()?.toTypedArray()
            "filosofia" -> return AppConstants.arrayAddress.filoImg?.asList()?.toTypedArray()
            "stem" -> return AppConstants.arrayAddress.stemImg?.asList()?.toTypedArray()
            "meccanico"-> return AppConstants.arrayAddress.mecImg?.asList()?.toTypedArray()
            "elettronico"-> return AppConstants.arrayAddress.eletImg?.asList()?.toTypedArray()
        } ; return AppConstants.arrayAddress.infoImg?.asList()?.toTypedArray()
    }

    private fun listArray(arrayStr: Array<String>?, arrayImg: Array<Int>?): ArrayList<DataModel> {
        val objList = ArrayList<DataModel>()
        var dm: DataModel?
        var r = 0 ; var s = 1

        refreshContents(arrayStr, arrayImg)

        for (i in 0..2) {
            dm = DataModel()
            dm.setRestaurantName(arrayStr?.get(i + r))
            r += 1
            dm.setDesc(arrayStr?.get(i + s))
            s += 1
            dm.setImgSrc(arrayImg?.get(i))
            objList.add(dm)
        } ; return objList
    }
    /* End of management RecyclerView */

    private fun loadMode() {
        for (i in 0..13) { if (HomeFragment.arrayPage[i]) { loadContents(getAddress(),toolbar) ; pdf = getAddress() } }
    }

    private fun checkDesEmpty(): Boolean { return text_view_other.text == "Descrizione non presente" }

    private fun openViewOther() {
        var des: Int? = null
        var title = R.string.title_comp
        val icon = R.drawable.ic_skills

        if (checkDesEmpty()) {
            Log.d("Error Text", "Des not found")
            toast("Descrizione non disponibile")
        } else {
            getAddress()
            des = arrayDesL[idAdr]
            title = arrayTitleDes[idAdr]
        }

        viewOther.setOnClickListener {
            MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
                title(title)
                icon(icon)
                message(des)
                negativeButton(R.string.title_quit)
                cornerRadius(16f)
            }
        }
    }

    private fun startDownload(pdf: String) {
        PdfHandler(pdf, this).execute()
    }

    private fun getAddress() : String {
        var adr = ""
        for (i in 0..13) {
            if (HomeFragment.arrayPage[i]) {
                idAdr = i
                adr = HomeFragment.arrayAdr[i]
            }
        }
        return adr
    }

    private fun loadContents (message: CharSequence, toolbar: Toolbar) {
        when (message) {
            "informatica" -> {
                toolbar.setLogo(R.drawable.ic_info_toolbar)
                txtDesAdr.setText(R.string.des_adr_info1)
                txtTitleAdr.setText(R.string.name_adr_title_info)
                txtDiploma.setText(R.string.perito_info)
                txtScuola.setText(R.string.belluzzi)
                img_destra.setImageResource(R.drawable.ic_account_balance_black_24dp)
                img_sinistra.setImageResource(R.drawable.ic_school_black_24dp)
                text_view_other.visibility = View.VISIBLE
                view_other.visibility = View.VISIBLE
            }
            "chimica" -> {
                toolbar.setLogo(R.drawable.ic_chimica_b)
                txtDesAdr.setText(R.string.des_adr_chi1)
                txtTitleAdr.setText(R.string.name_adr_title_chimica)
                txtDiploma.setText(R.string.perito_chimico)
                txtScuola.setText(R.string.belluzzi)
                img_destra.setImageResource(R.drawable.ic_account_balance_black_24dp)
                img_sinistra.setImageResource(R.drawable.ic_school_black_24dp)
                text_view_other.visibility = View.VISIBLE
                view_other.visibility = View.VISIBLE
            }
            "meccanica" -> {
                toolbar.setLogo(R.drawable.ic_mecca_b)
                txtDesAdr.setText(R.string.des_adr_mecc1)
                txtTitleAdr.setText(R.string.name_adr_title_meccanica)
                txtDiploma.setText(R.string.perito_mecca)
                txtScuola.setText(R.string.belluzzi)
                img_destra.setImageResource(R.drawable.ic_account_balance_black_24dp)
                img_sinistra.setImageResource(R.drawable.ic_school_black_24dp)
                text_view_other.visibility = View.VISIBLE
                view_other.visibility = View.VISIBLE
            }
            "elettronica" -> {
                toolbar.setLogo(R.drawable.ic_ele_b)
                txtDesAdr.setText(R.string.des_adr_ele1)
                txtTitleAdr.setText(R.string.name_adr_title_ele)
                txtDiploma.setText(R.string.perito_ele)
                txtScuola.setText(R.string.belluzzi)
                img_destra.setImageResource(R.drawable.ic_account_balance_black_24dp)
                img_sinistra.setImageResource(R.drawable.ic_school_black_24dp)
                text_view_other.visibility = View.VISIBLE
                view_other.visibility = View.VISIBLE
            }
            "manutenzione" -> {
                txtDesAdr.setText(R.string.des_manu1)
                toolbar.setLogo(R.drawable.ic_bfconnect_horizontal)
                txtTitleAdr.setText(R.string.name_adr_title_manu)
                txtScuola.setText(R.string.fioravanti)
                txtDiploma.text = proff
                img_destra.setImageResource(R.drawable.ic_account_balance_black_24dp)
                img_sinistra.setImageResource(R.drawable.ic_school_black_24dp)
                text_view_other.visibility = View.VISIBLE
                view_other.visibility = View.VISIBLE
            }
            "meccanico" -> {
                txtDesAdr.setText(R.string.des_meca1)
                toolbar.setLogo(R.drawable.ic_bfconnect_horizontal)
                txtTitleAdr.setText(R.string.name_adr_title_meca)
                txtScuola.setText(R.string.fioravanti)
                txtDiploma.text = proff
                img_destra.setImageResource(R.drawable.ic_account_balance_black_24dp)
                img_sinistra.setImageResource(R.drawable.ic_school_black_24dp)
                text_view_other.visibility = View.VISIBLE
                view_other.visibility = View.VISIBLE
            }
            "elettronico" -> {
                txtDesAdr.setText(R.string.des_elet1)
                toolbar.setLogo(R.drawable.ic_info_toolbar)

                txtTitleAdr.setText(R.string.name_adr_title_elet)
                txtScuola.setText(R.string.fioravanti)
                txtDiploma.text = proff
                img_destra.setImageResource(R.drawable.ic_account_balance_black_24dp)
                img_sinistra.setImageResource(R.drawable.ic_school_black_24dp)
                text_view_other.visibility = View.VISIBLE
                view_other.visibility = View.VISIBLE
            }
            "desi" -> {
                toolbar.setLogo(R.drawable.ic_bfconnect_horizontal)
                txtScuola.setText(R.string.belluzzi)
                txtDiploma.text = prj
                img_destra.setImageResource(R.drawable.ic_account_balance_black_24dp)
                img_sinistra.setImageResource(R.drawable.ic_address)
                txtTitleAdr.setText(R.string.project_desi)
                txtTitleDes.setText(R.string.title_des)
                txtDesAdr.setText(R.string.des_desi1)
                text_view_other.visibility = View.VISIBLE
                view_other.visibility = View.VISIBLE
            }
            "mast" -> {
                toolbar.setLogo(R.drawable.ic_bfconnect_horizontal)
                txtScuola.setText(R.string.belluzzi)
                txtDiploma.text = prj
                img_destra.setImageResource(R.drawable.ic_account_balance_black_24dp)
                img_sinistra.setImageResource(R.drawable.ic_address)
                txtTitleAdr.setText(R.string.project_mast)
                txtTitleDes.setText(R.string.title_des)
                txtDesAdr.setText(R.string.des_mast1)
                text_view_other.visibility = View.VISIBLE
                view_other.visibility = View.VISIBLE
            }
            "carpigiani" -> {
                toolbar.setLogo(R.drawable.ic_bfconnect_horizontal)
                txtDiploma.text = prj
                txtScuola.setText(R.string.belluzzi)
                img_destra.setImageResource(R.drawable.ic_account_balance_black_24dp)
                img_sinistra.setImageResource(R.drawable.ic_address)
                txtTitleAdr.setText(R.string.project_carp)
                txtTitleDes.setText(R.string.title_des)
                txtDesAdr.setText(R.string.carp_carp1)
                text_view_other.visibility = View.VISIBLE
                view_other.visibility = View.VISIBLE
            }
            "filosofia" -> {
                toolbar.setLogo(R.drawable.ic_bfconnect_horizontal)
                //mettere immagini
                txtDiploma.text = prj
                txtScuola.setText(R.string.belluzzi)
                img_destra.setImageResource(R.drawable.ic_account_balance_black_24dp)
                img_sinistra.setImageResource(R.drawable.ic_address)
                txtTitleAdr.setText(R.string.project_filo)
                recyclerView.visibility = View.GONE
                txtTitleDes.visibility = View.GONE
                txtDesAdr.setText(R.string.des_filo1)
                text_view_other.visibility = View.VISIBLE
                view_other.visibility = View.VISIBLE
            }
            "opusfacere" -> {
                toolbar.setLogo(R.drawable.ic_bfconnect_horizontal)
                if (!HomeFragment.project) {
                    txtDiploma.text = prj
                    txtScuola.setText(R.string.belluzzi)
                    img_destra.setImageResource(R.drawable.ic_account_balance_black_24dp)
                    img_sinistra.setImageResource(R.drawable.ic_address)
                    txtTitleAdr.setText(R.string.project_opus)
                    txtTitleDes.setText(R.string.title_des)
                    txtDesAdr.setText(R.string.des_opus1)
                    text_view_other.visibility = View.VISIBLE
                    view_other.visibility = View.VISIBLE
                } else {
                    txtDiploma.text = prj
                    txtScuola.setText(R.string.fioravanti)
                    img_destra.setImageResource(R.drawable.ic_account_balance_black_24dp)
                    img_sinistra.setImageResource(R.drawable.ic_address)
                    txtTitleAdr.setText(R.string.project_opus)
                    txtTitleDes.setText(R.string.title_des)
                    txtDesAdr.setText(R.string.des_opus1)
                    text_view_other.visibility = View.VISIBLE
                    view_other.visibility = View.VISIBLE
                }
            }
            "texa" -> {
                toolbar.setLogo(R.drawable.ic_bfconnect_horizontal)
                if (!HomeFragment.project) {
                    txtDiploma.text = prj
                    txtScuola.setText(R.string.belluzzi)
                    img_destra.setImageResource(R.drawable.ic_account_balance_black_24dp)
                    img_sinistra.setImageResource(R.drawable.ic_address)
                    txtTitleAdr.setText(R.string.project_texa)
                    txtTitleDes.setText(R.string.title_des)
                    txtDesAdr.setText(R.string.des_texa1)
                    text_view_other.visibility = View.VISIBLE
                    view_other.visibility = View.VISIBLE
                } else {
                    txtDiploma.text = prj
                    txtScuola.setText(R.string.fioravanti)
                    img_destra.setImageResource(R.drawable.ic_account_balance_black_24dp)
                    img_sinistra.setImageResource(R.drawable.ic_address)
                    txtTitleAdr.setText(R.string.project_texa)
                    txtTitleDes.setText(R.string.title_des)
                    txtDesAdr.setText(R.string.des_texa1)
                    text_view_other.visibility = View.VISIBLE
                    view_other.visibility = View.VISIBLE
                }
            }
            "stem" -> {
                toolbar.setLogo(R.drawable.ic_bfconnect_horizontal)
                txtDiploma.text = prj
                txtScuola.setText(R.string.belluzzi)
                img_destra.setImageResource(R.drawable.ic_account_balance_black_24dp)
                img_sinistra.setImageResource(R.drawable.ic_address)
                txtTitleAdr.setText(R.string.project_stem)
                recyclerView.visibility = View.GONE
                txtTitleDes.visibility = View.GONE
                txtDesAdr.setText(R.string.des_stem1)
                text_view_other.visibility = View.VISIBLE
                view_other.visibility = View.VISIBLE
            }
        }
    }

    private fun refreshFavourites() {
        val cursor = dbHandler.getAllFav()
        var i = 0
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val name: String = cursor.getString(cursor.getColumnIndex(DbColumns.DbFav.ADDRESS))
                arrayAdr[i]  = name
                cursor.moveToNext()
                i++
            }
        } ; cursor.close()
    }

    override fun onPrepareOptionsMenu(menu:Menu):Boolean {
        val settingsItem = menu.findItem(R.id.fav)
        if (dbHandler.is_Fav(arrayKeys[idAdr])) { settingsItem.setIcon(R.drawable.ic_favorite_on) }
        else { settingsItem.setIcon(R.drawable.ic_favorite_off) }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean { menuInflater.inflate(R.menu.options_lab, menu) ; return true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.close_qrcode) {
            HomeFragment.modeNoDes = true
            HomeFragment.project = false
            HomeFragment.modeAdrPrj = true
            address = "" ; pdf = ""
            for (i in HomeFragment.arrayPage.indices) { HomeFragment.arrayPage[i] = false }
            onBackPressed() ; finish()
        } else {
           if (dbHandler.is_Fav(arrayKeys[idAdr])) {
               item.setIcon(R.drawable.ic_favorite_off)
               dbHandler.removeFav(arrayKeys[idAdr])
               refreshFavourites()
               toast("Rimosso dai Preferiti")
          } else {
               item.setIcon(R.drawable.ic_favorite_on)
               dbHandler.addFav(arrayKeys[idAdr])
               refreshFavourites()
               Toast.makeText(this, "Aggiunto hai Preferiti", Toast.LENGTH_LONG).show()
           }
        } ; return super.onOptionsItemSelected(item)
    }


}


