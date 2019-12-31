package com.jjlf.library_layout.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.PixelCopy
import android.view.View
import android.view.animation.Animation
import android.view.animation.Interpolator
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateMarginsRelative
import com.google.android.material.appbar.AppBarLayout
import com.jjlf.library_layout.JJMargin
import com.jjlf.library_layout.JJPadding
import com.jjlf.library_layout.JJScreen
import com.jjlf.library_layout.R
import com.jjlf.library_layout.extension.padding
import java.lang.Exception
import kotlin.math.max
import kotlin.math.min

class JJRippleWrapper : RelativeLayout {

    private var mWIDTH = 0
    private var mHEIGHT = 0
    private var mFrameRate = 10
    private var mRippleDuration = 400
    private var mRippleAlpha = 90
    private val mCanvasHandler = Handler()
    private var mRadiusMax = 0f
    private var mAnimationRunning = false
    private var mTimer = 0
    private var mTimerEmpty = 0
    private var durationEmpty = -1
    private var mX = -1f
    private var mY = -1f
    private var mZoomDuration = 200
    private var mZoomScale = 1.03f
    private lateinit var mScaleAnimation: ScaleAnimation
    private var mHasToZoom: Boolean = false
    private var mIsCentered: Boolean = false
    private var mRippleType = 0
    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mOriginBitmap: Bitmap? = null
    private var mRippleColor = Color.WHITE
    private var mRipplePadding = 0
    private val gestureDetector: GestureDetector =  GestureDetector(context, object : SimpleOnGestureListener() {
        override fun onLongPress(event: MotionEvent) {
            super.onLongPress(event)
            //solo longperess
            animateRipple(event)
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return true
        }
    })
    private val runnable = Runnable { invalidate() }

    private var onCompletionListener: OnRippleCompleteListener? = null

    private var mIgnoreCl = false

     @Suppress("DEPRECATION")
     constructor(context: Context): super(context){
         id = View.generateViewId()
         mPaint.style = Paint.Style.FILL
         mPaint.color = mRippleColor
         mPaint.alpha = mRippleAlpha
         this.setWillNotDraw(false)
         isDrawingCacheEnabled = true
         isClickable = true
         mInit = false
     }

     @SuppressLint("ResourceType")
     constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

         mConstraintSet.constrainWidth(id,0)
         mConstraintSet.constrainHeight(id,0)
         val attrsArray = intArrayOf(
             android.R.attr.id,
             android.R.attr.layout_width, // 1
             android.R.attr.layout_height // 2
         )
         val ba = context.obtainStyledAttributes(attrs,
             attrsArray, 0, 0)

         val attrWidth = ba.getLayoutDimension(1, 0)
         val attrHeight = ba.getLayoutDimension(2, 0)

         val attrId = ba.getResourceId(0, View.NO_ID)
         if(attrId == View.NO_ID) id = View.generateViewId()


         ba.recycle()

         val a = context.obtainStyledAttributes(attrs,
             R.styleable.JJRippleWrapper, 0, 0)

          mIgnoreCl = a.getBoolean(R.styleable.JJRippleWrapper_layout_ignoreCl,false)


             //region attrs

             val aFillParent = a.getBoolean(R.styleable.JJRippleWrapper_clFillParent, false)
             val aFillParentHorizontal =
                 a.getBoolean(R.styleable.JJRippleWrapper_clFillParentHorizontally, false)
             val aFillParentVertical =
                 a.getBoolean(R.styleable.JJRippleWrapper_clFillParentVertically, false)

             val aCenterInParent = a.getBoolean(R.styleable.JJRippleWrapper_clCenterInParent, false)
             val aCenterInParentHorizontal =
                 a.getBoolean(R.styleable.JJRippleWrapper_clCenterInParentHorizontally, false)
             val aCenterInParentVertical =
                 a.getBoolean(R.styleable.JJRippleWrapper_clCenterInParentVertically, false)
             val aCenterInParentTopVertical =
                 a.getBoolean(R.styleable.JJRippleWrapper_clCenterInParentTopVertically, false)
             val aCenterInParentBottomVertical =
                 a.getBoolean(R.styleable.JJRippleWrapper_clCenterInParentBottomVertically, false)
             val aCenterInParentStartHorizontal =
                 a.getBoolean(R.styleable.JJRippleWrapper_clCenterInParentStartHorizontally, false)
             val aCenterInParentEndHorizontal =
                 a.getBoolean(R.styleable.JJRippleWrapper_clCenterInParentEndHorizontally, false)

             val aCenterInTopVerticalOf = a.getResourceId(
                 R.styleable.JJRippleWrapper_clCenterInTopVerticallyOf,
                 View.NO_ID
             )
             val aCenterInBottomVerticalOf = a.getResourceId(
                 R.styleable.JJRippleWrapper_clCenterInBottomVerticallyOf,
                 View.NO_ID
             )
             val aCenterInStartHorizontalOf = a.getResourceId(
                 R.styleable.JJRippleWrapper_clCenterInStartHorizontallyOf,
                 View.NO_ID
             )
             val aCenterInEndHorizontalOf = a.getResourceId(
                 R.styleable.JJRippleWrapper_clCenterInEndHorizontallyOf,
                 View.NO_ID
             )

             val aCenterVerticalOf = a.getResourceId(
                 R.styleable.JJRippleWrapper_clCenterVerticallyOf,
                 View.NO_ID
             )
             val aCenterHorizontalOf = a.getResourceId(
                 R.styleable.JJRippleWrapper_clCenterHorizontallyOf,
                 View.NO_ID
             )

