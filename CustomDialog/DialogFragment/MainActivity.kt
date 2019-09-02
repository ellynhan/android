package com.example.dialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fm = supportFragmentManager
        val myFragment=MyFragment()
        //myFragment.show(fm,"Simple Framgment")

        val btn = findViewById(R.id.button) as Button
        btn.setOnClickListener{
            myFragment.show(fm,"Simple Fragment")
        }
    }
}
