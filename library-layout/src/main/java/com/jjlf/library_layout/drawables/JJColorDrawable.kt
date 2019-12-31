package com.jjlf.library_layout.drawables

import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.FloatRange
import com.jjlf.library_layout.JJPadding
import com.jjlf.library_layout.extension.padding
import com.jjlf.library_layout.extension.scale
import kotlin.math.min

class JJColorDrawable : Drawable() {



    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintStroke = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mRect = RectF()
    private var mIsStroke = false
    private var mIsFill = true
    private var mPadding = JJPadding()
    private var mShape = 0
    private var mRadius = floatArrayOf(0f,0f,0f,0f,0f,0f,0f,0f)
    private var mPath = Path()
    private var mIsNewPath = false
    private var mSetupPath: ((RectF,Path)-> Unit)? = null
    private var mScaleX = -1f
    private var mScaleY = -1f
    private var mOffsetY = 0f
    private var mOffsetX = 0f
    private var mIsPathClosure = false

    private val mPaintStrokeShadow = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mStrokeShadowRadius = 4f
    private var mIsStrokeShadow = false
    private var mStrokeShadowClosure:((RectF, Path)->Unit)?=null
    private var mPathStrokeShadow = Path()
    private var mRectStrokeShadow = RectF()

    companion object{
        const val CIRCLE = 1
        const val ROUND_TO_MIN_SIZE = 2
        const val CORNER_SMOOTH_SAMLL = 3
        const val CORNER_SMOOTH_MEDIUM = 4
        const val CORNER_SMOOTH_LARGE = 5
        const val CORNER_SMOOTH_XLARGE = 6
    }

    init {
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.WHITE
        mPaintStroke.style = Paint.Style.STROKE
    }

    fun ssStrokeShadow(@FloatRange(from = 0.0 ,to = 7.0) radius: Float,color: Int, shadowPath: ((RectF,Path)-> Unit)?): JJColorDrawable {
        mStrokeShadowRadius = radius
        mPaintStrokeShadow.style = Paint.Style.STROKE
        mPaintStrokeShadow.strokeWidth = 0.5f
        mPaintStrokeShadow.setShadowLayer(mStrokeShadowRadius,0f,0f,color)
        mStrokeShadowClosure = shadowPath
        mIsStrokeShadow = true
        mIsStroke = false
        return this
    }

    fun ssScale(x: Float,y: Float): JJColorDrawable {
        mScaleX = x
        mScaleY = y
        return this
    }

    fun ssShape(type:Int): JJColorDrawable {
        mIsNewPath = false
        mIsPathClosure = false
        mShape = type
        return this
    }

    fun ssStroke(width: Float, color: Int) : JJColorDrawable {
        mIsStroke = true
        mPaintStroke.strokeWidth = width
        mPaintStroke.color = color
        mIsStrokeShadow = false
        return this
    }

    fun ssStrokeColor(color:Int) : JJColorDrawable {
        mPaintStroke.color = color
        return this
    }

    fun ssStrokeAndShadowLayer(width: Float, color: Int,shadowRadius: Float,shadowOffsetX:Float,shadowOffsetY:Float,shadowColor:Int) : JJColorDrawable {
        mIsStroke = true
        mPaintStroke.strokeWidth = width
        mPaintStroke.color = color
        mPaintStroke.setShadowLayer(shadowRadius,shadowOffsetX,shadowOffsetY,shadowColor)
        mIsStrokeShadow = false
        return this
    }

    fun ssRadius(radius: Float) : JJColorDrawable {
        mRadius = floatArrayOf(radius,radius,radius,radius,radius,radius,radius,radius)
        mIsNewPath = false
        mIsPathClosure = false
        mShape = 0
        return this
    }

    fun ssRadius(radius: FloatArray) : JJColorDrawable {
        mRadius = radius
        mIsNewPath = false
        mIsPathClosure = false
        mShape = 0
        return this
    }

