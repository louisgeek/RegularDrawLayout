package com.louisgeek.as_2023_1_1

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class DotView0301 : View {
    companion object {
        private const val TAG = "DotView0301"

        var direct_left = -1
        var direct_right = 0
    }

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView()
    }

    private lateinit var paint: Paint
    private lateinit var paintLine: Paint

    private lateinit var bmpDot: Bitmap
    private lateinit var bmpDelete: Bitmap

    //    private  var bmpDotRect = RectF()
    private var lineWid = 150
    private var lineSize = 10

    private var bmpDotPoint = PointF(0F, 0F)

    private var bmpDotLeft = 0F
    private var bmpDotTop = 0F
    private fun initView() {
        paint = Paint()
        paint.isAntiAlias = true
//        paint.style = Paint.Style.STROKE
//        paint.color = Color.RED
//        paint.strokeWidth = 3F

        paintLine = Paint()
        paintLine.isAntiAlias = true
        paintLine.style = Paint.Style.STROKE
        paintLine.color = Color.RED
        paintLine.strokeWidth = lineSize.toFloat()
//        rectF = RectF()
//        rectF.set(110F, 120F, 300F, 400F)

        bmpDot = BitmapFactory.decodeResource(resources, R.drawable.test_point)
        bmpDelete = BitmapFactory.decodeResource(resources, R.drawable.test_delete)


//        bmpDotLeft = bmpDotPoint.x - bmpDot.width / 2
//        bmpDotTop = bmpDotPoint.y - bmpDot.height / 2
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

//        Log.e(TAG, "onMeasure: ${bitmap.width},${bitmap.height}")
//        val wid = this.paddingStart + bmpDot.width + lineWid + bmpDelete.width + this.paddingEnd
//        val hei = this.paddingTop + max(bmpDot.width, bmpDelete.width) + this.paddingBottom
//        setMeasuredDimension(wid, hei)

        //
        refreshSize(measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        refreshSize(w.toFloat(), h.toFloat())
    }

    private fun refreshSize(wid: Float, hei: Float) {
        canOutWid = (hei / 2).toInt()
        canOutHei = bmpDot.width / 2
//        if (direct == direct_left) {
//
//        }else if (direct == direct_right){
//
//        }
        val xxx = Rect(this.left, this.top, this.right, this.bottom)
        Log.e(TAG, "onSizeChanged: zfq xxx=$xxx")
        onMoveOrSizeChangeListener?.invoke(xxx)
    }


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
                    newViewRect.left = originViewRect.left + disX
                    newViewRect.top = originViewRect.top + disY
                    newViewRect.right = originViewRect.right + disX
                    newViewRect.bottom = originViewRect.bottom + disY
                    val parentView = this.parent as View
                    if (newViewRect.left < 0 - canOutWid) {
                        newViewRect.left = 0 - canOutWid
                        newViewRect.right = this.width - canOutWid
                    }
                    if (newViewRect.top < 0 - canOutHei) {
                        newViewRect.top = 0 - canOutHei
                        newViewRect.bottom = this.height - canOutHei
                    }
                    if (newViewRect.right > parentView.width + canOutWid) {
                        newViewRect.right = parentView.width + canOutWid
                        newViewRect.left = parentView.width - this.width + canOutWid
                    }
                    if (newViewRect.bottom > parentView.height + canOutHei) {
                        newViewRect.bottom = parentView.height + canOutHei
                        newViewRect.top = parentView.height - this.height + canOutHei
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
//                touchArea = TOUCH_NONE
            }
        }

        return super.onTouchEvent(event)
    }

    private fun dealDown(event: MotionEvent) {

    }

    var canOutWid = 0
    var canOutHei = 0
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.YELLOW)


        canvas.drawBitmap(bmpDot, bmpDotLeft, bmpDotTop, paint)
//
        var startX = 0F
        var startY = 0F
        var stopX = 0F
        var stopY = 0F
        var bmpDeleteLeft = 0F
        var bmpDeleteTop = 0F
        if (direct == direct_left) {
            startX = bmpDotLeft - lineWid
            startY = bmpDotTop + bmpDot.height / 2
            stopX = startX + lineWid
            stopY = startY

            bmpDeleteLeft = startX - bmpDelete.width
            bmpDeleteTop = startY - bmpDelete.height / 2
        } else if (direct == direct_right) {
            startX = bmpDotLeft + bmpDot.width
            startY = bmpDotTop + bmpDot.height / 2
            stopX = startX + lineWid
            stopY = startY

            bmpDeleteLeft = stopX
            bmpDeleteTop = startY - bmpDelete.height / 2
        }
        canvas.drawLine(startX, startY, stopX, stopY, paintLine)
        canvas.drawBitmap(bmpDelete, bmpDeleteLeft, bmpDeleteTop, paint)
    }


    private var direct = direct_right
    fun setDirect(direct: Int) {
        this.direct = direct
        invalidate()
    }

    fun getDotPoint(): Point {
        val dotPoint = Point()
        dotPoint.set(this.left, this.top)
        return dotPoint
    }

    private var onMoveOrSizeChangeListener: ((boxRegionRect: Rect) -> Unit)? = null
    fun setOnMoveOrSizeChangeListener(listener: ((boxRegionRect: Rect) -> Unit)? = null) {
        onMoveOrSizeChangeListener = listener
    }
}