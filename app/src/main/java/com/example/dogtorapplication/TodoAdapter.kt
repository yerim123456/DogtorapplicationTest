
/*
package com.example.dogtorapplication


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.createBitmap
import androidx.recyclerview.widget.RecyclerView
import com.example.dogtorapplication.R
import java.text.SimpleDateFormat

class TodoAdapter : RecyclerView.Adapter<Holder>(){
    var lisData = mutableListOf<Memo>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_todo, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return lisData.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val memo = lisData.get(position)
        holder.setMemo(memo)
    }

}
class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun setMemo(memo: Memo){
        itemView.findViewById<TextView>(R.id.todoList).text="${memo.no}"
        itemView.findViewById<TextView>(R.id.textNo).text = "${memo.content}"
        itemView.findViewById<TextView>(R.id.color).text = "${memo.color}"
    }
}
*/



//import android.R

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dogtorapplication.*
import com.example.dogtorapplication.CareFragment.CustomDialog
import kotlinx.android.synthetic.main.item_calendar_body.*
import kotlinx.android.synthetic.main.layout_todo.view.*
import kotlinx.android.synthetic.main.todo_add.*
import java.util.*

var buf_view3: View? = null
var positionNum : Int? =0
var buff_holder : TodoAdapter.TodoViewHolder? = null
class Todoupdate(){
    val buf_view4: View? = buf_view3
    var num : Int? = positionNum
}

class TodoAdapter() :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    //yearmothday
    val colorjava = yearmothday()
    var listDate = mutableListOf<Memo>()
    var viewsList2= ArrayList<View>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.layout_todo, parent, false)
        view.findViewById<ImageButton>(R.id.updatebtn).setVisibility(View.INVISIBLE)
        viewsList2.add(view)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("메모", "size!!!!!!!!!! = ${listDate.size}")
        return listDate.size
        //return ItemCount + ItemPlus
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val memo = listDate.get(position)
        holder.setMemo(memo)
        //holder.todoList.text = "메롱" //Text
        //val mGradientDrawable = holder.rectangle.background as GradientDrawable
        //mGradientDrawable.setStroke(4, colorjava.getColor(1)!!)
        Log.d("aa","click22")
        holder.rectangle.setOnClickListener {
            buff_holder?.retRec()
            holder.setRec()
            buf_view3 = viewsList2.get(position)
            positionNum = position
            Todoupdate().buf_view4!!.findViewById<ImageButton>(R.id.updatebtn).setVisibility(View.VISIBLE)
            buff_holder = holder
        }
        Log.d("aa","$buf_view3")
        //buf_view3!!.findViewById<ImageButton>(R.id.update).setOnClickListener { Log.d("aa","fianl") }
        Log.d("aa","final")
    }

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var color: Int? = null
        var itemplus: Int? = null

        //데이터 베이스
        fun setMemo(memo: Memo) {
            itemplus = "${memo.ItemPlus}".toInt()
            itemView.todoList.text = "${memo.Text}"
            color = "${memo.Color}".toInt()
            val mGradientDrawable = itemView.rectangle.background as GradientDrawable
            mGradientDrawable.setStroke(8, colorjava.getColor(color)!!) //Color
        }
        fun setRec(){
            val mGradientDrawable = itemView.rectangle.background as GradientDrawable //Color
            mGradientDrawable.setColor(colorjava.getColor(color)!!)
        }
        fun retRec(){
            val mGradientDrawable = itemView.rectangle.background as GradientDrawable //Color
            mGradientDrawable.setColor(colorjava.getColor(10)!!)
        }

        //id
        var rectangle: ImageView
        var parentTodo: View
        var todoList: TextView
        var updateBtn : ImageButton

        init {

            rectangle = itemView.findViewById(R.id.rectangle)
            todoList = itemView.findViewById(R.id.todoList)
            parentTodo = itemView.findViewById(R.id.parentTodo)
            updateBtn = itemView.findViewById(R.id.updatebtn)
        }
    }

}