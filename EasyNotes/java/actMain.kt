package com.example.memo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.act_main.*


class actMain : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_main)
        loadData()
    }

    override fun onResume(){
        super.onResume()
        loadData()
    }

    fun loadData(){
        val db=dbManager(this)
        val adapter= listAdaptor(db.select())
        firstNotes.adapter = adapter
        firstNotes.setOnItemClickListener {parent:AdapterView<*>,view:View,position:Int,id:Long ->
            val i = Intent(this,actAddUpdateNotes::class.java)
            i.putExtra("state",firstNotes.getItemAtPosition(position).toString())
            startActivity(i)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?):Boolean{
        menuInflater.inflate(R.menu.menu_main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item != null){
            if(item.itemId == R.id.menuAdd){
                val i = Intent(this, actAddUpdateNotes::class.java)
                i.putExtra("state", "AddOnly")
                startActivity(i)
            }else{
//                val i = Intent(this,actAbout::class.java)
//                startActivity(i)
            }
        }
        //return super.onOptionsItemSelected(item)
        return super.onContextItemSelected(item)
    }

    inner class listAdaptor: BaseAdapter{
        var listItem = ArrayList<listItem>()
        constructor(listItem:ArrayList<listItem>){
            this.listItem=listItem
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view=layoutInflater.inflate(R.layout.custom_listview,null)
            val tvTitle: TextView =view!!.findViewById(R.id.tvTitle)
            val tvDateCreated: TextView=view!!.findViewById(R.id.tvDateCreatedView)

            tvTitle.text=listItem[position].title
            tvDateCreated.text=listItem[position].dateTime

            return view
        }

        override fun getItem(position: Int): Any {
            return listItem[position].id
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listItem.count()
        }

    }
}
