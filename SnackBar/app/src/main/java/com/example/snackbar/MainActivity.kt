package com.example.snackbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_show.setOnClickListener{
            // basic
//            val snackbar = Snackbar.make(root_layout,"This is Snackbar",Snackbar.LENGTH_LONG)
//            snackbar.show()

            // interact with user
            val snackbar = Snackbar.make(root_layout,"This is Snackbar",Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction("Close", View.OnClickListener{
                snackbar.dismiss()
            })
            snackbar.setActionTextColor(ContextCompat.getColor(this,R.color.colorAccent))
            val view = snackbar.view
            snackbar.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            snackbar.show()
        }

    }
}
