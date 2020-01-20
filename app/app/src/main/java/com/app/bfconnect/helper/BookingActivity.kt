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
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.ModalDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.app.bfconnect.R
import com.app.bfconnect.customview.spinner.CustomSpinner
import com.google.android.material.button.MaterialButton
import com.app.bfconnect.customview.spinner.setAdapter

class BookingActivity : AppCompatActivity() {

    //Gli spinner sono implementati nella cartella helper -> CustomSpinner

    /*val spinnerparents_position1 =  spinnerParents.getItemAtPosition(1) as String
    val spinnerparents_position2 =  spinnerParents.getItemAtPosition(2) as String*/

    private lateinit var btnBook : MaterialButton
    private var name_user : String? = null  //Nome Utente
    private var surname_user : String? = null  //Cognome Utente
    private var code_ci : String? = null  //Codice Carta d'Identità

    private var name_user1 : String? = null  //Nome Utente
    private var surname_user1 : String? = null  //Cognome Utente
    private var code_ci1 : String? = null  //Codice Carta d'Identità

    private val day = arrayOf("23/11", "14/12", "18/01") //Giorni in cui c'è l'openday, andare a fare un controllo della data reale sul disposito e mostrare solo quello imminente non i successivi
                                                        //e se sono già avvenuti degli open day non mostrare quelli già fatti
    private val hour = arrayOf("15:00", "16:00", "17:00") //Ore in cui l'utente può prenotare l'open day (Probabilmente non sono gli orari corretti)
    private val person = arrayOf("Scegli","1","2")
    private var check_privacy : Boolean = false  //Boolean per controllare se l'utente ha accettato i termini della privacy

    private var toast: Toast? = null //Variabile creata per gestire meglio i Toast nella BottomSheet

