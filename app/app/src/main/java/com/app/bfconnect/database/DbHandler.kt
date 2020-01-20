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

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * TODO: Implementare Db anche per tutti i contenuti delle room e degli address
 */

class DbHandler(context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        //non utile per ora
        val CREATE_PDF_TABLE = "CREATE TABLE " + DbColumns.DbPDF.TABLE_NAME + " (" +
                DbColumns.DbPDF.NAME + "TEXT PRIMARY KEY NOT NULL, " +
                DbColumns.DbPDF.SIZE + "TEXT, " +
                DbColumns.DbPDF.DATA + " MEDIUMBLOB NOT NULL)"
        val CREATE_FAV_TABLE = "CREATE TABLE " + DbColumns.DbFav.TABLE_NAME + " (" + DbColumns.DbFav.ADDRESS + " TEXT PRIMARY KEY, " + DbColumns.DbFav.SCHOOL + " TEXT)"
        //non utile per ora
        val CREATE_ROOMS_TABLE = "CREATE TABLE " + DbColumns.DbRoom.TABLE_NAME + "(" + DbColumns.DbRoom.ID + " TEXT PRIMARY KEY)"
        //db.execSQL(CREATE_PDF_TABLE)

        db.execSQL(CREATE_FAV_TABLE)
        db.execSQL(CREATE_ROOMS_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //db.execSQL("DROP TABLE IF EXISTS ${DbColumns.DbPDF.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DbColumns.DbFav.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DbColumns.DbRoom.TABLE_NAME}")
        onCreate(db)
    }
    /*
    fun addPDF(name: String, size : Int, data : String) {
        val values = ContentValues()
        values.put(DbColumns.DbPDF.NAME, name)
        values.put(DbColumns.DbPDF.SIZE, size)
        values.put(DbColumns.DbPDF.DATA, data)
        val db = this.writableDatabase
        db.insert(DbColumns.DbPDF.TABLE_NAME, null, values)
        db.close()
    }

    fun getAllPDF(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM ${DbColumns.DbPDF.TABLE_NAME}", null)
    }

    fun getPdf( name: String): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM ${DbColumns.DbPDF.TABLE_NAME} " +
                "todo WHERE ${DbColumns.DbPDF.NAME}=$name",
            null)
    }
    */


    /**
        var cursor = getAllRooms()
        var i = 0
        while(i<cursor.count){
        //non so come si facciano gli array quindi veditela un pò tu su come gestire i valori
        arraydistringhe = cursor.toString(cursor.getColumnIndex(DbColumns.DbRoom.ID))
        cursor.moveToNext()
        i++
        }
     **/
    fun getAllRooms(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM ${DbColumns.DbRoom.TABLE_NAME}", null)
    }

    fun addRoom(id:String){
        val value = ContentValues()
        value.put(DbColumns.DbRoom.ID , id)
        val db = this.writableDatabase
        db.insert(DbColumns.DbRoom.TABLE_NAME, null, value)
        db.close()
    }

    //restitutisce vero se è presente quell'indirizzo nel database

    fun is_Room_inDB( id : String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DbColumns.DbRoom.TABLE_NAME}  WHERE ${DbColumns.DbRoom.ID} = ? ", arrayOf(id) )
        return cursor.move(1)
    }

    fun removeRoom(id : String){
        if(is_Room_inDB(id)){
            val db = this.writableDatabase
            db.delete(DbColumns.DbRoom.TABLE_NAME, DbColumns.DbRoom.ID+ "=?", arrayOf(id))
        }
    }


    /**
        var cursor = getAllFav()
        var i = 0
        while(i<cursor.count){
            //non so come si facciano gli array quindi veditela un pò tu su come gestire i valori
            arraydistringhe = cursor.toString(cursor.getColumnIndex(DbColumns.DbFav.ADDRESS))
            cursor.moveToNext()
            i++
        }
     **/
    fun getAllFav(): Cursor? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DbColumns.DbFav.TABLE_NAME}", null)
        return cursor
    }



    //restitutisce vero se è presente quell'indirizzo nel database
    fun is_Fav( address : String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DbColumns.DbFav.TABLE_NAME}  WHERE ${DbColumns.DbFav.ADDRESS} = ? ", arrayOf(address) )
        return cursor.move(1)
    }

    fun removeFav(address : String){
        if(is_Fav(address)){
            val db = this.writableDatabase
            db.delete(DbColumns.DbFav.TABLE_NAME, DbColumns.DbFav.ADDRESS + "=?", arrayOf(address))
        }
    }


    //SE NON TI INTERESSA LA SCUOLA NON STARE A METTERE NULLA NEL SECONDO PARAMETRO
    fun addFav( address : String){
        val value = ContentValues()
        value.put(DbColumns.DbFav.ADDRESS , address)
        val db = this.writableDatabase
        db.insert(DbColumns.DbFav.TABLE_NAME, null, value)
        db.close()
    }

    //PRENDI TUTTI I FAVORITI DI UNA DELLE DUE SCUOLE
    /**
        var cursor = getAllFav()
        var i = 0
        while(i<cursor.count){
            //non so come si facciano gli array quindi veditela un pò tu su come gestire i valori
            arraydistringhe = cursor.toString(cursor.getColumnIndex(DbColumns.DbFav.SCHOOL))
            cursor.moveToNext()
            i++
        }
     **/
    fun getSchoolFav(school: String): Cursor? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DbColumns.DbFav.TABLE_NAME} WHERE ${DbColumns.DbFav.SCHOOL}=$school", null)
        return cursor
    }

    companion object {
        val DATABASE_VERSION = 11
        val DATABASE_NAME = "BFconnect.db"
    }

}