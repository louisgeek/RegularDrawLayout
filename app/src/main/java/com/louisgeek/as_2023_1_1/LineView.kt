package com.louisgeek.as_2023_1_1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import kotlin.math.abs

class LineView : View {
    companion object {
        private const val TAG = "LineView"
    }

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
    private lateinit var rectF: RectF
    private var startX = 0F
    private var startY = 0F
    private var endX = 0F
    private var endY = 0F
    private var TOUCH_NONE = 0
    private var TOUCH_START = 1
    private var TOUCH_CENTER = 2
    private var TOUCH_END = 3
    private var touchWhere = TOUCH_NONE
    private var scaledTouchSlop = 10
    private var pointWidth = 40F
    private fun init() {
        paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
        paint.strokeWidth = 5f

        paintTest = Paint()
        paintTest.isAntiAlias = true
        paintTest.style = Paint.Style.STROKE
        paintTest.color = Color.BLUE
        paintTest.strokeWidth = 2f

//        rectF = RectF()
//        rectF.set(startX, startY, 200F, 400F)
        startX = 20F
        startY = 50F
        endX = 320F
        endY = 350F
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension((endX - startX).toInt(), (endY - startY).toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.GREEN)
        Log.e(TAG, "onDraw: startX=$startX startY=$startY")
//        canvas.drawLine(rectF.left, rectF.top, rectF.right, rectF.bottom, paint)
        canvas.drawLine(startX, startY, endX, endY, paint)


        canvas.drawRect(
            startX - pointWidth,
            startY - pointWidth,
            startX + pointWidth,
            startY + pointWidth,
            paintTest
        )

        canvas.drawRect(
            startX + pointWidth,
            startY + pointWidth,
            endX - pointWidth,
            endY - pointWidth,
            paintTest
        )
    }

    var lastX = 0F
    var lastY = 0F
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return super.onTouchEvent(event)
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y
                if (event.x > startX - pointWidth && event.y > startY - pointWidth
                    && event.x < startX + pointWidth && event.y < startY + pointWidth
                ) {
                    touchWhere = TOUCH_START
                } else if (event.x > startX + pointWidth && event.y > startY + pointWidth
                    && event.x < endX - pointWidth && event.y < endY - pointWidth
                ) {
                    touchWhere = TOUCH_CENTER
                } else if (event.x > endX + pointWidth && event.y > endX + pointWidth
                    && event.x < endX - pointWidth && event.y < endY - pointWidth
                ) {
                    touchWhere = TOUCH_END
                }
                Log.e("TAG", "onTouchEvent: ACTION_DOWN touchWhere=$touchWhere")
//                if (event.x > startX - pointWidth && event.y > startY - pointWidth
//                    && event.x < startX + pointWidth && event.y < startY + pointWidth
//                ) {
//                    Log.e("TAG", "onTouchEvent: dddddddddddddddddd")
//                    isStart = true
//                }
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
                    if (touchWhere == TOUCH_START) {

                    } else if (touchWhere == TOUCH_CENTER) {
//                    startX = event.x
//                    startY = event.y
//                        rectF.set(event.x, event.y, rectF.right, rectF.bottom)
//                        rectFLineLeft.set(rectF.left, rectF.top, rectF.left, rectF.bottom)
//                        rectFLineLeft.inset(-paint.strokeWidth * 5, -paint.strokeWidth * 5)
//                    rectFPointLT.offset(dx, 0F)
//                    rectFPointTR.offset(0F, 0F)
//                    rectFPointRB.offset(0F, 0F)
//                    rectFPointBL.offset(dx, 0F)
//
//                    left = startX.toInt()
//                    top = startY.toInt()
//                    this.layout(left, top, right, bottom)
//                    requestLayout()
//                    invalidate()

//                    this.layout(l, t, right + dx.toInt(), bottom + dy.toInt())
//                    this.offsetLeftAndRight(dx.toInt())
//                    this.offsetTopAndBottom(dy.toInt())
//                    val layoutParams = MarginLayoutParams(width,height)
                        var l = left + dx
//                        if (l < 0F) {
//                            l = 0F
//                        }
                        var t = top + dy
//                        if (t < 0F) {
//                            t = 0F
//                        }
                        val mlp = this.layoutParams as MarginLayoutParams
                        Log.e(
                            "TAG",
                            "onTouchEvent: sssssssssssssss leftMargin=${mlp.leftMargin} topMargin=${mlp.topMargin} le=${left}"
                        )
                        mlp.leftMargin = l.toInt()
                        mlp.topMargin = t.toInt()
                        Log.e(
                            "TAG",
                            "onTouchEvent: eeeeeeeeeeee leftMargin=${mlp.leftMargin} topMargin=${mlp.topMargin} le=${left}"
                        )
                        //内部调requestLayout
                        // 会改变 l t r b 的值?
                        this.layoutParams = mlp
                    }
                }


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

//                lastX = event.x
//                lastY = event.y
                return false
            }

            MotionEvent.ACTION_UP -> {
                lastX = 0F
                lastY = 0F
                touchWhere = TOUCH_NONE
            }
        }
        return super.onTouchEvent(event)
    }

}