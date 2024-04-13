package com.louisgeek.as_2023_1_1.cc

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.louisgeek.as_2023_1_1.BoxView0301
import com.louisgeek.as_2023_1_1.LineView0303
import com.louisgeek.as_2023_1_1.R
import kotlin.math.min

class GZLayout : FrameLayout {
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

    private fun initView() {


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val parentLeft = left
        val parentTop = top
        val parentRight = right
        val parentBottom = bottom
        val parentWid = parentRight - parentLeft
        val parentHei = parentBottom - parentTop

        var childLeft: Int
        var childTop: Int
//        for (i in 0 until this.childCount) {
//            val childView = this.getChildAt(i)
//            if (childView.visibility != View.GONE) {
//                val mlp = childView.layoutParams as MarginLayoutParams
//                val childWid = childView.measuredWidth
//                val childHei = childView.measuredHeight
//
//                childLeft =
//                    parentLeft + mlp.marginStart + (parentWid - childWid) / 2 - mlp.marginEnd
//                if (direct == direct_top) {
//                    childTop =
//                        parentTop + mlp.topMargin + (parentHei - childHei) / 2 - mlp.bottomMargin
//
//                } else if (direct == direct_bottom) {
//                    childTop =
//                        parentTop + mlp.topMargin + (parentHei - childHei) / 2 - mlp.bottomMargin
//                } else if (direct == direct_inside) {
//                    childTop = parentTop + mlp.topMargin
//                }
//                childView.layout(childLeft, childTop, childLeft + childWid, childTop + childHei)
//            }
//        }
    }


    private var direct_top = 0
    private var direct_bottom = 1
    private var direct_inside = 2

    private var direct = direct_top
    private fun setDirect(direct: Int) {
        this.direct = direct
    }


    private var lineViews: LinkedHashMap<Int, LineView0303> = linkedMapOf()
    private var boxViews: LinkedHashMap<Int, BoxView0301> = linkedMapOf()

    //0,0   200,200
    fun addLineView(lineDotStart: Point, lineDotEnd: Point, lineRegionSize: Size) {
        val minX = min(lineDotStart.x, lineDotEnd.x)
        val minY = min(lineDotStart.y, lineDotEnd.y)
        //
        val lineView = LineView0303(context)
        lineView.id = 1
        val mlp = MarginLayoutParams(lineRegionSize.width, lineRegionSize.height)
        mlp.marginStart = minX
        mlp.topMargin = minY
        this.addView(lineView, mlp)
        //
        lineViews[lineView.id] = lineView
        //
        val operateTextView = TextView(context)
        operateTextView.text = "编辑"
        operateTextView.setTextColor(Color.WHITE)
        operateTextView.setPaddingRelative(5, 5, 5, 5)
        operateTextView.setBackgroundResource(R.drawable.bg_txt)
        operateTextView.visibility = View.INVISIBLE
        operateTextView.setOnClickListener {
            Toast.makeText(context, "点击编辑", Toast.LENGTH_SHORT).show()
        }
        val otv_mlp = MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

//        otv_mlp.marginStart = lineDotStart.x + lineRegionSize.width / 2
//        otv_mlp.topMargin = lineDotStart.y -
        this.addView(operateTextView, otv_mlp)
        //
        lineView.setOnMoveOrSizeChangeListener { lineRegionRect, startEndPoints ->
            Log.e("TAG", "addLineView: lineRegionRect=$lineRegionRect")
            Log.e("TAG", "addLineView: startEndPoints=$startEndPoints")
            if (operateTextView.width == 0 && operateTextView.height == 0) {
                lineView.post {
                    layout_line_otv(operateTextView, lineView, lineRegionRect, startEndPoints)
                }
            } else {
                layout_line_otv(operateTextView, lineView, lineRegionRect, startEndPoints)
            }

        }
    }

