package com.jjlf.library_layout.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.jjlf.library_layout.JJMargin
import com.jjlf.library_layout.JJPadding
import com.jjlf.library_layout.JJScreen
import com.jjlf.library_layout.R

class JJCoordinatorLayout : CoordinatorLayout {

    constructor(context: Context): super(context){
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.constrainHeight(id,0)
        id = View.generateViewId()
        mInit = false
    }

    private var mIgnoreCl = false
    @SuppressLint("ResourceType")
    constructor(context: Context, attrs: AttributeSet?):super(context,attrs){
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

        val attrId = ba.getResourceId(0,View.NO_ID)
        if(attrId == View.NO_ID) id = View.generateViewId()

        ba.recycle()

        val a = context.obtainStyledAttributes(attrs,
            R.styleable.JJCoordinatorLayout, 0, 0)

        mIgnoreCl = a.getBoolean(R.styleable.JJCoordinatorLayout_layout_ignoreCl,false)

        val aFillParent = a.getBoolean(R.styleable.JJCoordinatorLayout_clFillParent,false)
        val aFillParentHorizontal = a.getBoolean(R.styleable.JJCoordinatorLayout_clFillParentHorizontally,false)
        val aFillParentVertical = a.getBoolean(R.styleable.JJCoordinatorLayout_clFillParentVertically,false)

        val aCenterInParent = a.getBoolean(R.styleable.JJCoordinatorLayout_clCenterInParent,false)
        val aCenterInParentHorizontal = a.getBoolean(R.styleable.JJCoordinatorLayout_clCenterInParentHorizontally,false)
        val aCenterInParentVertical = a.getBoolean(R.styleable.JJCoordinatorLayout_clCenterInParentVertically,false)
        val aCenterInParentTopVertical = a.getBoolean(R.styleable.JJCoordinatorLayout_clCenterInParentTopVertically,false)
        val aCenterInParentBottomVertical = a.getBoolean(R.styleable.JJCoordinatorLayout_clCenterInParentBottomVertically,false)
        val aCenterInParentStartHorizontal = a.getBoolean(R.styleable.JJCoordinatorLayout_clCenterInParentStartHorizontally,false)
        val aCenterInParentEndHorizontal = a.getBoolean(R.styleable.JJCoordinatorLayout_clCenterInParentEndHorizontally,false)

        val aCenterInTopVerticalOf = a.getResourceId(R.styleable.JJCoordinatorLayout_clCenterInTopVerticallyOf,View.NO_ID)
        val aCenterInBottomVerticalOf = a.getResourceId(R.styleable.JJCoordinatorLayout_clCenterInBottomVerticallyOf,View.NO_ID)
        val aCenterInStartHorizontalOf= a.getResourceId(R.styleable.JJCoordinatorLayout_clCenterInStartHorizontallyOf,View.NO_ID)
        val aCenterInEndHorizontalOf = a.getResourceId(R.styleable.JJCoordinatorLayout_clCenterInEndHorizontallyOf,View.NO_ID)

        val aCenterVerticalOf = a.getResourceId(R.styleable.JJCoordinatorLayout_clCenterVerticallyOf,View.NO_ID)
        val aCenterHorizontalOf = a.getResourceId(R.styleable.JJCoordinatorLayout_clCenterHorizontallyOf,View.NO_ID)

        val aMarginEnd = a.getDimension(R.styleable.JJCoordinatorLayout_clMarginEnd,0f)
        val aMarginStart = a.getDimension(R.styleable.JJCoordinatorLayout_clMarginStart,0f)
        val aMarginTop = a.getDimension(R.styleable.JJCoordinatorLayout_clMarginTop,0f)
        val aMarginBottom = a.getDimension(R.styleable.JJCoordinatorLayout_clMarginBottom,0f)
        val aMarginEndPercent = a.getFloat(R.styleable.JJCoordinatorLayout_clMarginEndPercentScreenHeight,0f)
        val aMarginStartPercent = a.getFloat(R.styleable.JJCoordinatorLayout_clMarginStartPercentScreenHeight,0f)
        val aMarginTopPercent = a.getFloat(R.styleable.JJCoordinatorLayout_clMarginTopPercentScreenHeight,0f)
        val aMarginBottomPercent = a.getFloat(R.styleable.JJCoordinatorLayout_clMarginBottomPercentScreenHeight,0f)

        val aMarginEndPercentWidth = a.getFloat(R.styleable.JJCoordinatorLayout_clMarginEndPercentScreenWidth,0f)
        val aMarginStartPercentWidth = a.getFloat(R.styleable.JJCoordinatorLayout_clMarginStartPercentScreenWidth,0f)
        val aMarginTopPercentWidth = a.getFloat(R.styleable.JJCoordinatorLayout_clMarginTopPercentScreenWidth,0f)
        val aMarginBottomPercentWidth = a.getFloat(R.styleable.JJCoordinatorLayout_clMarginBottomPercentScreenWidth,0f)


        val aMargin = a.getDimension(R.styleable.JJCoordinatorLayout_clMargin,0f)
        val aMarginPercentHeight = a.getFloat(R.styleable.JJCoordinatorLayout_clMargin,0f)
        val aMarginPercentWidth = a.getFloat(R.styleable.JJCoordinatorLayout_clMargin,0f)
        val aMarginResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_clMargin,NO_ID)
        val aMarginResPerScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_clMargin,NO_ID)
        val aMarginResPerScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_clMargin,NO_ID)

        val aMarginEndResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_clMarginEndResponsive,NO_ID)
        val aMarginStartResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_clMarginStartResponsive,NO_ID)
        val aMarginTopResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_clMarginTopResponsive,NO_ID)
        val aMarginBottomResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_clMarginBottomResponsive,NO_ID)
        val aMarginEndResPerScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_clMarginEndResPerScHeight,NO_ID)
        val aMarginStartResPerScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_clMarginStartResPerScHeight,NO_ID)
        val aMarginTopResPerScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_clMarginTopResPerScHeight,NO_ID)
        val aMarginBottomResPerScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_clMarginBottomResPerScHeight,NO_ID)
        val aMarginEndResPerScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_clMarginEndResPerScWidth,NO_ID)
        val aMarginStartResPerScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_clMarginStartResPerScWidth,NO_ID)
        val aMarginTopResPerScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_clMarginTopResPerScWidth,NO_ID)
        val aMarginBottomResPerScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_clMarginBottomResPerScWidth,NO_ID)

        val aMarginVertical = a.getDimension(R.styleable.JJCoordinatorLayout_clMarginVertical,0f)
        val aMarginVerticalPercentHeight = a.getFloat(R.styleable.JJCoordinatorLayout_clMarginVerticalPerScHeight,0f)
        val aMarginVerticalPercentWidth = a.getFloat(R.styleable.JJCoordinatorLayout_clMarginVerticalPerScWidth,0f)
        val aMarginVerticalResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_clMarginVerticalResponsive,NO_ID)
        val aMarginVerticalResPerScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_clMarginVerticalResPerScHeight,NO_ID)
        val aMarginVerticalResPerScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_clMarginVerticalResPerScWidth,NO_ID)

        val aMarginHorizontal = a.getDimension(R.styleable.JJCoordinatorLayout_clMarginHorizontal,0f)
        val aMarginHorizontalPercentHeight = a.getFloat(R.styleable.JJCoordinatorLayout_clMarginHorizontalPerScHeight,0f)
        val aMarginHorizontalPercentWidth = a.getFloat(R.styleable.JJCoordinatorLayout_clMarginHorizontalPerScWidth,0f)
        val aMarginHorizontalResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_clMarginHorizontalResponsive,NO_ID)
        val aMarginHorizontalResPerScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_clMarginHorizontalResPerScHeight,NO_ID)
        val aMarginHorizontalResPerScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_clMarginHorizontalResPerScWidth,NO_ID)


        val aVerticalBias = a.getFloat(R.styleable.JJCoordinatorLayout_clVerticalBias,0.5f)
        val aHorizontalBias = a.getFloat(R.styleable.JJCoordinatorLayout_clHorizontalBias,0.5f)

        val aStartToStartParent = a.getBoolean(R.styleable.JJCoordinatorLayout_clStartToStartParent,false)
        val aStartToEndParent = a.getBoolean(R.styleable.JJCoordinatorLayout_clStartToEndParent,false)
        val aEndToEndParent = a.getBoolean(R.styleable.JJCoordinatorLayout_clEndToEndParent,false)
        val aEndToStartParent = a.getBoolean(R.styleable.JJCoordinatorLayout_clEndToStartParent,false)
        val aTopToTopParent = a.getBoolean(R.styleable.JJCoordinatorLayout_clTopToTopParent,false)
        val aTopToBottomParent = a.getBoolean(R.styleable.JJCoordinatorLayout_clTopToBottomParent,false)
        val aBottomToBottomParent = a.getBoolean(R.styleable.JJCoordinatorLayout_clBottomToBottomParent,false)
        val aBottomToTopParent = a.getBoolean(R.styleable.JJCoordinatorLayout_clBottomToTopParent,false)

        val aStartToStartOf = a.getResourceId(R.styleable.JJCoordinatorLayout_clStartToStartOf,View.NO_ID)
        val aStartToEndOf = a.getResourceId(R.styleable.JJCoordinatorLayout_clStartToEndOf,View.NO_ID)
        val aEndToEndOf = a.getResourceId(R.styleable.JJCoordinatorLayout_clEndToEndOf,View.NO_ID)
        val aEndToStartOf = a.getResourceId(R.styleable.JJCoordinatorLayout_clEndToStartOf,View.NO_ID)
        val aTopToTopOf = a.getResourceId(R.styleable.JJCoordinatorLayout_clTopToTopOf,View.NO_ID)
        val aTopToBottomOf = a.getResourceId(R.styleable.JJCoordinatorLayout_clTopToBottomOf,View.NO_ID)
        val aBottomToBottomOf = a.getResourceId(R.styleable.JJCoordinatorLayout_clBottomToBottomOf,View.NO_ID)
        val aBottomToTopOf = a.getResourceId(R.styleable.JJCoordinatorLayout_clBottomToTopOf,View.NO_ID)

        val aHeightPercent = a.getFloat(R.styleable.JJCoordinatorLayout_clHeightPercent,0f)
        val aWidthPercent = a.getFloat(R.styleable.JJCoordinatorLayout_clWidthPercent,0f)
        val aHeightPercentScreenWidth = a.getFloat(R.styleable.JJCoordinatorLayout_clHeightPercentScreenWidth,0f)
        val aWidthPercentScreenWidth = a.getFloat(R.styleable.JJCoordinatorLayout_clWidthPercentScreenWidth,0f)
        val aHeightPercentScreenHeight = a.getFloat(R.styleable.JJCoordinatorLayout_clHeightPercentScreenHeight,0f)
        val aWidthPercentScreenHeight = a.getFloat(R.styleable.JJCoordinatorLayout_clWidthPercentScreenHeight,0f)

        val aHeightResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_clHeightResponsive,View.NO_ID)
        val aWidthResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_clWidthResponsive,View.NO_ID)
        val aHeightResponsivePercentScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_clHeightResponsivePercentScreenHeight,View.NO_ID)
        val aWidthResponsivePercentScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_clWidthResponsivePercentScreenWidth,View.NO_ID)
        val aHeightResponsivePercentScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_clHeightResponsivePercentScreenWidth,View.NO_ID)
        val aWidthResponsivePercentScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_clWidthResponsivePercentScreenHeight,View.NO_ID)


        val lHeightPercentScreenWidth = a.getFloat(R.styleable.JJCoordinatorLayout_lpHeightPercentScreenWidth,0f)
        val lWidthPercentScreenWidth = a.getFloat(R.styleable.JJCoordinatorLayout_lpWidthPercentScreenWidth,0f)
        val lHeightPercentScreenHeight = a.getFloat(R.styleable.JJCoordinatorLayout_lpHeightPercentScreenHeight,0f)
        val lWidthPercentScreenHeight = a.getFloat(R.styleable.JJCoordinatorLayout_lpWidthPercentScreenHeight,0f)

        val lHeightResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_lpHeightResponsive,View.NO_ID)
        val lWidthResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_lpWidthResponsive,View.NO_ID)
        val lHeightResponsivePercentScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_lpHeightResponsivePercentScreenHeight,View.NO_ID)
        val lWidthResponsivePercentScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_lpWidthResponsivePercentScreenWidth,View.NO_ID)
        val lHeightResponsivePercentScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_lpHeightResponsivePercentScreenWidth,View.NO_ID)
        val lWidthResponsivePercentScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_lpWidthResponsivePercentScreenHeight,View.NO_ID)

        val lMarginTopPercentScHeight = a.getFloat(R.styleable.JJCoordinatorLayout_lpMarginTopPerScHeight,0f)
        val lMarginLeftPercentScHeight = a.getFloat(R.styleable.JJCoordinatorLayout_lpMarginLeftPerScHeight,0f)
        val lMarginRightPercentScHeight = a.getFloat(R.styleable.JJCoordinatorLayout_lpMarginRightPerScHeight,0f)
        val lMarginBottomPercentScHeight = a.getFloat(R.styleable.JJCoordinatorLayout_lpMarginBottomPerScHeight,0f)

        val lMarginTopPercentScWidth = a.getFloat(R.styleable.JJCoordinatorLayout_lpMarginTopPerScWidth,0f)
        val lMarginLeftPercentScWidth = a.getFloat(R.styleable.JJCoordinatorLayout_lpMarginLeftPerScWidth,0f)
        val lMarginRightPercentScWidth = a.getFloat(R.styleable.JJCoordinatorLayout_lpMarginRightPerScWidth,0f)
        val lMarginBottomPercentScWidth = a.getFloat(R.styleable.JJCoordinatorLayout_lpMarginBottomPerScWidth,0f)

        val lMarginTopResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_lpMarginTopResponsive,View.NO_ID)
        val lMarginLeftResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_lpMarginLeftResponsive,View.NO_ID)
        val lMarginRightResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_lpMarginRightResponsive,View.NO_ID)
        val lMarginBottomResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_lpMarginBottomResponsive,View.NO_ID)

        val lMarginTopResponsivePercentScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_lpMarginTopResPerScWidth,View.NO_ID)
        val lMarginLeftResponsivePercentScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_lpMarginLeftResPerScWidth,View.NO_ID)
        val lMarginRightResponsivePercentScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_lpMarginRightResPerScWidth,View.NO_ID)
        val lMarginBottomResponsivePercentScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_lpMarginBottomResPerScWidth,View.NO_ID)

        val lMarginTopResponsivePercentScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_lpMarginTopResPerScHeight,View.NO_ID)
        val lMarginLeftResponsivePercentScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_lpMarginLeftResPerScHeight,View.NO_ID)
        val lMarginRightResponsivePercentScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_lpMarginRightResPerScHeight,View.NO_ID)
        val lMarginBottomResponsivePercentScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_lpMarginBottomResPerScHeight,View.NO_ID)


        val lMarginPerScHeight = a.getFloat(R.styleable.JJCoordinatorLayout_lpMarginPercentScHeight,0f)
        val lMarginPerScWidth = a.getFloat(R.styleable.JJCoordinatorLayout_lpMarginPercentScWidth,0f)
        val lMarginResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_lpMarginResponsive,View.NO_ID)
        val lMarginResPerScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_lpMarginResPerScHeight,View.NO_ID)
        val lMarginResPerScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_lpMarginResPerScWidth,View.NO_ID)

        val lMarginVerticalPerScHeight = a.getFloat(R.styleable.JJCoordinatorLayout_lpMarginVerticalPerScHeight,0f)
        val lMarginVerticalPerScWidth = a.getFloat(R.styleable.JJCoordinatorLayout_lpMarginVerticalPerScWidth,0f)
        val lMarginVerticalResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_lpMarginVerticalResponsive,View.NO_ID)
        val lMarginVerticalResPerScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_lpMarginVerticalResPerScWidth,View.NO_ID)
        val lMarginVerticalResPerScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_lpMarginVerticalResPerScHeight,View.NO_ID)

        val lMarginHorizontalPerScHeight = a.getFloat(R.styleable.JJCoordinatorLayout_lpMarginHorizontalPerScHeight,0f)
        val lMarginHorizontalPerScWidth = a.getFloat(R.styleable.JJCoordinatorLayout_lpMarginHorizontalPerScWidth,0f)
        val lMarginHorizontalResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_lpMarginHorizontalResponsive,View.NO_ID)
        val lMarginHorizontalResPerScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_lpMarginHorizontalResPerScWidth,View.NO_ID)
        val lMarginHorizontalResPerScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_lpMarginHorizontalResPerScHeight,View.NO_ID)



        val lPaddingTopPercentScHeight = a.getFloat(R.styleable.JJCoordinatorLayout_lpPaddingTopPerScHeight,0f)
        val lPaddingLeftPercentScHeight = a.getFloat(R.styleable.JJCoordinatorLayout_lpPaddingLeftPerScHeight,0f)
        val lPaddingRightPercentScHeight = a.getFloat(R.styleable.JJCoordinatorLayout_lpPaddingRightPerScHeight,0f)
        val lPaddingBottomPercentScHeight = a.getFloat(R.styleable.JJCoordinatorLayout_lpPaddingBottomPerScHeight,0f)

        val lPaddingTopPercentScWidth = a.getFloat(R.styleable.JJCoordinatorLayout_lpPaddingTopPerScWidth,0f)
        val lPaddingLeftPercentScWidth = a.getFloat(R.styleable.JJCoordinatorLayout_lpPaddingLeftPerScWidth,0f)
        val lPaddingRightPercentScWidth = a.getFloat(R.styleable.JJCoordinatorLayout_lpPaddingRightPerScWidth,0f)
        val lPaddingBottomPercentScWidth = a.getFloat(R.styleable.JJCoordinatorLayout_lpPaddingBottomPerScWidth,0f)

        val lPaddingTopResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_lpPaddingTopResponsive,View.NO_ID)
        val lPaddingLeftResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_lpPaddingLeftResponsive,View.NO_ID)
        val lPaddingRightResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_lpPaddingRightResponsive,View.NO_ID)
        val lPaddingBottomResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_lpPaddingBottomResponsive,View.NO_ID)

        val lPaddingTopResponsivePercentScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_lpPaddingTopResPerScWidth,View.NO_ID)
        val lPaddingLeftResponsivePercentScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_lpPaddingLeftResPerScWidth,View.NO_ID)
        val lPaddingRightResponsivePercentScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_lpPaddingRightResPerScWidth,View.NO_ID)
        val lPaddingBottomResponsivePercentScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_lpPaddingBottomResPerScWidth,View.NO_ID)

        val lPaddingTopResponsivePercentScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_lpPaddingTopResPerScHeight,View.NO_ID)
        val lPaddingLeftResponsivePercentScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_lpPaddingLeftResPerScHeight,View.NO_ID)
        val lPaddingRightResponsivePercentScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_lpPaddingRightResPerScHeight,View.NO_ID)
        val lPaddingBottomResponsivePercentScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_lpPaddingBottomResPerScHeight,View.NO_ID)

        val lPaddingPerScHeight = a.getFloat(R.styleable.JJCoordinatorLayout_lpPaddingPercentScHeight,0f)
        val lPaddingPerScWidth = a.getFloat(R.styleable.JJCoordinatorLayout_lpPaddingPercentScWidth,0f)
        val lPaddingResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_lpPaddingResponsive,View.NO_ID)
        val lPaddingResPerScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_lpPaddingResPerScHeight,View.NO_ID)
        val lPaddingResPerScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_lpPaddingResPerScWidth,View.NO_ID)

        val lPaddingVerticalPerScHeight = a.getFloat(R.styleable.JJCoordinatorLayout_lpPaddingVerticalPerScHeight,0f)
        val lPaddingVerticalPerScWidth = a.getFloat(R.styleable.JJCoordinatorLayout_lpPaddingVerticalPerScWidth,0f)
        val lPaddingVerticalResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_lpPaddingVerticalResponsive,View.NO_ID)
        val lPaddingVerticalResPerScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_lpPaddingVerticalResPerScWidth,View.NO_ID)
        val lPaddingVerticalResPerScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_lpPaddingVerticalResPerScHeight,View.NO_ID)

        val lPaddingHorizontalPerScHeight = a.getFloat(R.styleable.JJCoordinatorLayout_lpPaddingHorizontalPerScHeight,0f)
        val lPaddingHorizontalPerScWidth = a.getFloat(R.styleable.JJCoordinatorLayout_lpPaddingHorizontalPerScWidth,0f)
        val lPaddingHorizontalResponsive = a.getResourceId(R.styleable.JJCoordinatorLayout_lpPaddingHorizontalResponsive,View.NO_ID)
        val lPaddingHorizontalResPerScWidth = a.getResourceId(R.styleable.JJCoordinatorLayout_lpPaddingHorizontalResPerScWidth,View.NO_ID)
        val lPaddingHorizontalResPerScHeight = a.getResourceId(R.styleable.JJCoordinatorLayout_lpPaddingHorizontalResPerScHeight,View.NO_ID)


        a.recycle()

        //linearLayout

        //region margin
        if(lMarginPerScHeight > 0f) {
            val mar = JJScreen.percentHeight(lMarginPerScHeight)
            mlpMargins = JJMargin(mar,mar,mar,mar)
        }
        if(lMarginPerScWidth > 0f) {
            val mar = JJScreen.percentWidth(lMarginPerScWidth)
            mlpMargins = JJMargin(mar,mar,mar,mar)
        }

        if(lMarginResponsive != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lMarginResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            mlpMargins = JJMargin(mar,mar,mar,mar)
        }

        if(lMarginResPerScHeight != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lMarginResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpMargins = JJMargin(mar,mar,mar,mar)
        }

        if(lMarginResPerScWidth != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lMarginResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpMargins = JJMargin(mar,mar,mar,mar)
        }


        //endregion

        //region margin vertical
        if(lMarginVerticalPerScHeight > 0f) {
            val mar = JJScreen.percentHeight(lMarginVerticalPerScHeight)
            mlpMargins?.top = mar ; mlpMargins?.bottom = mar
        }
        if(lMarginVerticalPerScWidth > 0f) {
            val mar = JJScreen.percentWidth(lMarginVerticalPerScWidth)
            mlpMargins?.top = mar ; mlpMargins?.bottom = mar
        }

        if(lMarginVerticalResponsive != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lMarginVerticalResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            mlpMargins?.top = mar ; mlpMargins?.bottom = mar
        }

        if(lMarginVerticalResPerScHeight != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lMarginVerticalResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpMargins?.top = mar ; mlpMargins?.bottom = mar
        }

        if(lMarginVerticalResPerScWidth != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lMarginVerticalResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpMargins?.top = mar ; mlpMargins?.bottom = mar
        }
        //endregion

        //region Horizontal margin

        if(lMarginHorizontalPerScHeight > 0f) {
            val mar = JJScreen.percentHeight(lMarginHorizontalPerScHeight)
            mlpMargins?.left = mar ; mlpMargins?.right = mar
        }
        if(lMarginHorizontalPerScWidth > 0f) {
            val mar = JJScreen.percentWidth(lMarginHorizontalPerScWidth)
            mlpMargins?.left = mar ; mlpMargins?.right = mar
        }

        if(lMarginHorizontalResponsive != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lMarginHorizontalResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            mlpMargins?.left = mar ; mlpMargins?.right = mar
        }

        if(lMarginHorizontalResPerScHeight != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lMarginHorizontalResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpMargins?.left = mar ; mlpMargins?.right = mar
        }

        if(lMarginHorizontalResPerScWidth != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lMarginHorizontalResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpMargins?.left = mar ; mlpMargins?.right = mar
        }

        //endregion

        if(lMarginTopPercentScHeight > 0f)  mlpMargins?.top = JJScreen.percentHeight(lMarginTopPercentScHeight)
        if(lMarginLeftPercentScHeight > 0f)  mlpMargins?.left = JJScreen.percentHeight(lMarginLeftPercentScHeight)
        if(lMarginRightPercentScHeight > 0f) mlpMargins?.right = JJScreen.percentHeight(lMarginRightPercentScHeight)
        if(lMarginBottomPercentScHeight > 0f) mlpMargins?.bottom = JJScreen.percentHeight(lMarginBottomPercentScHeight)

        if(lMarginTopPercentScWidth > 0f)  mlpMargins?.top = JJScreen.percentWidth(lMarginTopPercentScWidth)
        if(lMarginLeftPercentScWidth > 0f)  mlpMargins?.left = JJScreen.percentWidth(lMarginLeftPercentScWidth)
        if(lMarginRightPercentScWidth > 0f) mlpMargins?.right = JJScreen.percentWidth(lMarginRightPercentScWidth)
        if(lMarginBottomPercentScWidth > 0f) mlpMargins?.bottom = JJScreen.percentWidth(lMarginBottomPercentScWidth)

        if(lMarginTopResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginTopResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlpMargins?.top = v
        }

        if(lMarginLeftResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginLeftResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlpMargins?.left = v
        }

        if(lMarginRightResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginRightResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlpMargins?.right = v
        }

        if(lMarginBottomResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginBottomResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlpMargins?.bottom = v
        }


        if(lMarginTopResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginTopResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpMargins?.top = v
        }

        if(lMarginLeftResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginLeftResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpMargins?.left = v
        }

        if(lMarginRightResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginRightResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpMargins?.right = v
        }

        if(lMarginBottomResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginBottomResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpMargins?.bottom = v
        }

        if(lMarginTopResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginTopResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpMargins?.top = v
        }

        if(lMarginLeftResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginLeftResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpMargins?.left = v
        }

        if(lMarginRightResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginRightResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpMargins?.right = v
        }

        if(lMarginBottomResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginBottomResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpMargins?.bottom = v
        }


        //region padding

        if(lPaddingPerScHeight > 0f) {
            val mar = JJScreen.percentHeight(lPaddingPerScHeight)
            mlpPaddings = JJPadding(mar,mar,mar,mar)
        }
        if(lPaddingPerScWidth > 0f) {
            val mar = JJScreen.percentWidth(lPaddingPerScWidth)
            mlpPaddings = JJPadding(mar,mar,mar,mar)
        }

        if(lPaddingResponsive != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lPaddingResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            mlpPaddings = JJPadding(mar,mar,mar,mar)
        }

        if(lPaddingResPerScHeight != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lPaddingResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpPaddings = JJPadding(mar,mar,mar,mar)
        }

        if(lPaddingResPerScWidth != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lPaddingResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpPaddings = JJPadding(mar,mar,mar,mar)
        }


        //endregion

        //region padding vertical

        if(lPaddingVerticalPerScHeight > 0f) {
            val mar = JJScreen.percentHeight(lPaddingVerticalPerScHeight)
            mlpPaddings.top = mar ; mlpPaddings.bottom = mar
        }
        if(lPaddingVerticalPerScWidth > 0f) {
            val mar = JJScreen.percentWidth(lMarginVerticalPerScWidth)
            mlpPaddings.top = mar ; mlpPaddings.bottom = mar
        }

        if(lPaddingVerticalResponsive != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lPaddingVerticalResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            mlpPaddings.top = mar ; mlpPaddings.bottom = mar
        }

        if(lPaddingVerticalResPerScHeight != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lPaddingVerticalResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpPaddings.top = mar ; mlpPaddings.bottom = mar
        }

        if(lPaddingVerticalResPerScWidth != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lPaddingVerticalResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpPaddings.top = mar ; mlpPaddings.bottom = mar
        }
        //endregion

        //region Horizontal padding

        if(lPaddingHorizontalPerScHeight > 0f) {
            val mar = JJScreen.percentHeight(lPaddingHorizontalPerScHeight)
            mlpPaddings.left = mar ; mlpPaddings.right = mar
        }
        if(lPaddingHorizontalPerScWidth > 0f) {
            val mar = JJScreen.percentWidth(lPaddingHorizontalPerScWidth)
            mlpPaddings.left = mar ; mlpPaddings.right = mar
        }

        if(lPaddingHorizontalResponsive != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lPaddingHorizontalResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            mlpPaddings.left = mar ; mlpPaddings.right = mar
        }

        if(lPaddingHorizontalResPerScHeight != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lPaddingHorizontalResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpPaddings.left = mar ; mlpPaddings.right = mar
        }

        if(lPaddingHorizontalResPerScWidth != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lPaddingHorizontalResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpPaddings.left = mar ; mlpPaddings.right = mar
        }

        //endregion

        //region padding start end top bottom

        if(lPaddingTopPercentScHeight > 0f)  mlpPaddings.top = JJScreen.percentHeight(lPaddingTopPercentScHeight)
        if(lPaddingLeftPercentScHeight > 0f)  mlpPaddings.left = JJScreen.percentHeight(lPaddingLeftPercentScHeight)
        if(lPaddingRightPercentScHeight > 0f) mlpPaddings.right = JJScreen.percentHeight(lPaddingRightPercentScHeight)
        if(lPaddingBottomPercentScHeight > 0f) mlpPaddings.bottom = JJScreen.percentHeight(lPaddingBottomPercentScHeight)

        if(lPaddingTopPercentScWidth > 0f)  mlpPaddings.top = JJScreen.percentWidth(lPaddingTopPercentScWidth)
        if(lPaddingLeftPercentScWidth > 0f)  mlpPaddings.left = JJScreen.percentWidth(lPaddingLeftPercentScWidth)
        if(lPaddingRightPercentScWidth > 0f) mlpPaddings.right = JJScreen.percentWidth(lPaddingRightPercentScWidth)
        if(lPaddingBottomPercentScWidth > 0f) mlpPaddings.bottom = JJScreen.percentWidth(lPaddingBottomPercentScWidth)


        if(lPaddingTopResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingTopResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlpPaddings.top = v
        }

        if(lPaddingLeftResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingLeftResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlpPaddings.left = v
        }

        if(lPaddingRightResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingRightResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlpPaddings.right = v
        }

        if(lPaddingBottomResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingBottomResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlpPaddings.bottom = v
        }


        if(lPaddingTopResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingTopResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpPaddings.top = v
        }

        if(lPaddingLeftResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingLeftResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpPaddings.left = v
        }

        if(lPaddingRightResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingRightResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpPaddings.right = v
        }

        if(lPaddingBottomResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingBottomResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpPaddings.bottom = v
        }

        if(lPaddingTopResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingTopResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpPaddings.top = v
        }

        if(lPaddingLeftResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingLeftResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpPaddings.left = v
        }

        if(lPaddingRightResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingRightResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpPaddings.right = v
        }

        if(lPaddingBottomResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingBottomResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpPaddings.bottom = v
        }

        //endregion

        mlpHeight = attrHeight
        mlpWidth = attrWidth
        if(lHeightPercentScreenWidth > 0f)  mlpHeight = JJScreen.percentWidth(lHeightPercentScreenWidth)
        if(lHeightPercentScreenHeight > 0f)  mlpHeight = JJScreen.percentHeight(lHeightPercentScreenHeight)
        if(lWidthPercentScreenWidth > 0f) mlpWidth = JJScreen.percentWidth(lWidthPercentScreenWidth)
        if(lWidthPercentScreenHeight > 0f) mlpWidth = JJScreen.percentHeight(lWidthPercentScreenHeight)

        if(lHeightResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lHeightResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val h = JJScreen.responsiveSize(first,second,three,four)
            mlpHeight = h
        }

        if(lWidthResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lWidthResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val w = JJScreen.responsiveSize(first,second,three,four)
            mlpWidth = w
        }


        if(lHeightResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lHeightResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val h = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpHeight = h
        }

        if(lWidthResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lWidthResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val w = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)

            mlpWidth = w
        }

        if(lHeightResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lHeightResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val h = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpHeight = h
        }

        if(lWidthResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lWidthResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val w = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)

            mlpWidth = w
        }

        //constraint Layout

        //region margin

        var margins = JJMargin()

        if(aMargin > 0f) margins = JJMargin(aMargin.toInt(),aMargin.toInt(),aMargin.toInt(),aMargin.toInt())

        if(aMarginPercentHeight > 0f) {
            val mar = JJScreen.percentHeight(aMarginPercentHeight)
            margins = JJMargin(mar,mar,mar,mar)
        }

        if(aMarginPercentWidth > 0f) {
            val mar = JJScreen.percentWidth(aMarginPercentWidth)
            margins = JJMargin(mar,mar,mar,mar)
        }

        if(aMarginResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aMarginResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            margins = JJMargin(v,v,v,v)
        }

        if(aMarginResPerScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aMarginResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            margins = JJMargin(v,v,v,v)
        }

        if(aMarginResPerScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aMarginResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            margins = JJMargin(v,v,v,v)
        }

        //endregion

        //region margin Vertical

        if(aMarginVertical > 0f) { margins.top = aMarginVertical.toInt() ; margins.bottom = aMarginVertical.toInt() }

        if(aMarginVerticalPercentHeight > 0f) {
            val mar = JJScreen.percentHeight(aMarginVerticalPercentHeight)
            margins.top = mar ; margins.bottom = mar
        }

        if(aMarginVerticalPercentWidth > 0f) {
            val mar = JJScreen.percentWidth(aMarginVerticalPercentWidth)
            margins.top = mar ; margins.bottom = mar
        }

        if(aMarginVerticalResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aMarginVerticalResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            margins.top = mar ; margins.bottom = mar
        }

        if(aMarginVerticalResPerScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aMarginVerticalResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            margins.top = mar ; margins.bottom = mar
        }

        if(aMarginVerticalResPerScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aMarginVerticalResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            margins.top = mar ; margins.bottom = mar
        }

        //endregion

        // region margin Horizontal

        if(aMarginHorizontal > 0f) { margins.left = aMarginHorizontal.toInt() ; margins.right = aMarginHorizontal.toInt() }

        if(aMarginHorizontalPercentHeight > 0f) {
            val mar = JJScreen.percentHeight(aMarginHorizontalPercentHeight)
            margins.left = mar ; margins.right = mar
        }

        if(aMarginHorizontalPercentWidth > 0f) {
            val mar = JJScreen.percentWidth(aMarginHorizontalPercentWidth)
            margins.left = mar ; margins.right = mar
        }

        if(aMarginHorizontalResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aMarginHorizontalResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            margins.left = mar ; margins.right = mar
        }

        if(aMarginHorizontalResPerScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aMarginHorizontalResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            margins.left = mar ; margins.right = mar
        }

        if(aMarginHorizontalResPerScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aMarginHorizontalResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            margins.left = mar ; margins.right = mar
        }

        //endregion

        //region margin start end top bottom

        if(aMarginStart > 0f)   margins.left = aMarginStart.toInt()
        if(aMarginEnd > 0f)   margins.right = aMarginEnd.toInt()
        if(aMarginTop > 0f)   margins.top = aMarginTop.toInt()
        if(aMarginBottom > 0f)   margins.bottom = aMarginBottom.toInt()

        if(aMarginStartPercent > 0f) margins.left = JJScreen.percentHeight(aMarginStartPercent)
        if(aMarginTopPercent > 0f)  margins.top = JJScreen.percentHeight(aMarginTopPercent)
        if(aMarginEndPercent > 0f)  margins.right = JJScreen.percentHeight(aMarginEndPercent)
        if(aMarginBottomPercent > 0f)  margins.bottom = JJScreen.percentHeight(aMarginBottomPercent)

        if(aMarginStartPercentWidth > 0f)  margins.left = JJScreen.percentWidth(aMarginStartPercentWidth)
        if(aMarginTopPercentWidth > 0f) margins.top = JJScreen.percentWidth(aMarginTopPercentWidth)
        if(aMarginEndPercentWidth > 0f) margins.right = JJScreen.percentWidth(aMarginEndPercentWidth)
        if(aMarginBottomPercentWidth > 0f) margins.bottom = JJScreen.percentWidth(aMarginBottomPercentWidth)

        if(aMarginStartResponsive > 0f)  {
            val arrayDimen = resources.obtainTypedArray(aMarginStartResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            margins.left = mar
        }
        if(aMarginEndResponsive > 0f)   {
            val arrayDimen = resources.obtainTypedArray(aMarginEndResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            margins.right = mar
        }
        if(aMarginTopResponsive > 0f)  {
            val arrayDimen = resources.obtainTypedArray(aMarginTopResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            margins.top = mar
        }
        if(aMarginBottomResponsive > 0f)  {
            val arrayDimen = resources.obtainTypedArray(aMarginBottomResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            margins.bottom = mar
        }

        if(aMarginStartResPerScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aMarginStartResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            margins.left = mar
        }

        if(aMarginEndResPerScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aMarginEndResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            margins.right = mar
        }

        if(aMarginTopResPerScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aMarginTopResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            margins.top = mar
        }

        if(aMarginBottomResPerScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aMarginBottomResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            margins.bottom = mar
        }

        if(aMarginStartResPerScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aMarginStartResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            margins.left = mar
        }

        if(aMarginEndResPerScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aMarginEndResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            margins.right = mar
        }

        if(aMarginTopResPerScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aMarginTopResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            margins.top = mar
        }

        if(aMarginBottomResPerScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aMarginBottomResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            margins.bottom = mar
        }


        //endregion

        if(attrWidth > 0 || attrWidth == -2) clWidth(attrWidth)
        if(attrHeight > 0 || attrHeight == -2) clHeight(attrHeight)

        if(aHeightPercent > 0f) clPercentHeight(aHeightPercent)

        if(aHeightPercentScreenWidth > 0f) clHeight(JJScreen.percentWidth(aHeightPercentScreenWidth))
        if(aHeightPercentScreenHeight > 0f) clHeight(JJScreen.percentHeight(aHeightPercentScreenHeight))

        if(aWidthPercent > 0f) clPercentWidth(aWidthPercent)
        if(aWidthPercentScreenWidth > 0f) clWidth(JJScreen.percentWidth(aWidthPercentScreenWidth))
        if(aWidthPercentScreenHeight > 0f) clWidth(JJScreen.percentHeight(aWidthPercentScreenHeight))

        if(aHeightResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aHeightResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val h = JJScreen.responsiveSize(first,second,three,four)
            clHeight(h)
        }

        if(aWidthResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aWidthResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val w = JJScreen.responsiveSize(first,second,three,four)

            clWidth(w)
        }

        if(aHeightResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aHeightResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val h = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            clHeight(h)
        }

        if(aWidthResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aWidthResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val w = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)

            clWidth(w)
        }

        if(aHeightResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aHeightResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val h = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            clHeight(h)
        }

        if(aWidthResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(aWidthResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val w = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)

            clWidth(w)
        }


        if(aStartToStartOf != View.NO_ID) clStartToStart(aStartToStartOf,0)
        if(aStartToEndOf != View.NO_ID) clStartToEnd(aStartToEndOf,0)
        if(aEndToEndOf != View.NO_ID) clEndToEnd(aEndToEndOf,0)
        if(aEndToStartOf != View.NO_ID) clEndToStart(aEndToStartOf,0)
        if(aTopToTopOf != View.NO_ID) clTopToTop(aTopToTopOf,0)
        if(aTopToBottomOf != View.NO_ID) clTopToBottom(aTopToBottomOf,0)
        if(aBottomToBottomOf != View.NO_ID) clBottomToBottom(aBottomToBottomOf,0)
        if(aBottomToTopOf != View.NO_ID) clBottomToTop(aBottomToTopOf,0)

        if(aStartToStartParent) clStartToStartParent(0)
        if(aStartToEndParent) clStartToEndParent(0)
        if(aEndToEndParent) clEndToEndParent(0)
        if(aEndToStartParent) clEndToStartParent(0)
        if(aTopToTopParent) clTopToTopParent()
        if(aTopToBottomParent) clTopToBottomParent(0)
        if(aBottomToBottomParent) clBottomToBottomParent()
        if(aBottomToTopParent) clBottomToTopParent()


        if(aCenterInParentTopVertical) clCenterInParentTopVertically()
        if(aCenterInParentBottomVertical) clCenterInParentBottomVertically()
        if(aCenterInParentStartHorizontal) clCenterInParentStartHorizontally()
        if(aCenterInParentEndHorizontal) clCenterInParentEndHorizontally()

        if(aCenterInTopVerticalOf != View.NO_ID) clCenterInTopVertically(aCenterInTopVerticalOf)
        if(aCenterInBottomVerticalOf != View.NO_ID) clCenterInBottomVertically(aCenterInBottomVerticalOf)
        if(aCenterInStartHorizontalOf != View.NO_ID) clCenterInStartHorizontally(aCenterInStartHorizontalOf)
        if(aCenterInEndHorizontalOf != View.NO_ID) clCenterInEndHorizontally(aCenterInEndHorizontalOf)

        if(aCenterVerticalOf != View.NO_ID) clCenterVerticallyOf(aCenterVerticalOf)
        if(aCenterHorizontalOf != View.NO_ID) clCenterHorizontallyOf(aCenterHorizontalOf)

        if(aCenterInParentHorizontal) clCenterInParentHorizontally()
        if(aCenterInParentVertical)  clCenterInParentVertically()

        if(aFillParentHorizontal) clFillParentHorizontally()
        if(aFillParentVertical) clFillParentVertically()

        if(aCenterInParent) clCenterInParent()
        if(aFillParent) clFillParent()

        if(aVerticalBias > 0f)  clVerticalBias(aVerticalBias)
        if(aHorizontalBias > 0f)  clHorizontalBias(aHorizontalBias)

        clMargins(margins)
    }


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


    //region set
    fun addViews(vararg views: View): JJCoordinatorLayout {
        for (v in views) {
            addView(v)
        }
        return this
    }
    fun ssFullScreen(): JJCoordinatorLayout {
        systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        return this
    }

    fun ssFitsSystemWindows(boolean: Boolean): JJCoordinatorLayout {
        fitsSystemWindows = boolean
        return this
    }

    fun ssPadding(pad: JJPadding): JJCoordinatorLayout {
        setPaddingRelative(pad.left,pad.top,pad.right,pad.bottom)
        return this
    }

    fun ssBackgroundColor(color: Int ): JJCoordinatorLayout {
        setBackgroundColor(color)
        return this
    }

    fun ssBackground(drawable: Drawable?): JJCoordinatorLayout {
        background = drawable
        return this
    }

    fun ssPaddings(pad : JJPadding): JJCoordinatorLayout {
        mlpPaddings = pad
        setPaddingRelative(pad.left,pad.top,pad.right,pad.bottom)
        return this
    }


    fun ssVisibility(type: Int): JJCoordinatorLayout {
        visibility = type
        return this
    }
    

    fun ssMinimumHeight(h:Int): JJCoordinatorLayout {
        minimumHeight = h
        return this
    }

    fun ssMinimumWidth(w:Int): JJCoordinatorLayout {
        minimumWidth = w
        return this
    }

    //endregion

    //region RelativeLayout Params

    private var mRlp: RelativeLayout.LayoutParams? = null

    private fun setupRlp(){
        if(mRlp == null) {
            mRlp = layoutParams as?  RelativeLayout.LayoutParams
            layoutParams = mRlp
        }
    }

    fun rlWidth(width: Int): JJCoordinatorLayout {
        setupRlp()
        mRlp!!.width = width
        return this
    }

    fun rlHeight(height: Int): JJCoordinatorLayout {
        setupRlp()
        mRlp!!.height = height
        return this
    }

    fun rlAbove(viewId: Int): JJCoordinatorLayout {
        setupRlp()
        mRlp!!.addRule(RelativeLayout.ABOVE,viewId)
        return this
    }

    fun rlBelow(viewId: Int): JJCoordinatorLayout {
        setupRlp()
        mRlp!!.addRule(RelativeLayout.BELOW,viewId)
        return this
    }

    fun rlAlignParentBottom(value : Boolean = true): JJCoordinatorLayout {
        setupRlp()
        val data = if(value) 1 else 0
        mRlp!!.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,data)
        return this
    }

    fun rlAlignParentTop(value : Boolean = true): JJCoordinatorLayout {
        setupRlp()
        val data = if(value) 1 else 0
        mRlp!!.addRule(RelativeLayout.ALIGN_PARENT_TOP,data)
        return this
    }

    fun rlAlignParentStart(value : Boolean = true): JJCoordinatorLayout {
        setupRlp()
        val data = if(value) 1 else 0
        mRlp!!.addRule(RelativeLayout.ALIGN_PARENT_START,data)
        return this
    }

    fun rlAlignParentEnd(value : Boolean = true): JJCoordinatorLayout {
        setupRlp()
        val data = if(value) 1 else 0
        mRlp!!.addRule(RelativeLayout.ALIGN_PARENT_END,data)
        return this
    }

    fun rlAlignParentLeft(value : Boolean = true): JJCoordinatorLayout {
        setupRlp()
        val data = if(value) 1 else 0
        mRlp!!.addRule(RelativeLayout.ALIGN_PARENT_LEFT,data)
        return this
    }

    fun rlAlignParentRight(value : Boolean = true): JJCoordinatorLayout {
        setupRlp()
        val data = if(value) 1 else 0
        mRlp!!.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,data)
        return this
    }

    fun rlAlignEnd(viewId: Int): JJCoordinatorLayout {
        setupRlp()
        mRlp!!.addRule(RelativeLayout.ALIGN_END,viewId)
        return this
    }

    fun rlAlignStart(viewId: Int): JJCoordinatorLayout {
        setupRlp()
        mRlp!!.addRule(RelativeLayout.ALIGN_START,viewId)
        return this
    }

    fun rlAlignTop(viewId: Int): JJCoordinatorLayout {
        setupRlp()
        mRlp!!.addRule(RelativeLayout.ALIGN_TOP,viewId)
        return this
    }

    fun rlAlignBottom(viewId: Int): JJCoordinatorLayout {
        setupRlp()
        mRlp!!.addRule(RelativeLayout.ALIGN_BOTTOM,viewId)
        return this
    }


    fun rlAlignLeft(viewId: Int): JJCoordinatorLayout {
        setupRlp()
        mRlp!!.addRule(RelativeLayout.ALIGN_LEFT,viewId)
        return this
    }

    fun rlAlignRight(viewId: Int): JJCoordinatorLayout {
        setupRlp()
        mRlp!!.addRule(RelativeLayout.ALIGN_RIGHT,viewId)
        return this
    }

    fun rlRightToLeft(viewId: Int): JJCoordinatorLayout {
        setupRlp()
        mRlp!!.addRule(RelativeLayout.LEFT_OF,viewId)
        return this
    }

    fun rlLeftToRight(viewId: Int): JJCoordinatorLayout {
        setupRlp()
        mRlp!!.addRule(RelativeLayout.RIGHT_OF,viewId)
        return this
    }

    fun rlStartToEnd(viewId: Int): JJCoordinatorLayout {
        setupRlp()
        mRlp!!.addRule(RelativeLayout.END_OF,viewId)
        return this
    }

    fun rlEndToStart(viewId: Int): JJCoordinatorLayout {
        setupRlp()
        mRlp!!.addRule(RelativeLayout.START_OF,viewId)
        return this
    }

    fun rlCenterInParent(value:Boolean = true): JJCoordinatorLayout {
        setupRlp()
        val data = if(value) 1 else 0
        mRlp!!.addRule(RelativeLayout.CENTER_IN_PARENT,data)
        return this
    }

    fun rlCenterInParentVertically(value:Boolean = true): JJCoordinatorLayout {
        setupRlp()
        val data = if(value) 1 else 0
        mRlp!!.addRule(RelativeLayout.CENTER_VERTICAL,data)
        return this
    }

    fun rlCenterInParentHorizontally(value:Boolean = true): JJCoordinatorLayout {
        setupRlp()
        val data = if(value) 1 else 0
        mRlp!!.addRule(RelativeLayout.CENTER_HORIZONTAL,data)
        return this
    }

    fun rlAlignBaseline(viewId: Int): JJCoordinatorLayout {
        setupRlp()
        mRlp!!.addRule(RelativeLayout.ALIGN_BASELINE,viewId)
        return this
    }

    fun rlMargins(margins: JJMargin): JJCoordinatorLayout {
        setupRlp()
        mRlp!!.setMargins(margins.left,margins.top,margins.right,margins.bottom)
        return this
    }

    //endregion


    //region ConstraintLayout Params
    private val mConstraintSet = ConstraintSet()


    fun clGetConstraint() : ConstraintSet {
        return mConstraintSet
    }

    fun clMinWidth(w:Int): JJCoordinatorLayout {
        mConstraintSet.constrainMinWidth(id,w)
        return this
    }

    fun clMinHeight(h:Int): JJCoordinatorLayout {
        mConstraintSet.constrainMinHeight(id,h)
        return this
    }

    fun clMaxWidth(w:Int): JJCoordinatorLayout {
        mConstraintSet.constrainMaxWidth(id,w)
        return this
    }

    fun clMaxHeight(h:Int): JJCoordinatorLayout {
        mConstraintSet.constrainMaxHeight(id,h)
        return this
    }


    fun clVisibilityMode(mode: Int): JJCoordinatorLayout {
        mConstraintSet.setVisibilityMode(id,mode)
        return this
    }


    fun clApply(): JJCoordinatorLayout {
        mConstraintSet.applyTo(parent as ConstraintLayout)
        return this
    }

    fun clVerticalBias(float: Float): JJCoordinatorLayout {
        mConstraintSet.setVerticalBias(id,float)
        return this
    }
    fun clHorizontalBias(float: Float): JJCoordinatorLayout {
        mConstraintSet.setHorizontalBias(id,float)
        return this
    }

    fun clCenterHorizontallyOf(viewId: Int): JJCoordinatorLayout {
        mConstraintSet.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.START, 0)
        mConstraintSet.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.END, 0)
        mConstraintSet.setHorizontalBias(id,0.5f)
        return this
    }
    fun clCenterVerticallyOf(viewId: Int): JJCoordinatorLayout {
        mConstraintSet.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, 0)
        mConstraintSet.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, 0)
        mConstraintSet.setVerticalBias(id,0.5f)
        return this
    }

    fun clMargins(margins: JJMargin) : JJCoordinatorLayout {
        mConstraintSet.setMargin(id,ConstraintSet.TOP,margins.top)
        mConstraintSet.setMargin(id,ConstraintSet.BOTTOM,margins.bottom)
        mConstraintSet.setMargin(id,ConstraintSet.END,margins.right)
        mConstraintSet.setMargin(id,ConstraintSet.START,margins.left)
        return this
    }
    

    fun clTopToTop(viewId: Int, margin: Int = 0): JJCoordinatorLayout {
        mConstraintSet.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, margin)
        return this
    }

    fun clTopToTopParent(margin: Int = 0): JJCoordinatorLayout {
        mConstraintSet.connect(this.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin)
        return this
    }


    fun clTopToBottom(viewId: Int, margin: Int = 0): JJCoordinatorLayout {
        mConstraintSet.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun clTopToBottomParent(margin: Int = 0): JJCoordinatorLayout {
        mConstraintSet.connect(this.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun clBottomToTop(viewId: Int, margin: Int = 0): JJCoordinatorLayout {
        mConstraintSet.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.TOP, margin)
        return this
    }

    fun clBottomToTopParent(margin: Int = 0): JJCoordinatorLayout {
        mConstraintSet.connect(this.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin)
        return this
    }

    fun clBottomToBottom(viewId: Int, margin: Int = 0): JJCoordinatorLayout {
        mConstraintSet.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun clBottomToBottomParent(margin: Int = 0): JJCoordinatorLayout {
        mConstraintSet.connect(this.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun clStartToStart(viewId: Int, margin: Int = 0): JJCoordinatorLayout {
        mConstraintSet.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.START, margin)
        return this
    }

    fun clStartToStartParent(margin: Int = 0): JJCoordinatorLayout {
        mConstraintSet.connect(this.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin)
        return this
    }

    fun clStartToEnd(viewId: Int, margin: Int = 0): JJCoordinatorLayout {
        mConstraintSet.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.END, margin)
        return this
    }

    fun clStartToEndParent(margin: Int = 0): JJCoordinatorLayout {
        mConstraintSet.connect(this.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END, margin)
        return this
    }

    fun clEndToEnd(viewId: Int, margin: Int = 0): JJCoordinatorLayout {
        mConstraintSet.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.END, margin)
        return this
    }

    fun clEndToEndParent(margin: Int = 0): JJCoordinatorLayout {
        mConstraintSet.connect(this.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin)
        return this
    }


    fun clEndToStart(viewId: Int, margin: Int = 0): JJCoordinatorLayout {
        mConstraintSet.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.START, margin)
        return this
    }

    fun clEndToStartParent(margin: Int = 0): JJCoordinatorLayout {
        mConstraintSet.connect(this.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START, margin)
        return this
    }


    fun clWidth(width: Int): JJCoordinatorLayout {
        mConstraintSet.constrainWidth(id, width)
        return this
    }

    fun clHeight(height: Int): JJCoordinatorLayout {
        mConstraintSet.constrainHeight(id, height)
        return this
    }

    fun clPercentWidth(width: Float): JJCoordinatorLayout {
        mConstraintSet.constrainPercentWidth(id, width)
        return this
    }

    fun clPercentHeight(height: Float): JJCoordinatorLayout {
        mConstraintSet.constrainPercentHeight(id, height)
        return this
    }

    fun clCenterInParent(): JJCoordinatorLayout {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterInParent(verticalBias: Float, horizontalBias: Float, margin: JJMargin): JJCoordinatorLayout {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin.left)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin.right)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin.top)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin.bottom)
        mConstraintSet.setVerticalBias(id, verticalBias)
        mConstraintSet.setHorizontalBias(id, horizontalBias)
        return this
    }

    fun clCenterInParentVertically(): JJCoordinatorLayout {
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        return this
    }

    fun clCenterInParentHorizontally(): JJCoordinatorLayout {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterInParentVertically(bias: Float, topMargin: Int, bottomMargin: Int): JJCoordinatorLayout {
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
        mConstraintSet.setVerticalBias(id, bias)
        return this
    }

    fun clCenterInParentHorizontally(bias: Float, startMargin: Int, endtMargin: Int): JJCoordinatorLayout {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, startMargin)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, endtMargin)
        mConstraintSet.setHorizontalBias(id, bias)
        return this
    }


    fun clCenterInParentTopVertically(): JJCoordinatorLayout {
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        return this
    }


    fun clCenterInParentBottomVertically(): JJCoordinatorLayout {
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        return this
    }

    fun clCenterInParentStartHorizontally(): JJCoordinatorLayout {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterInParentEndHorizontally(): JJCoordinatorLayout {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterInTopVertically(topId: Int): JJCoordinatorLayout {
        mConstraintSet.connect(id, ConstraintSet.TOP, topId, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, topId, ConstraintSet.TOP, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        return this
    }


    fun clCenterInBottomVertically(bottomId: Int): JJCoordinatorLayout {
        mConstraintSet.connect(id, ConstraintSet.TOP, bottomId, ConstraintSet.BOTTOM, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, bottomId, ConstraintSet.BOTTOM, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        return this
    }

    fun clCenterInStartHorizontally(startId: Int): JJCoordinatorLayout {
        mConstraintSet.connect(id, ConstraintSet.START, startId, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, startId, ConstraintSet.START, 0)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterInEndHorizontally(endId: Int): JJCoordinatorLayout {
        mConstraintSet.connect(id, ConstraintSet.START, endId, ConstraintSet.END, 0)
        mConstraintSet.connect(id, ConstraintSet.END, endId, ConstraintSet.END, 0)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterVertically(topId: Int, topSide: Int, topMargin: Int, bottomId: Int, bottomSide: Int, bottomMargin: Int, bias: Float): JJCoordinatorLayout {
        mConstraintSet.centerVertically(id, topId, topSide, topMargin, bottomId, bottomSide, bottomMargin, bias)
        return this
    }

    fun clCenterHorizontally(startId: Int, startSide: Int, startMargin: Int, endId: Int, endSide: Int, endMargin: Int, bias: Float): JJCoordinatorLayout {
        mConstraintSet.centerHorizontally(id, startId, startSide, startMargin, endId, endSide, endMargin, bias)
        return this
    }


    fun clFillParent(): JJCoordinatorLayout {
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.constrainHeight(id,0)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        return this
    }

    fun clFillParent(margin: JJMargin): JJCoordinatorLayout {
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.constrainHeight(id,0)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin.top)
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin.left)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin.right)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin.bottom)
        return this
    }

    fun clFillParentHorizontally(): JJCoordinatorLayout {
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        return this
    }

    fun clFillParentVertically(): JJCoordinatorLayout {
        mConstraintSet.constrainHeight(id,0)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        return this
    }

    fun clFillParentHorizontally(startMargin: Int, endMargin: Int): JJCoordinatorLayout {
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, startMargin)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, endMargin)
        return this
    }

    fun clFillParentVertically(topMargin: Int, bottomMargin: Int): JJCoordinatorLayout {
        mConstraintSet.constrainHeight(id,0)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
        return this
    }

    fun clVisibility(visibility: Int): JJCoordinatorLayout {
        mConstraintSet.setVisibility(id, visibility)
        return this
    }

    fun clElevation(elevation: Float): JJCoordinatorLayout {
        mConstraintSet.setElevation(id, elevation)

        return this
    }

    //endregion

    //region LinearLayout params

    private var mLlp: LinearLayout.LayoutParams? = null
    private fun setupLlp() {
        if (mLlp == null) {
            mLlp = layoutParams as?  LinearLayout.LayoutParams
            layoutParams = mLlp
        }
    }

    fun llWidth(width: Int): JJCoordinatorLayout {
        setupLlp()
        mLlp!!.width = width
        return this
    }

    fun llHeight(height: Int): JJCoordinatorLayout {
        setupLlp()
        mLlp!!.height = height
        return this
    }

    fun llWeight(weigth: Float): JJCoordinatorLayout {
        setupLlp()
        mLlp!!.weight = weigth
        return this
    }

    fun llGravity(gravity: Int): JJCoordinatorLayout {
        setupLlp()
        mLlp!!.gravity = gravity
        return this
    }

    //endregion

    //region ScrollView params
    private var mSvp: FrameLayout.LayoutParams? = null

    private fun setupSvp() {
        if (mSvp == null) {
            mSvp = layoutParams as? FrameLayout.LayoutParams
            layoutParams = mSvp
        }
    }

    fun svWidth(width: Int): JJCoordinatorLayout {
        setupSvp()
        mSvp!!.width = width
        return this
    }

    fun svHeight(height: Int): JJCoordinatorLayout {
        setupSvp()
        mSvp!!.height = height
        return this
    }
    //endregion


    //region MotionLayout Params

    var mMotionConstraintSet: ConstraintSet? = null


    fun mlVisibilityMode(visibility: Int): JJCoordinatorLayout {
        mMotionConstraintSet?.setVisibilityMode(id, visibility)
        return this
    }

    fun mlVerticalBias(float: Float): JJCoordinatorLayout {
        mMotionConstraintSet?.setVerticalBias(id,float)
        return this
    }
    fun mlHorizontalBias(float: Float): JJCoordinatorLayout {
        mMotionConstraintSet?.setHorizontalBias(id,float)
        return this
    }

    fun mlCenterHorizontallyOf(viewId: Int, marginStart: Int = 0, marginEnd: Int = 0): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.START, marginStart)
        mMotionConstraintSet?.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.END, marginEnd)
        mMotionConstraintSet?.setHorizontalBias(viewId,0.5f)
        return this
    }
    fun mlCenterVerticallyOf(viewId: Int,marginTop: Int = 0, marginBottom: Int = 0): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, marginTop)
        mMotionConstraintSet?.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, marginBottom)
        mMotionConstraintSet?.setVerticalBias(viewId,0.5f)
        return this
    }

    fun mlMargins(margins: JJMargin) : JJCoordinatorLayout {
        mMotionConstraintSet?.setMargin(id,ConstraintSet.TOP,margins.top)
        mMotionConstraintSet?.setMargin(id,ConstraintSet.BOTTOM,margins.bottom)
        mMotionConstraintSet?.setMargin(id,ConstraintSet.END,margins.right)
        mMotionConstraintSet?.setMargin(id,ConstraintSet.START,margins.left)
        return this
    }


    fun mlFloatCustomAttribute(attrName: String, value: Float): JJCoordinatorLayout {
        mMotionConstraintSet?.setFloatValue(id,attrName,value)
        return this
    }

    fun mlIntCustomAttribute(attrName: String, value: Int): JJCoordinatorLayout {
        mMotionConstraintSet?.setIntValue(id,attrName,value)
        return this
    }

    fun mlColorCustomAttribute(attrName: String, value: Int): JJCoordinatorLayout {
        mMotionConstraintSet?.setColorValue(id,attrName,value)
        return this
    }

    fun mlStringCustomAttribute(attrName: String, value: String): JJCoordinatorLayout {
        mMotionConstraintSet?.setStringValue(id,attrName,value)
        return this
    }

    fun mlRotation(float: Float): JJCoordinatorLayout {
        mMotionConstraintSet?.setRotation(id,float)
        return this
    }

    fun mlRotationX(float: Float): JJCoordinatorLayout {
        mMotionConstraintSet?.setRotationX(id,float)
        return this
    }

    fun mlRotationY(float: Float): JJCoordinatorLayout {
        mMotionConstraintSet?.setRotationY(id,float)
        return this
    }

    fun mlTranslation(x: Float,y: Float): JJCoordinatorLayout {
        mMotionConstraintSet?.setTranslation(id,x,y)
        return this
    }
    fun mlTranslationX(x: Float): JJCoordinatorLayout {
        mMotionConstraintSet?.setTranslationX(id,x)
        return this
    }

    fun mlTranslationY(y: Float): JJCoordinatorLayout {
        mMotionConstraintSet?.setTranslationY(id,y)
        return this
    }

    fun mlTranslationZ(z: Float): JJCoordinatorLayout {
        mMotionConstraintSet?.setTranslationZ(id,z)
        return this
    }

    fun mlTransformPivot(x: Float, y: Float): JJCoordinatorLayout {
        mMotionConstraintSet?.setTransformPivot(id,x,y)
        return this
    }

    fun mlTransformPivotX(x: Float): JJCoordinatorLayout {
        mMotionConstraintSet?.setTransformPivotX(id,x)
        return this
    }

    fun mlTransformPivotY(y: Float): JJCoordinatorLayout {
        mMotionConstraintSet?.setTransformPivotY(id,y)
        return this
    }

    fun mlScaleX(x: Float): JJCoordinatorLayout {
        mMotionConstraintSet?.setScaleX(id,x)
        return this
    }

    fun mlScaleY(y: Float): JJCoordinatorLayout {
        mMotionConstraintSet?.setScaleY(id,y)
        return this
    }

    fun mlDimensionRatio(ratio: String): JJCoordinatorLayout {
        mMotionConstraintSet?.setDimensionRatio(id,ratio)
        return this
    }

    fun mlAlpha(alpha: Float): JJCoordinatorLayout {
        mMotionConstraintSet?.setAlpha(id,alpha)
        return this
    }



    fun mlTopToTop(viewId: Int, margin: Int = 0): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, margin)
        return this
    }

    fun mlTopToTopParent(margin: Int = 0): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin)
        return this
    }


    fun mlTopToBottomOf(viewId: Int, margin: Int = 0): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun mlTopToBottomParent(margin: Int = 0): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun mlBottomToTopOf(viewId: Int, margin: Int = 0): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.TOP, margin)

        return this
    }

    fun mlBottomToTopParent(margin: Int = 0): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin)

        return this
    }

    fun mlBottomToBottomOf(viewId: Int, margin: Int = 0): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, margin)

        return this
    }

    fun mlBottomToBottomParent(margin: Int = 0): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin)

        return this
    }

    fun mlStartToStartOf(viewId: Int, margin: Int = 0): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.START, margin)

        return this
    }

    fun mlStartToStartParent(margin: Int = 0): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin)

        return this
    }

    fun mlStartToEndOf(viewId: Int, margin: Int = 0): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.END, margin)

        return this
    }

    fun mlStartToEndParent(margin: Int = 0): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END, margin)

        return this
    }

    fun mlEndToEndOf(viewId: Int, margin: Int= 0): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.END, margin)

        return this
    }

    fun mlEndToEndParent(margin: Int = 0): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin)

        return this
    }


    fun mlEndToStartOf(viewId: Int, margin: Int = 0): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.START, margin)
        return this
    }

    fun mlEndToStartParent(margin: Int = 0): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START, margin)
        return this
    }


    fun mlWidth(width: Int): JJCoordinatorLayout {
        mMotionConstraintSet?.constrainWidth(id, width)
        return this
    }

    fun mlHeight(height: Int): JJCoordinatorLayout {
        mMotionConstraintSet?.constrainHeight(id, height)
        return this
    }

    fun mlPercentWidth(width: Float): JJCoordinatorLayout {
        mMotionConstraintSet?.constrainPercentWidth(id, width)
        return this
    }

    fun mlPercentHeight(height: Float): JJCoordinatorLayout {
        mMotionConstraintSet?.constrainPercentHeight(id, height)
        return this
    }

    fun mlCenterInParent(): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)

        return this
    }

    fun mlCenterInParent(verticalBias: Float, horizontalBias: Float, margin: JJMargin): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin.left)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin.right)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin.top)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin.bottom)
        mMotionConstraintSet?.setVerticalBias(id, verticalBias)
        mMotionConstraintSet?.setHorizontalBias(id, horizontalBias)
        return this
    }

    fun mlCenterInParentVertically(): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)

        return this
    }

    fun mlCenterInParentHorizontally(): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)
        return this
    }

    fun mlCenterInParentVertically(bias: Float, topMargin: Int, bottomMargin: Int): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
        mMotionConstraintSet?.setVerticalBias(id, bias)
        return this
    }

    fun mlCenterInParentHorizontally(bias: Float, startMargin: Int, endtMargin: Int): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, startMargin)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, endtMargin)
        mMotionConstraintSet?.setHorizontalBias(id, bias)
        return this
    }


    fun mlCenterInParentTopVertically(): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)
        return this
    }


    fun mlCenterInParentBottomVertically(): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)
        return this
    }

    fun mlCenterInParentStartHorizontally(): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)
        return this
    }

    fun mlCenterInParentEndHorizontally(): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)
        return this
    }

    fun mlCenterInTopVerticallyOf(viewId: Int): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, viewId, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)
        return this
    }


    fun mlCenterInBottomVerticallyOf(viewId: Int): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, viewId, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)
        return this
    }

    fun mlCenterInStartHorizontallyOf(viewId: Int): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, viewId, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, viewId, ConstraintSet.START, 0)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)
        return this
    }

    fun mlCenterInEndHorizontallyOf(viewId: Int): JJCoordinatorLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, viewId, ConstraintSet.END, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, viewId, ConstraintSet.END, 0)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)
        return this
    }

    fun mlCenterVertically(topId: Int, topSide: Int, topMargin: Int, bottomId: Int, bottomSide: Int, bottomMargin: Int, bias: Float): JJCoordinatorLayout {
        mMotionConstraintSet?.centerVertically(id, topId, topSide, topMargin, bottomId, bottomSide, bottomMargin, bias)
        return this
    }

    fun mlCenterHorizontally(startId: Int, startSide: Int, startMargin: Int, endId: Int, endSide: Int, endMargin: Int, bias: Float): JJCoordinatorLayout {
        mMotionConstraintSet?.centerHorizontally(id, startId, startSide, startMargin, endId, endSide, endMargin, bias)
        return this
    }


    fun mlFillParent(): JJCoordinatorLayout {
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.constrainHeight(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        return this
    }

    fun mlFillParent(margin: JJMargin): JJCoordinatorLayout {
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.constrainHeight(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin.top)
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin.left)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin.right)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin.bottom)
        return this
    }

    fun mlFillParentHorizontally(): JJCoordinatorLayout {
        mConstraintSet.constrainWidth(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        return this
    }

    fun mlFillParentVertically(): JJCoordinatorLayout {
        mConstraintSet.constrainHeight(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        return this
    }

    fun mlFillParentHorizontally(startMargin: Int, endMargin: Int): JJCoordinatorLayout {
        mConstraintSet.constrainWidth(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, startMargin)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, endMargin)
        return this
    }

    fun mlFillParentVertically(topMargin: Int, bottomMargin: Int): JJCoordinatorLayout {
        mConstraintSet.constrainHeight(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
        return this
    }

    fun mlVisibility(visibility: Int): JJCoordinatorLayout {
        mMotionConstraintSet?.setVisibility(id, visibility)
        return this
    }

    fun mlElevation(elevation: Float): JJCoordinatorLayout {
        mMotionConstraintSet?.setElevation(id, elevation)
        return this
    }

    fun mlApply(): JJCoordinatorLayout {
        mMotionConstraintSet?.applyTo(parent as ConstraintLayout)
        return this
    }

    fun mlSetConstraint(cs : ConstraintSet?): JJCoordinatorLayout {
        mMotionConstraintSet = cs
        return this
    }

    fun mlDisposeConstraint(): JJCoordinatorLayout {
        mMotionConstraintSet = null
        return this
    }

    //endregion

}