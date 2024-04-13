package com.louisgeek.as_2023_1_1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class PointLayout : FrameLayout {
    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView()
    }

    init {

    }

    //    private lateinit var bmpDot: Bitmap
    private fun initView() {
//        bmpDot = BitmapFactory.decodeResource(resources, R.drawable.test_point)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val parentLeft = left
        val parentTop = top
        val parentRight = right
        val parentBottom = bottom
        val parentWid = parentRight - parentLeft
        val parentHei = parentBottom - parentTop

        var childLeft = 0
        var childTop = 0
        for (i in 0 until this.childCount) {
            val childView = this.getChildAt(i)
            if (childView.visibility != View.GONE) {
                val mlp = childView.layoutParams as MarginLayoutParams
                val childWid = childView.measuredWidth
                val childHei = childView.measuredHeight

                childTop = parentTop + mlp.topMargin + (parentHei - childHei) / 2 - mlp.bottomMargin
                if (direct == direct_left) {
//                    childLeft =
//                        parentLeft + mlp.marginStart + (parentWid - childWid) / 2 - mlp.marginEnd
                } else if (direct == direct_right) {
                    childLeft = parentLeft + mlp.marginStart
                }
                childLeft += childWid
                childView.layout(childLeft, childTop, childLeft + childWid, childTop + childHei)
            }
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(Color.CYAN)
//        canvas.drawBitmap(bmpDot, 10F, 20F, paint)

    }


    private var direct_left = -1
    private var direct_right = 0

    private var direct = direct_right
    private fun setDirect(direct: Int) {
        this.direct = direct
    }

}