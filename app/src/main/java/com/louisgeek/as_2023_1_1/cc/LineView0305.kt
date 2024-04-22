package com.louisgeek.as_2023_1_1.cc

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
import android.util.Size
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kotlin.math.abs
import kotlin.math.max

class LineView0305 : View {
    companion object {
        private const val TAG = "LineView0305"
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
    private var TOUCH_LINE_START = 1
    private var TOUCH_LINE_END = 2
    private var TOUCH_LINE_CENTER = 3
    private var touchArea = TOUCH_NONE

    //
    private var touchRectLineStart = RectF()
    private var touchRectLineEnd = RectF()
    private var touchRectLineCenter = RectF()
    private var touchRectLineCenterList: MutableList<RectF> = mutableListOf()

    //line
    private lateinit var paintLine: Paint //画线
    private lateinit var paintLineDot: Paint //画线的两个端点
    private var lineDotStart = PointF() //线的起点坐标
    private var lineDotEnd = PointF() //线的终点坐标
    private var LINE_WIDTH = 6 //线的宽度
    private var LINE_DOT_WID = 24 //线上点的宽度


    //
    private var lineLengthMin = 100 //线的最小长度
    private var lineDirectLR_Reverse = false
    private var lineDirectTB_Reverse = false


    private var canOutWid = 0
    private var canOutHei = 0
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
        paintLine.color = Color.GRAY
        paintLine.strokeWidth = LINE_WIDTH.toFloat()

        paintLineDot = Paint()
        paintLineDot.isAntiAlias = true
        paintLineDot.style = Paint.Style.STROKE
        paintLineDot.color = Color.BLUE
        paintLineDot.strokeWidth = LINE_DOT_WID.toFloat()
        paintLineDot.strokeCap = Paint.Cap.ROUND


//        setPaddingRelative(20, 20, 20, 20)
        setLineRegionSize(Size(500, 300))

        setPaddingRelative(150, 150, 150, 150)
    }


