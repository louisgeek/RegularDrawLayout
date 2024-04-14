package com.louisgeek.as_2023_1_1

import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Size
import android.widget.TextView
import com.louisgeek.as_2023_1_1.cc.GZLayout
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv: TextView = findViewById(R.id.tv)
        val tvEnd: TextView = findViewById(R.id.tvEnd)
//        val rdl: RegularDrawLayout = findViewById(R.id.rdl)
        val gzLayout: GZLayout = findViewById(R.id.gzLayout)
        tv.setOnClickListener {
            gzLayout.removeAllViews()

            val dotPoint = Point(0, 0)
//            val dotPoint = Point(200, 200)
//            gzLayout.addDotView(dotPoint)

//            val lineDotStart = Point(0, 0)
//            val lineDotEnd = Point(200, 200)

            val lineDotStart = Point(300, 300)
            val lineDotEnd = Point(600, 600)

//            val lineDotStart = Point(200, 200)
//            val lineDotEnd = Point(400, 400)

//            val lineDotStart = Point(1080, 0)
//            val lineDotEnd = Point(880, 200)


            val lineRegionWid = abs(lineDotEnd.x - lineDotStart.x)
            val lineRegionHei = abs(lineDotEnd.y - lineDotStart.y)
            val lineRegionSize = Size(lineRegionWid, lineRegionHei)

//            gzLayout.addLineView(lineDotStart, lineDotEnd, lineRegionSize)

            val boxDotStart = Point(0, 0)
            val boxDotEnd = Point(200, 200)

//            val boxDotStart = Point(200, 200)
//            val boxDotEnd = Point(400, 400)

            val boxRegionWid = abs(lineDotEnd.x - lineDotStart.x)
            val boxRegionHei = abs(lineDotEnd.y - lineDotStart.y)
            val boxRegionSize = Size(boxRegionWid, boxRegionHei)

            gzLayout.addBoxView(boxDotStart, boxDotEnd, boxRegionSize)

        }

        tvEnd.setOnClickListener {
            gzLayout.getDotPointsList()
            gzLayout.getLinePointsList()
            gzLayout.getBoxPointsList()
        }

    }
}