package com.louisgeek.as_2023_1_1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs
import kotlin.math.min

class LineView0301 : View {
    companion object {
        private const val TAG = "LineView0301"
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
    private lateinit var paintPoint: Paint

    private var linePointStart = PointF()
    private var linePointEnd = PointF()

    private var TOUCH_DIS = 0F
    private var LINE_MIN_SIZE = 0

//    private var lineDirectLT_RB = 0//
//    private var lineDirectTR_BL = 1
//    private var lineDirectRB_LT = 2
//    private var lineDirectBL_TR = 3
//    private var lineDirect = lineDirectLT_RB

    private var lineDirectLR_RRRR = false
    private var lineDirectTB_RRRR = false

    private var lineWidth = 8 //线宽度

    private lateinit var paintTest: Paint

    private fun init() {

        paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
        paint.strokeWidth = lineWidth.toFloat()

        paintPoint = Paint()
        paintPoint.isAntiAlias = true
        paintPoint.style = Paint.Style.STROKE
        paintPoint.color = Color.BLUE
        paintPoint.strokeWidth = 60F
        paintPoint.strokeCap = Paint.Cap.ROUND

        paintTest = Paint()
        paintTest.isAntiAlias = true
        paintTest.style = Paint.Style.STROKE
        paintTest.color = Color.BLUE
        paintTest.strokeWidth = 2F


        //
//        setLineDirect(lineDirectLT_RB)
//        setLineDirect(lineDirectTR_BL)
//        setLineDirect(lineDirectRB_LT)
//        setLineDirect(lineDirectBL_TR)


    }

//    private fun setLineDirect(lineDirect: Int) {
//        this.lineDirect = lineDirect
//    }

    private fun refreshSize(width: Int, height: Int) {
        TOUCH_DIS = 1.0F / 4 * min(width, height)

        val viewPointLT = PointF(0F + lineWidth / 2, 0F + lineWidth / 2)
        val viewPointTR = PointF(width.toFloat() - lineWidth / 2, 0F + lineWidth / 2)
        val viewPointRB = PointF(width.toFloat() - lineWidth / 2, height.toFloat() - lineWidth / 2)
        val viewPointBL = PointF(0F + lineWidth / 2, height.toFloat() - lineWidth / 2)
//        if (lineDirect == lineDirectLT_RB) {
//            linePointStart.set(viewPointLT)
//            linePointEnd.set(viewPointRB)
//        } else if (lineDirect == lineDirectTR_BL) {
//            linePointStart.set(viewPointTR)
//            linePointEnd.set(viewPointBL)
//        } else if (lineDirect == lineDirectRB_LT) {
//            linePointStart.set(viewPointRB)
//            linePointEnd.set(viewPointLT)
//        } else if (lineDirect == lineDirectBL_TR) {
//            linePointStart.set(viewPointBL)
//            linePointEnd.set(viewPointTR)
//        }
        if (!lineDirectLR_RRRR && !lineDirectTB_RRRR) {
            linePointStart.set(viewPointLT)
            linePointEnd.set(viewPointRB)
        } else if (lineDirectLR_RRRR && !lineDirectTB_RRRR) {
            linePointStart.set(viewPointTR)
            linePointEnd.set(viewPointBL)
        } else if (lineDirectLR_RRRR && lineDirectTB_RRRR) {
            linePointStart.set(viewPointRB)
            linePointEnd.set(viewPointLT)
        } else if (!lineDirectLR_RRRR && lineDirectTB_RRRR) {
            linePointStart.set(viewPointBL)
            linePointEnd.set(viewPointTR)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //
        refreshSize(measuredWidth, measuredHeight)
        //
        LINE_MIN_SIZE = (1.0F / 4 * min(measuredWidth, measuredHeight)).toInt()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        refreshSize(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.YELLOW)

//        canvas.drawRect()

        canvas.drawLine(
            linePointStart.x,
            linePointStart.y,
            linePointEnd.x,
            linePointEnd.y,
            paint
        )

        var lineSize = paint.strokeWidth / 2
        var pointSize = paintPoint.strokeWidth / 2
//        if (lineDirect == lineDirectLT_RB) {
//        } else if (lineDirect == lineDirectTR_BL) {
//
//        } else if (lineDirect == lineDirectRB_LT) {
//
//        } else if (lineDirect == lineDirectBL_TR) {
//
//        }
    }


    private var TOUCH_NONE = 0
    private var TOUCH_LEFT_TOP = 1
    private var TOUCH_TOP_RIGHT = 2
    private var TOUCH_RIGHT_BOTTOM = 3
    private var TOUCH_BOTTOM_LEFT = 4
    private var TOUCH_CENTER = 5
    private var touchW = TOUCH_NONE

    private var downPoint = Point()
    private var lastPoint = Point()

    private var originViewRect = Rect()
    private var newViewRect = Rect()
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downPoint.set(event.rawX.toInt(), event.rawY.toInt())
                lastPoint.set(event.rawX.toInt(), event.rawY.toInt())
                //
                originViewRect.set(this.left, this.top, this.right, this.bottom)
                //init
                newViewRect.set(this.left, this.top, this.right, this.bottom)

                dealDown(event)
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val disX = event.rawX.toInt() - downPoint.x
                val disY = event.rawY.toInt() - downPoint.y
                val diffX = event.rawX.toInt() - lastPoint.x
                val diffY = event.rawY.toInt() - lastPoint.y
                if (abs(disX) > 10 || abs(disY) > 10) {
                    if (touchW == TOUCH_LEFT_TOP) {
                        changeLeft(disX)
                        changeTop(disY)
                    } else if (touchW == TOUCH_TOP_RIGHT) {
                        changeTop(disY)
                        changeRight(disX)
                    } else if (touchW == TOUCH_RIGHT_BOTTOM) {
                        changeRight(disX)
                        changeBottom(disY)
                    } else if (touchW == TOUCH_BOTTOM_LEFT) {
                        changeBottom(disY)
                        changeLeft(disX)
                    } else if (touchW == TOUCH_CENTER) {
                        newViewRect.left = originViewRect.left + disX
                        newViewRect.top = originViewRect.top + disY
                        newViewRect.right = originViewRect.right + disX
                        newViewRect.bottom = originViewRect.bottom + disY
                        val parentView = this.parent as View
                        if (newViewRect.left < 0) {
                            newViewRect.left = 0
                            newViewRect.right = this.width
                        }
                        if (newViewRect.top < 0) {
                            newViewRect.top = 0
                            newViewRect.bottom = this.height
                        }
                        if (newViewRect.right > parentView.width) {
                            newViewRect.right = parentView.width
                            newViewRect.left = parentView.width - this.width
                        }
                        if (newViewRect.bottom > parentView.height) {
                            newViewRect.bottom = parentView.height
                            newViewRect.top = parentView.height - this.height
                        }
                    }
                    this.left = newViewRect.left
                    this.top = newViewRect.top
                    this.right = newViewRect.right
                    this.bottom = newViewRect.bottom
                }
                //
                lastPoint.set(event.rawX.toInt(), event.rawY.toInt())
            }

            MotionEvent.ACTION_UP -> {
                downPoint.set(0, 0)
                lastPoint.set(0, 0)

                touchW = TOUCH_NONE
            }
        }

