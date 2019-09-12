package com.example.bottomsheetdialog

import android.app.usage.UsageEvents
import android.graphics.Color
import android.media.MediaCas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_layout.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        show_dialog.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.dialog_layout,null)
            val cancel = view.findViewById<TextView>(R.id.cancel_button)
            val One = view.findViewById<TextView>(R.id.one)
            val Two = view.findViewById<TextView>(R.id.two)
            val Three = view.findViewById<TextView>(R.id.three)

            cancel.setOnClickListener {
                dialog.dismiss()
            }

            One.setOnClickListener(OnClickListener)
            Two.setOnClickListener(OnClickListener)
            Three.setOnClickListener(OnClickListener)

            dialog.setContentView(view)
            dialog.show()
        }
    }
    val OnClickListener = View.OnClickListener {

    }

}
