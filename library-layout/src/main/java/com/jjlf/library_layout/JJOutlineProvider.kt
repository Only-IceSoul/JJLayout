package com.jjlf.library_layout

import android.graphics.Outline
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.FloatRange
import androidx.annotation.RequiresApi
import androidx.core.graphics.toRect
import com.jjlf.library_layout.extension.padding
import com.jjlf.library_layout.extension.scale
import kotlin.math.min

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class JJOutlineProvider : ViewOutlineProvider() {
    private var mRadius = floatArrayOf(0f,0f,0f,0f,0f,0f,0f,0f)
    private var mScaleX = -1f
    private var mScaleY = -1f
    private var mOffsetY = 0f
    private var mOffsetX = 0f
    private var mAlpha = 0.99f
    private var mPath = Path()
    private val mRect = RectF()
    private var mShape = 0
    private var mIsPathClosure = false
    private var mSetupPath: ((RectF,Path)-> Unit)? = null
    private var mIsNewPath = false
    private var mPadding = JJPadding()

    companion object{
        const val CIRCLE = 1
        const val ROUND_TO_MIN_SIZE = 2
        const val CORNER_SMOOTH_SAMLL = 3
        const val CORNER_SMOOTH_MEDIUM = 4
        const val CORNER_SMOOTH_LARGE = 5
        const val CORNER_SMOOTH_XLARGE = 6
    }

    fun ssScale(x: Float,y: Float):JJOutlineProvider{
        mScaleX = x
        mScaleY = y
        return this
    }

    fun ssShape(type: Int):JJOutlineProvider{
        mIsNewPath = false
        mIsPathClosure = false
        mShape = type
        return this
    }

    fun ssOffset(x: Float,y: Float):JJOutlineProvider{
        mOffsetX = x
        mOffsetY = y
        return this
    }


    fun ssAlpha(@FloatRange(from = 0.0, to = 1.0)alpha: Float): JJOutlineProvider{
        mAlpha = if(alpha == 1f) 0.99f else alpha
        return this
    }

    fun ssRadius(radius: Float): JJOutlineProvider{
        mRadius = floatArrayOf(radius,radius,radius,radius,radius,radius,radius,radius)
        mIsNewPath = false
        mIsPathClosure = false
        mShape = 0
        return this
    }

    fun ssRadius(radius: FloatArray) : JJOutlineProvider{
        mRadius = radius
        mIsNewPath = false
        mIsPathClosure = false
        mShape = 0
        return this
    }

    fun ssRadius(topLeft: Float,topRight:Float, bottomRight:Float,bottomLeft:Float) : JJOutlineProvider{
        mRadius = floatArrayOf(topLeft,topLeft,topRight,topRight,bottomRight,bottomRight,bottomLeft,bottomLeft)
        mIsNewPath = false
        mIsPathClosure = false
        mShape = 0
        return this
    }

    fun ssConvexPath(path:Path):JJOutlineProvider{
        mIsNewPath = true
        mIsPathClosure = false
        mShape = 0
        mPath = path
        return this
    }

    fun ssConvexPath(closure:(RectF,Path)->Unit):JJOutlineProvider{
        mIsNewPath = true
        mIsPathClosure = true
        mSetupPath = closure
        mShape = 0
        return this
    }

    fun diposePath():JJOutlineProvider{
        mIsNewPath = false
        mSetupPath = null
        mIsPathClosure = false
        mPath.reset()
        return this
    }


    fun ssPadding(padding: JJPadding): JJOutlineProvider {
        mPadding = padding
        return this
    }

    override fun getOutline(view: View?, outline: Outline?) {
        val h = view?.height ?: 0
        val w = view?.width ?: 0
        mRect.set(0f,0f,w.toFloat(),h.toFloat())
        outline?.alpha = mAlpha

        if(mRect.width() > 0f && mRect.height()> 0f) {

            setupRect()

            val isClipToOutline = view?.clipToOutline ?: false
            if(isClipToOutline){
                setupRadiusForShape()
                outline?.setRoundRect(mRect.toRect(),mRadius.first())
            }else {
                //just elevation

                if(mIsPathClosure  && mIsNewPath){
                    mPath.reset()
                    mSetupPath?.invoke(mRect, mPath)
                }
                if (!mIsNewPath){
                    mPath.reset()
                    setupRadiusForShape()
                    mPath.addRoundRect(mRect, mRadius, Path.Direction.CW)
                }

                outline?.setConvexPath(mPath)
            }
        }

    }


    private fun setupRect(){
        mRect.padding(mPadding)
        mRect.scale(mScaleX, mScaleY)
        mRect.offset(mOffsetX, mOffsetY)
    }

    private fun setupRadiusForShape(){
        when(mShape){
            1 -> {
                val radius = min(mRect.height(),mRect.width()) / 2f
                for (i in 0..7){
                    mRadius[i] = radius
                }
            }
            2 -> {
                val radius = min(mRect.height(),mRect.width())
                for (i in 0..7){
                    mRadius[i] = radius
                }
            }
            3 -> {
                val radius = min(mRect.height(),mRect.width()) * 0.05f
                for (i in 0..7){
                    mRadius[i] = radius
                }
            }
            4 -> {
                val radius = min(mRect.height(),mRect.width()) * 0.1f
                for (i in 0..7){
                    mRadius[i] = radius
                }
            }

            5 -> {
                val radius = min(mRect.height(),mRect.width()) * 0.15f
                for (i in 0..7){
                    mRadius[i] = radius
                }
            }

            6 -> {
                val radius = min(mRect.height(),mRect.width()) * 0.2f
                for (i in 0..7){
                    mRadius[i] = radius
                }
            }
            else -> Log.v("JJOutlineProvider","Custom Radius")
        }
    }
}

