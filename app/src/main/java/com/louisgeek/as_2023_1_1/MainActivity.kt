package com.louisgeek.as_2023_1_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv: TextView = findViewById(R.id.tv)
        val rdl: RegularDrawLayout = findViewById(R.id.rdl)
        tv.setOnClickListener {

            val boxView = BoxView(this)

            rdl.addBoxView(boxView)
        }
    }
}