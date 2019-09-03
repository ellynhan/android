package com.example.dialogwriting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.login_dialog.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //button click to show dialog
        mainLoginBtn.setOnClickListener{
            //Inflate the dialog with custom view
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.login_dialog,null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Login Form")
            //show dialog
            val mAlertDialog = mBuilder.show()
            //login button click of custom layout
            mDialogView.dialogLoginBtn.setOnClickListener{
                //dismiss dialog
                mAlertDialog.dismiss()
                //get text from EditText of custom layout
                val name=mDialogView.dialogNameEt.text.toString()
                val email=mDialogView.dialogEmailEt.text.toString()
                val password=mDialogView.dialogPasswordEt.text.toString()
                //set the input text in TextView
                mainInfoTv.setText("Name:"+ name +"\nEmail: "+email+"\nPassword: "+password)
            }
            mDialogView.dialogCancelBtn.setOnClickListener{
                mAlertDialog.dismiss()
            }
        }
    }

}
