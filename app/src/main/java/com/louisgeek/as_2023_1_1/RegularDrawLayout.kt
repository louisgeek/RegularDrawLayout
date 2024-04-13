package com.louisgeek.as_2023_1_1

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.ViewConfigurationCompat
import androidx.core.view.isVisible
import androidx.customview.widget.ViewDragHelper
import kotlin.math.max


class RegularDrawLayout : ViewGroup {

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

    private fun init() {


        viewDragHelper = ViewDragHelper.create(this, 1.0F, object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                //是否捕获当前 childView
                return true
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                //left 表示 x 轴坐标，相对于ViewGroup而言。dx表示偏移的距离 返回值确定view最终的x轴坐标
                //childView 需要水平移动的距离 默认返回0
                return left
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                //top 表示y轴坐标，相对于ViewGroup而言。dy表示偏移的距离 返回值确定view最终的y轴坐标
                //childView 需要垂直移动的距离 默认返回0
                return top
            }

            /*  override fun getViewHorizontalDragRange(child: View): Int {
                  return measuredWidth
              }*/

            /*override fun getViewVerticalDragRange(child: View): Int {
                return measuredHeight
            }*/
        })
    }


    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //所有子view加起来总的Measured Dimension高度和宽度
        var measuredWidth = 0
        var measuredHeight = 0

        for (i in 0 until this.childCount) {
            val childView = getChildAt(i)
            if (childView.isVisible) {
                //
//                measureChild(childView, widthMeasureSpec, heightMeasureSpec)
                measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0);

                measuredWidth += childView.measuredWidth
//                measuredHeight += childView.measuredHeight
                measuredHeight = max(measuredHeight, childView.measuredHeight)
            }
        }

        measuredWidth += paddingStart + paddingEnd
        measuredHeight += paddingTop + paddingBottom

        //
        measuredWidth = max(measuredWidth, suggestedMinimumWidth)
        measuredHeight = max(measuredHeight, suggestedMinimumHeight)

        //另外一种set度量值的方法
        //setMeasuredDimension(resolveSize(measuredWidth, widthMeasureSpec),resolveSize(measuredHeight, heightMeasureSpec))

        setMeasuredDimension(measuredWidth, measuredHeight)


    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        val parentLeft = l
        val parentRight = r

        for (i in 0 until this.childCount) {
            val childView = getChildAt(i)
            if (childView.isVisible) {
                //  获取margin
                val marginLayoutParams = childView.layoutParams as MarginLayoutParams
                var childLeft = l + marginLayoutParams.marginStart
                var childTop = t + marginLayoutParams.topMargin
                //开始摆放

                childView.layout(
                    childLeft,
                    childTop,
                    childLeft + childView.measuredWidth,
                    childTop + childView.measuredHeight
                )
                //把左边的锚定位置往右移
                childLeft += childLeft + childView.measuredWidth

                //本例简单演示的是水平摆放子view，所以此处不用累加高度
                //t += childHeight;
            }
        }


    }

    var viewDragHelper: ViewDragHelper? = null

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (ev == null) {
            return super.onInterceptTouchEvent(ev)
        }
        return viewDragHelper?.shouldInterceptTouchEvent(ev) ?: false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return super.onTouchEvent(event)
        }
        viewDragHelper?.processTouchEvent(event)
        return true
    }

    var viewSize = 0
    fun addBoxView(boxView: BoxView) {
        val marginLayoutParams =
            MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        boxView.x = 100F * viewSize
        boxView.y = 100F * viewSize
        this.addView(boxView, marginLayoutParams)
        viewSize++
    }
}