    fun ssRadius(topLeft: Float,topRight:Float,bottomRight:Float,bottomLeft:Float) : JJColorDrawable {
        mRadius = floatArrayOf(topLeft,topLeft,topRight,topRight,bottomRight,bottomRight,bottomLeft,bottomLeft)
        mIsNewPath = false
        mIsPathClosure = false
        mShape = 0
        return this
    }

    fun ssFillColor(color: Int) : JJColorDrawable {
        mPaint.color = color
        return this
    }

    fun ssIsFillEnabled(boolean: Boolean) : JJColorDrawable {
        mIsFill = boolean
        return this
    }

    fun ssIsStrokeEnabled(boolean: Boolean) : JJColorDrawable {
        mIsStroke = boolean
        return this
    }

    fun ssIsStrokeShadowEnabled(boolean: Boolean) : JJColorDrawable {
        mIsStrokeShadow = boolean
        return this
    }
    fun ssPadding(padding: JJPadding): JJColorDrawable {
        mPadding = padding
        return this
    }

    fun ssOffset(x: Float,y: Float): JJColorDrawable {
        mOffsetX = x
        mOffsetY = y
        return this
    }


    fun ssPath(path:Path): JJColorDrawable {
        mIsNewPath = true
        mIsPathClosure = false
        mPath = path
        mShape = 0
        return this
    }

    fun ssPath(closure:(RectF, Path)->Unit): JJColorDrawable {
        mIsNewPath = true
        mIsPathClosure = true
        mSetupPath = closure
        mShape = 0
        return this
    }
    fun diposePath(): JJColorDrawable {
        mIsNewPath = false
        mIsPathClosure = false
        mPath.reset()
        mSetupPath = null
        return this
    }

    fun diposeStrokeShadow(): JJColorDrawable {
        mIsStrokeShadow = false
        mPathStrokeShadow.reset()
        mStrokeShadowClosure = null
        return this
    }



    override fun onBoundsChange(bounds: Rect?) {
        if(bounds != null){
            mRect.set(bounds)
            setupRect()
            if(mIsStroke) {
                val inset = mPaintStroke.strokeWidth / 2
                mRect.inset(inset,inset)
            }
        }
    }

    override fun draw(canvas: Canvas) {

        if(mRect.width() > 0f && mRect.height() >0f) {

            if(mIsStrokeShadow) {
                mPathStrokeShadow.reset()
                mRectStrokeShadow.set(mRect)
                mRectStrokeShadow.inset(mStrokeShadowRadius,mStrokeShadowRadius)
                mStrokeShadowClosure?.invoke(mRectStrokeShadow,mPathStrokeShadow)
                mRect.inset(mStrokeShadowRadius+0.25f,mStrokeShadowRadius+0.25f)
                mPaintStrokeShadow.color = if(mIsFill && mPaint.color != 0) mPaint.color else Color.parseColor("#80D1D1D1")
            }


            if(mIsPathClosure  && mIsNewPath){
                mPath.reset()
                mSetupPath?.invoke(mRect, mPath)
            }

            if (!mIsNewPath) {
                mPath.reset()
                mPathStrokeShadow.reset()
                setupRadiusForShape()
                mPath.addRoundRect(mRect, mRadius, Path.Direction.CW)
                mPathStrokeShadow.addRoundRect(mRectStrokeShadow, mRadius, Path.Direction.CW)
            }

            if(mIsStrokeShadow) canvas.drawPath(mPathStrokeShadow, mPaintStrokeShadow)
            if (mIsFill) canvas.drawPath(mPath, mPaint)
            if (mIsStroke) canvas.drawPath(mPath, mPaintStroke)

        }
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
        mPaintStroke.alpha = alpha
        invalidateSelf()
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaintStroke.colorFilter = colorFilter
        mPaint.colorFilter = colorFilter
        invalidateSelf()
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
                val radius = min(mRect.height(),mRect.width()) * 0.04f
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
                val radius = min(mRect.height(),mRect.width()) * 0.14f
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