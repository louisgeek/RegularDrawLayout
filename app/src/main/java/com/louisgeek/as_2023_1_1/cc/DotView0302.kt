package com.louisgeek.as_2023_1_1.cc

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import com.louisgeek.as_2023_1_1.R
import kotlin.math.abs
import kotlin.math.max

class DotView0302 : View {
    companion object {
        private const val TAG = "DotView0302"

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
    private var lineWid = 120
    private var lineSize = 5

//    private var bmpDotPoint = PointF(0F, 0F)

    //    private var bmpDotLeft = 0F
//    private var bmpDotTop = 0F
    private fun initView() {
        paint = Paint()
        paint.isAntiAlias = true
//        paint.style = Paint.Style.STROKE
//        paint.color = Color.RED
//        paint.strokeWidth = 3F

        paintLine = Paint()
        paintLine.isAntiAlias = true
        paintLine.style = Paint.Style.STROKE
        paintLine.color = Color.GRAY
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
        val wid = this.paddingStart + bmpDot.width + lineWid + bmpDelete.width + this.paddingEnd
        val hei = this.paddingTop + max(bmpDot.width, bmpDelete.width) + this.paddingBottom
        setMeasuredDimension(wid, hei)

        //
        refreshSize(measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        refreshSize(w.toFloat(), h.toFloat())
    }

    private fun refreshSize(wid: Float, hei: Float) {
        canOutWid = bmpDot.width / 2
        canOutHei = (hei / 2).toInt()
//        if (direct == direct_left) {
//
//        }else if (direct == direct_right){
//
//        }
        val xxx = Rect(this.left, this.top, this.right, this.bottom)
        Log.e(TAG, "onSizeChanged: zfq xxx=$xxx")
        val yyy = getDotPoint()
        onMoveOrSizeChangeListener?.invoke(xxx, yyy, bmpDot.width)
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
                var disX = event.rawX.toInt() - downPoint.x
                var disY = event.rawY.toInt() - downPoint.y
                val diffX = event.rawX.toInt() - lastPoint.x
                val diffY = event.rawY.toInt() - lastPoint.y
                if (abs(disX) > 10 || abs(disY) > 10) {
                    newViewRect.left = originViewRect.left + disX
                    newViewRect.top = originViewRect.top + disY
                    newViewRect.right = originViewRect.right + disX
                    newViewRect.bottom = originViewRect.bottom + disY

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

                    if (directReverse) {
                        //线、叉在左侧
                        if (newViewRect.right < parentView.right - (this.width - bmpDot.width)) {
                            setDirectReverse(false)
                            //通过篡改 originViewRect 变相实现 fixme
                            originViewRect.left = originViewRect.left + (this.width - bmpDot.width)
                            originViewRect.right =
                                originViewRect.right + (this.width - bmpDot.width)
                            newViewRect.left = originViewRect.left + disX
                            newViewRect.right = newViewRect.left + this.width
//                            disX =  disX + (this.width - bmpDot.width)
//                            newViewRect.left = originViewRect.left + disX
//                            newViewRect.right = newViewRect.left + this.width
                        }
                    } else {
                        //线、叉在右侧
                        if (newViewRect.right > parentView.right) {
                            setDirectReverse(true)
                            //通过篡改 originViewRect 变相实现 fixme
                            originViewRect.left = originViewRect.left - (this.width - bmpDot.width)
                            originViewRect.right =
                                originViewRect.right - (this.width - bmpDot.width)
                            newViewRect.left = originViewRect.left + disX
                            newViewRect.right = newViewRect.left + this.width
//                            disX =  disX - (this.width - bmpDot.width)
//                            newViewRect.left = newViewRect.left -  (this.width - bmpDot.width)
//                            newViewRect.right = newViewRect.left + this.width
                        }

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
//                downLineDirectLR_Reverse = false
//                downLineDirectTB_Reverse = false
//                touchArea = TOUCH_NONE
            }
        }

        return super.onTouchEvent(event)
    }

    private fun dealDown(event: MotionEvent) {

    }


    private var canOutWid = 0
    private var canOutHei = 0
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        canvas.drawColor(Color.YELLOW)

        Log.e(TAG, "onDraw: left=$left top=$top right=$right bottom=$bottom")
        var bmpDotLeft = 0F
        var bmpDotTop = 0F
        if (directReverse) {
            //线、叉在左侧
            bmpDotLeft = (this.width - bmpDot.width).toFloat()
        } else {
            bmpDotLeft = 0F
        }
        bmpDotTop = (this.height / 2 - bmpDot.height / 2).toFloat()
        canvas.drawBitmap(bmpDot, bmpDotLeft, bmpDotTop, paint)
//
        var startX = 0F
        var startY = 0F
        var stopX = 0F
        var stopY = 0F
        var bmpDeleteLeft = 0F
        var bmpDeleteTop = 0F
        if (directReverse) {
            //线、叉在左侧
            startX = bmpDotLeft - lineWid
            startY = bmpDotTop + bmpDot.height / 2
            stopX = startX + lineWid
            stopY = startY

            bmpDeleteLeft = startX - bmpDelete.width
            bmpDeleteTop = startY - bmpDelete.height / 2
        } else {
            //线、叉在右侧
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


    private var directReverse = false
    fun setDirectReverse(directReverse: Boolean) {
        this.directReverse = directReverse
        invalidate()
    }

    fun getDirectReverse(): Boolean {
        return directReverse
    }

    fun getBmpDotWid(): Int {
        return bmpDot.width
    }

    fun getDotPoint(): Point {
        Log.e(TAG, "getDotPoint: marginStart=${this.marginStart} marginEnd=${this.marginEnd}")
        Log.e(TAG, "getDotPoint: left=${this.left} top=${this.top}")
        val bmpDotPoint = Point()
        if (directReverse) {
            //线、叉在左侧
            bmpDotPoint.x = this.right - bmpDot.width / 2
            bmpDotPoint.y = this.top + this.height / 2
        } else {
            //线、叉在右侧
            bmpDotPoint.x = this.left + bmpDot.width / 2
            bmpDotPoint.y = this.top + this.height / 2
        }
        return bmpDotPoint
    }

    private var onMoveOrSizeChangeListener: ((dotRegionRect: Rect, dotPoint: Point, bmpDotWid: Int) -> Unit)? =
        null

    fun setOnMoveOrSizeChangeListener(listener: ((dotRegionRect: Rect, dotPoint: Point, bmpDotWid: Int) -> Unit)? = null) {
        onMoveOrSizeChangeListener = listener
    }
}