    private fun layout_line_otv(
        operateTextView: TextView,
        lineView: View,
        lineRegionRect: Rect,
        startEndPoints: Pair<Point, Point>
    ) {
        val lineParentView = lineView.parent as View
        val parentLeft = lineParentView.left
        val parentTop = lineParentView.top
        val parentRight = lineParentView.right
        val parentBottom = lineParentView.bottom

        val lineRegionWid = lineRegionRect.width()
        val lineRegionHei = lineRegionRect.height()
//            val left = lineView.left //抖动
//            val top = lineView.top //抖动
        val lineRegionLeft = lineRegionRect.left
        val lineRegionTop = lineRegionRect.top
        val lineRegionRight = lineRegionRect.right
        val lineRegionBottom = lineRegionRect.bottom

        val startPoint = startEndPoints.first
        val endPoint = startEndPoints.second
        Log.e(
            "TAG",
            "layout_line_otv: operateTextView.height=${operateTextView.height} operateTextView.width=${operateTextView.width}" +
                    " parentLeft=$parentLeft parentTop=$parentTop  left=$lineRegionLeft top=$lineRegionTop" +
                    " parentRight=$parentRight parentBottom=$parentBottom  right=$lineRegionRight bottom=$lineRegionBottom"
        )
//            val mlp = operateTextView.layoutParams as MarginLayoutParams
//            mlp.marginStart = lineDotStart.x + lineRegionWid / 2
//            mlp.topMargin = lineDotStart.y + lineRegionHei
//            operateTextView.layout()
//            operateTextView.layoutParams = mlp
        val centerNameRectWid = 50
        val centerNameRectHei = 50
        var otvLeft = 0
        var otvTop = 0
        val startCh = startPoint.x >= operateTextView.left && startPoint.x <= operateTextView.right
        val endCh = endPoint.x >= operateTextView.left && endPoint.x <= operateTextView.right
        Log.e("TAG", "layout_line_otv: startCh=$startCh endCh=$endCh")
        if (startCh || endCh) {
            if (parentBottom - operateTextView.bottom > operateTextView.height) {
                //显示在下方
                otvLeft = lineRegionLeft + lineRegionWid / 2 - operateTextView.width / 2
                otvTop = lineRegionTop + lineRegionHei / 2 + centerNameRectHei / 2
            } else {
                //显示在上方
                otvLeft = lineRegionLeft + lineRegionWid / 2 - operateTextView.width / 2
                otvTop =
                    lineRegionTop + lineRegionHei / 2 - centerNameRectHei / 2 - operateTextView.height
            }
        } else {
            if (parentRight - operateTextView.right > operateTextView.width) {
                //显示在右方
                otvLeft = lineRegionLeft + lineRegionWid / 2 + centerNameRectWid / 2
                otvTop = lineRegionTop + lineRegionHei / 2 - operateTextView.height / 2
            } else {
                //显示在左方
                otvLeft =
                    lineRegionLeft + lineRegionWid / 2 - centerNameRectWid / 2 - operateTextView.width
                otvTop = lineRegionTop + lineRegionHei / 2 - operateTextView.height / 2
            }

        }

//        if (operateTextView.right - parentRight > operateTextView.width) {
//            val otvLeft = lineRegionLeft + lineRegionWid / 2 + centerNameRectWid / 2
//            val otvTop = lineRegionTop + lineRegionHei / 2 - operateTextView.height / 2
//            val otvRight = otvLeft + operateTextView.width
//            val otvBottom = otvTop + operateTextView.height
//            operateTextView.layout(otvLeft, otvTop, otvRight, otvBottom)
//            Log.e(
//                "“ATG”",
//                "layout_line_otv: right1 otvLeft=$otvLeft  otvTop=$otvTop lineRegionLeft=$lineRegionLeft "
//            )
//        } else if (operateTextView.right - parentRight > operateTextView.width) {
//            val otvLeft = lineRegionLeft + lineRegionWid / 2 + centerNameRectWid / 2
//            val otvTop = lineRegionTop + lineRegionHei / 2 - operateTextView.height / 2
//            val otvRight = otvLeft + operateTextView.width
//            val otvBottom = otvTop + operateTextView.height
//            operateTextView.layout(otvLeft, otvTop, otvRight, otvBottom)
//            Log.e(
//                "“ATG”",
//                "layout_line_otv: right1 otvLeft=$otvLeft  otvTop=$otvTop lineRegionLeft=$lineRegionLeft "
//            )
//        } else if (operateTextView.left - parentLeft > operateTextView.width) {
//            Log.e("“ATG”", "layout_line_otv: left1 operateTextView.width=${operateTextView.width}")
//            Log.e(
//                "“ATG”",
//                "layout_line_otv: left1 operateTextView.height=${operateTextView.height}"
//            )
//            val otvLeft =
//                lineRegionLeft + lineRegionWid / 2 - centerNameRectWid / 2 - operateTextView.width
//            val otvTop = lineRegionTop + lineRegionHei / 2 - operateTextView.height / 2
//            val otvRight = otvLeft + operateTextView.width
//            val otvBottom = otvTop + operateTextView.height
//            operateTextView.layout(otvLeft, otvTop, otvRight, otvBottom)
//        } else {
//            Log.e("“ATG”", "layout_line_otv: inside")
//            val otvLeft = lineRegionLeft + lineRegionWid / 4 - operateTextView.width / 2
//            val otvTop = lineRegionTop + 3 * operateTextView.height
//            val otvRight = otvLeft + operateTextView.width
//            val otvBottom = otvTop + operateTextView.height
//            operateTextView.layout(otvLeft, otvTop, otvRight, otvBottom)
//        }
        val otvRight = otvLeft + operateTextView.width
        val otvBottom = otvTop + operateTextView.height
        operateTextView.layout(otvLeft, otvTop, otvRight, otvBottom)
        operateTextView.visibility = View.VISIBLE
//            operateTextView.left = otvLeft
//            operateTextView.top = otvTop
//            operateTextView.right = otvRight
//            operateTextView.bottom = otvBottom
    }

