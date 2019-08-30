package com.example.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment

class MyFragment: DialogFragment {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:Bundle?):{
        var rootView: View =inflater.inflate(R.layout.dialog_fragment,container,false)
        var cancelButton=rootView.findViewById<Button>(R.id.cancelButton)
        var submitButton=rootView.findViewById<Button>(R.id.submitButton)
        var surveyRadioGroup=rootView.findViewById<RadioGroup>(R.id.myradiogroup)

        cancelButton.setOnClickListener()
    }
}
