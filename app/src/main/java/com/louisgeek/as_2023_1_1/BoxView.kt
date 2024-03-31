package com.louisgeek.as_2023_1_1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.view.marginStart
import kotlin.math.abs

class BoxView : View {
    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private lateinit var paint: Paint
    private lateinit var paintTest: Paint
    private lateinit var paintPoint: Paint
    private lateinit var rectF: RectF

    private lateinit var rectFPointLT: RectF
    private lateinit var rectFPointTR: RectF
    private lateinit var rectFPointRB: RectF
    private lateinit var rectFPointBL: RectF

    private lateinit var rectFLineLeft: RectF
    private lateinit var rectFLineTop: RectF
    private lateinit var rectFLineRight: RectF
    private lateinit var rectFLineBottom: RectF
    private var rectFList: MutableList<RectF> = arrayListOf()

    //    private val scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private val scaledTouchSlop = 10
    private fun init() {
        paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
        paint.strokeWidth = 10F

        paintTest = Paint()
        paintTest.isAntiAlias = true
        paintTest.style = Paint.Style.STROKE
        paintTest.color = Color.CYAN
        paintTest.strokeWidth = 5F

        paintPoint = Paint()
        paintPoint.isAntiAlias = true
        paintPoint.style = Paint.Style.STROKE
        paintPoint.color = Color.BLUE
        paintPoint.strokeWidth = 30F
        paintPoint.strokeCap = Paint.Cap.ROUND //圆点

        rectF = RectF()
        rectF.set(20F, 30F, 300F, 400F)

        rectFPointLT = RectF()
        rectFPointLT.set(
            rectF.left - paintPoint.strokeWidth / 2,
            rectF.top - paintPoint.strokeWidth / 2,
            rectF.left + paintPoint.strokeWidth / 2,
            rectF.top + paintPoint.strokeWidth / 2
        )
        rectFPointLT.inset(-10F, -10F)
//
        rectFPointTR = RectF()
        rectFPointTR.set(
            rectF.right - paintPoint.strokeWidth / 2,
            rectF.top - paintPoint.strokeWidth / 2,
            rectF.right + paintPoint.strokeWidth / 2,
            rectF.top + paintPoint.strokeWidth / 2
        )

        rectFPointRB = RectF()
        rectFPointRB.set(
            rectF.right - paintPoint.strokeWidth / 2,
            rectF.bottom - paintPoint.strokeWidth / 2,
            rectF.right + paintPoint.strokeWidth / 2,
            rectF.bottom + paintPoint.strokeWidth / 2
        )

        rectFPointBL = RectF()
        rectFPointBL.set(
            rectF.left - paintPoint.strokeWidth / 2,
            rectF.bottom - paintPoint.strokeWidth / 2,
            rectF.left + paintPoint.strokeWidth / 2,
            rectF.bottom + paintPoint.strokeWidth / 2
        )
//
        rectFLineLeft = RectF()
        rectFLineLeft.set(rectF.left, rectF.top, rectF.left, rectF.bottom)
        rectFLineLeft.inset(-paint.strokeWidth * 5, -paint.strokeWidth * 5)
//
        rectFLineTop = RectF()
        rectFLineTop.set(rectF.left, rectF.top, rectF.right, rectF.top)
        rectFLineTop.inset(-10F, -10F)

        val xxx1 = ViewConfiguration.get(context).scaledTouchSlop
        val xxx2 = ViewConfiguration.get(context).scaledPagingTouchSlop
        val xxx3 = ViewConfiguration.get(context).scaledWindowTouchSlop
        Log.e("TAG", "init: xxx1=$xxx1")
        Log.e("TAG", "init: xxx2=$xxx2")
        Log.e("TAG", "init: xxx3=$xxx3")
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(
            rectF.width().toInt() + (rectFPointLT.width() + rectFPointRB.width()).toInt(),
            rectF.height().toInt() + (rectFPointLT.height() + rectFPointBL.height()).toInt()
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.YELLOW)
        canvas.drawRect(rectF, paint)
        //
//        canvas.drawPoint(
//            rectFPointLT.left + rectFPointLT.width() / 2,
//            rectFPointLT.top + rectFPointLT.height() / 2,
//            paintPoint
//        )
//        canvas.drawPoint(
//            rectFPointTR.left + rectFPointTR.width() / 2,
//            rectFPointTR.top + rectFPointTR.height() / 2,
//            paintPoint
//        )
//        canvas.drawPoint(
//            rectFPointRB.left + rectFPointRB.width() / 2,
//            rectFPointRB.top + rectFPointRB.height() / 2,
//            paintPoint
//        )
//        canvas.drawPoint(
//            rectFPointBL.left + rectFPointBL.width() / 2,
//            rectFPointBL.top + rectFPointBL.height() / 2,
//            paintPoint
//        )
//        canvas.drawRect(rectFPoint,paint)
//        rectFList.add(rectFPoint)
//        canvas.drawRect(rectFPointLT,paint)
        canvas.drawRect(rectFLineLeft, paintTest)
    }