    private fun getMLayoutParams(): MarginLayoutParams {
        val mlp = MarginLayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        mlp.marginStart = lineDotStart.x + lineViewSize.width / 2
//        mlp.topMargin = lineDotStart.y + lineViewSize.height
        return mlp
    }


    fun addBoxView(boxDotStart: Point, boxDotEnd: Point, boxRegionSize: Size) {
        val minX = min(boxDotStart.x, boxDotEnd.x)
        val minY = min(boxDotStart.y, boxDotEnd.y)
        //
        val boxView = BoxView0301(context)
        boxView.id = 2
        val mlp = MarginLayoutParams(boxRegionSize.width, boxRegionSize.height)
        mlp.marginStart = minX
        mlp.topMargin = minY
        this.addView(boxView, mlp)
        //
        boxViews[boxView.id] = boxView
        //
        val operateTextView = TextView(context)
        operateTextView.text = "编辑"
        operateTextView.setTextColor(Color.WHITE)
        operateTextView.setPaddingRelative(5, 5, 5, 5)
        operateTextView.setBackgroundResource(R.drawable.bg_txt)
        operateTextView.visibility = View.INVISIBLE
        operateTextView.setOnClickListener {
            Toast.makeText(context, "点击编辑", Toast.LENGTH_SHORT).show()
        }
        val otv_mlp = MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

//        otv_mlp.marginStart = lineDotStart.x + lineRegionSize.width / 2
//        otv_mlp.topMargin = lineDotStart.y -
        this.addView(operateTextView, otv_mlp)
        //
        boxView.setOnMoveOrSizeChangeListener { boxRegionRect ->
            Log.e("TAG", "addBoxView: boxRegionRect=$boxRegionRect")
            if (operateTextView.width == 0 && operateTextView.height == 0) {
                boxView.post {
                    layout_box_otv(operateTextView, boxView, boxRegionRect)
                }
            } else {
                layout_box_otv(operateTextView, boxView, boxRegionRect)
            }

        }
    }