    private fun refreshSize(wid: Float, hei: Float) {
        //设定可触摸区域
//        touchDisWid = 1.0F / 4 * wid
//        touchDisHei = 1.0F / 4 * hei
        touchDisWid = 60F
        touchDisHei = 60F
        canOutWid = LINE_DOT_WID / 2 + paddingStart
        canOutHei = LINE_DOT_WID / 2 + paddingTop

        Log.e(TAG, "refreshSize: wid=$wid hei=$hei")
        //设定线的启动和终点---以view为基准
        val viewPointLeftTop =
            PointF(0F + LINE_DOT_WID / 2 + paddingStart, 0F + LINE_DOT_WID / 2 + paddingTop)
        val viewPointTopRight =
            PointF(wid - LINE_DOT_WID / 2 - paddingEnd, 0F + LINE_DOT_WID / 2 + paddingTop)
        val viewPointRightBottom =
            PointF(wid - LINE_DOT_WID / 2 - paddingEnd, hei - LINE_DOT_WID / 2 - paddingBottom)
        val viewPointBottomLeft =
            PointF(0F + LINE_DOT_WID / 2 + paddingStart, hei - LINE_DOT_WID / 2 - paddingBottom)
        //
        val leftTop_To_RightBottom = !lineDirectLR_Reverse && !lineDirectTB_Reverse
        val topRight_To_BottomLeft = lineDirectLR_Reverse && !lineDirectTB_Reverse
        val rightBottom_To_LeftTop = lineDirectLR_Reverse && lineDirectTB_Reverse
        val bottomLeft_To_TopRight = !lineDirectLR_Reverse && lineDirectTB_Reverse
        if (leftTop_To_RightBottom) {
            lineDotStart.set(viewPointLeftTop)
            lineDotEnd.set(viewPointRightBottom)
        } else if (topRight_To_BottomLeft) {
            lineDotStart.set(viewPointTopRight)
            lineDotEnd.set(viewPointBottomLeft)
        } else if (rightBottom_To_LeftTop) {
            lineDotStart.set(viewPointRightBottom)
            lineDotEnd.set(viewPointLeftTop)
        } else if (bottomLeft_To_TopRight) {
            lineDotStart.set(viewPointBottomLeft)
            lineDotEnd.set(viewPointTopRight)
        }


        touchRectLineStart.set(
            lineDotStart.x - touchDisWid,
            lineDotStart.y - touchDisHei,
            lineDotStart.x + touchDisWid,
            lineDotStart.y + touchDisHei
        )

        touchRectLineEnd.set(
            lineDotEnd.x - touchDisWid,
            lineDotEnd.y - touchDisHei,
            lineDotEnd.x + touchDisWid,
            lineDotEnd.y + touchDisHei
        )

        touchRectLineCenter.set(
            lineDotStart.x + touchDisWid,
            lineDotStart.y - touchDisHei,
            lineDotEnd.x - touchDisWid,
            lineDotEnd.y + touchDisHei
        )
        Log.e(TAG, "refreshSize: touchRectLineStart=$touchRectLineStart")
        Log.e(TAG, "refreshSize: touchRectLineEnd=$touchRectLineEnd")
        Log.e(TAG, "refreshSize: touchRectLineCenter=$touchRectLineCenter")

        val touchDis = 100
        val touchDisHalf = touchDis / 2 //半径
        val canUseW = abs(lineDotEnd.x - lineDotStart.x) + 2 * touchDisHalf
        val canUseH = abs(lineDotEnd.y - lineDotStart.y) + 2 * touchDisHalf
        val canUseMaxBain = max(canUseW, canUseH)
//        val disForMaxBain = touchDis
        val disForMaxBain = touchDis / 2
//        val disForMaxBain = touchDis/3
//        val disForMaxBain = touchDis/4
        val canLayoutSize = (canUseMaxBain / disForMaxBain).toInt() //!!!
        val disWDiff = (canUseW - touchDis) / (canLayoutSize - 1)
        val disHDiff = (canUseH - touchDis) / (canLayoutSize - 1)
        //
        touchRectLineCenterList.clear()
        for (i in 0 until canLayoutSize) {
            var left = 0F
            var top = 0f
            if (leftTop_To_RightBottom) {
                left = lineDotStart.x - touchDisHalf + disWDiff * i
                top = lineDotStart.y - touchDisHalf + disHDiff * i
            } else if (topRight_To_BottomLeft) {
                left = lineDotEnd.x - touchDisHalf + disWDiff * i
                top = lineDotEnd.y - touchDisHalf - disHDiff * i
            } else if (rightBottom_To_LeftTop) {
                left = lineDotEnd.x - touchDisHalf + disWDiff * i
                top = lineDotEnd.y - touchDisHalf + disHDiff * i
            } else if (bottomLeft_To_TopRight) {
                left = lineDotStart.x - touchDisHalf + disWDiff * i
                top = lineDotStart.y - touchDisHalf - disHDiff * i
            }
            touchRectLineCenterList.add(RectF(left, top, left + touchDis, top + touchDis))
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //
        val wid =
            this.paddingStart + LINE_DOT_WID / 2 + lineRegionSize.width + LINE_DOT_WID / 2 + this.paddingEnd
        val hei =
            this.paddingTop + LINE_DOT_WID / 2 + lineRegionSize.height + LINE_DOT_WID / 2 + this.paddingBottom
        setMeasuredDimension(wid, hei)

        refreshSize(measuredWidth.toFloat(), measuredHeight.toFloat())
        //
//        lineLengthMin = (1.0F / 4 * min(measuredWidth, measuredHeight)).toInt()
        Log.e(TAG, "onMeasure: zfq ")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        refreshSize(w.toFloat(), h.toFloat())

        val xxx = Rect(this.left, this.top, this.right, this.bottom)
        Log.e(TAG, "onSizeChanged: zfq xxx=$xxx")
        val startEndPoints = getStartEndPoints()
        onMoveOrSizeChangeListener?.invoke(xxx, startEndPoints)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.YELLOW)

        paintTest.color = Color.CYAN
        canvas.drawRect(touchRectLineStart, paintTest)
        paintTest.color = Color.BLUE
        canvas.drawRect(touchRectLineEnd, paintTest)
        paintTest.color = Color.MAGENTA
//        canvas.drawRect(touchRectLineCenter, paintTest)
        Log.e(TAG, "onDraw: lineDotStart.y=${lineDotStart.y}")
        touchRectLineCenterList.forEach {
            canvas.drawRect(it, paintTest)
        }

//        val wid = abs(lineDotStart.x - lineDotEnd.x)
//        val hei = abs(lineDotStart.y - lineDotEnd.y)
//        val nameWid = 50
//        val nameHei = 50
//        val nameLeft = this.width / 2 - nameWid / 2
//        val nameTop = this.height / 2 - nameHei / 2
//        val nameRect = Rect(nameLeft, nameTop, nameLeft + nameWid, nameTop + nameHei)
//        canvas.drawRect(nameRect, paintTest)

        paintLineDot.color = Color.BLUE
        canvas.drawPoint(
            lineDotStart.x,
            lineDotStart.y,
            paintLineDot
        )
        paintLineDot.color = Color.GREEN
        canvas.drawPoint(
            lineDotEnd.x,
            lineDotEnd.y,
            paintLineDot
        )

        canvas.drawLine(
            lineDotStart.x,
            lineDotStart.y,
            lineDotEnd.x,
            lineDotEnd.y,
            paintLine
        )


//        paintTest.color = Color.BLACK
//        canvas.drawLine(
//            0F,
//            lineDotStart.y - LINE_DOT_WID / 2,
//            this.width.toFloat(),
//            lineDotStart.y - LINE_DOT_WID / 2,
//            paintTest
//        )
//        canvas.drawLine(
//            0F,
//            lineDotStart.y + LINE_DOT_WID / 2,
//            this.width.toFloat(),
//            lineDotStart.y + LINE_DOT_WID / 2,
//            paintTest
//        )
//        paintTest.color = Color.CYAN
//        canvas.drawLine(
//            0F,
//            lineDotEnd.y - LINE_DOT_WID / 2,
//            this.width.toFloat(),
//            lineDotEnd.y - LINE_DOT_WID / 2,
//            paintTest
//        )
//        canvas.drawLine(
//            0F,
//            lineDotEnd.y + LINE_DOT_WID / 2,
//            this.width.toFloat(),
//            lineDotEnd.y + LINE_DOT_WID / 2,
//            paintTest
//        )
    }

