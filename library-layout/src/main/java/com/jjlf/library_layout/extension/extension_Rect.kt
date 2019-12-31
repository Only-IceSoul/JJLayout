package com.jjlf.library_layout.extension

import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import androidx.annotation.FloatRange
import androidx.core.graphics.transform
import com.jjlf.library_layout.JJPadding

fun Rect.scale(
    @FloatRange(from = 0.0, to = 1000.0) scaleX: Float,
    @FloatRange(from = 0.0, to = 1000.0) scaleY: Float
) {
    val newWidth = width() * scaleX
    val newHeight = height() * scaleY
    val deltaX = (width() - newWidth) / 2
    val deltaY = (height() - newHeight) / 2

    if(scaleX >= 0f && scaleY >= 0f)  set((left + deltaX).toInt(), (top + deltaY).toInt(), (right - deltaX).toInt(), (bottom - deltaY).toInt())
}

fun Rect.padding(padding: JJPadding){
    left += padding.left
    right -= padding.right
    top += padding.top
    bottom -= padding.bottom
}

fun Rect.flipMirror(vertical:Boolean,horizontal:Boolean){
    when {
        vertical && horizontal -> set(right, bottom, left, top)
        vertical -> set(left, bottom, right, top)
        horizontal -> set(right, top, left, bottom)
    }
}

fun RectF.scale(
    @FloatRange(from = 0.0, to = 1000.0) scaleX: Float,
    @FloatRange(from = 0.0, to = 1000.0) scaleY: Float
) {
    val ma = Matrix()
    ma.postScale(scaleX,scaleY,centerX(),centerY())
    if(scaleX >= 0f && scaleY >= 0f) transform(ma)
}

fun RectF.padding(padding: JJPadding){
    left += padding.left.toFloat()
    right -= padding.right.toFloat()
    top += padding.top.toFloat()
    bottom -= padding.bottom.toFloat()
}


fun RectF.flipMirror(vertical:Boolean,horizontal:Boolean){
    val ma = Matrix()
    when {
        vertical && horizontal -> ma.postScale(-1f, -1f, centerX(), centerY())
        vertical -> ma.postScale(1f, -1f, centerX(), centerY())
        horizontal -> ma.postScale(-1f, 1f, centerX(), centerY())
    }
    if(vertical || horizontal) transform(ma)
}

fun Rect.percentHeight(@FloatRange(from = 0.0, to = 1.0) percent: Float):Float{
    return height() * percent
}

fun Rect.percentWidth(@FloatRange(from = 0.0, to = 1.0) percent: Float):Float{
    return width() * percent
}

fun RectF.percentHeight(@FloatRange(from = 0.0, to = 1.0) percent: Float):Float{
     return height() * percent
}

fun RectF.percentWidth(@FloatRange(from = 0.0, to = 1.0) percent: Float):Float{
    return width() * percent
}