    private fun layout_box_otv(operateTextView: TextView, boxView: View, boxRegionRect: Rect) {
        val lineParentView = boxView.parent as View
        val parentLeft = lineParentView.left
        val parentTop = lineParentView.top
        val parentRight = lineParentView.right
        val parentBottom = lineParentView.bottom

        val lineRegionWid = boxRegionRect.width()
        val lineRegionHei = boxRegionRect.height()
//            val left = lineView.left //抖动
//            val top = lineView.top //抖动
        val lineRegionLeft = boxRegionRect.left
        val lineRegionTop = boxRegionRect.top
        val lineRegionRight = boxRegionRect.right
        val lineRegionBottom = boxRegionRect.bottom
        Log.e(
            "TAG",
            "layout_box_otv: operateTextView.height=${operateTextView.height} operateTextView.width=${operateTextView.width}" +
                    " parentLeft=$parentLeft parentTop=$parentTop  left=$lineRegionLeft top=$lineRegionTop" +
                    " parentRight=$parentRight parentBottom=$parentBottom  right=$lineRegionRight bottom=$lineRegionBottom"
        )
//            val mlp = operateTextView.layoutParams as MarginLayoutParams
//            mlp.marginStart = lineDotStart.x + lineRegionWid / 2
//            mlp.topMargin = lineDotStart.y + lineRegionHei
//            operateTextView.layout()
//            operateTextView.layoutParams = mlp
        if (lineRegionTop - parentTop > operateTextView.height) {
            val otvLeft = lineRegionLeft + lineRegionWid / 2 - operateTextView.width / 2
            val otvTop = lineRegionTop - operateTextView.height
            val otvRight = otvLeft + operateTextView.width
            val otvBottom = otvTop + operateTextView.height
            operateTextView.layout(otvLeft, otvTop, otvRight, otvBottom)
            Log.e(
                "ATG",
                "layout_box_otv: otvLeft=$otvLeft  otvTop=$otvTop lineRegionLeft=$lineRegionLeft "
            )
        } else if (parentBottom - lineRegionBottom > operateTextView.height) {
            Log.e("ATG", "layout_box_otv: bo1 operateTextView.width=${operateTextView.width}")
            Log.e("ATG", "layout_box_otv: bo2 operateTextView.height=${operateTextView.height}")
            val otvLeft = lineRegionLeft + lineRegionWid / 2 - operateTextView.width / 2
            val otvTop = lineRegionTop + lineRegionHei
            val otvRight = otvLeft + operateTextView.width
            val otvBottom = otvTop + operateTextView.height
            operateTextView.layout(otvLeft, otvTop, otvRight, otvBottom)
        } else {
            Log.e("ATG", "layout_box_otv: inside")
            val otvLeft = lineRegionLeft + lineRegionWid / 4 - operateTextView.width / 2
            val otvTop = lineRegionTop + 3 * operateTextView.height
            val otvRight = otvLeft + operateTextView.width
            val otvBottom = otvTop + operateTextView.height
            operateTextView.layout(otvLeft, otvTop, otvRight, otvBottom)
        }
        //
        operateTextView.visibility = View.VISIBLE
//            operateTextView.left = otvLeft
//            operateTextView.top = otvTop
//            operateTextView.right = otvRight
//            operateTextView.bottom = otvBottom
    }

    fun getPoList() {
        lineViews.forEach { vId, lineView ->
            val startEndPoints = lineView.getStartEndPoints()
            Log.e("TAG", "startEndPoints: $startEndPoints")
        }
    }

    fun getBoxPointsList() {
        boxViews.forEach { vId, lineView ->
            val startEndPoints = lineView.getStartEndPoints()
            Log.e("TAG", "startEndPoints: $startEndPoints")
        }
    }
}