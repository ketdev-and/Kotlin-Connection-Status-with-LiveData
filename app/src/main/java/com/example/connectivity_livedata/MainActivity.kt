package com.example.connectivity_livedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData

class MainActivity : AppCompatActivity() {
    lateinit var connectionLD: ConnectionLD
    lateinit var constaint : ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        constaint = findViewById(R.id.constraint)
        connectionLD = ConnectionLD(this)
        connectionLD.observe(this,{
            if(it == true){
                constaint.visibility = View.GONE
            }else{
                constaint.visibility = View.VISIBLE
            }
        })





    }
}


