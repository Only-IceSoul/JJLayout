package com.jjlf.library_layout.extension

import android.graphics.RectF
import com.jjlf.library_layout.JJPadding


fun RectF.padding(padding: JJPadding){
    left += padding.left.toFloat()
    right -= padding.right.toFloat()
    top += padding.top.toFloat()
    bottom -= padding.bottom.toFloat()
}