package com.example.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dialog = AlertDialog.Builder(this)
        val dialogView= layoutInflater.inflate(R.layout.custom_dialog,null)
        val et_number = dialogView.findViewById<EditText>(R.id.et_number)
        dialog.setView(dialogView)
        dialog.setCancelable(false)
        dialog.setPositiveButton("validate",{ dialogInterface: DialogInterface, i:Int->})//if I click the validate button, the dialog die
        val customDialog = dialog.create()
        customDialog.show()
        customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if(et_number.text.length>5)
                customDialog.dismiss()
            else
                Toast.makeText(baseContext,"Number not valid", Toast.LENGTH_SHORT).show()
        }

    }


}