             val aMarginEnd = a.getDimension(R.styleable.JJRippleWrapper_clMarginEnd, 0f)
             val aMarginStart = a.getDimension(R.styleable.JJRippleWrapper_clMarginStart, 0f)
             val aMarginTop = a.getDimension(R.styleable.JJRippleWrapper_clMarginTop, 0f)
             val aMarginBottom = a.getDimension(R.styleable.JJRippleWrapper_clMarginBottom, 0f)
             val aMarginEndPercent =
                 a.getFloat(R.styleable.JJRippleWrapper_clMarginEndPercentScreenHeight, 0f)
             val aMarginStartPercent =
                 a.getFloat(R.styleable.JJRippleWrapper_clMarginStartPercentScreenHeight, 0f)
             val aMarginTopPercent =
                 a.getFloat(R.styleable.JJRippleWrapper_clMarginTopPercentScreenHeight, 0f)
             val aMarginBottomPercent =
                 a.getFloat(R.styleable.JJRippleWrapper_clMarginBottomPercentScreenHeight, 0f)
             val aMarginEndPercentWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_clMarginEndPercentScreenWidth, 0f)
             val aMarginStartPercentWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_clMarginStartPercentScreenWidth, 0f)
             val aMarginTopPercentWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_clMarginTopPercentScreenWidth, 0f)
             val aMarginBottomPercentWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_clMarginBottomPercentScreenWidth, 0f)

             val aMargin = a.getDimension(R.styleable.JJRippleWrapper_clMargin, 0f)
             val aMarginPercentHeight = a.getFloat(R.styleable.JJRippleWrapper_clMargin, 0f)
             val aMarginPercentWidth = a.getFloat(R.styleable.JJRippleWrapper_clMargin, 0f)
             val aMarginResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_clMargin,
                 ConstraintLayout.NO_ID
             )
             val aMarginResPerScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_clMargin,
                 ConstraintLayout.NO_ID
             )
             val aMarginResPerScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_clMargin,
                 ConstraintLayout.NO_ID
             )

             val aMarginEndResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_clMarginEndResponsive,
                 ConstraintLayout.NO_ID
             )
             val aMarginStartResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_clMarginStartResponsive,
                 ConstraintLayout.NO_ID
             )
             val aMarginTopResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_clMarginTopResponsive,
                 ConstraintLayout.NO_ID
             )
             val aMarginBottomResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_clMarginBottomResponsive,
                 ConstraintLayout.NO_ID
             )
             val aMarginEndResPerScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_clMarginEndResPerScHeight,
                 ConstraintLayout.NO_ID
             )
             val aMarginStartResPerScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_clMarginStartResPerScHeight,
                 ConstraintLayout.NO_ID
             )
             val aMarginTopResPerScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_clMarginTopResPerScHeight,
                 ConstraintLayout.NO_ID
             )
             val aMarginBottomResPerScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_clMarginBottomResPerScHeight,
                 ConstraintLayout.NO_ID
             )
             val aMarginEndResPerScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_clMarginEndResPerScWidth,
                 ConstraintLayout.NO_ID
             )
             val aMarginStartResPerScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_clMarginStartResPerScWidth,
                 ConstraintLayout.NO_ID
             )
             val aMarginTopResPerScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_clMarginTopResPerScWidth,
                 ConstraintLayout.NO_ID
             )
             val aMarginBottomResPerScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_clMarginBottomResPerScWidth,
                 ConstraintLayout.NO_ID
             )

             val aMarginVertical = a.getDimension(R.styleable.JJRippleWrapper_clMarginVertical, 0f)
             val aMarginVerticalPercentHeight =
                 a.getFloat(R.styleable.JJRippleWrapper_clMarginVerticalPerScHeight, 0f)
             val aMarginVerticalPercentWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_clMarginVerticalPerScWidth, 0f)
             val aMarginVerticalResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_clMarginVerticalResponsive,
                 ConstraintLayout.NO_ID
             )
             val aMarginVerticalResPerScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_clMarginVerticalResPerScHeight,
                 ConstraintLayout.NO_ID
             )
             val aMarginVerticalResPerScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_clMarginVerticalResPerScWidth,
                 ConstraintLayout.NO_ID
             )

             val aMarginHorizontal =
                 a.getDimension(R.styleable.JJRippleWrapper_clMarginHorizontal, 0f)
             val aMarginHorizontalPercentHeight =
                 a.getFloat(R.styleable.JJRippleWrapper_clMarginHorizontalPerScHeight, 0f)
             val aMarginHorizontalPercentWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_clMarginHorizontalPerScWidth, 0f)
             val aMarginHorizontalResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_clMarginHorizontalResponsive,
                 ConstraintLayout.NO_ID
             )
             val aMarginHorizontalResPerScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_clMarginHorizontalResPerScHeight,
                 ConstraintLayout.NO_ID
             )
             val aMarginHorizontalResPerScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_clMarginHorizontalResPerScWidth,
                 ConstraintLayout.NO_ID
             )


             val aVerticalBias = a.getFloat(R.styleable.JJRippleWrapper_clVerticalBias, 0.5f)
             val aHorizontalBias = a.getFloat(R.styleable.JJRippleWrapper_clHorizontalBias, 0.5f)

             val aStartToStartParent =
                 a.getBoolean(R.styleable.JJRippleWrapper_clStartToStartParent, false)
             val aStartToEndParent =
                 a.getBoolean(R.styleable.JJRippleWrapper_clStartToEndParent, false)
             val aEndToEndParent = a.getBoolean(R.styleable.JJRippleWrapper_clEndToEndParent, false)
             val aEndToStartParent =
                 a.getBoolean(R.styleable.JJRippleWrapper_clEndToStartParent, false)
             val aTopToTopParent = a.getBoolean(R.styleable.JJRippleWrapper_clTopToTopParent, false)
             val aTopToBottomParent =
                 a.getBoolean(R.styleable.JJRippleWrapper_clTopToBottomParent, false)
             val aBottomToBottomParent =
                 a.getBoolean(R.styleable.JJRippleWrapper_clBottomToBottomParent, false)
             val aBottomToTopParent =
                 a.getBoolean(R.styleable.JJRippleWrapper_clBottomToTopParent, false)

             val aStartToStartOf =
                 a.getResourceId(R.styleable.JJRippleWrapper_clStartToStartOf, View.NO_ID)
             val aStartToEndOf =
                 a.getResourceId(R.styleable.JJRippleWrapper_clStartToEndOf, View.NO_ID)
             val aEndToEndOf = a.getResourceId(R.styleable.JJRippleWrapper_clEndToEndOf, View.NO_ID)
             val aEndToStartOf =
                 a.getResourceId(R.styleable.JJRippleWrapper_clEndToStartOf, View.NO_ID)
             val aTopToTopOf = a.getResourceId(R.styleable.JJRippleWrapper_clTopToTopOf, View.NO_ID)
             val aTopToBottomOf =
                 a.getResourceId(R.styleable.JJRippleWrapper_clTopToBottomOf, View.NO_ID)
             val aBottomToBottomOf = a.getResourceId(
                 R.styleable.JJRippleWrapper_clBottomToBottomOf,
                 View.NO_ID
             )
             val aBottomToTopOf =
                 a.getResourceId(R.styleable.JJRippleWrapper_clBottomToTopOf, View.NO_ID)

             val aHeightPercent = a.getFloat(R.styleable.JJRippleWrapper_clHeightPercent, 0f)
             val aWidthPercent = a.getFloat(R.styleable.JJRippleWrapper_clWidthPercent, 0f)
             val aHeightPercentScreenWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_clHeightPercentScreenWidth, 0f)
             val aWidthPercentScreenWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_clWidthPercentScreenWidth, 0f)
             val aHeightPercentScreenHeight =
                 a.getFloat(R.styleable.JJRippleWrapper_clHeightPercentScreenHeight, 0f)
             val aWidthPercentScreenHeight =
                 a.getFloat(R.styleable.JJRippleWrapper_clWidthPercentScreenHeight, 0f)

             val aHeightResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_clHeightResponsive,
                 View.NO_ID
             )
             val aWidthResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_clWidthResponsive,
                 View.NO_ID
             )

             val aHeightResponsivePercentScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_clHeightResponsivePercentScreenHeight,
                 View.NO_ID
             )
             val aWidthResponsivePercentScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_clWidthResponsivePercentScreenWidth,
                 View.NO_ID
             )
             val aHeightResponsivePercentScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_clHeightResponsivePercentScreenWidth,
                 View.NO_ID
             )
             val aWidthResponsivePercentScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_clWidthResponsivePercentScreenHeight,
                 View.NO_ID
             )

             val lHeightPercentScreenWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_lpHeightPercentScreenWidth, 0f)
             val lWidthPercentScreenWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_lpWidthPercentScreenWidth, 0f)
             val lHeightPercentScreenHeight =
                 a.getFloat(R.styleable.JJRippleWrapper_lpHeightPercentScreenHeight, 0f)
             val lWidthPercentScreenHeight =
                 a.getFloat(R.styleable.JJRippleWrapper_lpWidthPercentScreenHeight, 0f)

             val lHeightResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpHeightResponsive,
                 View.NO_ID
             )
             val lWidthResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpWidthResponsive,
                 View.NO_ID
             )
             val lHeightResponsivePercentScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpHeightResponsivePercentScreenHeight,
                 View.NO_ID
             )
             val lWidthResponsivePercentScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpWidthResponsivePercentScreenWidth,
                 View.NO_ID
             )
             val lHeightResponsivePercentScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpHeightResponsivePercentScreenWidth,
                 View.NO_ID
             )
             val lWidthResponsivePercentScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpWidthResponsivePercentScreenHeight,
                 View.NO_ID
             )

             val lMarginTopPercentScHeight =
                 a.getFloat(R.styleable.JJRippleWrapper_lpMarginTopPerScHeight, 0f)
             val lMarginLeftPercentScHeight =
                 a.getFloat(R.styleable.JJRippleWrapper_lpMarginLeftPerScHeight, 0f)
             val lMarginRightPercentScHeight =
                 a.getFloat(R.styleable.JJRippleWrapper_lpMarginRightPerScHeight, 0f)
             val lMarginBottomPercentScHeight =
                 a.getFloat(R.styleable.JJRippleWrapper_lpMarginBottomPerScHeight, 0f)

             val lMarginTopPercentScWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_lpMarginTopPerScWidth, 0f)
             val lMarginLeftPercentScWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_lpMarginLeftPerScWidth, 0f)
             val lMarginRightPercentScWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_lpMarginRightPerScWidth, 0f)
             val lMarginBottomPercentScWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_lpMarginBottomPerScWidth, 0f)

             val lMarginTopResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpMarginTopResponsive,
                 View.NO_ID
             )
             val lMarginLeftResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpMarginLeftResponsive,
                 View.NO_ID
             )
             val lMarginRightResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpMarginRightResponsive,
                 View.NO_ID
             )
             val lMarginBottomResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpMarginBottomResponsive,
                 View.NO_ID
             )

             val lMarginTopResponsivePercentScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpMarginTopResPerScWidth,
                 View.NO_ID
             )
             val lMarginLeftResponsivePercentScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpMarginLeftResPerScWidth,
                 View.NO_ID
             )
             val lMarginRightResponsivePercentScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpMarginRightResPerScWidth,
                 View.NO_ID
             )
             val lMarginBottomResponsivePercentScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpMarginBottomResPerScWidth,
                 View.NO_ID
             )

             val lMarginTopResponsivePercentScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpMarginTopResPerScHeight,
                 View.NO_ID
             )
             val lMarginLeftResponsivePercentScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpMarginLeftResPerScHeight,
                 View.NO_ID
             )
             val lMarginRightResponsivePercentScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpMarginRightResPerScHeight,
                 View.NO_ID
             )
             val lMarginBottomResponsivePercentScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpMarginBottomResPerScHeight,
                 View.NO_ID
             )

             val lMarginPerScHeight =
                 a.getFloat(R.styleable.JJRippleWrapper_lpMarginPercentScHeight, 0f)
             val lMarginPerScWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_lpMarginPercentScWidth, 0f)
             val lMarginResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpMarginResponsive,
                 View.NO_ID
             )
             val lMarginResPerScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpMarginResPerScHeight,
                 View.NO_ID
             )
             val lMarginResPerScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpMarginResPerScWidth,
                 View.NO_ID
             )

             val lMarginVerticalPerScHeight =
                 a.getFloat(R.styleable.JJRippleWrapper_lpMarginVerticalPerScHeight, 0f)
             val lMarginVerticalPerScWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_lpMarginVerticalPerScWidth, 0f)
             val lMarginVerticalResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpMarginVerticalResponsive,
                 View.NO_ID
             )
             val lMarginVerticalResPerScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpMarginVerticalResPerScWidth,
                 View.NO_ID
             )
             val lMarginVerticalResPerScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpMarginVerticalResPerScHeight,
                 View.NO_ID
             )

             val lMarginHorizontalPerScHeight =
                 a.getFloat(R.styleable.JJRippleWrapper_lpMarginHorizontalPerScHeight, 0f)
             val lMarginHorizontalPerScWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_lpMarginHorizontalPerScWidth, 0f)
             val lMarginHorizontalResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpMarginHorizontalResponsive,
                 View.NO_ID
             )
             val lMarginHorizontalResPerScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpMarginHorizontalResPerScWidth,
                 View.NO_ID
             )
             val lMarginHorizontalResPerScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpMarginHorizontalResPerScHeight,
                 View.NO_ID
             )


             val lPaddingTopPercentScHeight =
                 a.getFloat(R.styleable.JJRippleWrapper_lpPaddingTopPerScHeight, 0f)
             val lPaddingLeftPercentScHeight =
                 a.getFloat(R.styleable.JJRippleWrapper_lpPaddingLeftPerScHeight, 0f)
             val lPaddingRightPercentScHeight =
                 a.getFloat(R.styleable.JJRippleWrapper_lpPaddingRightPerScHeight, 0f)
             val lPaddingBottomPercentScHeight =
                 a.getFloat(R.styleable.JJRippleWrapper_lpPaddingBottomPerScHeight, 0f)

             val lPaddingTopPercentScWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_lpPaddingTopPerScWidth, 0f)
             val lPaddingLeftPercentScWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_lpPaddingLeftPerScWidth, 0f)
             val lPaddingRightPercentScWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_lpPaddingRightPerScWidth, 0f)
             val lPaddingBottomPercentScWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_lpPaddingBottomPerScWidth, 0f)

             val lPaddingTopResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpPaddingTopResponsive,
                 View.NO_ID
             )
             val lPaddingLeftResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpPaddingLeftResponsive,
                 View.NO_ID
             )
             val lPaddingRightResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpPaddingRightResponsive,
                 View.NO_ID
             )
             val lPaddingBottomResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpPaddingBottomResponsive,
                 View.NO_ID
             )

             val lPaddingTopResponsivePercentScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpPaddingTopResPerScWidth,
                 View.NO_ID
             )
             val lPaddingLeftResponsivePercentScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpPaddingLeftResPerScWidth,
                 View.NO_ID
             )
             val lPaddingRightResponsivePercentScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpPaddingRightResPerScWidth,
                 View.NO_ID
             )
             val lPaddingBottomResponsivePercentScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpPaddingBottomResPerScWidth,
                 View.NO_ID
             )

             val lPaddingTopResponsivePercentScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpPaddingTopResPerScHeight,
                 View.NO_ID
             )
             val lPaddingLeftResponsivePercentScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpPaddingLeftResPerScHeight,
                 View.NO_ID
             )
             val lPaddingRightResponsivePercentScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpPaddingRightResPerScHeight,
                 View.NO_ID
             )
             val lPaddingBottomResponsivePercentScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpPaddingBottomResPerScHeight,
                 View.NO_ID
             )

             val lPaddingPerScHeight =
                 a.getFloat(R.styleable.JJRippleWrapper_lpPaddingPercentScHeight, 0f)
             val lPaddingPerScWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_lpPaddingPercentScWidth, 0f)
             val lPaddingResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpPaddingResponsive,
                 View.NO_ID
             )
             val lPaddingResPerScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpPaddingResPerScHeight,
                 View.NO_ID
             )
             val lPaddingResPerScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpPaddingResPerScWidth,
                 View.NO_ID
             )

             val lPaddingVerticalPerScHeight =
                 a.getFloat(R.styleable.JJRippleWrapper_lpPaddingVerticalPerScHeight, 0f)
             val lPaddingVerticalPerScWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_lpPaddingVerticalPerScWidth, 0f)
             val lPaddingVerticalResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpPaddingVerticalResponsive,
                 View.NO_ID
             )
             val lPaddingVerticalResPerScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpPaddingVerticalResPerScWidth,
                 View.NO_ID
             )
             val lPaddingVerticalResPerScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpPaddingVerticalResPerScHeight,
                 View.NO_ID
             )

             val lPaddingHorizontalPerScHeight =
                 a.getFloat(R.styleable.JJRippleWrapper_lpPaddingHorizontalPerScHeight, 0f)
             val lPaddingHorizontalPerScWidth =
                 a.getFloat(R.styleable.JJRippleWrapper_lpPaddingHorizontalPerScWidth, 0f)
             val lPaddingHorizontalResponsive = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpPaddingHorizontalResponsive,
                 View.NO_ID
             )
             val lPaddingHorizontalResPerScWidth = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpPaddingHorizontalResPerScWidth,
                 View.NO_ID
             )
             val lPaddingHorizontalResPerScHeight = a.getResourceId(
                 R.styleable.JJRippleWrapper_lpPaddingHorizontalResPerScHeight,
                 View.NO_ID
             )



             a.recycle()

             init(context,attrs)

             //endregion

             //region Layoutparams

             //region margin


             if (lMarginPerScHeight > 0f) {
                 val mar = JJScreen.percentHeight(lMarginPerScHeight)
                 mlpMargins = JJMargin(mar, mar, mar, mar)
             }
             if (lMarginPerScWidth > 0f) {
                 val mar = JJScreen.percentWidth(lMarginPerScWidth)
                 mlpMargins = JJMargin(mar, mar, mar, mar)
             }

             if (lMarginResponsive != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lMarginResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSize(first, second, three, four)
                 mlpMargins = JJMargin(mar, mar, mar, mar)
             }

             if (lMarginResPerScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lMarginResPerScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 mlpMargins = JJMargin(mar, mar, mar, mar)
             }

             if (lMarginResPerScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lMarginResPerScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 mlpMargins = JJMargin(mar, mar, mar, mar)
             }


             //endregion

             //region margin vertical
             if (lMarginVerticalPerScHeight > 0f) {
                 val mar = JJScreen.percentHeight(lMarginVerticalPerScHeight)
                 mlpMargins?.top = mar; mlpMargins?.bottom = mar
             }
             if (lMarginVerticalPerScWidth > 0f) {
                 val mar = JJScreen.percentWidth(lMarginVerticalPerScWidth)
                 mlpMargins?.top = mar; mlpMargins?.bottom = mar
             }

             if (lMarginVerticalResponsive != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lMarginVerticalResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSize(first, second, three, four)
                 mlpMargins?.top = mar; mlpMargins?.bottom = mar
             }

             if (lMarginVerticalResPerScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lMarginVerticalResPerScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 mlpMargins?.top = mar; mlpMargins?.bottom = mar
             }

             if (lMarginVerticalResPerScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lMarginVerticalResPerScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 mlpMargins?.top = mar; mlpMargins?.bottom = mar
             }
             //endregion

             //region Horizontal margin

             if (lMarginHorizontalPerScHeight > 0f) {
                 val mar = JJScreen.percentHeight(lMarginHorizontalPerScHeight)
                 mlpMargins?.left = mar; mlpMargins?.right = mar
             }
             if (lMarginHorizontalPerScWidth > 0f) {
                 val mar = JJScreen.percentWidth(lMarginHorizontalPerScWidth)
                 mlpMargins?.left = mar; mlpMargins?.right = mar
             }

             if (lMarginHorizontalResponsive != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lMarginHorizontalResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSize(first, second, three, four)
                 mlpMargins?.left = mar; mlpMargins?.right = mar
             }

             if (lMarginHorizontalResPerScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lMarginHorizontalResPerScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 mlpMargins?.left = mar; mlpMargins?.right = mar
             }

             if (lMarginHorizontalResPerScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lMarginHorizontalResPerScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 mlpMargins?.left = mar; mlpMargins?.right = mar
             }

             //endregion

             //region margin start end top bottom

             if (lMarginTopPercentScHeight > 0f) mlpMargins?.top =
                 JJScreen.percentHeight(lMarginTopPercentScHeight)
             if (lMarginLeftPercentScHeight > 0f) mlpMargins?.left =
                 JJScreen.percentHeight(lMarginLeftPercentScHeight)
             if (lMarginRightPercentScHeight > 0f) mlpMargins?.right =
                 JJScreen.percentHeight(lMarginRightPercentScHeight)
             if (lMarginBottomPercentScHeight > 0f) mlpMargins?.bottom =
                 JJScreen.percentHeight(lMarginBottomPercentScHeight)

             if (lMarginTopPercentScWidth > 0f) mlpMargins?.top =
                 JJScreen.percentWidth(lMarginTopPercentScWidth)
             if (lMarginLeftPercentScWidth > 0f) mlpMargins?.left =
                 JJScreen.percentWidth(lMarginLeftPercentScWidth)
             if (lMarginRightPercentScWidth > 0f) mlpMargins?.right =
                 JJScreen.percentWidth(lMarginRightPercentScWidth)
             if (lMarginBottomPercentScWidth > 0f) mlpMargins?.bottom =
                 JJScreen.percentWidth(lMarginBottomPercentScWidth)


             if (lMarginTopResponsive != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lMarginTopResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSize(first, second, three, four)
                 mlpMargins?.top = v
             }

             if (lMarginLeftResponsive != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lMarginLeftResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSize(first, second, three, four)
                 mlpMargins?.left = v
             }

             if (lMarginRightResponsive != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lMarginRightResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSize(first, second, three, four)
                 mlpMargins?.right = v
             }

             if (lMarginBottomResponsive != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lMarginBottomResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSize(first, second, three, four)
                 mlpMargins?.bottom = v
             }


             if (lMarginTopResponsivePercentScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lMarginTopResponsivePercentScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 mlpMargins?.top = v
             }

             if (lMarginLeftResponsivePercentScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lMarginLeftResponsivePercentScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 mlpMargins?.left = v
             }

             if (lMarginRightResponsivePercentScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lMarginRightResponsivePercentScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 mlpMargins?.right = v
             }

             if (lMarginBottomResponsivePercentScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lMarginBottomResponsivePercentScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 mlpMargins?.bottom = v
             }

             if (lMarginTopResponsivePercentScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lMarginTopResponsivePercentScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 mlpMargins?.top = v
             }

             if (lMarginLeftResponsivePercentScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lMarginLeftResponsivePercentScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 mlpMargins?.left = v
             }

             if (lMarginRightResponsivePercentScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lMarginRightResponsivePercentScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 mlpMargins?.right = v
             }

             if (lMarginBottomResponsivePercentScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lMarginBottomResponsivePercentScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 mlpMargins?.bottom = v
             }

             //endregion

             //region padding

             if (lPaddingPerScHeight > 0f) {
                 val mar = JJScreen.percentHeight(lPaddingPerScHeight)
                 mlpPaddings = JJPadding(mar, mar, mar, mar)
             }
             if (lPaddingPerScWidth > 0f) {
                 val mar = JJScreen.percentWidth(lPaddingPerScWidth)
                 mlpPaddings = JJPadding(mar, mar, mar, mar)
             }

             if (lPaddingResponsive != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lPaddingResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSize(first, second, three, four)
                 mlpPaddings = JJPadding(mar, mar, mar, mar)
             }

             if (lPaddingResPerScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lPaddingResPerScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 mlpPaddings = JJPadding(mar, mar, mar, mar)
             }

             if (lPaddingResPerScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lPaddingResPerScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 mlpPaddings = JJPadding(mar, mar, mar, mar)
             }


             //endregion

             //region padding vertical

             if (lPaddingVerticalPerScHeight > 0f) {
                 val mar = JJScreen.percentHeight(lPaddingVerticalPerScHeight)
                 mlpPaddings.top = mar; mlpPaddings.bottom = mar
             }
             if (lPaddingVerticalPerScWidth > 0f) {
                 val mar = JJScreen.percentWidth(lMarginVerticalPerScWidth)
                 mlpPaddings.top = mar; mlpPaddings.bottom = mar
             }

             if (lPaddingVerticalResponsive != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lPaddingVerticalResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSize(first, second, three, four)
                 mlpPaddings.top = mar; mlpPaddings.bottom = mar
             }

             if (lPaddingVerticalResPerScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lPaddingVerticalResPerScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 mlpPaddings.top = mar; mlpPaddings.bottom = mar
             }

             if (lPaddingVerticalResPerScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lPaddingVerticalResPerScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 mlpPaddings.top = mar; mlpPaddings.bottom = mar
             }
             //endregion

             //region Horizontal padding

             if (lPaddingHorizontalPerScHeight > 0f) {
                 val mar = JJScreen.percentHeight(lPaddingHorizontalPerScHeight)
                 mlpPaddings.left = mar; mlpPaddings.right = mar
             }
             if (lPaddingHorizontalPerScWidth > 0f) {
                 val mar = JJScreen.percentWidth(lPaddingHorizontalPerScWidth)
                 mlpPaddings.left = mar; mlpPaddings.right = mar
             }

             if (lPaddingHorizontalResponsive != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lPaddingHorizontalResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSize(first, second, three, four)
                 mlpPaddings.left = mar; mlpPaddings.right = mar
             }

             if (lPaddingHorizontalResPerScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lPaddingHorizontalResPerScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 mlpPaddings.left = mar; mlpPaddings.right = mar
             }

             if (lPaddingHorizontalResPerScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lPaddingHorizontalResPerScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 mlpPaddings.left = mar; mlpPaddings.right = mar
             }

             //endregion

             //region padding start end top bottom

             if (lPaddingTopPercentScHeight > 0f) mlpPaddings.top =
                 JJScreen.percentHeight(lPaddingTopPercentScHeight)
             if (lPaddingLeftPercentScHeight > 0f) mlpPaddings.left =
                 JJScreen.percentHeight(lPaddingLeftPercentScHeight)
             if (lPaddingRightPercentScHeight > 0f) mlpPaddings.right =
                 JJScreen.percentHeight(lPaddingRightPercentScHeight)
             if (lPaddingBottomPercentScHeight > 0f) mlpPaddings.bottom =
                 JJScreen.percentHeight(lPaddingBottomPercentScHeight)

             if (lPaddingTopPercentScWidth > 0f) mlpPaddings.top =
                 JJScreen.percentWidth(lPaddingTopPercentScWidth)
             if (lPaddingLeftPercentScWidth > 0f) mlpPaddings.left =
                 JJScreen.percentWidth(lPaddingLeftPercentScWidth)
             if (lPaddingRightPercentScWidth > 0f) mlpPaddings.right =
                 JJScreen.percentWidth(lPaddingRightPercentScWidth)
             if (lPaddingBottomPercentScWidth > 0f) mlpPaddings.bottom =
                 JJScreen.percentWidth(lPaddingBottomPercentScWidth)


             if (lPaddingTopResponsive != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lPaddingTopResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSize(first, second, three, four)
                 mlpPaddings.top = v
             }

             if (lPaddingLeftResponsive != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lPaddingLeftResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSize(first, second, three, four)
                 mlpPaddings.left = v
             }

             if (lPaddingRightResponsive != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lPaddingRightResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSize(first, second, three, four)
                 mlpPaddings.right = v
             }

             if (lPaddingBottomResponsive != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lPaddingBottomResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSize(first, second, three, four)
                 mlpPaddings.bottom = v
             }


             if (lPaddingTopResponsivePercentScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lPaddingTopResponsivePercentScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 mlpPaddings.top = v
             }

             if (lPaddingLeftResponsivePercentScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lPaddingLeftResponsivePercentScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 mlpPaddings.left = v
             }

             if (lPaddingRightResponsivePercentScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lPaddingRightResponsivePercentScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 mlpPaddings.right = v
             }

             if (lPaddingBottomResponsivePercentScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lPaddingBottomResponsivePercentScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 mlpPaddings.bottom = v
             }

             if (lPaddingTopResponsivePercentScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lPaddingTopResponsivePercentScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 mlpPaddings.top = v
             }

             if (lPaddingLeftResponsivePercentScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lPaddingLeftResponsivePercentScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 mlpPaddings.left = v
             }

             if (lPaddingRightResponsivePercentScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lPaddingRightResponsivePercentScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 mlpPaddings.right = v
             }

             if (lPaddingBottomResponsivePercentScHeight != View.NO_ID) {
                 val arrayDimen =
                     resources.obtainTypedArray(lPaddingBottomResponsivePercentScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 mlpPaddings.bottom = v
             }

             //endregion


             //region params layout heigt width

             mlpHeight = attrHeight
             mlpWidth = attrWidth
             if (lHeightPercentScreenWidth > 0f) mlpHeight =
                 JJScreen.percentWidth(lHeightPercentScreenWidth)
             if (lHeightPercentScreenHeight > 0f) mlpHeight =
                 JJScreen.percentHeight(lHeightPercentScreenHeight)
             if (lWidthPercentScreenWidth > 0f) mlpWidth =
                 JJScreen.percentWidth(lWidthPercentScreenWidth)
             if (lWidthPercentScreenHeight > 0f) mlpWidth =
                 JJScreen.percentHeight(lWidthPercentScreenHeight)

             if (lHeightResponsive != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lHeightResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val h = JJScreen.responsiveSize(first, second, three, four)
                 mlpHeight = h
             }

             if (lWidthResponsive != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lWidthResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val w = JJScreen.responsiveSize(first, second, three, four)
                 mlpWidth = w
             }


             if (lHeightResponsivePercentScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lHeightResponsivePercentScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val h = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 mlpHeight = h
             }

             if (lWidthResponsivePercentScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lWidthResponsivePercentScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val w = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)

                 mlpWidth = w
             }

             if (lHeightResponsivePercentScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lHeightResponsivePercentScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val h = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 mlpHeight = h
             }

             if (lWidthResponsivePercentScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(lWidthResponsivePercentScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val w = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)

                 mlpWidth = w
             }

             //endregion

             //endregion

             //region constraint Layout attr

             //region margin
             var margins = JJMargin()

             if (aMargin > 0f) margins =
                 JJMargin(aMargin.toInt(), aMargin.toInt(), aMargin.toInt(), aMargin.toInt())

             if (aMarginPercentHeight > 0f) {
                 val mar = JJScreen.percentHeight(aMarginPercentHeight)
                 margins = JJMargin(mar, mar, mar, mar)
             }

             if (aMarginPercentWidth > 0f) {
                 val mar = JJScreen.percentWidth(aMarginPercentWidth)
                 margins = JJMargin(mar, mar, mar, mar)
             }

             if (aMarginResponsive != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aMarginResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSize(first, second, three, four)
                 margins = JJMargin(v, v, v, v)
             }

             if (aMarginResPerScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aMarginResPerScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 margins = JJMargin(v, v, v, v)
             }

             if (aMarginResPerScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aMarginResPerScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val v = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 margins = JJMargin(v, v, v, v)
             }

             //endregion

             //region margin Vertical

             if (aMarginVertical > 0f) {
                 margins.top = aMarginVertical.toInt(); margins.bottom = aMarginVertical.toInt()
             }

             if (aMarginVerticalPercentHeight > 0f) {
                 val mar = JJScreen.percentHeight(aMarginVerticalPercentHeight)
                 margins.top = mar; margins.bottom = mar
             }

             if (aMarginVerticalPercentWidth > 0f) {
                 val mar = JJScreen.percentWidth(aMarginVerticalPercentWidth)
                 margins.top = mar; margins.bottom = mar
             }

             if (aMarginVerticalResponsive != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aMarginVerticalResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSize(first, second, three, four)
                 margins.top = mar; margins.bottom = mar
             }

             if (aMarginVerticalResPerScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aMarginVerticalResPerScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 margins.top = mar; margins.bottom = mar
             }

             if (aMarginVerticalResPerScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aMarginVerticalResPerScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 margins.top = mar; margins.bottom = mar
             }

             //endregion

             // region margin Horizontal

             if (aMarginHorizontal > 0f) {
                 margins.left = aMarginHorizontal.toInt(); margins.right = aMarginHorizontal.toInt()
             }

             if (aMarginHorizontalPercentHeight > 0f) {
                 val mar = JJScreen.percentHeight(aMarginHorizontalPercentHeight)
                 margins.left = mar; margins.right = mar
             }

             if (aMarginHorizontalPercentWidth > 0f) {
                 val mar = JJScreen.percentWidth(aMarginHorizontalPercentWidth)
                 margins.left = mar; margins.right = mar
             }

             if (aMarginHorizontalResponsive != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aMarginHorizontalResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSize(first, second, three, four)
                 margins.left = mar; margins.right = mar
             }

             if (aMarginHorizontalResPerScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aMarginHorizontalResPerScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 margins.left = mar; margins.right = mar
             }

             if (aMarginHorizontalResPerScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aMarginHorizontalResPerScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 margins.left = mar; margins.right = mar
             }

             //endregion

             //region margin start end top bottom

             if (aMarginStart > 0f) margins.left = aMarginStart.toInt()
             if (aMarginEnd > 0f) margins.right = aMarginEnd.toInt()
             if (aMarginTop > 0f) margins.top = aMarginTop.toInt()
             if (aMarginBottom > 0f) margins.bottom = aMarginBottom.toInt()

             if (aMarginStartPercent > 0f) margins.left =
                 JJScreen.percentHeight(aMarginStartPercent)
             if (aMarginTopPercent > 0f) margins.top = JJScreen.percentHeight(aMarginTopPercent)
             if (aMarginEndPercent > 0f) margins.right = JJScreen.percentHeight(aMarginEndPercent)
             if (aMarginBottomPercent > 0f) margins.bottom =
                 JJScreen.percentHeight(aMarginBottomPercent)

             if (aMarginStartPercentWidth > 0f) margins.left =
                 JJScreen.percentWidth(aMarginStartPercentWidth)
             if (aMarginTopPercentWidth > 0f) margins.top =
                 JJScreen.percentWidth(aMarginTopPercentWidth)
             if (aMarginEndPercentWidth > 0f) margins.right =
                 JJScreen.percentWidth(aMarginEndPercentWidth)
             if (aMarginBottomPercentWidth > 0f) margins.bottom =
                 JJScreen.percentWidth(aMarginBottomPercentWidth)

             if (aMarginStartResponsive > 0f) {
                 val arrayDimen = resources.obtainTypedArray(aMarginStartResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSize(first, second, three, four)
                 margins.left = mar
             }
             if (aMarginEndResponsive > 0f) {
                 val arrayDimen = resources.obtainTypedArray(aMarginEndResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSize(first, second, three, four)
                 margins.right = mar
             }
             if (aMarginTopResponsive > 0f) {
                 val arrayDimen = resources.obtainTypedArray(aMarginTopResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSize(first, second, three, four)
                 margins.top = mar
             }
             if (aMarginBottomResponsive > 0f) {
                 val arrayDimen = resources.obtainTypedArray(aMarginBottomResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSize(first, second, three, four)
                 margins.bottom = mar
             }

             if (aMarginStartResPerScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aMarginStartResPerScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 margins.left = mar
             }

             if (aMarginEndResPerScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aMarginEndResPerScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 margins.right = mar
             }

             if (aMarginTopResPerScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aMarginTopResPerScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 margins.top = mar
             }

             if (aMarginBottomResPerScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aMarginBottomResPerScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 margins.bottom = mar
             }

             if (aMarginStartResPerScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aMarginStartResPerScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 margins.left = mar
             }

             if (aMarginEndResPerScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aMarginEndResPerScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 margins.right = mar
             }

             if (aMarginTopResPerScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aMarginTopResPerScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 margins.top = mar
             }

             if (aMarginBottomResPerScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aMarginBottomResPerScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val mar = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 margins.bottom = mar
             }


             //endregion

             if (attrWidth > 0 || attrWidth == -2) clWidth(attrWidth)
             if (attrHeight > 0 || attrHeight == -2) clHeight(attrHeight)

             if (aHeightPercent > 0f) clPercentHeight(aHeightPercent)

             if (aHeightPercentScreenWidth > 0f) clHeight(
                 JJScreen.percentWidth(
                     aHeightPercentScreenWidth
                 )
             )
             if (aHeightPercentScreenHeight > 0f) clHeight(
                 JJScreen.percentHeight(
                     aHeightPercentScreenHeight
                 )
             )

             if (aWidthPercent > 0f) clPercentWidth(aWidthPercent)
             if (aWidthPercentScreenWidth > 0f) clWidth(
                 JJScreen.percentWidth(
                     aWidthPercentScreenWidth
                 )
             )
             if (aWidthPercentScreenHeight > 0f) clWidth(
                 JJScreen.percentHeight(
                     aWidthPercentScreenHeight
                 )
             )

             if (aHeightResponsive != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aHeightResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val h = JJScreen.responsiveSize(first, second, three, four)
                 clHeight(h)
             }

             if (aWidthResponsive != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aWidthResponsive)
                 val first = arrayDimen.getDimension(0, 0f).toInt()
                 val second = arrayDimen.getDimension(1, 0f).toInt()
                 val three = arrayDimen.getDimension(2, 0f).toInt()
                 val four = arrayDimen.getDimension(3, 0f).toInt()
                 arrayDimen.recycle()

                 val w = JJScreen.responsiveSize(first, second, three, four)

                 clWidth(w)
             }

             if (aHeightResponsivePercentScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aHeightResponsivePercentScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val h = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)
                 clHeight(h)
             }

             if (aWidthResponsivePercentScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aWidthResponsivePercentScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val w = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)

                 clWidth(w)
             }

             if (aHeightResponsivePercentScWidth != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aHeightResponsivePercentScWidth)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val h = JJScreen.responsiveSizePercentScreenWidth(first, second, three, four)
                 clHeight(h)
             }

             if (aWidthResponsivePercentScHeight != View.NO_ID) {
                 val arrayDimen = resources.obtainTypedArray(aWidthResponsivePercentScHeight)
                 val first = arrayDimen.getFloat(0, 0f)
                 val second = arrayDimen.getFloat(1, 0f)
                 val three = arrayDimen.getFloat(2, 0f)
                 val four = arrayDimen.getFloat(3, 0f)
                 arrayDimen.recycle()

                 val w = JJScreen.responsiveSizePercentScreenHeight(first, second, three, four)

                 clWidth(w)
             }


             if (aStartToStartOf != View.NO_ID) clStartToStart(aStartToStartOf)
             if (aStartToEndOf != View.NO_ID) clStartToEnd(aStartToEndOf)
             if (aEndToEndOf != View.NO_ID) clEndToEnd(aEndToEndOf)
             if (aEndToStartOf != View.NO_ID) clEndToStart(aEndToStartOf)
             if (aTopToTopOf != View.NO_ID) clTopToTop(aTopToTopOf)
             if (aTopToBottomOf != View.NO_ID) clTopToBottom(aTopToBottomOf)
             if (aBottomToBottomOf != View.NO_ID) clBottomToBottom(aBottomToBottomOf)
             if (aBottomToTopOf != View.NO_ID) clBottomToTop(aBottomToTopOf)

             if (aStartToStartParent) clStartToStartParent()
             if (aStartToEndParent) clStartToEndParent()
             if (aEndToEndParent) clEndToEndParent()
             if (aEndToStartParent) clEndToStartParent()
             if (aTopToTopParent) clTopToTopParent()
             if (aTopToBottomParent) clTopToBottomParent()
             if (aBottomToBottomParent) clBottomToBottomParent()
             if (aBottomToTopParent) clBottomToTopParent()


             if (aCenterInParentTopVertical) clCenterInParentTopVertically()
             if (aCenterInParentBottomVertical) clCenterInParentBottomVertically()
             if (aCenterInParentStartHorizontal) clCenterInParentStartHorizontally()
             if (aCenterInParentEndHorizontal) clCenterInParentEndHorizontally()

             if (aCenterInTopVerticalOf != View.NO_ID) clCenterInTopVertically(
                 aCenterInTopVerticalOf
             )
             if (aCenterInBottomVerticalOf != View.NO_ID) clCenterInBottomVertically(
                 aCenterInBottomVerticalOf
             )
             if (aCenterInStartHorizontalOf != View.NO_ID) clCenterInStartHorizontally(
                 aCenterInStartHorizontalOf
             )
             if (aCenterInEndHorizontalOf != View.NO_ID) clCenterInEndHorizontally(
                 aCenterInEndHorizontalOf
             )

             if (aCenterVerticalOf != View.NO_ID) clCenterVerticallyOf(aCenterVerticalOf)
             if (aCenterHorizontalOf != View.NO_ID) clCenterHorizontallyOf(aCenterHorizontalOf)

             if (aCenterInParentHorizontal) clCenterInParentHorizontally()
             if (aCenterInParentVertical) clCenterInParentVertically()

             if (aFillParentHorizontal) clFillParentHorizontally()
             if (aFillParentVertical) clFillParentVertically()

             if (aCenterInParent) clCenterInParent()
             if (aFillParent) clFillParent()

             if (aVerticalBias > 0f) clVerticalBias(aVerticalBias)
             if (aHorizontalBias > 0f) clHorizontalBias(aHorizontalBias)

             clMargins(margins)

             //endregion


     }




    /**
     * Method that initializes all fields and sets listeners
     *
     * @param context Context used to create this view
     * @param attrs   Attribute used to initialize fields
     */
    @Suppress("DEPRECATION")
    @SuppressLint("CustomViewStyleable", "ResourceType")
    fun init(context: Context, attrs: AttributeSet) {
        if (isInEditMode) return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.JJRippleWrapper)

        mRippleColor = typedArray.getColor(R.styleable.JJRippleWrapper_rw_color, Color.WHITE)
        mRippleType = typedArray.getInt(R.styleable.JJRippleWrapper_rw_type, 0)
        mHasToZoom = typedArray.getBoolean(R.styleable.JJRippleWrapper_rw_zoom, false)
        mIsCentered = typedArray.getBoolean(R.styleable.JJRippleWrapper_rw_centered, false)
        mRadiusMax = typedArray.getDimension(R.styleable.JJRippleWrapper_rw_radiusMax,0f)
        val radiusResponsive = typedArray.getResourceId(R.styleable.JJRippleWrapper_rw_radiusMaxResponsive,View.NO_ID)
       val radiusPerScHeight = typedArray.getFloat(R.styleable.JJRippleWrapper_rw_radiusMaxPercentScHeight,0f)
       val radiusPerScWidth  = typedArray.getFloat(R.styleable.JJRippleWrapper_rw_radiusMaxPercentScWidth,0f)
        mRippleDuration =
            typedArray.getInteger(R.styleable.JJRippleWrapper_rw_rippleDuration, mRippleDuration)
        mFrameRate = typedArray.getInteger(R.styleable.JJRippleWrapper_rw_frameRate, mFrameRate)
        mRippleAlpha = typedArray.getInteger(R.styleable.JJRippleWrapper_rw_alpha, mRippleAlpha)
        mRipplePadding = typedArray.getDimensionPixelSize(R.styleable.JJRippleWrapper_rw_ripplePadding, 0)
        mZoomScale = typedArray.getFloat(R.styleable.JJRippleWrapper_rw_zoomScale, 1.03f)
        mZoomDuration = typedArray.getInt(R.styleable.JJRippleWrapper_rw_zoomDuration, 200)
        typedArray.recycle()
        mPaint.style = Paint.Style.FILL
        mPaint.color = mRippleColor
        mPaint.alpha = mRippleAlpha
        this.setWillNotDraw(false)

        if(radiusPerScHeight > 0f) {
            mRadiusMax = JJScreen.percentHeightFloat(radiusPerScHeight)
        }

        if(radiusPerScWidth > 0f) {
            mRadiusMax = JJScreen.percentWidthFloat(radiusPerScWidth)
        }

        if (radiusResponsive != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(radiusResponsive)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

             mRadiusMax = JJScreen.responsiveSize(first, second, three, four)
        }

        isDrawingCacheEnabled = true
        isClickable = true
    }


    //region override
    var mInit = true
    private var mlpHeight = 0
    private var mlpWidth = 0
    private var mlpMargins : JJMargin? = JJMargin()
    private var mlpPaddings = JJPadding()
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if(mInit){
            val csParent = parent as? ConstraintLayout
            val mlParent = parent as? MotionLayout
            when {
                mlParent != null -> Log.e("JJKIT","PARENT MOTION LAYOUT")
                csParent != null -> if(!mIgnoreCl) clApply()
                else -> {
                    layoutParams.height = mlpHeight
                    layoutParams.width = mlpWidth
                    val margin = layoutParams as? MarginLayoutParams
                    margin?.topMargin = mlpMargins?.top ?: 0
                    margin?.marginStart =  mlpMargins?.left ?: 0
                    margin?.marginEnd =  mlpMargins?.right ?: 0
                    margin?.bottomMargin =  mlpMargins?.bottom ?: 0
                    mlpMargins = null

                }
            }

            var pl = paddingLeft
            var pr = paddingRight
            if (paddingStart > 0) pl = paddingStart
            if (paddingEnd > 0) pr = paddingEnd

            if(mlpPaddings.top <= 0 && paddingTop > 0) mlpPaddings.top = paddingTop
            if(mlpPaddings.bottom <= 0 && paddingBottom > 0) mlpPaddings.bottom = paddingBottom
            if(mlpPaddings.left <= 0 && pl > 0) mlpPaddings.left = pl
            if(mlpPaddings.right <= 0 && pr > 0) mlpPaddings.right = pr

            setPaddingRelative(mlpPaddings.left,mlpPaddings.top,mlpPaddings.right,mlpPaddings.bottom)
            mInit = false
        }

    }


    private var mIsClipChildrenEnabled = false
    private var mPathClipChildren = Path()
    private var mIsClipInPathChildren = false
    private var mIsClipOutPathChildren = false
    private var mIsPathClosureClipChildren = false
    private var mClosurePathClipChildren : ((RectF, Path)->Unit)? = null
    private var mRectClip = RectF()
    @Suppress("DEPRECATION")
    override fun onDraw(canvas: Canvas?) {

        if(mIsClipChildrenEnabled) {

            mRectClip.setEmpty()
            mRectClip.right = width.toFloat()
            mRectClip.bottom = height.toFloat()
            mRectClip.padding(mlpPaddings)

            canvas?.save()
            if (mIsClipInPathChildren) {
                if (mIsPathClosureClipChildren) {
                    mPathClipChildren.reset()
                    mClosurePathClipChildren?.invoke(mRectClip, mPathClipChildren)
                }
                canvas?.clipPath(mPathClipChildren)
            }
            if (mIsClipOutPathChildren) {
                canvas?.restore()
                canvas?.save()
                if (mIsPathClosureClipChildren) {
                    mPathClipChildren.reset()
                    mClosurePathClipChildren?.invoke(mRectClip, mPathClipChildren)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) canvas?.clipOutPath(mPathClipChildren)
                else canvas?.clipPath(mPathClipChildren, Region.Op.DIFFERENCE)
            }

        }
        super.onDraw(canvas)

    }

    private var mIsClipAllEnabled = false
    private var mPathClipAll = Path()
    private var mIsClipInPathAll = false
    private var mIsClipOutPathAll = false
    private var mIsPathClosureClipAll = false
    private var mClosurePathClipAll : ((RectF, Path, JJPadding)->Unit)? = null
    @Suppress("DEPRECATION")
    override fun draw(canvas: Canvas) {
        //clip
        if(mIsClipAllEnabled) {

            mRectClip.setEmpty()

            mRectClip.right = width.toFloat()
            mRectClip.bottom = height.toFloat()

            canvas.save()
            if (mIsClipInPathAll) {
                if (mIsPathClosureClipAll) {
                    mPathClipAll.reset()
                    mClosurePathClipAll?.invoke(mRectClip, mPathClipAll,mlpPaddings)
                }
                canvas.clipPath(mPathClipAll)
            }
            if (mIsClipOutPathAll) {
                canvas.restore()
                canvas.save()
                if (mIsPathClosureClipAll) {
                    mPathClipAll.reset()
                    mClosurePathClipAll?.invoke(mRectClip, mPathClipAll,mlpPaddings)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) canvas.clipOutPath(mPathClipAll)
                else canvas.clipPath(mPathClipAll, Region.Op.DIFFERENCE)
            }

        }

        //all and riple
        super.draw(canvas)
        if (mAnimationRunning) {
            canvas.save()
            if (mRippleDuration <= mTimer * mFrameRate) {
                mAnimationRunning = false
                mTimer = 0
                durationEmpty = -1
                mTimerEmpty = 0
                // There is problem on Android M where canvas.restore() seems to be called automatically
                // For now, don't call canvas.restore() manually on Android M (API 23)
                if (Build.VERSION.SDK_INT != 23) {
                    canvas.restore()
                }
                invalidate()
                if (onCompletionListener != null) onCompletionListener!!.onComplete(this)
                return
            } else mCanvasHandler.postDelayed(runnable, mFrameRate.toLong())
            if (mTimer == 0) canvas.save()
            canvas.drawCircle(
                mX,
                mY,
                mRadiusMax * (mTimer.toFloat() * mFrameRate / mRippleDuration),
                mPaint
            )
            mPaint.color = Color.parseColor("#ffff4444")
            if (mRippleType == 1 && mOriginBitmap != null && mTimer.toFloat() * mFrameRate / mRippleDuration > 0.4f) {
                if (durationEmpty == -1) durationEmpty = mRippleDuration - mTimer * mFrameRate
                mTimerEmpty++
                val tmpBitmap: Bitmap? =
                    getCircleBitmap((mRadiusMax * (mTimerEmpty.toFloat() * mFrameRate / durationEmpty)).toInt())
                if(tmpBitmap != null)  canvas.drawBitmap(tmpBitmap, 0f, 0f, mPaint)

                tmpBitmap?.recycle()
            }
            mPaint.color = mRippleColor
            if (mRippleType == 1) {
                if (mTimer.toFloat() * mFrameRate / mRippleDuration > 0.6f) mPaint.alpha =
                    (mRippleAlpha - mRippleAlpha * (mTimerEmpty.toFloat() * mFrameRate / durationEmpty)).toInt() else mPaint.alpha =
                    mRippleAlpha
            } else mPaint.alpha =
                (mRippleAlpha - mRippleAlpha * (mTimer.toFloat() * mFrameRate / mRippleDuration)).toInt()
            mTimer++
        }


    }

    override fun setOnTouchListener(l: OnTouchListener?) {
       throw IllegalAccessException("Not Working, use instead ssSendTouchToChild(index: position added)")
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (gestureDetector.onTouchEvent(event)) {
            animateRipple(event)
        }
        if(mIsTouchChild) getChildAt(mIndexChild).onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (gestureDetector.onTouchEvent(event)) {
            animateRipple(event)
        }
        return super.onInterceptTouchEvent(event)
    }



    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWIDTH = w
        mHEIGHT = h
        mScaleAnimation = ScaleAnimation(
            1.0f, mZoomScale, 1.0f, mZoomScale,
            (w / 2).toFloat(),
            (h / 2).toFloat()
        )
        mScaleAnimation.duration = mZoomDuration.toLong()
        mScaleAnimation.repeatMode = Animation.REVERSE
        mScaleAnimation.repeatCount = 1

    }


    //endregion

    
    //region set and get
    private var mIndexChild = 0
    private var mIsTouchChild = false
    fun ssSendTouchToChild(index: Int): JJRippleWrapper {
        mIndexChild = index
        mIsTouchChild = true
        return this
    }


    fun ssClipChildrenToPath(path: Path): JJRippleWrapper {
        mPathClipChildren = path
        mIsPathClosureClipChildren = false
        mIsClipInPathChildren = true
        mIsClipChildrenEnabled = true
        mIsClipOutPathChildren = false
        return this
    }

    fun ssClipAllToPath(path: Path): JJRippleWrapper {
        mPathClipAll = path
        mIsPathClosureClipAll = false
        mIsClipInPathAll = true
        mIsClipAllEnabled = true
        mIsClipOutPathAll = false
        return this
    }


    fun ssClipOutChildrenToPath(path: Path): JJRippleWrapper {
        mPathClipChildren = path
        mIsPathClosureClipChildren = false
        mIsClipOutPathChildren = true
        mIsClipChildrenEnabled = true
        mIsClipInPathChildren = false
        return this
    }


    fun ssClipOutAllToPath(path: Path): JJRippleWrapper {
        mPathClipAll = path
        mIsPathClosureClipAll = false
        mIsClipOutPathAll = true
        mIsClipAllEnabled = true
        mIsClipInPathAll = false
        return this
    }

    fun ssClipChildrenToPath(closure:(RectF, Path)->Unit): JJRippleWrapper {
        mIsClipInPathChildren = true
        mIsPathClosureClipChildren = true
        mIsClipOutPathChildren = false
        mIsClipChildrenEnabled = true
        mClosurePathClipChildren = closure
        return this
    }

    fun ssClipAllToPath(closure:(RectF, Path, JJPadding)->Unit): JJRippleWrapper {
        mIsClipInPathAll = true
        mIsPathClosureClipAll = true
        mIsClipOutPathAll = false
        mIsClipAllEnabled = true
        mClosurePathClipAll = closure
        return this
    }

    fun ssClipOutChildrenToPath(closure:(RectF, Path)->Unit): JJRippleWrapper {
        mIsClipInPathChildren = false
        mIsPathClosureClipChildren = true
        mIsClipOutPathChildren = true
        mIsClipChildrenEnabled = true
        mClosurePathClipChildren = closure
        return this
    }

    fun ssClipOutAllToPath(closure:(RectF, Path, JJPadding)->Unit): JJRippleWrapper {
        mIsClipInPathAll = false
        mIsPathClosureClipAll = true
        mIsClipOutPathAll = true
        mIsClipAllEnabled = true
        mClosurePathClipAll = closure
        return this
    }

    fun disposeClipPathChildren(): JJRippleWrapper {
        mIsClipOutPathChildren = false
        mIsPathClosureClipChildren = false
        mIsClipChildrenEnabled = false
        mIsClipInPathChildren = false
        mClosurePathClipChildren = null
        return  this
    }
    fun disposeClipPathAll(): JJRippleWrapper {
        mIsClipOutPathAll = false
        mIsPathClosureClipAll = false
        mIsClipAllEnabled = false
        mIsClipInPathAll = false
        mClosurePathClipAll = null
        return  this
    }

    fun ggPadding() : JJPadding {
        return JJPadding(mlpPaddings.left,mlpPaddings.top,mlpPaddings.right,mlpPaddings.bottom)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun ssClipToOutline(boolean: Boolean) : JJRippleWrapper {
        clipToOutline = boolean
        return this
    }
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun ssClipBounds(bounds: Rect) : JJRippleWrapper {
        clipBounds = bounds
        return this
    }

    fun ssPadding(padding: JJPadding): JJRippleWrapper {
        mlpPaddings = padding
        setPaddingRelative(padding.left,padding.top,padding.right,padding.bottom)
        return this
    }

    fun ssRippleColorRes(resId: Int): JJRippleWrapper {
        mRippleColor = ContextCompat.getColor(context,resId)
        return this
    }
    fun ssRippleColor(color: Int): JJRippleWrapper {
        mRippleColor = color
        return this
    }

    fun ggRippleColor(): Int {
        return mRippleColor
    }

    fun ggRippleType(): Int {
        return mRippleType
    }

    fun ssRippleType(rippleType: Int) : JJRippleWrapper {
        this.mRippleType = rippleType
        return this
    }

    fun isCentered(): Boolean {
        return mIsCentered
    }

    /**
     * Set if ripple animation has to be centered in its parent view or not, default is False
     *
     * @param isCentered
     */
    fun ssCentered(isCentered: Boolean) : JJRippleWrapper {
        mIsCentered = isCentered
        return this
    }

    fun ggRipplePadding(): Int {
        return mRipplePadding
    }

    /**
     * Set Ripple padding if you want to avoid some graphic glitch
     *
     * @param ripplePadding New Ripple padding in pixel, default is 0px
     */
    fun ssRipplePadding(ripplePadding: Int) : JJRippleWrapper {
        mRipplePadding = ripplePadding
        return this
    }

    fun isZooming(): Boolean {
        return mHasToZoom
    }

    /**
     * At the end of Ripple effect, the child views has to zoom
     *
     * @param hasToZoom Do the child views have to zoom ? default is False
     */
    fun ssZooming(hasToZoom: Boolean): JJRippleWrapper {
        mHasToZoom = hasToZoom
        return this
    }

    fun ggZoomScale(): Float {
        return mZoomScale
    }

    /**
     * Scale of the end animation
     *
     * @param zoomScale Value of scale animation, default is 1.03f
     */
    fun ssZoomScale(scale: Float): JJRippleWrapper {
        mZoomScale = scale
        return this
    }

    fun ggZoomDuration(): Int {
        return mZoomDuration
    }

    /**
     * Zoom
     * Duration of the ending animation in ms
     *
     * @param duration default is 200ms
     */
    fun ssZoomDuration(duration: Int) : JJRippleWrapper {
        mZoomDuration = duration
        return this
    }

    fun ggRippleDuration(): Int {
        return mRippleDuration
    }

    /**
     * Duration of the Ripple animation in ms
     *
     * @param duration Duration, default is 400ms
     */
    fun ssRippleDuration(duration: Int): JJRippleWrapper {
        mRippleDuration = duration
        return this
    }
    
    fun ggFrameRate(): Int {
        return mFrameRate
    }

    /**
     * Set framerate for Ripple animation
     *
     * @param rate New framerate value, default is 10
     */
    fun ssFrameRate(rate: Int) : JJRippleWrapper {
        mFrameRate = rate
        return this
    }

    fun ggRippleAlpha(): Int {
        return mRippleAlpha
    }

    /**
     * Set alpha for ripple effect color
     *
     * @param alpha Alpha value between 0 and 255, default is 90
     */
    fun ssRippleAlpha(alpha: Int) : JJRippleWrapper {
        mRippleAlpha = alpha
        return this
    }

    fun ssRippleRadius(radius: Float): JJRippleWrapper {
        mRadiusMax = radius
        return this
    }

    fun ggRippleRadius(): Float {
        return mRadiusMax
    }

    fun ssOnRippleCompleteListener(listener: OnRippleCompleteListener) : JJRippleWrapper {
        onCompletionListener = listener
        return this
    }

    fun diposeOnRippleCompleteListener() : JJRippleWrapper {
        onCompletionListener = null
        return this
    }


    //region private
    @Suppress("DEPRECATION")
    private fun getSnapShootView(): Bitmap {
        val location = IntArray(2)
        var bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try{
                getLocationInWindow(location)
                PixelCopy.request(
                    (context as Activity).window,
                    Rect(location[0], location[1], location[0] + width, location[1] + height),
                    bitmap,
                    { copyResult ->
                        if(copyResult== PixelCopy.SUCCESS) Log.e("ICESOUL","CORRECTO PIXEL COpu")
                    },
                    Handler())
            } catch (e: Exception) {
                // PixelCopy may throw IllegalArgumentException, make sure to handle it
                e.printStackTrace()
            }
            return bitmap
        }
        bitmap = getDrawingCache(true)
        return bitmap
    }


    /**
     * Launch Ripple animation for the current view with a MotionEvent
     *
     * @param event MotionEvent registered by the Ripple gesture listener
     */
    private fun animateRipple(event: MotionEvent) {
        createAnimation(event.x, event.y)
    }

    /**
     * Launch Ripple animation for the current view centered at x and y position
     *
     * @param x Horizontal position of the ripple center
     * @param y Vertical position of the ripple center
     */
    private fun animateRipple(x: Float, y: Float) {
        createAnimation(x, y)
    }

    /**
     * Create Ripple animation centered at x, y
     *
     * @param x Horizontal position of the ripple center
     * @param y Vertical position of the ripple center
     */
    private  fun createAnimation(x: Float, y: Float) {
        val savedRadius = mRadiusMax
        if (isEnabled && !mAnimationRunning) {
            if (mHasToZoom) this.startAnimation(mScaleAnimation)
            mRadiusMax = max(mWIDTH, mHEIGHT).toFloat()
            if (mRippleType != 2) mRadiusMax /= 2f
            mRadiusMax -= mRipplePadding.toFloat()
            if (mIsCentered || mRippleType == 1) {
                mX = measuredWidth / 2.toFloat()
                mY = measuredHeight / 2.toFloat()
            } else {
                mX = x
                mY = y
            }
            if(mRippleType == 0 && savedRadius == 0f) mRadiusMax = min(mWIDTH, mHEIGHT).toFloat()
            if(mRippleType == 0 && savedRadius > 0f) mRadiusMax = savedRadius
            mAnimationRunning = true
            if (mRippleType == 1 && mOriginBitmap == null) mOriginBitmap = getSnapShootView()
            invalidate()
        }
    }



    private fun getCircleBitmap(radius: Int): Bitmap? {
        if(mOriginBitmap != null) {
            val output = Bitmap.createBitmap(
                mOriginBitmap!!.width,
                mOriginBitmap!!.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(output)
            val paint = Paint()
            val rect = Rect(
                (mX - radius).toInt(),
                (mY - radius).toInt(),
                (mX + radius).toInt(),
                (mY + radius).toInt()
            )
            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            canvas.drawCircle(mX, mY, radius.toFloat(), paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(mOriginBitmap!!, rect, rect, paint)
            return output
        }
        return null
    }


    //endregion

    //endregion

    /**
     * Defines a callback called at the end of the Ripple effect
     */
    interface OnRippleCompleteListener {
        fun onComplete(JJRippleWrapper: JJRippleWrapper)
    }

    //region CoordinatorLayout params

    private var mCol: CoordinatorLayout.LayoutParams? = null
    private fun setupCol() {
        if (mCol == null) {
            mCol = layoutParams as?  CoordinatorLayout.LayoutParams
            layoutParams = mCol
        }
    }

    fun colWidth(width: Int): JJRippleWrapper {
        setupCol()
        mCol!!.width = width
        return this
    }

    fun colHeight(height: Int): JJRippleWrapper {
        setupCol()
        mCol!!.height = height
        return this
    }

    fun colGravity(gravity: Int): JJRippleWrapper {
        setupCol()
        mCol!!.gravity = gravity
        return this
    }

    fun colBehavior(behavior: AppBarLayout.Behavior){
        setupCol()
        mCol!!.behavior = behavior
    }

    //endregion

    //region AppBarLayout Params
    private var ablp : AppBarLayout.LayoutParams? = null
    private fun setupAblp(){
        if(ablp == null) {
            ablp = layoutParams as? AppBarLayout.LayoutParams
            layoutParams = ablp
        }
    }

     fun ablWidth(width: Int): JJRippleWrapper {
        setupAblp()
        ablp!!.width = width
        return this
    }

     fun ablHeight(height: Int): JJRippleWrapper {
        setupAblp()
        ablp!!.height = height
        return this
    }

     fun ablScrollFlags(flags: Int) : JJRippleWrapper {
        setupAblp()
        ablp!!.scrollFlags = flags
        return this
    }

     fun ablScrollInterpolator(interpolator: Interpolator) : JJRippleWrapper {
        setupAblp()
        ablp!!.scrollInterpolator = interpolator
        return this
    }

     fun ablMargins(margins: JJMargin): JJRippleWrapper {
        setupAblp()
        ablp!!.updateMarginsRelative(margins.left,margins.top,margins.right,margins.bottom)
        return this
    }

    //endregion

    //region RelativeLayout Params

    private var mRlp: RelativeLayout.LayoutParams? = null

    private fun setupRlp(){
        if(mRlp == null) {
            mRlp = layoutParams as? RelativeLayout.LayoutParams
            layoutParams = mRlp
        }
    }

    fun rlWidth(width: Int): JJRippleWrapper {
        setupRlp()
        mRlp!!.width = width
        return this
    }

    fun rlHeight(height: Int): JJRippleWrapper {
        setupRlp()
        mRlp!!.height = height
        return this
    }

    fun rlAbove(viewId: Int): JJRippleWrapper {
        setupRlp()
        mRlp!!.addRule(RelativeLayout.ABOVE,viewId)
        return this
    }

    fun rlBelow(viewId: Int): JJRippleWrapper {
        setupRlp()
        mRlp!!.addRule(RelativeLayout.BELOW,viewId)
        return this
    }

    fun rlAlignParentBottom(value : Boolean = true): JJRippleWrapper {
        setupRlp()
        val data = if(value) 1 else 0
        mRlp!!.addRule(ALIGN_PARENT_BOTTOM,data)
        return this
    }

    fun rlAlignParentTop(value : Boolean = true): JJRippleWrapper {
        setupRlp()
        val data = if(value) 1 else 0
        mRlp!!.addRule(ALIGN_PARENT_TOP,data)
        return this
    }

    fun rlAlignParentStart(value : Boolean = true): JJRippleWrapper {
        setupRlp()
        val data = if(value) 1 else 0
        mRlp!!.addRule(ALIGN_PARENT_START,data)
        return this
    }

    fun rlAlignParentEnd(value : Boolean = true): JJRippleWrapper {
        setupRlp()
        val data = if(value) 1 else 0
        mRlp!!.addRule(ALIGN_PARENT_END,data)
        return this
    }

    fun rlAlignParentLeft(value : Boolean = true): JJRippleWrapper {
        setupRlp()
        val data = if(value) 1 else 0
        mRlp!!.addRule(ALIGN_PARENT_LEFT,data)
        return this
    }

    fun rlAlignParentRight(value : Boolean = true): JJRippleWrapper {
        setupRlp()
        val data = if(value) 1 else 0
        mRlp!!.addRule(ALIGN_PARENT_RIGHT,data)
        return this
    }

    fun rlAlignEnd(viewId: Int): JJRippleWrapper {
        setupRlp()
        mRlp!!.addRule(ALIGN_END,viewId)
        return this
    }

    fun rlAlignStart(viewId: Int): JJRippleWrapper {
        setupRlp()
        mRlp!!.addRule(ALIGN_START,viewId)
        return this
    }

    fun rlAlignTop(viewId: Int): JJRippleWrapper {
        setupRlp()
        mRlp!!.addRule(ALIGN_TOP,viewId)
        return this
    }

    fun rlAlignBottom(viewId: Int): JJRippleWrapper {
        setupRlp()
        mRlp!!.addRule(ALIGN_BOTTOM,viewId)
        return this
    }


    fun rlAlignLeft(viewId: Int): JJRippleWrapper {
        setupRlp()
        mRlp!!.addRule(ALIGN_LEFT,viewId)
        return this
    }

    fun rlAlignRight(viewId: Int): JJRippleWrapper {
        setupRlp()
        mRlp!!.addRule(ALIGN_RIGHT,viewId)
        return this
    }

    fun rlRightToLeft(viewId: Int): JJRippleWrapper {
        setupRlp()
        mRlp!!.addRule(LEFT_OF,viewId)
        return this
    }

    fun rlLeftToRight(viewId: Int): JJRippleWrapper {
        setupRlp()
        mRlp!!.addRule(RIGHT_OF,viewId)
        return this
    }

    fun rlStartToEnd(viewId: Int): JJRippleWrapper {
        setupRlp()
        mRlp!!.addRule(END_OF,viewId)
        return this
    }

    fun rlEndToStart(viewId: Int): JJRippleWrapper {
        setupRlp()
        mRlp!!.addRule(START_OF,viewId)
        return this
    }

    fun rlCenterInParent(value:Boolean = true): JJRippleWrapper {
        setupRlp()
        val data = if(value) 1 else 0
        mRlp!!.addRule(CENTER_IN_PARENT,data)
        return this
    }

    fun rlCenterInParentVertically(value:Boolean = true): JJRippleWrapper {
        setupRlp()
        val data = if(value) 1 else 0
        mRlp!!.addRule(CENTER_VERTICAL,data)
        return this
    }

    fun rlCenterInParentHorizontally(value:Boolean = true): JJRippleWrapper {
        setupRlp()
        val data = if(value) 1 else 0
        mRlp!!.addRule(CENTER_HORIZONTAL,data)
        return this
    }

    fun rlAlignBaseline(viewId: Int): JJRippleWrapper {
        setupRlp()
        mRlp!!.addRule(ALIGN_BASELINE,viewId)
        return this
    }

    fun rlMargins(margins: JJMargin): JJRippleWrapper {
        setupRlp()
        mRlp!!.setMargins(margins.left,margins.top,margins.right,margins.bottom)
        return this
    }

    //endregion

    //region MotionLayout Params

    var mMotionConstraintSet: ConstraintSet? = null


    fun mlVisibilityMode(visibility: Int): JJRippleWrapper {
        mMotionConstraintSet?.setVisibilityMode(id, visibility)
        return this
    }

    fun mlVerticalBias(float: Float): JJRippleWrapper {
        mMotionConstraintSet?.setVerticalBias(id,float)
        return this
    }
    fun mlHorizontalBias(float: Float): JJRippleWrapper {
        mMotionConstraintSet?.setHorizontalBias(id,float)
        return this
    }

    fun mlCenterHorizontallyOf(viewId: Int, marginStart: Int = 0, marginEnd: Int = 0): JJRippleWrapper {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.START, marginStart)
        mMotionConstraintSet?.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.END, marginEnd)
        mMotionConstraintSet?.setHorizontalBias(viewId,0.5f)
        return this
    }
    fun mlCenterVerticallyOf(viewId: Int,marginTop: Int = 0, marginBottom: Int = 0): JJRippleWrapper {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, marginTop)
        mMotionConstraintSet?.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, marginBottom)
        mMotionConstraintSet?.setVerticalBias(viewId,0.5f)
        return this
    }

    fun mlMargins(margins: JJMargin) : JJRippleWrapper {
        mMotionConstraintSet?.setMargin(id, ConstraintSet.TOP,margins.top)
        mMotionConstraintSet?.setMargin(id, ConstraintSet.BOTTOM,margins.bottom)
        mMotionConstraintSet?.setMargin(id, ConstraintSet.END,margins.right)
        mMotionConstraintSet?.setMargin(id, ConstraintSet.START,margins.left)
        return this
    }


    fun mlFloatCustomAttribute(attrName: String, value: Float): JJRippleWrapper {
        mMotionConstraintSet?.setFloatValue(id,attrName,value)
        return this
    }

    fun mlIntCustomAttribute(attrName: String, value: Int): JJRippleWrapper {
        mMotionConstraintSet?.setIntValue(id,attrName,value)
        return this
    }

    fun mlColorCustomAttribute(attrName: String, value: Int): JJRippleWrapper {
        mMotionConstraintSet?.setColorValue(id,attrName,value)
        return this
    }

    fun mlStringCustomAttribute(attrName: String, value: String): JJRippleWrapper {
        mMotionConstraintSet?.setStringValue(id,attrName,value)
        return this
    }

    fun mlRotation(float: Float): JJRippleWrapper {
        mMotionConstraintSet?.setRotation(id,float)
        return this
    }

    fun mlRotationX(float: Float): JJRippleWrapper {
        mMotionConstraintSet?.setRotationX(id,float)
        return this
    }

    fun mlRotationY(float: Float): JJRippleWrapper {
        mMotionConstraintSet?.setRotationY(id,float)
        return this
    }

    fun mlTranslation(x: Float,y: Float): JJRippleWrapper {
        mMotionConstraintSet?.setTranslation(id,x,y)
        return this
    }
    fun mlTranslationX(x: Float): JJRippleWrapper {
        mMotionConstraintSet?.setTranslationX(id,x)
        return this
    }

    fun mlTranslationY(y: Float): JJRippleWrapper {
        mMotionConstraintSet?.setTranslationY(id,y)
        return this
    }

    fun mlTranslationZ(z: Float): JJRippleWrapper {
        mMotionConstraintSet?.setTranslationZ(id,z)
        return this
    }

    fun mlTransformPivot(x: Float, y: Float): JJRippleWrapper {
        mMotionConstraintSet?.setTransformPivot(id,x,y)
        return this
    }

    fun mlTransformPivotX(x: Float): JJRippleWrapper {
        mMotionConstraintSet?.setTransformPivotX(id,x)
        return this
    }

    fun mlTransformPivotY(y: Float): JJRippleWrapper {
        mMotionConstraintSet?.setTransformPivotY(id,y)
        return this
    }

    fun mlScaleX(x: Float): JJRippleWrapper {
        mMotionConstraintSet?.setScaleX(id,x)
        return this
    }

    fun mlScaleY(y: Float): JJRippleWrapper {
        mMotionConstraintSet?.setScaleY(id,y)
        return this
    }

    fun mlDimensionRatio(ratio: String): JJRippleWrapper {
        mMotionConstraintSet?.setDimensionRatio(id,ratio)
        return this
    }

    fun mlAlpha(alpha: Float): JJRippleWrapper {
        mMotionConstraintSet?.setAlpha(id,alpha)
        return this
    }



    fun mlTopToTop(viewId: Int, margin: Int = 0): JJRippleWrapper {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, margin)
        return this
    }

    fun mlTopToTopParent(margin: Int = 0): JJRippleWrapper {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin)
        return this
    }


    fun mlTopToBottomOf(viewId: Int, margin: Int = 0): JJRippleWrapper {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun mlTopToBottomParent(margin: Int = 0): JJRippleWrapper {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun mlBottomToTopOf(viewId: Int, margin: Int = 0): JJRippleWrapper {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.TOP, margin)

        return this
    }

    fun mlBottomToTopParent(margin: Int = 0): JJRippleWrapper {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin)

        return this
    }

    fun mlBottomToBottomOf(viewId: Int, margin: Int = 0): JJRippleWrapper {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, margin)

        return this
    }

    fun mlBottomToBottomParent(margin: Int = 0): JJRippleWrapper {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin)

        return this
    }

    fun mlStartToStartOf(viewId: Int, margin: Int = 0): JJRippleWrapper {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.START, margin)

        return this
    }

    fun mlStartToStartParent(margin: Int = 0): JJRippleWrapper {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin)

        return this
    }

    fun mlStartToEndOf(viewId: Int, margin: Int = 0): JJRippleWrapper {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.END, margin)

        return this
    }

    fun mlStartToEndParent(margin: Int = 0): JJRippleWrapper {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END, margin)

        return this
    }

    fun mlEndToEndOf(viewId: Int, margin: Int= 0): JJRippleWrapper {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.END, margin)

        return this
    }

    fun mlEndToEndParent(margin: Int = 0): JJRippleWrapper {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin)

        return this
    }


    fun mlEndToStartOf(viewId: Int, margin: Int = 0): JJRippleWrapper {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.START, margin)
        return this
    }

    fun mlEndToStartParent(margin: Int = 0): JJRippleWrapper {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START, margin)
        return this
    }


    fun mlWidth(width: Int): JJRippleWrapper {
        mMotionConstraintSet?.constrainWidth(id, width)
        return this
    }

    fun mlHeight(height: Int): JJRippleWrapper {
        mMotionConstraintSet?.constrainHeight(id, height)
        return this
    }

    fun mlPercentWidth(width: Float): JJRippleWrapper {
        mMotionConstraintSet?.constrainPercentWidth(id, width)
        return this
    }

    fun mlPercentHeight(height: Float): JJRippleWrapper {
        mMotionConstraintSet?.constrainPercentHeight(id, height)
        return this
    }

    fun mlCenterInParent(): JJRippleWrapper {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)

        return this
    }

    fun mlCenterInParent(verticalBias: Float, horizontalBias: Float, margin: JJMargin): JJRippleWrapper {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin.left)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin.right)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin.top)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin.bottom)
        mMotionConstraintSet?.setVerticalBias(id, verticalBias)
        mMotionConstraintSet?.setHorizontalBias(id, horizontalBias)
        return this
    }

    fun mlCenterInParentVertically(): JJRippleWrapper {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)

        return this
    }

    fun mlCenterInParentHorizontally(): JJRippleWrapper {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)
        return this
    }

    fun mlCenterInParentVertically(bias: Float, topMargin: Int, bottomMargin: Int): JJRippleWrapper {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
        mMotionConstraintSet?.setVerticalBias(id, bias)
        return this
    }

    fun mlCenterInParentHorizontally(bias: Float, startMargin: Int, endtMargin: Int): JJRippleWrapper {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, startMargin)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, endtMargin)
        mMotionConstraintSet?.setHorizontalBias(id, bias)
        return this
    }


    fun mlCenterInParentTopVertically(): JJRippleWrapper {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)
        return this
    }


    fun mlCenterInParentBottomVertically(): JJRippleWrapper {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)
        return this
    }

    fun mlCenterInParentStartHorizontally(): JJRippleWrapper {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)
        return this
    }

    fun mlCenterInParentEndHorizontally(): JJRippleWrapper {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)
        return this
    }

    fun mlCenterInTopVerticallyOf(viewId: Int): JJRippleWrapper {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, viewId, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)
        return this
    }


    fun mlCenterInBottomVerticallyOf(viewId: Int): JJRippleWrapper {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, viewId, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)
        return this
    }

    fun mlCenterInStartHorizontallyOf(viewId: Int): JJRippleWrapper {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, viewId, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, viewId, ConstraintSet.START, 0)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)
        return this
    }

    fun mlCenterInEndHorizontallyOf(viewId: Int): JJRippleWrapper {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, viewId, ConstraintSet.END, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, viewId, ConstraintSet.END, 0)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)
        return this
    }

    fun mlCenterVertically(topId: Int, topSide: Int, topMargin: Int, bottomId: Int, bottomSide: Int, bottomMargin: Int, bias: Float): JJRippleWrapper {
        mMotionConstraintSet?.centerVertically(id, topId, topSide, topMargin, bottomId, bottomSide, bottomMargin, bias)
        return this
    }

    fun mlCenterHorizontally(startId: Int, startSide: Int, startMargin: Int, endId: Int, endSide: Int, endMargin: Int, bias: Float): JJRippleWrapper {
        mMotionConstraintSet?.centerHorizontally(id, startId, startSide, startMargin, endId, endSide, endMargin, bias)
        return this
    }


    fun mlFillParent(): JJRippleWrapper {
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.constrainHeight(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        return this
    }

    fun mlFillParent(margin: JJMargin): JJRippleWrapper {
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.constrainHeight(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin.top)
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin.left)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin.right)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin.bottom)
        return this
    }

    fun mlFillParentHorizontally(): JJRippleWrapper {
        mConstraintSet.constrainWidth(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        return this
    }

    fun mlFillParentVertically(): JJRippleWrapper {
        mConstraintSet.constrainHeight(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        return this
    }

    fun mlFillParentHorizontally(startMargin: Int, endMargin: Int): JJRippleWrapper {
        mConstraintSet.constrainWidth(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, startMargin)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, endMargin)
        return this
    }

    fun mlFillParentVertically(topMargin: Int, bottomMargin: Int): JJRippleWrapper {
        mConstraintSet.constrainHeight(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
        return this
    }

    fun mlVisibility(visibility: Int): JJRippleWrapper {
        mMotionConstraintSet?.setVisibility(id, visibility)
        return this
    }

    fun mlElevation(elevation: Float): JJRippleWrapper {
        mMotionConstraintSet?.setElevation(id, elevation)
        return this
    }

    fun mlApply(): JJRippleWrapper {
        mMotionConstraintSet?.applyTo(parent as ConstraintLayout)
        return this
    }

    fun mlSetConstraint(cs : ConstraintSet?): JJRippleWrapper {
        mMotionConstraintSet = cs
        return this
    }

    fun mlDisposeConstraint(): JJRippleWrapper {
        mMotionConstraintSet = null
        return this
    }

    //endregion

    //region ConstraintLayout Params
    private val mConstraintSet = ConstraintSet()

    fun clApply(): JJRippleWrapper {
        mConstraintSet.applyTo(parent as ConstraintLayout)
        return this
    }

    fun clVisibilityMode(visibility: Int): JJRippleWrapper {
        mConstraintSet.setVisibilityMode(id, visibility)
        return this
    }

    fun clVerticalBias(float: Float): JJRippleWrapper {
        mConstraintSet.setVerticalBias(id,float)
        return this
    }
    fun clHorizontalBias(float: Float): JJRippleWrapper {
        mConstraintSet.setHorizontalBias(id,float)
        return this
    }

    fun clCenterHorizontallyOf(viewId: Int, marginStart: Int = 0, marginEnd: Int = 0): JJRippleWrapper {
        mConstraintSet.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.START, marginStart)
        mConstraintSet.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.END, marginEnd)
        mConstraintSet.setHorizontalBias(id,0.5f)
        return this
    }
    fun clCenterVerticallyOf(viewId: Int,marginTop: Int = 0, marginBottom: Int = 0): JJRippleWrapper {
        mConstraintSet.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, marginTop)
        mConstraintSet.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, marginBottom)
        mConstraintSet.setVerticalBias(id,0.5f)
        return this
    }

    fun clMargins(margins: JJMargin) : JJRippleWrapper {
        mConstraintSet.setMargin(id, ConstraintSet.TOP,margins.top)
        mConstraintSet.setMargin(id, ConstraintSet.BOTTOM,margins.bottom)
        mConstraintSet.setMargin(id, ConstraintSet.END,margins.right)
        mConstraintSet.setMargin(id, ConstraintSet.START,margins.left)
        return this
    }


    fun clTopToTop(viewId: Int, margin: Int = 0): JJRippleWrapper {
        mConstraintSet.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, margin)
        return this
    }

    fun clTopToTopParent(margin: Int = 0): JJRippleWrapper {
        mConstraintSet.connect(this.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin)
        return this
    }


    fun clTopToBottom(viewId: Int, margin: Int = 0): JJRippleWrapper {
        mConstraintSet.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun clTopToBottomParent(margin: Int = 0): JJRippleWrapper {
        mConstraintSet.connect(this.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun clBottomToTop(viewId: Int, margin: Int = 0): JJRippleWrapper {
        mConstraintSet.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.TOP, margin)
        return this
    }

    fun clBottomToTopParent(margin: Int = 0): JJRippleWrapper {
        mConstraintSet.connect(this.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin)
        return this
    }

    fun clBottomToBottom(viewId: Int, margin: Int = 0): JJRippleWrapper {
        mConstraintSet.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun clBottomToBottomParent(margin: Int = 0): JJRippleWrapper {
        mConstraintSet.connect(this.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun clStartToStart(viewId: Int, margin: Int = 0): JJRippleWrapper {
        mConstraintSet.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.START, margin)
        return this
    }

    fun clStartToStartParent(margin: Int = 0): JJRippleWrapper {
        mConstraintSet.connect(this.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin)
        return this
    }

    fun clStartToEnd(viewId: Int, margin: Int = 0): JJRippleWrapper {
        mConstraintSet.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.END, margin)
        return this
    }

    fun clStartToEndParent(margin: Int = 0): JJRippleWrapper {
        mConstraintSet.connect(this.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END, margin)
        return this
    }

    fun clEndToEnd(viewId: Int, margin: Int = 0): JJRippleWrapper {
        mConstraintSet.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.END, margin)
        return this
    }

    fun clEndToEndParent(margin: Int = 0): JJRippleWrapper {
        mConstraintSet.connect(this.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin)
        return this
    }


    fun clEndToStart(viewId: Int, margin: Int = 0): JJRippleWrapper {
        mConstraintSet.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.START, margin)
        return this
    }

    fun clEndToStartParent(margin: Int = 0): JJRippleWrapper {
        mConstraintSet.connect(this.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START, margin)
        return this
    }


    fun clWidth(width: Int): JJRippleWrapper {
        mConstraintSet.constrainWidth(id, width)
        return this
    }

    fun clHeight(height: Int): JJRippleWrapper {
        mConstraintSet.constrainHeight(id, height)
        return this
    }

    fun clPercentWidth(width: Float): JJRippleWrapper {
        mConstraintSet.constrainPercentWidth(id, width)
        return this
    }

    fun clPercentHeight(height: Float): JJRippleWrapper {
        mConstraintSet.constrainPercentHeight(id, height)
        return this
    }

    fun clCenterInParent(): JJRippleWrapper {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterInParent(verticalBias: Float, horizontalBias: Float, margin: JJMargin): JJRippleWrapper {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin.left)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin.right)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin.top)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin.bottom)
        mConstraintSet.setVerticalBias(id, verticalBias)
        mConstraintSet.setHorizontalBias(id, horizontalBias)
        return this
    }

    fun clCenterInParentVertically(): JJRippleWrapper {
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        return this
    }

    fun clCenterInParentHorizontally(): JJRippleWrapper {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterInParentVertically(bias: Float, topMargin: Int, bottomMargin: Int): JJRippleWrapper {
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
        mConstraintSet.setVerticalBias(id, bias)
        return this
    }

    fun clCenterInParentHorizontally(bias: Float, startMargin: Int, endtMargin: Int): JJRippleWrapper {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, startMargin)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, endtMargin)
        mConstraintSet.setHorizontalBias(id, bias)
        return this
    }


    fun clCenterInParentTopVertically(): JJRippleWrapper {
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        return this
    }


    fun clCenterInParentBottomVertically(): JJRippleWrapper {
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        return this
    }

    fun clCenterInParentStartHorizontally(): JJRippleWrapper {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterInParentEndHorizontally(): JJRippleWrapper {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterInTopVertically(topId: Int): JJRippleWrapper {
        mConstraintSet.connect(id, ConstraintSet.TOP, topId, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, topId, ConstraintSet.TOP, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        return this
    }


    fun clCenterInBottomVertically(bottomId: Int): JJRippleWrapper {
        mConstraintSet.connect(id, ConstraintSet.TOP, bottomId, ConstraintSet.BOTTOM, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, bottomId, ConstraintSet.BOTTOM, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        return this
    }

    fun clCenterInStartHorizontally(startId: Int): JJRippleWrapper {
        mConstraintSet.connect(id, ConstraintSet.START, startId, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, startId, ConstraintSet.START, 0)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterInEndHorizontally(endId: Int): JJRippleWrapper {
        mConstraintSet.connect(id, ConstraintSet.START, endId, ConstraintSet.END, 0)
        mConstraintSet.connect(id, ConstraintSet.END, endId, ConstraintSet.END, 0)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterVertically(topId: Int, topSide: Int, topMargin: Int, bottomId: Int, bottomSide: Int, bottomMargin: Int, bias: Float): JJRippleWrapper {
        mConstraintSet.centerVertically(id, topId, topSide, topMargin, bottomId, bottomSide, bottomMargin, bias)
        return this
    }

    fun clCenterHorizontally(startId: Int, startSide: Int, startMargin: Int, endId: Int, endSide: Int, endMargin: Int, bias: Float): JJRippleWrapper {
        mConstraintSet.centerHorizontally(id, startId, startSide, startMargin, endId, endSide, endMargin, bias)
        return this
    }


    fun clFillParent(): JJRippleWrapper {
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.constrainHeight(id,0)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        return this
    }

    fun clFillParent(margin: JJMargin): JJRippleWrapper {
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.constrainHeight(id,0)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin.top)
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin.left)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin.right)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin.bottom)
        return this
    }

    fun clFillParentHorizontally(): JJRippleWrapper {
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        return this
    }

    fun clFillParentVertically(): JJRippleWrapper {
        mConstraintSet.constrainHeight(id,0)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        return this
    }

    fun clFillParentHorizontally(startMargin: Int, endMargin: Int): JJRippleWrapper {
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, startMargin)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, endMargin)
        return this
    }

    fun clFillParentVertically(topMargin: Int, bottomMargin: Int): JJRippleWrapper {
        mConstraintSet.constrainHeight(id,0)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
        return this
    }

    fun clVisibility(visibility: Int): JJRippleWrapper {
        mConstraintSet.setVisibility(id, visibility)
        return this
    }



    fun clElevation(elevation: Float): JJRippleWrapper {
        mConstraintSet.setElevation(id, elevation)

        return this
    }

    fun clGetConstraint() : ConstraintSet {
        return mConstraintSet
    }

    fun clMinWidth(w:Int): JJRippleWrapper {
        mConstraintSet.constrainMinWidth(id,w)
        return this
    }

    fun clMinHeight(h:Int): JJRippleWrapper {
        mConstraintSet.constrainMinHeight(id,h)
        return this
    }

    fun clMaxWidth(w:Int): JJRippleWrapper {
        mConstraintSet.constrainMaxWidth(id,w)
        return this
    }

    fun clMaxHeight(h:Int): JJRippleWrapper {
        mConstraintSet.constrainMaxHeight(id,h)
        return this
    }






    //endregion

    //region LinearLayout Params

    private var mLlp: LinearLayout.LayoutParams? = null
    private fun setupLlp() {
        if (mLlp == null) {
            mLlp = layoutParams as? LinearLayout.LayoutParams
            layoutParams = mLlp
        }
    }

    fun llWidth(width: Int): JJRippleWrapper {
        setupLlp()
        mLlp!!.width = width
        return this
    }

    fun llHeight(height: Int): JJRippleWrapper {
        setupLlp()
        mLlp!!.height = height
        return this
    }

    fun llWeight(weigth: Float): JJRippleWrapper {
        setupLlp()
        mLlp!!.weight = weigth
        return this
    }

    fun llGravity(gravity: Int): JJRippleWrapper {
        setupLlp()
        mLlp!!.gravity = gravity
        return this
    }

    fun llTopMargin(m : Int): JJRippleWrapper {
        setupLlp()
        mLlp!!.topMargin = m
        return this
    }

    fun llBottomMargin(m : Int): JJRippleWrapper {
        setupLlp()
        mLlp!!.bottomMargin = m
        return this
    }

    fun llStartMargin(m : Int): JJRippleWrapper {
        setupLlp()
        mLlp!!.marginStart = m
        return this
    }

    fun llEndMargin(m : Int): JJRippleWrapper {
        setupLlp()
        mLlp!!.marginEnd = m
        return this
    }

    fun llLeftMargin(m : Int): JJRippleWrapper {
        setupLlp()
        mLlp!!.leftMargin = m
        return this
    }

    fun llRightMargin(m : Int): JJRippleWrapper {
        setupLlp()
        mLlp!!.rightMargin = m
        return this
    }


    fun llMargins( margins : JJMargin): JJRippleWrapper {
        setupLlp()
        mLlp!!.setMargins(margins.left,margins.top,margins.right,margins.bottom)
        return this
    }




    //endregion

    //region ScrollView Params

    private var mSvp: FrameLayout.LayoutParams? = null

    private fun setupSvp() {
        if (mSvp == null) {
            mSvp = layoutParams as? FrameLayout.LayoutParams
            layoutParams = mSvp
        }
    }

    fun svWidth(width: Int): JJRippleWrapper {
        setupSvp()
        mSvp!!.width = width
        return this
    }

    fun svHeight(height: Int): JJRippleWrapper {
        setupSvp()
        mSvp!!.height = height
        return this
    }


    fun svMargins( margins : JJMargin): JJRippleWrapper {
        setupSvp()
        mSvp!!.setMargins(margins.left,margins.top,margins.right,margins.bottom)
        return this
    }
    //endregion


}
