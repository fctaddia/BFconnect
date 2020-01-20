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

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.app.bfconnect.AppConstants
import com.app.bfconnect.R
import com.app.bfconnect.customview.recycler.DataModel
import com.app.bfconnect.customview.recycler.RecyclerAdapter
import com.app.bfconnect.customview.tab.launchWithFallback
import com.app.bfconnect.database.DbColumns
import com.app.bfconnect.database.DbHandler
import com.app.bfconnect.database.DbKeys
import com.app.bfconnect.nfc.NfcReadFragment.Companion.id_room
import com.app.bfconnect.nfc.NfcReadFragment.Companion.mode
import com.app.bfconnect.ui.HomeFragment
import com.app.bfconnect.ui.QrcodeFragment.Companion.id_roomQ
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.activity_lab.*
import kotlinx.android.synthetic.main.activity_lab.text_view_other
import kotlinx.android.synthetic.main.activity_lab.title_adr
import kotlinx.android.synthetic.main.activity_lab.txtDiploma
import kotlinx.android.synthetic.main.activity_lab.txtScuola
import kotlinx.android.synthetic.main.card_lin.view.*

class LabActivity : AppCompatActivity(){

    private var message : CharSequence = ""
    private var num : Int = 0;
    private lateinit var rvPhoto : RecyclerView
    private lateinit var viewOther : LinearLayout
    private lateinit var cvLing1 : LinearLayout
    private lateinit var cvLing2 : LinearLayout
    private lateinit var cvLing3 : LinearLayout
    private lateinit var cvLing4 : LinearLayout
    private var arR = Array(14) {""}
    private lateinit var txtDes1 : TextView
    private val dbHandler : DbHandler = DbHandler(this,null)

    private fun toast(message: CharSequence) {
        Toast.makeText(this@LabActivity, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppConstants.SharedPreferences.modeLabPro = true
        setContentView(R.layout.activity_lab)
        findViewById<Toolbar>(R.id.toolbar_lab).apply {
            setSupportActionBar(this)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            setLogo(R.drawable.ic_bfconnect_horizontal)
        }
        viewOther = findViewById(R.id.view_other)
        txtDes1  = findViewById(R.id.text_des1)
        toast("Scorri l'immagine per vederne altre")
        checkMode()
        setArrayImg(message)
        setRecyclerView()
        openViewOther()
        refreshRoom()
        initViews()
        setLingImage(message)
        listnersLin(message)
    }

    private fun initViews() {
        cvLing1 = findViewById(R.id.java)
        cvLing2 = findViewById(R.id.cpp)
        cvLing3 = findViewById(R.id.php)
        cvLing4 = findViewById(R.id.sql)
    }

    private fun setArrayImg(message: CharSequence) {
        when (message) {
            DbKeys.rooms_key.lab_info -> {
                num = 5
                arrayImg = arrayOf(R.drawable.li1,R.drawable.li2,R.drawable.li3,R.drawable.li4,R.drawable.li5,R.drawable.li6)
            }
            DbKeys.rooms_key.lab_tps -> {
                num = 5
                arrayImg = arrayOf(R.drawable.lt1,R.drawable.lt2,R.drawable.lt3,R.drawable.lt4,R.drawable.lt5,R.drawable.lt6)
            }
            DbKeys.rooms_key.lab_sis -> {
                num = 4
                arrayImg = arrayOf(R.drawable.ls1,R.drawable.ls2,R.drawable.ls3,R.drawable.ls4,R.drawable.ls5)
            }
            DbKeys.rooms_key.lab_chi1 -> {
                num = 5
                arrayImg = arrayOf(R.drawable.lc1,R.drawable.lc2,R.drawable.lc3,R.drawable.lc4,R.drawable.lc5,R.drawable.lc6)
            }
            DbKeys.rooms_key.lab_chi2 -> {
                num = 3
                arrayImg = arrayOf(R.drawable.lch1,R.drawable.lch2,R.drawable.lch3,R.drawable.lch4)
            }
            DbKeys.rooms_key.lab_chi3 -> {
                num = 5
                arrayImg = arrayOf(R.drawable.lchi1,R.drawable.lchi2,R.drawable.lchi3,R.drawable.lchi4,R.drawable.lchi5,R.drawable.lchi6)
            }
            DbKeys.rooms_key.lab_camera -> {
                num = 3
                arrayImg = arrayOf(R.drawable.camera1,R.drawable.camera2,R.drawable.camera3,R.drawable.camera4)
            }
            DbKeys.rooms_key.lab_ele1 -> {
                num = 4
                arrayImg = arrayOf(R.drawable.elet1,R.drawable.elet2,R.drawable.elet3,R.drawable.elet4,R.drawable.elet6)
            }
            DbKeys.rooms_key.lab_ele2 -> {
                num = 5
                arrayImg = arrayOf(R.drawable.elet21,R.drawable.elet22,R.drawable.elet23,R.drawable.elet24,R.drawable.elet25,R.drawable.elet26)
            }
        }
    }


    private fun setRecyclerView() {
        rvPhoto = findViewById(R.id.rvl1)
        rvPhoto.setHasFixedSize(true)
        rvPhoto.isNestedScrollingEnabled = false
        rvPhoto.setItemViewCacheSize(10)
        val listDataModel = listArray(arrayImg, num)
        val mLayoutManager = LinearLayoutManager(this@LabActivity)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rvPhoto.layoutManager = mLayoutManager
        rvPhoto.adapter = RecyclerAdapter.CustomRA(listDataModel)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.options_qrcode, menu)
        return true
    }