        return super.onTouchEvent(event)
    }

    private fun changeLeft(disX: Int) {
        Log.e(TAG, "changeLeft: disX=$disX")
        newViewRect.left = originViewRect.left + disX
        if (newViewRect.left > originViewRect.right - lineWidth) {
            newViewRect.left = originViewRect.right - lineWidth
            newViewRect.right = originViewRect.left + disX + lineWidth
            lineDirectLR_RRRR = true
        }
        if (newViewRect.left < originViewRect.right - lineWidth && lineDirectLR_RRRR) { //？？？
            newViewRect.left = originViewRect.right - lineWidth
            newViewRect.right = originViewRect.left + disX + lineWidth
            lineDirectLR_RRRR = false
        }
    }

    private fun changeTop(disY: Int) {
        Log.e(TAG, "changeTop:  disY=$disY")
        newViewRect.top = originViewRect.top + disY
        if (newViewRect.top > originViewRect.bottom - lineWidth) {
            newViewRect.top = originViewRect.bottom - lineWidth
            newViewRect.bottom = originViewRect.top + disY + lineWidth
            lineDirectTB_RRRR = true
        }
        if (newViewRect.top < originViewRect.bottom - lineWidth && lineDirectTB_RRRR) { //？？？
            newViewRect.top = originViewRect.bottom - lineWidth
            newViewRect.bottom = originViewRect.top + disY + lineWidth
            lineDirectTB_RRRR = false
        }
    }

    private fun changeRight(disX: Int) {
//        Log.e(TAG, "changeRight: lineDirect=$lineDirect" )
        newViewRect.right = originViewRect.right + disX
        if (newViewRect.right < originViewRect.left + lineWidth) {
            newViewRect.right = originViewRect.left + lineWidth
            newViewRect.left = originViewRect.right + disX - lineWidth
            lineDirectLR_RRRR = true
        }
        if (newViewRect.right > originViewRect.left + lineWidth && lineDirectLR_RRRR) { //？？？
            newViewRect.right = originViewRect.left + lineWidth
            newViewRect.left = originViewRect.right + disX - lineWidth
            lineDirectLR_RRRR = false
        }
    }

    private fun changeBottom(disY: Int) {
        Log.e(TAG, "changeBottom: disY=$disY")
        newViewRect.bottom = originViewRect.bottom + disY
        if (newViewRect.bottom < originViewRect.top + lineWidth) {
            newViewRect.bottom = originViewRect.top + lineWidth
            newViewRect.top = originViewRect.bottom + disY - lineWidth
            lineDirectTB_RRRR = true
        }
        if (newViewRect.bottom > originViewRect.top + lineWidth && lineDirectTB_RRRR) { //???
            newViewRect.bottom = originViewRect.top + lineWidth
            newViewRect.top = originViewRect.bottom + disY - lineWidth
            lineDirectTB_RRRR = false
        }
    }

    private fun dealDown(event: MotionEvent) {
        val x = event.x
        val y = event.y
        val w = this.width
        val h = this.height
        if (x > 0 - TOUCH_DIS && x < 0 + TOUCH_DIS &&
            y > 0 - TOUCH_DIS && y < 0 + TOUCH_DIS
        ) {
            touchW = TOUCH_LEFT_TOP
            Log.e(TAG, "dealDown: TOUCH_LEFT_TOP")
        } else if (x > w - TOUCH_DIS && x < w + TOUCH_DIS &&
            y > 0 - TOUCH_DIS && y < 0 + TOUCH_DIS
        ) {
            touchW = TOUCH_TOP_RIGHT
            Log.e(TAG, "dealDown: TOUCH_TOP_RIGHT")
        } else if (x > w - TOUCH_DIS && x < w + TOUCH_DIS &&
            y > h - TOUCH_DIS && h < h + TOUCH_DIS
        ) {
            touchW = TOUCH_RIGHT_BOTTOM
            Log.e(TAG, "dealDown: TOUCH_RIGHT_BOTTOM")
        } else if (x > 0 - TOUCH_DIS && x < 0 + TOUCH_DIS &&
            y > h - TOUCH_DIS && y < h + TOUCH_DIS
        ) {
            touchW = TOUCH_BOTTOM_LEFT
            Log.e(TAG, "dealDown: TOUCH_BOTTOM_LEFT")
        } else if (x > 0 + TOUCH_DIS && x < w - TOUCH_DIS &&
            y > 0 + TOUCH_DIS && y < h - TOUCH_DIS
        ) {
            touchW = TOUCH_CENTER
            Log.e(TAG, "dealDown: TOUCH_CENTER")
        }
    }
}