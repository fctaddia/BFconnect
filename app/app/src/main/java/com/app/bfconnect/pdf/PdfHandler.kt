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

package com.app.bfconnect.pdf

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.Toast
import android.content.Intent
import android.os.AsyncTask
import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import android.content.ContentValues.TAG
import com.app.bfconnect.AppConstants

class PdfHandler (indirizzo: String, context: Context) : AsyncTask<Void, Void, Boolean>() {
    private var pdf: String? = null
    private var nPdf: Int = 0
    var offline : Boolean = false

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var cont : Context
    }

    private lateinit var outputFile: File

    init {
        cont = context
        when (indirizzo) {
            "elettronica" -> {
                pdf = "elettronica_elettrotecnica.pdf"
                nPdf = 4
            }
            "informatica" -> {
                pdf = "informatica_telecomunicazioni.pdf"
                nPdf = 1
            }
            "meccanica" -> {
                pdf = "meccanica_mecatronicaedenergia.pdf"
                nPdf = 2
            }
            "chimica" -> {
                pdf = "chimica_materiali_biotecnologie.pdf"
                nPdf = 3
            }
            "seraliApparati" -> {
                pdf = "BF_serale.pdf"
                nPdf = 5
            }
            "seraliMezzi" -> {
                pdf = "BF_serale.pdf"
                nPdf = 5
            }
            "meccanico" -> {
                pdf = "qualificheRegionali.pdf"
                nPdf = 6
            }
            "manutenzione" -> {
                pdf = "manutenzione_assistenzaTecnica.pdf"
                nPdf = 7
            }
            "elettronico" -> {
                pdf = "apparati_impianti_serviziTecniciIndustriali.pdf"
                nPdf = 8
            }
            "mezzi" -> {
                pdf = "manutenzioneMezziDiTrasporto.pdf"
                nPdf = 9
            }
            "work_school" -> {
                pdf = "Alternanza_scuola_Lavoro_BF.pdf"
                nPdf = 10
            }
            "mast" -> {
                pdf = "Mast_BF.pdf"
                nPdf = 11
            }
            "desi" -> {
                pdf = "Ducati_BF.pdf"
                nPdf = 12
            }
            "opusfacere" -> {
                pdf = "Opus_facere_BF.pdf"
                nPdf = 13
            }
            "magneti" -> {
                pdf = "Magneti_Marelli_BF.pdf"
                nPdf = 14
            }
            "filosofia" -> {
                pdf = "Filosofia_BF.pdf"
                nPdf = 15
            }
            "carpigiani" -> {
                pdf = "Carpigiani_BF.pdf"
                nPdf = 16
            }
            "stem" -> {
                pdf = "stem_BF.pdf"
                nPdf = 17
            }
            "texa" -> {
                pdf = "texa_BF.pdf"
                nPdf = 18
            }
            "toyota" -> {
                pdf = "toyota_BF.pdf"
                nPdf = 19
            }
            else -> {
                pdf = "orari_del_tecnico.pdf"
                nPdf = 0
            }
        }
        Log.i("DownloadTask", "Constructor done")
    }

    override fun onPreExecute() {
        super.onPreExecute()

        val donwload = Toast.makeText(cont, "Download in corso", Toast.LENGTH_SHORT)
        donwload.setGravity(Gravity.CENTER, 0, 0)
        donwload.show()

        val pdfHandler = this
    }

    override fun doInBackground(vararg arg0: Void): Boolean? {

        try {
            val downloadPath = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS)
            outputFile = File("$downloadPath/$pdf")

            //Create New File if not present
            if (!outputFile.exists()) {
                val url = URL("${AppConstants.HighLevel.server.ips}/?pdf=$nPdf")//Create Download URl
                val c = url.openConnection() as HttpURLConnection//Open Url Connection
                c.setRequestMethod("GET")//Set Request Method to "GET" since we are grtting data
                c.connect()//connect the URL Connection
                //If Connection response is not OK then show Logs
                if (c.responseCode != HttpURLConnection.HTTP_OK)  return false

                outputFile.createNewFile()
                Log.d(TAG, outputFile.getPath())
                Log.d(TAG, "File Created")

                val fos = FileOutputStream(outputFile)//Get OutputStream for NewFile Location
                val i = c.inputStream //Get InputStream for connection

                val buffer = ByteArray(1024)//Set buffer type
                var len1 = 0

                do {
                    len1 = i.read(buffer)
                    fos.write(buffer, 0, len1)//Write new file
                } while (len1 > 0)

                fos.flush()
                fos.close()
                i.close()
                c.disconnect()
            } else {
                Log.d("file exist", "passing over")
                offline = false
            }
        } catch (e: Exception) {
            //Read exception if something went wrong
            e.printStackTrace()
            Log.e(TAG, "Download Error Exception " + e.message)
            return false
        }

        return true
    }


    override fun onPostExecute( check: Boolean?) {
        super.onPostExecute(check)

        if (!check!!) {
            val tdonwload =
                Toast.makeText(cont, "Download PDF riuscito", Toast.LENGTH_SHORT)
            tdonwload.setGravity(Gravity.TOP, 0, 500)
            tdonwload.show()
            //Open PDF
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.fromFile(outputFile), "application/pdf")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            cont.startActivity(intent)

        } else if (!check == false && offline == false) {

            val tdonwload =
                Toast.makeText(cont, "PDF già scaricato", Toast.LENGTH_SHORT)
            tdonwload.setGravity(Gravity.TOP, 0, 500)
            tdonwload.show()

            //Open PDF
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.fromFile(outputFile), "application/pdf")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            cont.startActivity(intent)

        } else if (check){

            val tdonwload =
                Toast.makeText(cont, "Download PDF riuscito", Toast.LENGTH_SHORT)
            tdonwload.setGravity(Gravity.TOP, 0, 500)
            tdonwload.show()

            //Open PDF
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.fromFile(outputFile), "application/pdf")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            cont.startActivity(intent)
        } else {

            val tdonwload = Toast.makeText(cont, "Download PDF non riuscito", Toast.LENGTH_LONG)
            tdonwload.setGravity(Gravity.CENTER, 0, 0)
            tdonwload.show()
        }
    }

}