    //Controllare bene se sono presenti tutti i boolean
    //Funzione per ascoltare la decisione dell'utente sulla BottomNavigationView
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        if (item.itemId == R.id.close_qrcode) {
            AppConstants.SharedPreferences.modeLabPro = false
            onBackPressed()
            finish()
        }

        return super.onOptionsItemSelected(item)

    }

    private fun checkMode() {
        when {
            mode -> {
                message = id_room
                checkRoom(id_room)
            }
            else -> {
                message = id_roomQ
                checkRoom(id_roomQ)
            }
        }
    }

    private fun setLingImage(message: CharSequence) {
        when (message) {
            DbKeys.rooms_key.lab_info -> {
                cvLing1.imgRestaurant.setImageResource(R.drawable.banner_java)
                cvLing2.imgRestaurant.setImageResource(R.drawable.banner_php)
                cvLing3.imgRestaurant.setImageResource(R.drawable.banner_sql)
                cvLing4.visibility=View.GONE
            }
            DbKeys.rooms_key.lab_tps -> {
                cvLing1.imgRestaurant.setImageResource(R.drawable.banner_js)
                cvLing2.imgRestaurant.setImageResource(R.drawable.banner_css)
                cvLing3.imgRestaurant.setImageResource(R.drawable.banner_html)
                cvLing4.visibility=View.GONE
            }
        }
    }

    private fun listnersLin(message: CharSequence) {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        when (message) {
            DbKeys.rooms_key.lab_info -> {
                cvLing1.setOnClickListener {
                    customTabsIntent.launchWithFallback(this, Uri.parse("https://netbeans.org/downloads/8.2/"))
                }
                cvLing2.setOnClickListener {
                    customTabsIntent.launchWithFallback(this, Uri.parse("https://www.php.net/"))
                }

                cvLing3.setOnClickListener {
                    customTabsIntent.launchWithFallback(this, Uri.parse("https://www.mysql.com/it/"))
                }
            }
            DbKeys.rooms_key.lab_tps -> {
                cvLing1.setOnClickListener {
                    customTabsIntent.launchWithFallback(this, Uri.parse("https://www.javascript.com/"))
                }
                cvLing2.setOnClickListener {
                    customTabsIntent.launchWithFallback(this, Uri.parse("https://www.w3schools.com/css/"))
                }

                cvLing3.setOnClickListener {
                    customTabsIntent.launchWithFallback(this, Uri.parse("https://www.w3schools.com/html/default.asp"))
                }
            }
            DbKeys.rooms_key.lab_sis -> {  }
        }
    }


    private fun checkRoom (message : String) {
        when(message) {
            DbKeys.rooms_key.lab_info -> {
                if (!dbHandler.is_Room_inDB(DbKeys.rooms_key.lab_info)){
                    dbHandler.addRoom(DbKeys.rooms_key.lab_info)
                    toast("Aggiunto nuovo Laboratorio in Profilo")
                }
                txtScuola.setText(R.string.belluzzi)
                txtDiploma.text = lab
                title_adr.text = name_lab_info[0]
                txtDes1.text = desViewOtherS[0]
                text_view_other.visibility = View.VISIBLE
                viewOther.visibility = View.VISIBLE
            }
            DbKeys.rooms_key.lab_tps -> {
                if (!dbHandler.is_Room_inDB(DbKeys.rooms_key.lab_tps)){
                    dbHandler.addRoom(DbKeys.rooms_key.lab_tps)
                    toast("Aggiunto nuovo Laboratorio in Profilo")
                }
                txtScuola.setText(R.string.belluzzi)
                txtDiploma.text = lab
                title_adr.text = name_lab_info[1]
                txtDes1.text = desViewOtherS[0]
                text_view_other.visibility = View.VISIBLE
                viewOther.visibility = View.VISIBLE
            }
            DbKeys.rooms_key.lab_sis -> {
                if (!dbHandler.is_Room_inDB(DbKeys.rooms_key.lab_sis)){
                    dbHandler.addRoom(DbKeys.rooms_key.lab_sis)
                    toast("Aggiunto nuovo Laboratorio in Profilo")
                }
                txtDes1.text = "Laboratorio all'avanguardia adatto ad una didattica innovativa. In questo laboratorio si utilizzano schede elettroniche, si effettua studio di macchine virtuali e si simulano reti di calcolatori."
                txtScuola.setText(R.string.belluzzi)
                txtDiploma.text = lab
                title_adr.text = name_lab_info[2]
                ling.visibility = View.GONE
            }
            DbKeys.rooms_key.lab_chi1 -> {
                if (!dbHandler.is_Room_inDB(DbKeys.rooms_key.lab_chi1)){
                    dbHandler.addRoom(DbKeys.rooms_key.lab_chi2)
                    toast("Aggiunto nuovo Laboratorio in Profilo")
                }
                txtDes1.text = "In questo laboratorio si svolgono esperienze per separare materiali (filtrazione, distillazione, cromatografia su carta) e studiarne le caratteristiche fisiche (densità..) e chimiche, facendo avvenire reazioni che permettono di spiegare fenomeni che avvengono nella vita di tutti i giorni."
                txtScuola.setText(R.string.belluzzi)
                txtDiploma.text = lab
                title_adr.text = name_room[3]
                ling.visibility = View.GONE
            }
            DbKeys.rooms_key.lab_chi2 -> {
                if (!dbHandler.is_Room_inDB(DbKeys.rooms_key.lab_chi2)){
                    dbHandler.addRoom(DbKeys.rooms_key.lab_chi2)
                    toast("Aggiunto nuovo Laboratorio in Profilo")
                }
                txtDes1.text = "In questo laboratorio si svolgono esperienze per separare materiali (filtrazione, distillazione, cromatografia su carta) e studiarne le caratteristiche fisiche (densità..) e chimiche, facendo avvenire reazioni che permettono di spiegare fenomeni che avvengono nella vita di tutti i giorni."
                txtScuola.setText(R.string.belluzzi)
                txtDiploma.text = lab
                title_adr.text = name_room[4]
                ling.visibility = View.GONE
            }
            DbKeys.rooms_key.lab_chi3 -> {
                if (!dbHandler.is_Room_inDB(DbKeys.rooms_key.lab_chi3)){
                    dbHandler.addRoom(DbKeys.rooms_key.lab_chi3)
                    toast("Aggiunto nuovo Laboratorio in Profilo")
                }
                txtDes1.text = "In questi laboratori si eseguono prove per riconoscere e determinare le quantità di sostanze presenti in campioni dapprima semplici e via via più complessi (fertilizzanti, acque, vino, olio...) attraverso metodi classici o tecniche strumentali. Inoltre si eseguono prove sui materiali, bilanci di materia ed energia e studi sui processi che avvengono negli impianti industriali."
                txtScuola.setText(R.string.belluzzi)
                txtDiploma.text = lab
                title_adr.text = name_room[5]
                ling.visibility = View.GONE
            }
            DbKeys.rooms_key.lab_ele1 -> {
                if (!dbHandler.is_Room_inDB(DbKeys.rooms_key.lab_ele1)){
                    dbHandler.addRoom(DbKeys.rooms_key.lab_ele1)
                    toast("Aggiunto nuovo Laboratorio in Profilo")
                }
                txtDes1.text = "Studio dei circuiti elettronici e della tecnologia dei componenti. Progettazione e realizzazione dei circuiti ed utilizzo della strumentazione per il collaudo"
                txtScuola.setText(R.string.belluzzi)
                txtDiploma.text = lab
                title_adr.text = name_room[2]
                ling.visibility = View.GONE
            }
            DbKeys.rooms_key.lab_ele2 -> {
                if (!dbHandler.is_Room_inDB(DbKeys.rooms_key.lab_ele2)){
                    dbHandler.addRoom(DbKeys.rooms_key.lab_ele2)
                    toast("Aggiunto nuovo Laboratorio in Profilo")
                }
                txtDes1.text = "Studio di linguaggi di programmazione ad alto e basso livello per microcontrollori, plc, sistemi programmabili. Studio della stabilita` dei sistemi e delle automazioni"
                txtScuola.setText(R.string.belluzzi)
                txtDiploma.text = lab
                title_adr.text = name_room[0]
                ling.visibility = View.GONE
            }
            DbKeys.rooms_key.lab_camera -> {
                if (!dbHandler.is_Room_inDB(DbKeys.rooms_key.lab_camera)){
                    dbHandler.addRoom(DbKeys.rooms_key.lab_camera)
                    toast("Aggiunto nuovo Laboratorio in Profilo")
                }
                txtDes1.text = "Studio dei campi elettromagnetici e misure con particolare riferimento alle normative sulla compatibilità elettromagnetica per la marcatura CE"
                txtScuola.setText(R.string.belluzzi)
                txtDiploma.text = lab
                title_adr.text = name_room[1]
                ling.visibility = View.GONE
            }

        }
    }

    private fun refreshRoom() {
        val cursor = dbHandler.getAllFav()
        var i = 0
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val name: String = cursor.getString(cursor.getColumnIndex(DbColumns.DbFav.ADDRESS))
                arR[i] = name
                cursor.moveToNext()
                i++
            }
        }
        cursor.close()
    }

    private fun openViewOther() {

        var des: Int? = null
        var title = R.string.title_comp
        val icon = R.drawable.ic_skills

        when(message) {
            DbKeys.rooms_key.lab_info -> {
                title = R.string.linf
                des = R.string.linf_des
            }
            DbKeys.rooms_key.lab_tps -> {
                title = R.string.ltps
                des = R.string.ltps_des
            }
            DbKeys.rooms_key.lab_sis -> {

            }
            DbKeys.rooms_key.lab_chi1 -> {

            }
            DbKeys.rooms_key.lab_chi2 -> {

            }
            DbKeys.rooms_key.lab_chi3 -> {

            }
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

    /*override fun onPrepareOptionsMenu(menu: Menu):Boolean {
        val settingsItem = menu.findItem(R.id.fav)
        // set your desired icon here based on a flag if you like
        if (dbHandler.is_Fav(arrayKeys[idAdr])) {
            settingsItem.setIcon(R.drawable.ic_favorite_on)
        } else {
            settingsItem.setIcon(R.drawable.ic_favorite_off)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.options_lab, menu)
        return true
    }




    //Controllare bene se sono presenti tutti i boolean
    //Funzione per ascoltare la decisione dell'utente sulla BottomNavigationView
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        if (item.getItemId() == R.id.close_qrcode) {
            onBackPressed()
            finish()
        } else {
            if (dbHandler.is_Fav(arrayKeys[idAdr])) {
                item.setIcon(R.drawable.ic_favorite_off)
                dbHandler.removeFav(arrayKeys[idAdr])
                toast("Rimosso dai preferiti")
            } else {
                item.setIcon(R.drawable.ic_favorite_on)
                dbHandler.addFav(arrayKeys[idAdr])
                toast("Aggiunto ai preferiti")
            }
        }

        return super.onOptionsItemSelected(item)
    }*/


    companion object {

        const val lab = "Laboratorio"
        private val name_room : Array<String> = arrayOf("Sistemi elettronici automatici","Camera anecoica","Misure e progettazione elettronica","Laboratorio di chimica biennio","Scienze integrate","Chimica analitica e strumentale","Laboratorio di Fisica","")
        private val name_lab_info : Array<String> = arrayOf("Laboratorio di Informatica","Laboratorio di TPS","Laboratorio di Sistemi e Reti")
        private var arrayImg : Array<Int> = arrayOf()
        private val desViewOtherS : Array<String> = arrayOf(
            "Laboratorio pensato per una didattica progettuale, in questo laboratorio si studia programmazione web e gestione di progetti.",
            "Studio di linguaggi di programmazione ad alto e basso livello per microcontrollori, plc, sistemi programmabili. Studio della stabilita` dei sistemi e delle automazioni"
        )
    }
}