    private var lastX = 0F
    private var lastY = 0F
    private var nowIsLeft = false
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return super.onTouchEvent(event)
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y

                if (rectFLineLeft.contains(event.x, event.y)) {
                    Log.e("TAG", "onTouchEvent: sssssssssssssss")
//                    rectF.set(event.x, rectF.top, rectF.right, rectF.bottom)
//                    rectFLineLeft.set(rectF.left, rectF.top, rectF.left, rectF.bottom)
//                    rectFLineLeft.inset(-paint.strokeWidth*5,-paint.strokeWidth*5)
//                    rectFPointLT.offset(dx, 0F)
//                    rectFPointTR.offset(0F, 0F)
//                    rectFPointRB.offset(0F, 0F)
//                    rectFPointBL.offset(dx, 0F)
                    nowIsLeft = true
//                    invalidate()

                }
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - lastX
                val dy = event.y - lastY
//                if (rectFList[0].contains(event.x,event.y)){
//                if (rectFPointLT.contains(event.x, event.y)) {
//                    Log.e("TAG", "onTouchEvent: aaaaaaaaaaaaaaaaaa" )
//                    rectF.set(rectF.left + dx, rectF.top + dy, rectF.right, rectF.bottom)
//                    rectFPointLT.offset(dx, dy)
//                    rectFPointTR.offset(0F, dy)
//                    rectFPointRB.offset(0F, 0F)
//                    rectFPointBL.offset(dx, 0F)
//                    invalidate()
////                    lastX = event.x
////                    lastY = event.y
//                    return true
//                }
//
//                if (rectFPointTR.contains(event.x, event.y)) {
//                    Log.e("TAG", "onTouchEvent: aaaaaaaaaaaaaaaaaa" )
//                    rectF.set(rectF.left + dx, rectF.top + dy, rectF.right, rectF.bottom)
//                    rectFPointLT.offset(0F, dy)
//                    rectFPointTR.offset(dx, dy)
//                    rectFPointRB.offset(0F, 0F)
//                    rectFPointBL.offset(dx, 0F)
//                    invalidate()
////                    lastX = event.x
////                    lastY = event.y
//                    return true
//                }
                if (abs(dx) > scaledTouchSlop || abs(dy) > scaledTouchSlop) {
                    if (nowIsLeft) {
                        rectF.set(rectF.left + dx, rectF.top, rectF.right, rectF.bottom)
                        rectFLineLeft.set(rectF.left, rectF.top, rectF.left, rectF.bottom)
                        rectFLineLeft.inset(-paint.strokeWidth * 5, -paint.strokeWidth * 5)
//                    rectFPointLT.offset(dx, 0F)
//                    rectFPointTR.offset(0F, 0F)
//                    rectFPointRB.offset(0F, 0F)
//                    rectFPointBL.offset(dx, 0F)
                        invalidate()
                    }
                }
                Log.e("TAG", "onTouchEvent: eeeeeeeeeeee event=$event")

//                if (rectFLineTop.contains(event.x, event.y)) {
//                    rectF.set(rectF.left, rectF.top + dy, rectF.right, rectF.bottom)
//                    rectFPointLT.offset(0F, dy)
//                    rectFPointTR.offset(0F, dy)
//                    rectFPointRB.offset(0F, 0F)
//                    rectFPointBL.offset(0F, 0F)
//                    invalidate()
////                    lastX = event.x
////                    lastY = event.y
//                    return true
//                }
//                rectF.set(rectF.left+dx,rectF.top+dy,rectF.right+dx,rectF.bottom+dy)
//
//                invalidate()

                lastX = event.x
                lastY = event.y
                return true
            }

            MotionEvent.ACTION_UP -> {
                lastX = 0F
                lastY = 0F
                nowIsLeft = false
            }
        }
        return super.onTouchEvent(event)
    }

}