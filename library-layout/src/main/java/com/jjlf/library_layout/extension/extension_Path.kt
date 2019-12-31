package com.jjlf.library_layout.extension

import android.graphics.Matrix
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.RectF
import androidx.annotation.FloatRange
import java.lang.IllegalArgumentException

fun Path.scale(  @FloatRange(from = 0.0, to = 1000.0) scaleX: Float,
                 @FloatRange(from = 0.0, to = 1000.0) scaleY: Float){
    val ma = Matrix()
    val rect = RectF()
    computeBounds(rect,true)
    ma.postScale(scaleX,scaleY,rect.centerX(),rect.centerY())
    if(scaleX >= 0f && scaleY >= 0f) transform(ma)
}

fun Path.flipMirror(vertical: Boolean,
                    horizontal: Boolean){
    val ma = Matrix()
    val rect = RectF()
    computeBounds(rect,true)
    when {
        vertical && horizontal -> ma.postScale(-1f, -1f, rect.centerX(), rect.centerY())
        vertical -> ma.postScale (1f,-1f, rect.centerX(), rect.centerY())
        horizontal -> ma.postScale(-1f, 1f, rect.centerX(), rect.centerY())
    }
    if(vertical || horizontal) transform(ma)
}

fun Path.moveToBounds(rect: RectF, @FloatRange(from = 0.0, to = 1.0) percentX: Float, @FloatRange(from = 0.0, to = 1.0) percentY:Float){
    val pointX = rect.left + (rect.right - rect.left) * percentX
    val pointY = rect.top + (rect.bottom - rect.top) * percentY
    moveTo(pointX,pointY)
}

fun Path.lineToBounds(rect: RectF, @FloatRange(from = 0.0, to = 1.0) percentX: Float,@FloatRange(from = 0.0, to = 1.0) percentY:Float){
    val pointX = rect.left + (rect.right - rect.left) * percentX
    val pointY = rect.top + (rect.bottom - rect.top) * percentY
    lineTo(pointX,pointY)
}



//Percent relative to rect startX-Y and endX-Y
fun Path.curve(startX:Float, startY:Float, cPercentX: Float,
               cPercentY: Float, endX: Float, endY:Float){
    val eX = (endX - startX)
    val eY = (endY - startY)
    moveTo(startX,startY)
    val radiusX = eX * cPercentX
    val radiusY = eY * cPercentY
    quadTo( startX + radiusX,startY + radiusY,endX,endY)

}


fun Path.curveDiagonal(startX:Float, startY:Float, radius: Float, endX: Float, endY:Float){
    val centerX = startX + (endX - startX) / 2f
    val centerY = startY +(endY - startY) / 2f
    moveTo(startX,startY)
    if(startX < endX && startY > endY)       quadTo(centerX - radius, centerY - radius,endX,endY)
    else if(startX < endX && startY < endY)  quadTo(centerX + radius, centerY - radius,endX,endY)
    else if(startX > endX && startY > endY)   quadTo(centerX + radius, centerY - radius,endX,endY)
    else if(startX > endX && startY < endY)    quadTo(centerX - radius, centerY - radius,endX,endY)
    else {
        val help = if(startX == endX) "X points are equals" else "Y points are equals"
        throw IllegalArgumentException("Path Must be Diagonal error: $help ")
    }
}

fun Path.curveHorizontal(startX:Float, startY:Float, radius: Float, endX: Float){
    val centerX = startX +  (endX - startX) / 2f
    moveTo(startX,startY)
    quadTo(centerX,radius,endX,startY)

}


fun Path.curveVertical(startX:Float, startY:Float, radius: Float, endY:Float){
    val centerY = startY + (endY - startY) / 2f
    moveTo(startX,startY)
    quadTo(radius, centerY,startX,endY)
}





//Percent relative to rect startX-Y and endX-Y
fun Path.rCurve(cPercentX: Float, cPercentY: Float, endX: Float, endY:Float){
    val pm =  PathMeasure(this,false)
    val point = floatArrayOf(0f,0f)
    pm.getPosTan(pm.length,point,null)
    val startX = point[0]
    val startY = point[1]
    val eX = (endX - startX)
    val eY = (endY - startY)
    val radiusX = eX * cPercentX
    val radiusY = eY * cPercentY

    rQuadTo( radiusX,radiusY,eX,eY)

}

