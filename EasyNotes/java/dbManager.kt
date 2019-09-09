package com.example.memo

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class dbManager(context: Context): SQLiteOpenHelper(context,"MyNotes.db",null,1) {
    val db=this.writableDatabase
    override fun onCreate(db:SQLiteDatabase?){
        db!!.execSQL("CREATE TABLE MyNotes (ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,DETAILS TEXT, DATE_TIME TEXT)")
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion:Int, newVersion:Int){
        db!!.execSQL("DROP TABLE IF EXISTS MyNotes")
        onCreate(db)
    }
    fun insert(TITLE: String, DETAILS: String, DATE_TIME:String){
        db.execSQL("INSERT INTO MyNotes (TITLE, DETAILS, DATE_TIME) VALUES ('$TITLE','$DETAILS','$DATE_TIME')")
    }
    fun update(ID: Int, TITLE:String, DETAILS: String){
        db.execSQL("UPDATE MyNotes SET TITLE='$TITLE',DETAILS='$DETAILS' WHERE ID = $ID")
    }
    fun delete(ID: Int){
        db.execSQL("DELETE FROM MyNotes WHERE ID=$ID")
    }
    fun select(): ArrayList<listItem>{
        var arr=ArrayList<listItem>()
        var cursor=db.rawQuery("SELECT * FROM MyNOtes",null)

        cursor.moveToFirst()
        while(!cursor.isAfterLast){
            val ID:String=cursor.getString(0)
            val TITLE:String=cursor.getString(1)
            val DETAILS:String=cursor.getString(2)
            val DATE_TIME:String=cursor.getString(3)
            arr.add( listItem(ID, TITLE, DETAILS, DATE_TIME))
            cursor.moveToNext()
        }
        return arr
    }
     fun selectForUpdate(ID:Int):ArrayList<String>{
         var arr=ArrayList<String>()
         var cursor: Cursor = db.rawQuery("SELECT TITLE, DETAILS FROM MyNotes WHERE ID=$ID",null)

         cursor.moveToFirst()
         while(!cursor.isAfterLast){
             val TITLE: String = cursor.getString(0)
             val DETAILS: String=cursor.getString(1)
             arr.add(0,TITLE.toString())
             arr.add(1,DETAILS.toString())
             cursor.moveToNext()
         }
         return arr
     }

}