    //Funzione che ho creato per gestire meglio i Toast all'interno della BottomSheet
    internal fun Activity.toast(message: CharSequence) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
                .apply { show() }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_booking)

        setTheme(R.style.AppThemeCustom) //Setto il tema dell'Activity

        //Configuro la Toolbar
        findViewById<Toolbar>(R.id.toolbar_book).apply {
            setSupportActionBar(this)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            setLogo(R.drawable.ic_nfc_logo)
        }

        initViews() //Implemento la grafica

        btnBook.setOnClickListener {
            bottomSheetBook() //Avvia BottomSheet per prenotare l'OpenDay
        }

    }

    private fun initViews() {
        btnBook = findViewById(R.id.btnBook)
    }


    //Funzioni per la BottomSheet

    private fun bottomSheetBook() {
        showCustomViewDialog(BottomSheet(LayoutMode.WRAP_CONTENT))  //Apro Layout Custom della BottomSheet per la prenotazione
    }

    private fun showCustomViewDialog(dialogBehavior: DialogBehavior = ModalDialog) {
        val dialog = MaterialDialog(this, dialogBehavior).show {
            setPeekHeight(R.dimen.bookpeekHeight)
            title(R.string.title_book)
            customView(R.layout.book_bottomsheet, scrollable = true, horizontalPadding = true)
            positiveButton(R.string.title_bookAction) { dialog -> /*Edo inserisci qui la funzione per inviare i dati al server*/}
            negativeButton(R.string.title_quit)
            cornerRadius(16f)
            lifecycleOwner(this@BookingActivity)

        }

        // Setto la grafica nella finestra della bottom sheet


        val customView = dialog.getCustomView()
        val persona1 : LinearLayout = customView.findViewById(R.id.persona1)
        val persona2 : LinearLayout = customView.findViewById(R.id.persona2)
        val giorni : LinearLayout = customView.findViewById(R.id.giorni)
        val person_choose :LinearLayout = customView.findViewById(R.id.person_choose)
        val txtPersona1 : TextView = customView.findViewById(R.id.txtPersona1)

        val nomeInput : EditText = customView.findViewById(R.id.txtName)
        val cognomeInput: EditText = customView.findViewById(R.id.txtCogn)
        val codice_ci_Input : EditText = customView.findViewById(R.id.txtCI)

        val spinnerDay : CustomSpinner = customView.findViewById(R.id.spinnerDay)
        val spinnerHour : CustomSpinner = customView.findViewById(R.id.spinnerHour)
        val spinnerParents : CustomSpinner = customView.findViewById(R.id.spinnerParents)

        val nomeInput1 : EditText = customView.findViewById(R.id.txtName1)
        val cognomeInput1: EditText = customView.findViewById(R.id.txtCogn1)
        val codice_ci_Input1 : EditText = customView.findViewById(R.id.txtCI1)


        val privacyCheck: CheckBox = customView.findViewById(R.id.showPassword)
        privacyCheck.visibility = View.GONE

        //Setto alle variabili String il valore contenuto all'interno delle caselle di testo della BottomSheet
        name_user = nomeInput.text.toString()
        surname_user = cognomeInput.text.toString()
        code_ci = codice_ci_Input.text.toString() //Provare a capire come fare un controllo sul codice per verificare se è conforme

        name_user1 = nomeInput1.text.toString()
        surname_user1 = cognomeInput1.text.toString()
        code_ci1 = codice_ci_Input1.text.toString() //Provare a capire come fare un controllo sul codice per verificare se è conforme


        spinnerDay.setAdapter(this, day) //Edo gestisci l'array in modo da darti sempre un opzione, l'opzione dovrebbe essere quella più vicino come data rispetto alla data reale dell'utente

        spinnerDay.setOnItemClickListener { parent, view, position, id ->
            //Edo qua puoi inserire un qualcosa per capire quale giorno l'utente vuole

            //potresti utilizzare per sapere la posizione dell'Item dello spinner questo metodo getItemAtPosition() o getItemIdAtPosition()
        }

        spinnerHour.setAdapter(this, hour) //Le ore se non cambiano possono rimanere statiche

        spinnerHour.setOnItemClickListener { parent, view, position, id ->
            //Edo qua puoi inserire un qualcosa per capire quale ora l'utente vuole

            //potresti utilizzare per sapere la posizione dell'Item dello spinner questo metodo getItemAtPosition() o getItemIdAtPosition()
        }

        spinnerParents.setAdapter(this,person)

        spinnerParents.setOnItemClickListener { parent, view, position, id ->
            if (spinnerParents.getItemAtPosition(position) == "1"){
                txtPersona1.visibility = View.VISIBLE
                persona1.visibility = View.VISIBLE
                persona2.visibility = View.GONE
                giorni.visibility = View.VISIBLE
                privacyCheck.visibility = View.VISIBLE
                dialog.setPeekHeight(R.dimen.bookpeekHeight)
            } else if (spinnerParents.getItemAtPosition(position) == "2") {
                txtPersona1.visibility = View.VISIBLE
                persona1.visibility = View.VISIBLE
                persona2.visibility = View.VISIBLE
                giorni.visibility = View.VISIBLE
                privacyCheck.visibility = View.VISIBLE
                dialog.setPeekHeight(R.dimen.bookpeekHeight)
            } else {
                txtPersona1.visibility = View.GONE
                persona1.visibility = View.GONE
                persona2.visibility = View.GONE
                giorni.visibility = View.GONE
                privacyCheck.visibility = View.GONE
            }
        }

        privacyCheck.setOnCheckedChangeListener { _, isChecked ->

        }
    }


    //Inizio metodi creati per capire dove l'utente sta cliccando

    fun MaterialDialog.lifecycleOwner(owner: LifecycleOwner? = null): MaterialDialog {
        val observer = DialogLifecycleObserver(::dismiss)
        val lifecycleOwner = owner ?: (windowContext as? LifecycleOwner
                ?: throw IllegalStateException(
                        "$windowContext is not a LifecycleOwner."
                ))
        lifecycleOwner.lifecycle.addObserver(observer)
        return this
    }

    internal class DialogLifecycleObserver(private val dismiss: () -> Unit) : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() = dismiss()
        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPause() = dismiss()
    }

    //Fine metodi per l'observer della BottomSheet




    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.options_lab, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        if (item.getItemId() == R.id.close_qrcode) {
            onBackPressed()
            finish()
        } else {
            Log.d("Fail","Error with ItemId")
        }
        return super.onOptionsItemSelected(item)
    }

}