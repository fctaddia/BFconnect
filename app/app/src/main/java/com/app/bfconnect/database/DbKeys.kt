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

package com.app.bfconnect.database

import com.app.bfconnect.R

class DbKeys {
    object address_key{
        val informatica = "informatica"
        var meccanica = "meccanica"
        var elettronica = "elettronica"
        var chimica = "chimica"
        var manutenzione = "manutenzione"
        var meccanico = "meccanico"
        val elettronico = "elettronico"
    }

    object  projects_key{
        var desi = "desi"
        var mast = "mast"
        var carpigiani = "carpigiani"
        var filosofia = "filosofia"
        var opusfacere = "opusfacere"
        var texa = "texa"
        var stem  = "stem"
        var toyota = "toyota"
        var magneti = "magneti"
    }

    object rooms_key {
        val lab_info : String = "li15"
        val lab_tps : String = "lt14"
        val lab_sis : String = "ls16"
        val lab_chi1 : String = "lc1"
        val lab_chi2 : String = "lc2"
        val lab_chi3 : String = "lc3"
        val lab_camera : String = "lca"
        val lab_ele1 : String = "le1"
        val lab_ele2 : String = "le2"
        val lab_fisica : String = "lf1"
        val lab_mec1 : String = "lm1"
        val lab_mec2 : String = "lm2"
        val lab_mec3 : String = "lm3"
        val lab_rob : String = "lrob"
    }

    object schools_key{
        var belluzzi = "belluzzi"
        var fioravanti = "fioravanti"
    }

    object img_key {
        val informatica : String = "0"
        val chimica : String= "1"
        val meccanica : String= "2"
        val elettronica : String= "3"
        val manutenzione : String= "4"
        val meccanico : String= "5"
        val elettronico : String= "6"
        val desi : String= "7"
        val mast : String= "8"
        val carpigiani : String = "9"
        val filosofia : String= "10"
        val opusfacere : String= "11"
        val texa : String= "12"
        val stem : String= "13"
    }

    //var arrayFavImg : Array<Int> = arrayOf(R.drawable.apparati,R.drawable.ic_desi,R.drawable.ic_mast,R.drawable.ic_carpigiani,R.drawable.ic_filo,R.drawable.ic_opus,R.drawable.ic_texa,R.drawable.ic_stem)
    //need rooms etc...

}