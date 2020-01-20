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

package com.app.bfconnect

import com.app.bfconnect.database.DbKeys

/**
 * Created by Francesco Taddia
 */

object AppConstants {

    const val prefixTag: String = "com.app.bfconnect"


    interface SharedPreferences {
        companion object Keys {
            const val wizardHelp = "wizardHelp"
            var dev : Boolean = false
            var modeLabPro : Boolean = false
            const val notifyMode = "notifyMode"
        }
    }

    interface arrayAddress {
        companion object {
            //Array Dati RV Adr_Belluzzi

            val infoArray: Array<String>? = arrayOf(
                "Linguaggio HTML",
                "Creazione di una pagina web con HTML",
                "BeLinux",
                "OS Linux proprietario della scuola",
                "Laboratorio di Informatica",
                "Aula addetta alla programmazione"
            )
            val infoImg: Array<Int>? =
                arrayOf(R.drawable.info1, R.drawable.info2, R.drawable.info3)
            val chiArray: Array<String>? = arrayOf(
                "Microscopio",
                "Usato per analizzare gli elementi in laboratorio",
                "Bunsen",
                "Fiamma usata per la reazione chimica dei materiali",
                "Ampolla",
                "Utilizzata per conservare liquidi"
            )
            val chiImg: Array<Int>? =
                arrayOf(R.drawable.chimica1, R.drawable.chimica2, R.drawable.chimica3)
            //Modificare Titolo e Sottotitolo
            val mecArray: Array<String>? = arrayOf(
                "Carter del motore",
                "Studio dell'albero primario, secondario e dell'albero motore",
                "Torni",
                "Il tornio è una macchina utensile utilizzata per la lavorazione di un pezzo posto in rotazione",
                "Studio del motore",
                "Simulazione del funzionamento di un motore"
            )
            val mecImg: Array<Int>? =
                arrayOf(R.drawable.mecca1, R.drawable.mecca2, R.drawable.mecca3)
            val eleArray: Array<String>? = arrayOf(
                "Scheda madre",
                "Studio dei sistemi elettronici",
                "Laboratorio di Elettronica",
                "Aula addetta allo studio dei sistemi elettronici",
                "Oscilloscopio",
                "Strumento utlizzato per studiare le funzioni dei sistemi elettrici"
            )
            val eleImg: Array<Int>? = arrayOf(R.drawable.ele1, R.drawable.ele2, R.drawable.ele3)

            //Array Dati RV Adr_Fioravanti
            val manuArray: Array<String>? = arrayOf("","","","","","")
            val manuImg: Array<Int>? = arrayOf(R.drawable.manu1, R.drawable.manu2, R.drawable.manu3)
            val mecaArray: Array<String>? = arrayOf("","","","","","")
            val mecaImg: Array<Int>? = arrayOf(R.drawable.qual1,R.drawable.qual3,R.drawable.qual4)
            val eletArray: Array<String>? = arrayOf("","","","","","")
            val eletImg: Array<Int>? = arrayOf(R.drawable.app1,R.drawable.app2,R.drawable.app3)

            //Array Dati RV Prj_Belluzzi
            val desiArray: Array<String>? = arrayOf(
                "Presentazione progetto DESI",
                "Esposizione degl'elementi e delle attività del progetto DESI",
                "Mettersi alla prova",
                "Studio e riparazione dei motori",
                "Programmazione",
                "Programmazione delle macchine automatiche"
            )
            val desiImg: Array<Int>? =
                arrayOf(R.drawable.desi1, R.drawable.desi2, R.drawable.desi3)
            val mastArray: Array<String>? = arrayOf(
                "Contest",
                "Nella giornata finale del progetto MAST. si partecipa al contest per verificare la qualità del progetto",
                "Photo Challenge",
                "Giornata in cui i vari gruppi realizzano una foto basandosi sullo studio eseguito",
                "Expedition",
                "Presentazione del progetto Expeditions"
            )
            val mastImg: Array<Int>? = arrayOf(R.drawable.mast1, R.drawable.mast2, R.drawable.mast3)
            val carpArray: Array<String>? = arrayOf(
                "Lavorare coni torni",
                "Approfondimento nello stage della carpigiani dei torni",
                "Lezione in stage",
                "Momento in cui elementi della Carpigiani espongono una lezione sulle macchine automatiche",
                "Mettersi alla prova",
                "Lavorare con i torni in team"
            )
            val carpImg: Array<Int>? = arrayOf(R.drawable.carp1, R.drawable.carp2, R.drawable.carp3)

            val filoArray: Array<String>? = arrayOf("","","","","","")
            val filoImg: Array<Int>? = arrayOf(1,2,3)

            val opusArray: Array<String>? = arrayOf(
                "Progetto Opus Facere",
                "Il laboratorio è organizzato come una rete con un hub centrale",
                "Fare per capire",
                "Superare la separazione tra percorsi formativi liceali, tecnici e professionali",
                "Lavoro di gruppo",
                "Giornata School Maker Day"
            )
            val opusImg: Array<Int>? = arrayOf(R.drawable.opus1, R.drawable.opus2, R.drawable.opus3)
            val texaArray: Array<String>? = arrayOf(
                "Riparazione impianto elettrico",
                "TEXAEDU progetto per imparare e approfondire lo studo dell'auto",
                "Lavoro di gruppo",
                "Foto con tutti i componeti",
                "Centralina",
                "Cambio della centralina dell'auto"
            )
            val texaImg: Array<Int>? = arrayOf(R.drawable.texa1, R.drawable.texa2, R.drawable.texa3)

            val stemArray: Array<String>? = arrayOf("","","","","","")
            val stemImg: Array<Int>? = arrayOf(1,2,3)

        }
    }

    interface arrayKeys {
        companion object {
            val arrayKeys : Array<String> = arrayOf(
                DbKeys.rooms_key.lab_info,
                DbKeys.rooms_key.lab_tps,
                DbKeys.rooms_key.lab_sis,
                DbKeys.rooms_key.lab_chi1,
                DbKeys.rooms_key.lab_chi2,
                DbKeys.rooms_key.lab_chi3,
                DbKeys.rooms_key.lab_camera,
                DbKeys.rooms_key.lab_ele1,
                DbKeys.rooms_key.lab_ele2)
        }
    }

    /**
     * High level app constants, based on human decisions
     */
    interface HighLevel {

        /**
         * Copy these manually to ApiClient, do not introduce a link because
         * you would create a circular dependency
         */
        interface server {
            companion object {
                val ips = "http://taddia.sytes.net:6005"
            }
        }

    }


}