fun Path.rCurveDiagonal(radius: Float, endX: Float, endY:Float){

    val pm =  PathMeasure(this,false)
    val point = floatArrayOf(0f,0f)
    pm.getPosTan(pm.length,point,null)
    val startX = point[0]
    val startY = point[1]
    val eX = (endX - startX)
    val eY = (endY - startY)
    val centerX = eX / 2f
    val centerY = eY / 2f
    if(startX < endX && startY > endY)   rQuadTo(centerX - radius, centerY - radius,eX,eY)
    else if(startX < endX && startY < endY)  rQuadTo(centerX + radius, centerY - radius,eX,eY)
    else if(startX > endX && startY > endY)    rQuadTo(centerX + radius, centerY - radius,eX,eY)
    else if(startX > endX && startY < endY)     rQuadTo(centerX - radius, centerY - radius,eX,eY)
    else {
        val help = if(startX == endX) "X points are equals" else "Y points are equals"
        throw IllegalArgumentException("Path Must be Diagonal error: $help ")
    }

}


fun Path.rCurveHorizontal(radius: Float,endX: Float){
    val pm =  PathMeasure(this,false)
    val point = floatArrayOf(0f,0f)
    pm.getPosTan(pm.length,point,null)
    val startX = point[0]
    val startY = point[1]
    val eX = (endX - startX)
    val centerX = eX / 2f
    rQuadTo(centerX, radius,eX,startY)
}



fun Path.rCurveVertical(radius: Float, endY:Float){
    val pm =  PathMeasure(this,false)
    val point = floatArrayOf(0f,0f)
    pm.getPosTan(pm.length,point,null)
    val startX = point[0]
    val startY = point[1]
    val eY = (endY - startY)
    val centerY = eY / 2f
    rQuadTo(radius, centerY,startX,eY)
}




//Percent relative to View bounds
fun Path.rLineToBounds(rect: RectF, @FloatRange(from = 0.0, to = 1.0) ePercentX: Float, @FloatRange(from = 0.0, to = 1.0) ePercentY:Float){
    val pm =  PathMeasure(this,false)
    val point = floatArrayOf(0f,0f)
    pm.getPosTan(pm.length,point,null)
    val startX = point[0]
    val startY = point[1]
    val endX = rect.left + (rect.right - rect.left) * ePercentX
    val endY = rect.top + (rect.bottom - rect.top) * ePercentY
    val dx = endX - startX
    val dy = endY - startY
    rLineTo(dx,dy)
}

fun Path.rCurveDiagonalToBounds(rect: RectF,radius: Float,  @FloatRange(from = 0.0, to = 1.0) ePercentX: Float, @FloatRange(from = 0.0, to = 1.0) ePercentY:Float){

    val pm =  PathMeasure(this,false)
    val point = floatArrayOf(0f,0f)
    pm.getPosTan(pm.length,point,null)
    val startX = point[0]
    val startY = point[1]
    val endX = rect.left + (rect.right - rect.left) * ePercentX
    val endY = rect.top + (rect.bottom - rect.top) * ePercentY
    val eX = (endX - startX)
    val eY = (endY - startY)
    val centerX = eX / 2f
    val centerY = eY / 2f
    if(startX < endX && startY > endY)   rQuadTo(centerX - radius, centerY - radius,eX,eY)
    else if(startX < endX && startY < endY)  rQuadTo(centerX + radius, centerY - radius,eX,eY)
    else if(startX > endX && startY > endY)    rQuadTo(centerX + radius, centerY - radius,eX,eY)
    else if(startX > endX && startY < endY)     rQuadTo(centerX - radius, centerY - radius,eX,eY)
    else {
        val help = if(startX == endX) "X points are equals" else "Y points are equals"
        throw IllegalArgumentException("Path Must be Diagonal error: $help ")
    }

}


fun Path.rCurveHorizontalToBounds(rect: RectF,radius: Float,  @FloatRange(from = 0.0, to = 1.0) ePercentX: Float){
    val pm =  PathMeasure(this,false)
    val point = floatArrayOf(0f,0f)
    pm.getPosTan(pm.length,point,null)
    val startX = point[0]
    val endX = rect.left + (rect.right - rect.left) * ePercentX
    val eX = (endX - startX)
    val centerX = eX / 2f
    rQuadTo(centerX, radius,eX,0f)
}


fun Path.rCurveVerticalToBounds(rect: RectF,radius: Float, @FloatRange(from = 0.0, to = 1.0) ePercentY:Float){
    val pm =  PathMeasure(this,false)
    val point = floatArrayOf(0f,0f)
    pm.getPosTan(pm.length,point,null)
    val startY = point[1]
    val endY = rect.top + (rect.bottom - rect.top) * ePercentY
    val eY = (endY - startY)
    val centerY = eY / 2f
    rQuadTo(radius, centerY,0f,eY)
}

