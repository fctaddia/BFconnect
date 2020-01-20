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

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.app.bfconnect.AppConstants
import com.app.bfconnect.R
import com.app.bfconnect.nfc.Listener
import com.app.bfconnect.nfc.NfcReadFragment
import com.app.bfconnect.nfc.NfcWriteFragment
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_nfc.*
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.*
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.app.bfconnect.nfc.NfcController

private val TAG = NfcWriteFragment::class.java.simpleName

class NfcActivity : AppCompatActivity(), Listener {

    private lateinit var btnRead: MaterialButton
    private lateinit var btnWrite: MaterialButton
    private lateinit var mEtMessage: EditText
    private lateinit var imgNfc : ImageView
    private lateinit var txtNfc : TextView
    private lateinit var cvCheckNfc : CardView
    private lateinit var toolbar: Toolbar
    private lateinit var nfcController : NfcController
    private var nfcAdapter: NfcAdapter ?= null
    private var mNfcReadFragment: NfcReadFragment? = null
    private var mNfcWriteFragment: NfcWriteFragment? = null
    private var toast: Toast? = null
    private var isWrite = false
    private var isDialogDisplayed : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)
        setTheme(R.style.AppThemeCustom)

        initObj()
        initListeners()
        initNFC()
        devRefresh()

        toolbar.apply {
            setSupportActionBar(this)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            setLogo(R.drawable.ic_nfc_logo)
        }
    }

    override fun onDialogDisplayed() { isDialogDisplayed = true }

    override fun onDialogDismissed() { isDialogDisplayed = false ; isWrite = false }

    override fun onResume() {
        super.onResume()
        refreshNfc() ; devRefresh()
        val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        val ndefDetected = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        val techDetected = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        val nfcIntentFilter = arrayOf(techDetected, tagDetected, ndefDetected)
        val pendingIntent = PendingIntent.getActivity(this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        if (nfcAdapter != null) nfcAdapter!!.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null)
    }

    override fun onPause() {
        super.onPause()
        refreshNfc()
        devRefresh()
        if (nfcAdapter != null) nfcAdapter!!.disableForegroundDispatch(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_nfc, menu)
        val item : MenuItem = menu.findItem(R.id.devmode)
        if (AppConstants.SharedPreferences.dev){ item.isVisible = false }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.close_nfc) {
            onBackPressed()
            finish()
        } else {
            if (AppConstants.SharedPreferences.dev) {
                toast("Sei già in Opzioni Sviluppatore")
            } else {
                MaterialDialog(this).show {
                    title(res = R.string.label_insert_PIN)
                    message (res = R.string.message)
                    input(
                        waitForPositiveButton = false,
                        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD,
                        maxLength = 5,
                        allowEmpty = false) { dialog, text ->
                        val inputField = dialog.getInputField()
                        val isValid = text.startsWith("41901", true)
                        inputField.error = if (isValid) null else "Pin Errato"
                        dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid)
                    }
                    cornerRadius(10f)
                    positiveButton(res = R.string.confirm) {
                        AppConstants.SharedPreferences.dev = true
                        refreshNfc() ; devRefresh()
                    }
                }
            }
        } ; return super.onOptionsItemSelected(item)
    }

    @SuppressLint("MissingSuperCall")
    override fun onNewIntent(intent: Intent) {
        val tag = intent.getParcelableExtra<Parcelable>(NfcAdapter.EXTRA_TAG)
        Log.d(TAG, "onNewIntent: " + intent.action)
        if (tag != null) {
            Toast.makeText(this, getString(R.string.message_tag_detected), Toast.LENGTH_SHORT).show()
            val ndef = Ndef.get(tag as Tag?)
            if (isDialogDisplayed) {
                if (isWrite) {
                    val messageToWrite = mEtMessage.text.toString()
                    mNfcWriteFragment = supportFragmentManager.findFragmentByTag(NfcWriteFragment.TAG) as NfcWriteFragment
                    mNfcWriteFragment?.onNfcDetected(ndef, messageToWrite)
                } else {
                    mNfcReadFragment = supportFragmentManager.findFragmentByTag(NfcReadFragment.TAG) as NfcReadFragment
                    mNfcReadFragment?.onNfcDetected(ndef)
                }
            }
        }
    }

    private fun initObj() {
        nfcController = NfcController(this.applicationContext)
        toolbar = findViewById(R.id.toolbar_nfc)
        mEtMessage = findViewById(R.id.et_message)
        btnWrite = findViewById(R.id.btnWrite)
        btnRead = findViewById(R.id.btnRead)
        imgNfc = findViewById(R.id.imgNfc)
        txtNfc = findViewById(R.id.txtNfc)
        cvCheckNfc = findViewById(R.id.cvCheckNfc)
    }

    private fun initListeners() {
        cvCheckNfc.setOnClickListener { if(!isNfcEnabled()){showAlertDialogNdefCheck()} else {showAlertDialogCheckNFC()}}
        btnWrite.setOnClickListener { if(!isNfcEnabled()){showAlertDialogNdefWrite()} else {showWriteFragment()}}
        btnRead.setOnClickListener { if(!isNfcEnabled()){showAlertDialogNdefRead()} else {showReadFragment()}}
        //btnRead.setOnClickListener({ nfcController.enableNfcPower(true)})  Do not uncomment
    }

    /**
     * Management Dialog
     */
    private fun showAlertDialogNdefWrite() {
        val dialogBuilder = AlertDialog.Builder(this, R.style.CustomDialogTheme)
        dialogBuilder.setMessage("Per poter scrivere sul TAG è necessario attivare l'NFC")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Attiva") { dialog, _ ->
                startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
                dialog.dismiss()
            }
            // negative button text and action
            .setNegativeButton("Annulla") { dialog, _ ->
                dialog.cancel()
            }

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Vui scrivere sul TAG?")
        // show alert dialog
        alert.show()
    }

    private fun showAlertDialogNdefRead() {
        val dialogBuilder = AlertDialog.Builder(this, R.style.CustomDialogTheme)
        dialogBuilder.setMessage("Per poter leggere un TAG è necessario attivare l'NFC")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Attiva") { dialog, _ ->
                startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
                dialog.dismiss()
            }
            // negative button text and action
            .setNegativeButton("Annulla") { dialog, _ ->
                dialog.cancel()
            }

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Vuoi leggere un TAG?")
        // show alert dialog
        alert.show()
    }

    private fun showAlertDialogCheckNFC() {
        val dialogBuilder = AlertDialog.Builder(this, R.style.CustomDialogTheme)
        dialogBuilder.setMessage("NFC già attivato su questo dispositivo")
            // if the dialog is cancelable
            .setCancelable(false)
            // negative button text and action
            .setNegativeButton("Chiudi") { dialog, _ ->
                dialog.cancel()
            }
        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Vuoi attivare l'NFC?")
        // show alert dialog
        alert.show()
    }

    private fun showAlertDialogNdefCheck() {
        val dialogBuilder = AlertDialog.Builder(this, R.style.CustomDialogTheme)
        dialogBuilder.setMessage("Attiva l'NFC così da poter leggere i TAG e ricevere le informazioni sui laboratori")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Attiva") { dialog, _ ->
                startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
                dialog.dismiss()
            }
            // negative button text and action
            .setNegativeButton("Annulla") { dialog, _ ->
                dialog.cancel()
            }
        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Vuoi attivare l'NFC?")
        // show alert dialog
        alert.show()
    }

    private fun showAlertDialogNfcNa() {
        val dialogBuilder = AlertDialog.Builder(this, R.style.CustomDialogTheme)
        dialogBuilder.setMessage("Sembra che il tuo telefono non abbia l'NFC, per poter comunque avere informazioni sui laboratori puoi utilizzare il QRcode")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Utilizza QRcode") { dialog, _ ->
                startActivity(Intent(this,QrActivity::class.java))
                dialog.dismiss()
                finish()
            }
        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Non hai l'NFC?")
        // show alert dialog
        alert.show()
    }

    /**
     * Functions Generic
     */
    private fun devRefresh(){ if (AppConstants.SharedPreferences.dev) { cvWrite.visibility = View.VISIBLE } }

    private fun Activity.toast(message: String) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT).apply { show() }
    }

    /**
     * Management Nfc
     */
    private fun initNFC() {
        val pm = packageManager
        if (pm.hasSystemFeature(PackageManager.FEATURE_NFC)) {
            nfcAdapter = NfcAdapter.getDefaultAdapter(this)
            refreshNfc()
        } else {
            refreshNfc()
            Toast.makeText(this, "Il tuo dispositivo non supporta NFC", Toast.LENGTH_SHORT).show()
        }
    }

    private fun refreshNfc(){
        if (nfcAdapter != null && isNfcEnabled()) {
            imgNfc.setImageResource(R.drawable.nfc_on)
            txtNfc.setText(R.string.nfc_on)
        } else if (nfcAdapter != null && !isNfcEnabled()){
            imgNfc.setImageResource(R.drawable.nfc_off)
            txtNfc.setText(R.string.nfc_off)
        } else {
            imgNfc.setImageResource(R.drawable.nfc_not_av)
            txtNfc.setText(R.string.nfc_nv)
            showAlertDialogNfcNa()
        }
    }

    //Metodi per bypassare securty settings

    /*fun powerNfc(isOn:Boolean, context:Context):Boolean {
        var success = false
        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        if (nfcAdapter != null) {
            val NfcManagerClass:Class<*>
            val setNfcEnabled: Method
            try {
                NfcManagerClass = Class.forName(nfcAdapter.javaClass.name)
                setNfcEnabled = NfcManagerClass.getDeclaredMethod(if (isOn)
                    "enable"
                else
                    "disable")
                setNfcEnabled.setAccessible(true)
                success = setNfcEnabled.invoke(nfcAdapter) as Boolean
            } catch (e:ClassNotFoundException) { Log.e(TAG, Log.getStackTraceString(e)) }
                catch (e:NoSuchMethodException) { Log.e(TAG, Log.getStackTraceString(e)) }
                    catch (e:IllegalArgumentException) { Log.e(TAG, Log.getStackTraceString(e)) }
                        catch (e:IllegalAccessException) { Log.e(TAG, Log.getStackTraceString(e)) }
                            catch (e: InvocationTargetException) { Log.e(TAG, Log.getStackTraceString(e)) }
        }
        return success
    }

    fun checkNfcPowerStatus(context:Context):Boolean {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        var enabled = false
        if (nfcAdapter != null) { enabled = nfcAdapter.isEnabled() }
        return enabled
    }*/

    private fun showWriteFragment() {
        isWrite = true
        mNfcWriteFragment = supportFragmentManager.findFragmentByTag(NfcWriteFragment.TAG) as? NfcWriteFragment
        if (mNfcWriteFragment == null) { mNfcWriteFragment = NfcWriteFragment.newInstance() }
        mNfcWriteFragment?.show(supportFragmentManager, NfcWriteFragment.TAG)
    }

    private fun showReadFragment() {
        mNfcReadFragment = supportFragmentManager.findFragmentByTag(NfcReadFragment.TAG) as? NfcReadFragment
        if (mNfcReadFragment == null) { mNfcReadFragment = NfcReadFragment.newInstance() }
        mNfcReadFragment?.show(supportFragmentManager, NfcReadFragment.TAG)
    }

    private fun isNfcEnabled(): Boolean {
        if (nfcAdapter != null) {
            return try { nfcAdapter!!.isEnabled
            } catch (exp: Exception) { try { nfcAdapter!!.isEnabled } catch (exp: Exception) { false } }
        } ; return false
    }
}
