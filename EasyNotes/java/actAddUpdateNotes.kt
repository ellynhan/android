package com.example.memo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.DropBoxManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.act_add_update_notes.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class actAddUpdateNotes : AppCompatActivity() {
    lateinit var state:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_add_update_notes)
        state=intent.getStringExtra("state")
        if(state=="AddOnly"){
            this.title="Add New Notes"
        }else{
            this.title= "Update Notes"
            val db=dbManager(this)
            try{
                val arr : ArrayList<String> = ArrayList(db.selectForUpdate(state.toInt()))
                etTitle.setText(arr[0])
                etDesc.setText(arr[1])
            }catch(e:Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?):Boolean{
        menuInflater.inflate(R.menu.menu_add_update,menu)
        if(state == "AddOnly"){
            val item:MenuItem=menu!!.findItem(R.id.menuDelete)
            item.isVisible=false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item != null){
            if(item.itemId==R.id.menuSave&&state=="AddOnly"){
                if(etTitle.text.toString()==""||etDesc.text.toString()==""){
                    Toast.makeText(this,"Please Enter Data First Before Inserted...",Toast.LENGTH_SHORT)
                } else{
                    val db=dbManager(this)
                    try{
                        val sdf: SimpleDateFormat = SimpleDateFormat("yyy/mm/dd-hh:mm:ss")
                        val dtNow:String = sdf.format(Date())
                        db.insert(etTitle.text.toString(),etDesc.text.toString(),dtNow)
                        Toast.makeText(this,"Insert New Notes Successfully...",Toast.LENGTH_SHORT)
                        finish()
                    } catch(e:Exception){
                        Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (item.itemId == R.id.menuSave&&state !="AddOnly"){
                if(etTitle.text.toString() == "" || etDesc.text.toString() =="")
                {
                    Toast.makeText(this,"Please Enter Data First Before Updated...",Toast.LENGTH_SHORT)
                }else{
                    val db=dbManager(this)
                    try{
                        db.update(state.toInt(),etTitle.text.toString(),etDesc.text.toString())
                        Toast.makeText(this,"Insert New Notes Successfully...",Toast.LENGTH_SHORT)
                        finish()
                    } catch(e:Exception){
                        Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()
                        Log.d("test","here??3")
                    }
                }
            } else if (item.itemId == R.id.menuDelete){
                AlertDialog.Builder(this)
                    .setTitle("Are You Sure Delete This Notes !!")
                    .setMessage("Do you want to Delete this note ?")
                    .setIcon(R.drawable.ic_delete)
                    .setNegativeButton("delete"){dialog, which ->
                        val db=dbManager(this)
                        try{
                            db.delete(state.toInt())
                            Toast.makeText(this,"Deleted Notes Successfully...", Toast.LENGTH_SHORT)
                            finish()
                        } catch(e:Exception){
                            Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setPositiveButton("Cancel"){dialog,which->
                        dialog.dismiss()
                    }
                    .create()
                    .show()

            }
        }
        return super.onOptionsItemSelected(item)
    }
}