    private var downPoint = Point()
    private var lastPoint = Point()

    private var originViewRect = Rect()
    private var newViewRect = Rect()

    private var downLineDirectLR_Reverse = false
    private var downLineDirectTB_Reverse = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downPoint.set(event.rawX.toInt(), event.rawY.toInt())
                lastPoint.set(event.rawX.toInt(), event.rawY.toInt())
                //
                originViewRect.set(this.left, this.top, this.right, this.bottom)
                Log.e(TAG, "onTouchEvent: ori=$originViewRect downPoint=$downPoint")
                //init
                newViewRect.set(this.left, this.top, this.right, this.bottom)
                //
                downLineDirectLR_Reverse = lineDirectLR_Reverse
                downLineDirectTB_Reverse = lineDirectTB_Reverse

                dealDown(event)
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val disX = event.rawX.toInt() - downPoint.x
                val disY = event.rawY.toInt() - downPoint.y
                val diffX = event.rawX.toInt() - lastPoint.x
                val diffY = event.rawY.toInt() - lastPoint.y
                if (abs(disX) > 10 || abs(disY) > 10) {
                    val leftTop_To_RightBottom =
                        !downLineDirectLR_Reverse && !downLineDirectTB_Reverse
                    val topRight_To_BottomLeft =
                        downLineDirectLR_Reverse && !downLineDirectTB_Reverse
                    val rightBottom_To_LeftTop =
                        downLineDirectLR_Reverse && downLineDirectTB_Reverse
                    val bottomLeft_To_TopRight =
                        !downLineDirectLR_Reverse && downLineDirectTB_Reverse
                    when {
                        leftTop_To_RightBottom -> {
                            if (touchArea == TOUCH_LINE_START) {
                                changeLeft(disX)
                                changeTop(disY)
                            } else if (touchArea == TOUCH_LINE_END) {
                                changeRight(disX)
                                changeBottom(disY)
                            }
                        }

                        topRight_To_BottomLeft -> {
                            if (touchArea == TOUCH_LINE_START) {
                                changeTop(disY)
                                changeRight(disX)
                            } else if (touchArea == TOUCH_LINE_END) {
                                changeBottom(disY)
                                changeLeft(disX)
                            }
                        }

                        rightBottom_To_LeftTop -> {
                            if (touchArea == TOUCH_LINE_START) {
                                changeRight(disX)
                                changeBottom(disY)
                            } else if (touchArea == TOUCH_LINE_END) {
                                changeLeft(disX)
                                changeTop(disY)
                            }
                        }

                        bottomLeft_To_TopRight -> {
                            if (touchArea == TOUCH_LINE_START) {
                                changeBottom(disY)
                                changeLeft(disX)
                            } else if (touchArea == TOUCH_LINE_END) {
                                changeTop(disY)
                                changeRight(disX)
                            }
                        }
                    }
                    //
                    if (touchArea == TOUCH_LINE_CENTER) {
                        newViewRect.left = originViewRect.left + disX
                        newViewRect.top = originViewRect.top + disY
                        newViewRect.right = originViewRect.right + disX
                        newViewRect.bottom = originViewRect.bottom + disY
                    }
                    val mLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
                    Log.e(
                        TAG,
                        "onTouchEvent: topMargin=${mLayoutParams.topMargin} marginStart=${mLayoutParams.marginStart}"
                    )
                    //
                    val parentView = this.parent as View
                    if (newViewRect.left < parentView.left - canOutWid) {
                        newViewRect.left = parentView.left - canOutWid
                        newViewRect.right = this.width - canOutWid
                    }
                    if (newViewRect.top < parentView.top - canOutHei) {
                        newViewRect.top = parentView.top - canOutHei
                        newViewRect.bottom = this.height - canOutHei
                    }
                    if (newViewRect.right > parentView.right + canOutWid) {
                        newViewRect.right = parentView.right + canOutWid
                        newViewRect.left = parentView.right + canOutWid - this.width
                    }
                    if (newViewRect.bottom > parentView.bottom + canOutHei) {
                        newViewRect.bottom = parentView.bottom + canOutHei
                        newViewRect.top = parentView.bottom + canOutHei - this.height
                    }
                    //
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
                downLineDirectLR_Reverse = false
                downLineDirectTB_Reverse = false
                touchArea = TOUCH_NONE
            }
        }

        return super.onTouchEvent(event)
    }

    private fun changeLeft(disX: Int) {
        Log.e(TAG, "changeLeft: disX=$disX")
        newViewRect.left = originViewRect.left + disX
        //
        val paddingHor = paddingStart + paddingEnd
        if (newViewRect.left > originViewRect.right - LINE_DOT_WID - paddingHor) {
            newViewRect.left = originViewRect.right - LINE_DOT_WID - paddingHor
            newViewRect.right = originViewRect.left + disX + LINE_DOT_WID + paddingHor
            lineDirectLR_Reverse = !downLineDirectLR_Reverse //!按下
        }
        if (lineDirectLR_Reverse != downLineDirectLR_Reverse) {
            if (newViewRect.left < originViewRect.right - LINE_DOT_WID - paddingHor) {
                lineDirectLR_Reverse = downLineDirectLR_Reverse //按下
            }
        }
//        if (newViewRect.left < originViewRect.right - lineWidth) { //？？？
//            newViewRect.left = originViewRect.right - lineWidth
//            newViewRect.right = originViewRect.left + disX + lineWidth
//            lineDirectLR_RRRR = false
//        }
    }

    private fun changeTop(disY: Int) {
        newViewRect.top = originViewRect.top + disY
        //
        val paddingVer = paddingTop + paddingBottom
        if (newViewRect.top > originViewRect.bottom - LINE_DOT_WID - paddingVer) {
            //上边到了下边的下面--交换两边
            newViewRect.top = originViewRect.bottom - LINE_DOT_WID - paddingVer
            newViewRect.bottom = originViewRect.top + disY + LINE_DOT_WID + paddingVer
            lineDirectTB_Reverse = !downLineDirectTB_Reverse //!按下
        }
        if (lineDirectTB_Reverse != downLineDirectTB_Reverse) {
            if (newViewRect.top < originViewRect.bottom - LINE_DOT_WID - paddingVer) {
                lineDirectTB_Reverse = downLineDirectTB_Reverse //按下
            }
        }

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
        val paddingHor = paddingStart + paddingEnd
        if (newViewRect.right < originViewRect.left + LINE_DOT_WID + paddingHor) {
            newViewRect.right = originViewRect.left + LINE_DOT_WID + paddingHor
            newViewRect.left = originViewRect.right + disX - LINE_DOT_WID - paddingHor
            lineDirectLR_Reverse = !downLineDirectLR_Reverse //!按下
        }
        if (lineDirectLR_Reverse != downLineDirectLR_Reverse) {
            if (newViewRect.right > originViewRect.left + LINE_DOT_WID + paddingHor) {
                lineDirectLR_Reverse = downLineDirectLR_Reverse //按下
            }
        }
//        if (newViewRect.right > originViewRect.left + lineWidth) { //？？？
//            newViewRect.right = originViewRect.left + lineWidth
//            newViewRect.left = originViewRect.right + disX - lineWidth
//        }
    }

    private fun changeBottom(disY: Int) {
        Log.e(TAG, "changeBottom: disY=$disY")
        newViewRect.bottom = originViewRect.bottom + disY
        //
        val paddingVer = paddingTop + paddingBottom
        if (newViewRect.bottom < originViewRect.top + LINE_DOT_WID + paddingVer) {
            newViewRect.bottom = originViewRect.top + LINE_DOT_WID + paddingVer
            newViewRect.top = originViewRect.bottom + disY - LINE_DOT_WID - paddingVer
            lineDirectTB_Reverse = !downLineDirectTB_Reverse //!按下
        }
        if (lineDirectTB_Reverse != downLineDirectTB_Reverse) {
            if (newViewRect.bottom > originViewRect.top + LINE_DOT_WID + paddingVer) {
                lineDirectTB_Reverse = downLineDirectTB_Reverse //按下
            }
        }
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
//        touchRectLeftTop.set(
//            0 - touchDisWid,
//            0 - touchDisHei,
//            0 + touchDisWid,
//            0 + touchDisHei
//        )
//        touchRectLeftTop.outset(paddingStart, paddingTop)
//
//        touchRectTopRight.set(
//            wid - touchDisWid,
//            0 - touchDisHei,
//            wid + touchDisWid,
//            0 + touchDisHei
//        )
//        touchRectTopRight.outset(paddingTop, paddingEnd)
//
//        touchRectRightBottom.set(
//            wid - touchDisWid,
//            hei - touchDisHei,
//            wid + touchDisWid,
//            hei + touchDisHei
//        )
//        touchRectBottomLeft.set(
//            0 - touchDisWid,
//            hei - touchDisHei,
//            0 + touchDisWid,
//            hei + touchDisHei
//        )
//        touchRectCenter.set(
//            0 + touchDisWid,
//            0 + touchDisHei,
//            wid - touchDisWid,
//            hei - touchDisHei
//        )

//        touchRectLeftTop.set(
//            0 - touchDisWid,
//            0 - touchDisHei,
//            0 + touchDisWid,
//            0 + touchDisHei
//        )
//        touchRectTopRight.set(
//            wid - touchDisWid,
//            0 - touchDisHei,
//            wid + touchDisWid,
//            0 + touchDisHei
//        )
//        touchRectRightBottom.set(
//            wid - touchDisWid,
//            hei - touchDisHei,
//            wid + touchDisWid,
//            hei + touchDisHei
//        )
//        touchRectBottomLeft.set(
//            0 - touchDisWid,
//            hei - touchDisHei,
//            0 + touchDisWid,
//            hei + touchDisHei
//        )
//        touchRectCenter.set(
//            0 + touchDisWid,
//            0 + touchDisHei,
//            wid - touchDisWid,
//            hei - touchDisHei
//        )

//        if (touchRectLineStart.contains(eventX, eventY)) {
//            touchArea = TOUCH_LINE_START
//            Log.e(TAG, "dealDown: TOUCH_LINE_START")
//        } else if (touchRectLineEnd.contains(eventX, eventY)) {
//            touchArea = TOUCH_LINE_END
//            Log.e(TAG, "dealDown: TOUCH_LINE_END")
////        } else if (touchRectLineCenter.contains(eventX, eventY)) {
//        } else { //!!!!!!!!!!!!
//            touchArea = TOUCH_LINE_CENTER
//            Log.e(TAG, "dealDown: TOUCH_LINE_CENTER")
//        }

        if (touchRectLineStart.contains(eventX, eventY)) {
            touchArea = TOUCH_LINE_START
            Log.e(TAG, "dealDown: TOUCH_LINE_START")
        } else if (touchRectLineEnd.contains(eventX, eventY)) {
            touchArea = TOUCH_LINE_END
            Log.e(TAG, "dealDown: TOUCH_LINE_END")
        } else if (checkXCenter(eventX, eventY)) {
            touchArea = TOUCH_LINE_CENTER
            Log.e(TAG, "dealDown: TOUCH_LINE_CENTER")
        }
    }

    private fun checkXCenter(eventX: Float, eventY: Float): Boolean {
        touchRectLineCenterList.forEach {
            if (it.contains(eventX, eventY)) {
                return true
            }
        }
        return false
    }

    fun getLineDirectLR_RRRR(): Boolean {
        return lineDirectLR_Reverse
    }

    fun getLineRegionRect(): Rect {
        val xxx = Rect(this.left, this.top, this.right, this.bottom)
        return xxx
    }

    fun getStartEndPoints(): Pair<Point, Point> {
        val leftTop_To_RightBottom =
            !lineDirectLR_Reverse && !lineDirectTB_Reverse
        val topRight_To_BottomLeft =
            lineDirectLR_Reverse && !lineDirectTB_Reverse
        val rightBottom_To_LeftTop =
            lineDirectLR_Reverse && lineDirectTB_Reverse
        val bottomLeft_To_TopRight =
            !lineDirectLR_Reverse && lineDirectTB_Reverse
        val start = Point()
        val end = Point()
        if (leftTop_To_RightBottom) {
            start.set(this.left + LINE_DOT_WID / 2, this.top + LINE_DOT_WID / 2)
            end.set(this.right - LINE_DOT_WID / 2, this.bottom - LINE_DOT_WID / 2)
            Log.e(TAG, "getStartEndPoints: $left $top $right $bottom")
        } else if (topRight_To_BottomLeft) {
            start.set(this.right - LINE_DOT_WID / 2, this.top + LINE_DOT_WID / 2)
            end.set(this.left + LINE_DOT_WID / 2, this.bottom - LINE_DOT_WID / 2)
        } else if (rightBottom_To_LeftTop) {
            start.set(this.right - LINE_DOT_WID / 2, this.bottom - LINE_DOT_WID / 2)
            end.set(this.left + LINE_DOT_WID / 2, this.top + LINE_DOT_WID / 2)
        } else if (bottomLeft_To_TopRight) {
            start.set(this.left + LINE_DOT_WID / 2, this.bottom - LINE_DOT_WID / 2)
            end.set(this.right - LINE_DOT_WID / 2, this.top + LINE_DOT_WID / 2)
        }
        return Pair(start, end)
    }

    private var onMoveOrSizeChangeListener: ((lineRegionRect: Rect, Pair<Point, Point>) -> Unit)? =
        null

    fun setOnMoveOrSizeChangeListener(listener: ((lineRegionRect: Rect, Pair<Point, Point>) -> Unit)? = null) {
        onMoveOrSizeChangeListener = listener
    }

    fun getLineDotWid(): Int {
        return LINE_DOT_WID
    }

    private var lineRegionSize = Size(0, 0)
    fun setLineRegionSize(lineRegionSize: Size) {
        this.lineRegionSize = lineRegionSize
    }
}
