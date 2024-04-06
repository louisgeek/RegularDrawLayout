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

class LineView03 : View {
    companion object {
        private const val TAG = "LineView03"
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


    private lateinit var paintTest: Paint
    private var touchDisWid = 0F
    private var touchDisHei = 0F

    private var TOUCH_NONE = 0
    private var TOUCH_LEFT_TOP = 1
    private var TOUCH_TOP_RIGHT = 2
    private var TOUCH_RIGHT_BOTTOM = 3
    private var TOUCH_BOTTOM_LEFT = 4
    private var TOUCH_CENTER = 5
    private var touchArea = TOUCH_NONE

    //
    private var touchRectLeftTop = RectF()
    private var touchRectTopRight = RectF()
    private var touchRectRightBottom = RectF()
    private var touchRectBottomLeft = RectF()
    private var touchRectCenter = RectF()


    //line
    private lateinit var paintLine: Paint //画线
    private lateinit var paintLineDot: Paint //画线的两个端点
    private var lineDotStart = PointF() //线的起点坐标
    private var lineDotEnd = PointF() //线的终点坐标
    private var LINE_WIDTH = 8 //线的宽度


    //
    private var lineLengthMin = 0 //线的最小长度
    private var lineDirectLR_Reverse = false
    private var lineDirectTB_Reverse = false


    private fun init() {
        paintTest = Paint()
        paintTest.isAntiAlias = true
        paintTest.style = Paint.Style.FILL_AND_STROKE
        paintTest.color = Color.BLUE
        paintTest.strokeWidth = 2F

        //
        paintLine = Paint()
        paintLine.isAntiAlias = true
        paintLine.style = Paint.Style.STROKE
        paintLine.color = Color.RED
        paintLine.strokeWidth = LINE_WIDTH.toFloat()

        paintLineDot = Paint()
        paintLineDot.isAntiAlias = true
        paintLineDot.style = Paint.Style.STROKE
        paintLineDot.color = Color.BLUE
        paintLineDot.strokeWidth = 30F
        paintLineDot.strokeCap = Paint.Cap.ROUND

        //
//        setLineDirect(lineDirectLT_RB)
    }

//    private fun setLineDirect(lineDirect: Int) {
//        this.lineDirect = lineDirect
//    }

    private fun refreshSize(wid: Float, hei: Float) {
        //设定可触摸区域
        touchDisWid = 1.0F / 4 * wid
        touchDisHei = 1.0F / 4 * hei

        //设定线的启动和终点---以view为基准
        val viewPointLeftTop = PointF(0F + LINE_WIDTH / 2, 0F + LINE_WIDTH / 2)
        val viewPointTopRight = PointF(wid - LINE_WIDTH / 2, 0F + LINE_WIDTH / 2)
        val viewPointRightBottom = PointF(wid - LINE_WIDTH / 2, hei - LINE_WIDTH / 2)
        val viewPointBottomLeft = PointF(0F + LINE_WIDTH / 2, hei - LINE_WIDTH / 2)
        //
        if (!lineDirectLR_Reverse && !lineDirectTB_Reverse) {
            lineDotStart.set(viewPointLeftTop)
            lineDotEnd.set(viewPointRightBottom)
        } else if (lineDirectLR_Reverse && !lineDirectTB_Reverse) {
            lineDotStart.set(viewPointTopRight)
            lineDotEnd.set(viewPointBottomLeft)
        } else if (lineDirectLR_Reverse && lineDirectTB_Reverse) {
            lineDotStart.set(viewPointRightBottom)
            lineDotEnd.set(viewPointLeftTop)
        } else if (!lineDirectLR_Reverse && lineDirectTB_Reverse) {
            lineDotStart.set(viewPointBottomLeft)
            lineDotEnd.set(viewPointTopRight)
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //
        refreshSize(measuredWidth.toFloat(), measuredHeight.toFloat())
        //
//        lineLengthMin = (1.0F / 4 * min(measuredWidth, measuredHeight)).toInt()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

//        refreshSize(w.toFloat(), h.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.YELLOW)


        canvas.drawLine(
            lineDotStart.x,
            lineDotStart.y,
            lineDotEnd.x,
            lineDotEnd.y,
            paintLine
        )

//        paintLineDot.color = Color.BLUE
//        canvas.drawPoint(
//            lineDotStart.x,
//            lineDotStart.y,
//            paintLineDot
//        )
//        paintLineDot.color = Color.GREEN
//        canvas.drawPoint(
//            lineDotEnd.x,
//            lineDotEnd.y,
//            paintLineDot
//        )

    }

    private var downPoint = Point()
    private var lastPoint = Point()

    private var originViewRect = Rect()
    private var newViewRect = Rect()

//    private var downLineDirectLR_Reverse = false
//    private var downLineDirectTB_Reverse = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downPoint.set(event.rawX.toInt(), event.rawY.toInt())
                lastPoint.set(event.rawX.toInt(), event.rawY.toInt())
                //
                originViewRect.set(this.left, this.top, this.right, this.bottom)
                Log.e(TAG, "onTouchEvent: ori=$originViewRect")
                //init
                newViewRect.set(this.left, this.top, this.right, this.bottom)
                //
//                downLineDirectLR_Reverse = lineDirectLR_Reverse
//                downLineDirectTB_Reverse = lineDirectTB_Reverse

                dealDown(event)
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val disX = event.rawX.toInt() - downPoint.x
                val disY = event.rawY.toInt() - downPoint.y
                val diffX = event.rawX.toInt() - lastPoint.x
                val diffY = event.rawY.toInt() - lastPoint.y
                if (abs(disX) > 10 || abs(disY) > 10) {
                    if (touchArea == TOUCH_LEFT_TOP) {
                        changeLeft(disX)
                        changeTop(disY)
                    } else if (touchArea == TOUCH_TOP_RIGHT) {
                        changeTop(disY)
                        changeRight(disX)
                    } else if (touchArea == TOUCH_RIGHT_BOTTOM) {
                        changeRight(disX)
                        changeBottom(disY)
                    } else if (touchArea == TOUCH_BOTTOM_LEFT) {
                        changeBottom(disY)
                        changeLeft(disX)
                    } else if (touchArea == TOUCH_CENTER) {
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
//                downLineDirectLR_Reverse = false
//                downLineDirectTB_Reverse = false
                touchArea = TOUCH_NONE
            }
        }

        return super.onTouchEvent(event)
    }

    private fun changeLeft(disX: Int) {
        newViewRect.left = originViewRect.left + disX
    }

    private fun changeTop(disY: Int) {
        newViewRect.top = originViewRect.top + disY
    }

    private fun changeRight(disX: Int) {
        newViewRect.right = originViewRect.right + disX
    }

    private fun changeBottom(disY: Int) {
        newViewRect.bottom = originViewRect.bottom + disY
    }

    private fun dealDown(event: MotionEvent) {
        val eventX = event.x
        val eventY = event.y
        val wid = this.width
        val hei = this.height

        touchRectLeftTop.set(
            0 - touchDisWid,
            0 - touchDisHei,
            0 + touchDisWid,
            0 + touchDisHei
        )
        touchRectTopRight.set(
            wid - touchDisWid,
            0 - touchDisHei,
            wid + touchDisWid,
            0 + touchDisHei
        )
        touchRectRightBottom.set(
            wid - touchDisWid,
            hei - touchDisHei,
            wid + touchDisWid,
            hei + touchDisHei
        )
        touchRectBottomLeft.set(
            0 - touchDisWid,
            hei - touchDisHei,
            0 + touchDisWid,
            hei + touchDisHei
        )
        touchRectCenter.set(
            0 + touchDisWid,
            0 + touchDisHei,
            wid - touchDisWid,
            hei - touchDisHei
        )
        if (touchRectLeftTop.contains(eventX, eventY)) {
            touchArea = TOUCH_LEFT_TOP
            Log.e(TAG, "dealDown: TOUCH_LEFT_TOP")
        } else if (touchRectTopRight.contains(eventX, eventY)) {
            touchArea = TOUCH_TOP_RIGHT
            Log.e(TAG, "dealDown: TOUCH_TOP_RIGHT")
        } else if (touchRectRightBottom.contains(eventX, eventY)) {
            touchArea = TOUCH_RIGHT_BOTTOM
            Log.e(TAG, "dealDown: TOUCH_RIGHT_BOTTOM")
        } else if (touchRectBottomLeft.contains(eventX, eventY)) {
            touchArea = TOUCH_BOTTOM_LEFT
            Log.e(TAG, "dealDown: TOUCH_BOTTOM_LEFT")
        } else if (touchRectCenter.contains(eventX, eventY)) {
            touchArea = TOUCH_CENTER
            Log.e(TAG, "dealDown: TOUCH_CENTER")
        }
    }
}
