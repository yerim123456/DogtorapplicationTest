
package com.example.dogtorapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

data class Memo(var ItemPlus: Int?, var Color: Int, var Text: String)
var TableName : String? = CareFragment.Main().input2
class MyDBHelper (
    context: Context?,
    name : String?
): SQLiteOpenHelper(context, name,null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("DBTABLE", "$TableName")
        var sql : String = "create table memo(`Itemplus` integer primary key, Color integer, Text text)"
        db!!.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //테이블 변경사항이 있는 경우 불러오는 함수
    }

    fun insertMemo(memo: Memo){
        // db 가져오기
        val wd = writableDatabase
        //wd.execSQL("INSERT INTO memo VALUES ('" + memo.ItemPlus.toString()+"',"+memo.Color.toString()+","+memo.Text.toString()+");")
        //Memo를 입력타입으로 변환
        val values = ContentValues()
        values.put("ItemPlus",memo.ItemPlus)
        values.put("Color",memo.Color)
        values.put("Text",memo.Text)

        //db에 넣기
        wd.insert("memo", null,values)
        //db닫기
        wd.close()
    }
    //데이터 조회 함수
    @SuppressLint("Range")
    fun selectMemo() : MutableList<Memo>{
        val list = mutableListOf<Memo>()

        val select = "select * from memo" // content
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null) // 위 sql과 다르게 반환값 존재
        while(cursor.moveToNext()){
            val itemcolor = cursor.getInt(0)
            val text = cursor.getString(2)
            val color = cursor.getInt(1)
            val memo = Memo(itemcolor, color, text)
            list.add(memo)
        }
        cursor.close()
        rd.close()
        return list
    }
    fun selectText(num : Int) : String{
        Log.d("num","$num")
        val select = "select * from memo where ItemPlus = $num" // content
        val rd = readableDatabase
        var text = "NAME"
        var memo : Memo = Memo(0,0,"")
        val cursor = rd.rawQuery(select, null) // 위 sql과 다르게 반환값 존재
        while(cursor.moveToNext()){
            text = cursor.getString(2)

        }
        cursor.close()
        rd.close()
        return text
    }
    //데이터 수정 함수
    fun updateMemo(memo: Memo){
        val wd = writableDatabase

        val values = ContentValues()
        values.put("ItemPlus",memo.ItemPlus)
        values.put("Text",memo.Text)
        values.put("Color",memo.Color)

        wd.update("memo", values, "no = ${memo.ItemPlus}",null)
        wd.close()
    }
    //데이터 삭제함수
    fun deleteMemo(memo: Memo){
        val wd = writableDatabase
        //val delete = "delete from memo where no = ${memo.no}"
        //wd.execSQL(delete)

        wd.delete("memo","no=${memo.ItemPlus}",null)
        wd.close()
    }
}