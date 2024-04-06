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

class BoxView0301 : View {
    companion object {
        private const val TAG = "BoxView0301"
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


    //box
    private lateinit var paintBox: Paint //画框
    private lateinit var paintBoxDot: Paint //画框的四个端点
    private var boxRect = RectF()
    private var LINE_WIDTH = 80 //线的宽度


    //
    private var boxLengthMin = 0 //线的最小长度
//    private var lineDirectLR_Reverse = false
//    private var lineDirectTB_Reverse = false


    private fun init() {
        paintTest = Paint()
        paintTest.isAntiAlias = true
        paintTest.style = Paint.Style.FILL_AND_STROKE
        paintTest.color = Color.BLUE
        paintTest.strokeWidth = 2F

        //
        paintBox = Paint()
        paintBox.isAntiAlias = true
        paintBox.style = Paint.Style.STROKE
        paintBox.color = Color.RED
        paintBox.strokeWidth = LINE_WIDTH.toFloat()

        paintBoxDot = Paint()
        paintBoxDot.isAntiAlias = true
        paintBoxDot.style = Paint.Style.STROKE
        paintBoxDot.color = Color.BLUE
        paintBoxDot.strokeWidth = 60F
        paintBoxDot.strokeCap = Paint.Cap.ROUND


    }


    private fun refreshSize(wid: Float, hei: Float) {
        //设定可触摸区域
        touchDisWid = 1.0F / 4 * wid
        touchDisHei = 1.0F / 4 * hei

        //设定线的启动和终点---以view为基准
        //
        boxRect.set(
            0F + LINE_WIDTH / 2,
            0F + LINE_WIDTH / 2,
            wid - LINE_WIDTH / 2,
            hei - LINE_WIDTH / 2
        )
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

        refreshSize(w.toFloat(), h.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.YELLOW)

        paintTest.color = Color.CYAN
        canvas.drawRect(touchRectLeftTop, paintTest)
        paintTest.color = Color.GRAY
        canvas.drawRect(touchRectTopRight, paintTest)
        paintTest.color = Color.BLACK
        canvas.drawRect(touchRectRightBottom, paintTest)
        paintTest.color = Color.DKGRAY
        canvas.drawRect(touchRectBottomLeft, paintTest)
        paintTest.color = Color.RED
        canvas.drawRect(touchRectCenter, paintTest)

        canvas.drawRect(boxRect, paintBox)

//        paintBoxDot.color = Color.BLUE
//        canvas.drawPoint(
//            boxDotLeftTop.x,
//            boxDotLeftTop.y,
//            paintBoxDot
//        )
//        paintBoxDot.color = Color.GREEN
//        canvas.drawPoint(
//            boxDotBottomLeft.x,
//            boxDotBottomLeft.y,
//            paintBoxDot
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
        Log.e(TAG, "changeLeft: disX=$disX")
        newViewRect.left = originViewRect.left + disX
//        if (newViewRect.left > originViewRect.right - LINE_WIDTH) {
//            newViewRect.left = originViewRect.right - LINE_WIDTH
//            newViewRect.right = originViewRect.left + disX + LINE_WIDTH
//            lineDirectLR_Reverse = !downLineDirectLR_Reverse //!按下
//        }
//        if (lineDirectLR_Reverse != downLineDirectLR_Reverse) {
//            if (newViewRect.left < originViewRect.right - LINE_WIDTH) {
//                lineDirectLR_Reverse = downLineDirectLR_Reverse //按下
//            }
//        }
//        if (newViewRect.left < originViewRect.right - lineWidth) { //？？？
//            newViewRect.left = originViewRect.right - lineWidth
//            newViewRect.right = originViewRect.left + disX + lineWidth
//            lineDirectLR_RRRR = false
//        }
    }

    private fun changeTop(disY: Int) {
        newViewRect.top = originViewRect.top + disY
//        if (newViewRect.top > originViewRect.bottom - LINE_WIDTH) {
//            //上边到了下边的下面--交换两边
//            newViewRect.top = originViewRect.bottom - LINE_WIDTH
//            newViewRect.bottom = originViewRect.top + disY + LINE_WIDTH
//            lineDirectTB_Reverse = !downLineDirectTB_Reverse //!按下
//        }
//        if (lineDirectTB_Reverse != downLineDirectTB_Reverse) {
//            if (newViewRect.top < originViewRect.bottom - LINE_WIDTH) {
//                lineDirectTB_Reverse = downLineDirectTB_Reverse //按下
//            }
//        }

//        if (newViewRect.top < originViewRect.bottom - lineWidth) { //？？？
//            newViewRect.top = originViewRect.bottom - lineWidth
//            newViewRect.bottom = originViewRect.top + disY + lineWidth
//            lineDirectTB_RRRR = false
//        }
    }

    private fun changeRight(disX: Int) {
        Log.e(TAG, "changeRight: disX=$disX")
        newViewRect.right = originViewRect.right + disX
        //
//        if (newViewRect.right < originViewRect.left + LINE_WIDTH) {
//            newViewRect.right = originViewRect.left + LINE_WIDTH
//            newViewRect.left = originViewRect.right + disX - LINE_WIDTH
//            lineDirectLR_Reverse = !downLineDirectLR_Reverse //!按下
//        }
//        if (lineDirectLR_Reverse != downLineDirectLR_Reverse) {
//            if (newViewRect.right > originViewRect.left + LINE_WIDTH) {
//                lineDirectLR_Reverse = downLineDirectLR_Reverse //按下
//            }
//        }
//        if (newViewRect.right > originViewRect.left + lineWidth) { //？？？
//            newViewRect.right = originViewRect.left + lineWidth
//            newViewRect.left = originViewRect.right + disX - lineWidth
//        }
    }

    private fun changeBottom(disY: Int) {
        Log.e(TAG, "changeBottom: disY=$disY")
        newViewRect.bottom = originViewRect.bottom + disY
//        if (newViewRect.bottom < originViewRect.top + LINE_WIDTH) {
//            newViewRect.bottom = originViewRect.top + LINE_WIDTH
//            newViewRect.top = originViewRect.bottom + disY - LINE_WIDTH
//            lineDirectTB_Reverse = !downLineDirectTB_Reverse //!按下
//        }
//        if (lineDirectTB_Reverse != downLineDirectTB_Reverse) {
//            if (newViewRect.bottom > originViewRect.top + LINE_WIDTH) {
//                lineDirectTB_Reverse = downLineDirectTB_Reverse //按下
//            }
//        }
        //        if (newViewRect.bottom > originViewRect.top + lineWidth) { //???
//            newViewRect.bottom = originViewRect.top + lineWidth
//            newViewRect.top = originViewRect.bottom + disY - lineWidth
//            lineDirectTB_RRRR = false
//        }
    }

    private fun dealDown(event: MotionEvent) {
        val eventX = event.x
        val eventY = event.y
        val wid = this.width
        val hei = this.height

//        if (x > 0 - TOUCH_DIS && x < 0 + TOUCH_DIS &&
//            y > 0 - TOUCH_DIS && y < 0 + TOUCH_DIS
//        ) {
//            touchW = TOUCH_LEFT_TOP
//            Log.e(TAG, "dealDown: TOUCH_LEFT_TOP")
//        } else if (x > w - TOUCH_DIS && x < w + TOUCH_DIS &&
//            y > 0 - TOUCH_DIS && y < 0 + TOUCH_DIS
//        ) {
//            touchW = TOUCH_TOP_RIGHT
//            Log.e(TAG, "dealDown: TOUCH_TOP_RIGHT")
//        } else if (x > w - TOUCH_DIS && x < w + TOUCH_DIS &&
//            y > h - TOUCH_DIS && h < h + TOUCH_DIS
//        ) {
//            touchW = TOUCH_RIGHT_BOTTOM
//            Log.e(TAG, "dealDown: TOUCH_RIGHT_BOTTOM")
//        } else if (x > 0 - TOUCH_DIS && x < 0 + TOUCH_DIS &&
//            y > h - TOUCH_DIS && y < h + TOUCH_DIS
//        ) {
//            touchW = TOUCH_BOTTOM_LEFT
//            Log.e(TAG, "dealDown: TOUCH_BOTTOM_LEFT")
//        } else if (x > 0 + TOUCH_DIS && x < w - TOUCH_DIS &&
//            y > 0 + TOUCH_DIS && y < h - TOUCH_DIS
//        ) {
//            touchW = TOUCH_CENTER
//            Log.e(TAG, "dealDown: TOUCH_CENTER")
//        }
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

//    fun getLineDirectLR_RRRR(): Boolean {
//        return lineDirectLR_Reverse
//    }
//
//    fun getLineDirectTB_RRRR(): Boolean {
//        return lineDirectTB_Reverse
//    }
}
