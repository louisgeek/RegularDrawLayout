package com.louisgeek.as_2023_1_1

import android.graphics.Rect
import android.graphics.RectF

fun Rect.outset(dx: Int, dy: Int) {
    this.left -= dx
    this.top -= dy
    this.right += dx
    this.bottom += dy
}

fun Rect.outset(dx: Float, dy: Float) {
    this.left -= dx.toInt()
    this.top -= dy.toInt()
    this.right += dx.toInt()
    this.bottom += dy.toInt()
}

fun RectF.outset(dx: Float, dy: Float) {
    this.left -= dx
    this.top -= dy
    this.right += dx
    this.bottom += dy
}

fun RectF.outset(dx: Int, dy: Int) {
    this.left -= dx.toFloat()
    this.top -= dy.toFloat()
    this.right += dx.toFloat()
    this.bottom += dy.toFloat()
}