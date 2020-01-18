package com.jjlf.library_layout.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.motion.widget.MotionLayout

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.jjlf.library_layout.R
import com.jjlf.jjkit_layoututils.JJScreen
import com.jjlf.jjkit_layoututils.JJMargin
import com.jjlf.jjkit_layoututils.JJPadding

open class JJRelativeLayout : RelativeLayout {

    //region init

    constructor(context: Context) : super(context) {
        this.id = View.generateViewId()
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.constrainHeight(id,0)
        mConstraintSetLandScape.constrainWidth(id,0)
        mConstraintSetLandScape.constrainHeight(id,0)
        mInit = false
    }

    private var mIgnoreCl = false
    private var mSupportLandScape = false
    @SuppressLint("ResourceType")
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){

        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.constrainHeight(id,0)

        mConstraintSetLandScape.constrainWidth(id,0)
        mConstraintSetLandScape.constrainHeight(id,0)

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
            R.styleable.JJRelativeLayout, 0, 0)


        mIgnoreCl = a.getBoolean(R.styleable.JJRelativeLayout_layout_ignoreCl,false)

        mConfigurationChanged = a.getBoolean(R.styleable.JJRelativeLayout_support_configuration_changed,false)
        mSupportLandScape = a.getBoolean(R.styleable.JJRelativeLayout_support_landscape,false)

        //region attrs portrait

        val aFillParent = a.getBoolean(R.styleable.JJRelativeLayout_clFillParent,false)
        val aFillParentHorizontal = a.getBoolean(R.styleable.JJRelativeLayout_clFillParentHorizontally,false)
        val aFillParentVertical = a.getBoolean(R.styleable.JJRelativeLayout_clFillParentVertically,false)

        val aCenterInParent = a.getBoolean(R.styleable.JJRelativeLayout_clCenterInParent,false)
        val aCenterInParentHorizontal = a.getBoolean(R.styleable.JJRelativeLayout_clCenterInParentHorizontally,false)
        val aCenterInParentVertical = a.getBoolean(R.styleable.JJRelativeLayout_clCenterInParentVertically,false)
        val aCenterInParentTopVertical = a.getBoolean(R.styleable.JJRelativeLayout_clCenterInParentTopVertically,false)
        val aCenterInParentBottomVertical = a.getBoolean(R.styleable.JJRelativeLayout_clCenterInParentBottomVertically,false)
        val aCenterInParentStartHorizontal = a.getBoolean(R.styleable.JJRelativeLayout_clCenterInParentStartHorizontally,false)
        val aCenterInParentEndHorizontal = a.getBoolean(R.styleable.JJRelativeLayout_clCenterInParentEndHorizontally,false)

        val aCenterInTopVerticalOf = a.getResourceId(R.styleable.JJRelativeLayout_clCenterInTopVerticallyOf,View.NO_ID)
        val aCenterInBottomVerticalOf = a.getResourceId(R.styleable.JJRelativeLayout_clCenterInBottomVerticallyOf,View.NO_ID)
        val aCenterInStartHorizontalOf= a.getResourceId(R.styleable.JJRelativeLayout_clCenterInStartHorizontallyOf,View.NO_ID)
        val aCenterInEndHorizontalOf = a.getResourceId(R.styleable.JJRelativeLayout_clCenterInEndHorizontallyOf,View.NO_ID)

        val aCenterVerticalOf = a.getResourceId(R.styleable.JJRelativeLayout_clCenterVerticallyOf,View.NO_ID)
        val aCenterHorizontalOf = a.getResourceId(R.styleable.JJRelativeLayout_clCenterHorizontallyOf,View.NO_ID)

        val aMarginEnd = a.getDimension(R.styleable.JJRelativeLayout_clMarginEnd,0f)
        val aMarginStart = a.getDimension(R.styleable.JJRelativeLayout_clMarginStart,0f)
        val aMarginTop = a.getDimension(R.styleable.JJRelativeLayout_clMarginTop,0f)
        val aMarginBottom = a.getDimension(R.styleable.JJRelativeLayout_clMarginBottom,0f)
        val aMarginEndPercent = a.getFloat(R.styleable.JJRelativeLayout_clMarginEndPercentScreenHeight,0f)
        val aMarginStartPercent = a.getFloat(R.styleable.JJRelativeLayout_clMarginStartPercentScreenHeight,0f)
        val aMarginTopPercent = a.getFloat(R.styleable.JJRelativeLayout_clMarginTopPercentScreenHeight,0f)
        val aMarginBottomPercent = a.getFloat(R.styleable.JJRelativeLayout_clMarginBottomPercentScreenHeight,0f)
        val aMarginEndPercentWidth = a.getFloat(R.styleable.JJRelativeLayout_clMarginEndPercentScreenWidth,0f)
        val aMarginStartPercentWidth = a.getFloat(R.styleable.JJRelativeLayout_clMarginStartPercentScreenWidth,0f)
        val aMarginTopPercentWidth = a.getFloat(R.styleable.JJRelativeLayout_clMarginTopPercentScreenWidth,0f)
        val aMarginBottomPercentWidth = a.getFloat(R.styleable.JJRelativeLayout_clMarginBottomPercentScreenWidth,0f)

        val aMargin = a.getDimension(R.styleable.JJRelativeLayout_clMargin,0f)
        val aMarginPercentHeight = a.getFloat(R.styleable.JJRelativeLayout_clMarginPerScHeight,0f)
        val aMarginPercentWidth = a.getFloat(R.styleable.JJRelativeLayout_clMarginPerScWidth,0f)
        val aMarginResponsive = a.getResourceId(R.styleable.JJRelativeLayout_clMarginResponsive,NO_ID)
        val aMarginResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_clMarginResPerScHeight,NO_ID)
        val aMarginResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_clMarginResPerScWidth,NO_ID)

        val aMarginEndResponsive = a.getResourceId(R.styleable.JJRelativeLayout_clMarginEndResponsive,NO_ID)
        val aMarginStartResponsive = a.getResourceId(R.styleable.JJRelativeLayout_clMarginStartResponsive,NO_ID)
        val aMarginTopResponsive = a.getResourceId(R.styleable.JJRelativeLayout_clMarginTopResponsive,NO_ID)
        val aMarginBottomResponsive = a.getResourceId(R.styleable.JJRelativeLayout_clMarginBottomResponsive,NO_ID)
        val aMarginEndResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_clMarginEndResPerScHeight,NO_ID)
        val aMarginStartResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_clMarginStartResPerScHeight,NO_ID)
        val aMarginTopResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_clMarginTopResPerScHeight,NO_ID)
        val aMarginBottomResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_clMarginBottomResPerScHeight,NO_ID)
        val aMarginEndResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_clMarginEndResPerScWidth,NO_ID)
        val aMarginStartResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_clMarginStartResPerScWidth,NO_ID)
        val aMarginTopResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_clMarginTopResPerScWidth,NO_ID)
        val aMarginBottomResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_clMarginBottomResPerScWidth,NO_ID)

        val aMarginVertical = a.getDimension(R.styleable.JJRelativeLayout_clMarginVertical,0f)
        val aMarginVerticalPercentHeight = a.getFloat(R.styleable.JJRelativeLayout_clMarginVerticalPerScHeight,0f)
        val aMarginVerticalPercentWidth = a.getFloat(R.styleable.JJRelativeLayout_clMarginVerticalPerScWidth,0f)
        val aMarginVerticalResponsive = a.getResourceId(R.styleable.JJRelativeLayout_clMarginVerticalResponsive,NO_ID)
        val aMarginVerticalResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_clMarginVerticalResPerScHeight,NO_ID)
        val aMarginVerticalResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_clMarginVerticalResPerScWidth,NO_ID)

        val aMarginHorizontal = a.getDimension(R.styleable.JJRelativeLayout_clMarginHorizontal,0f)
        val aMarginHorizontalPercentHeight = a.getFloat(R.styleable.JJRelativeLayout_clMarginHorizontalPerScHeight,0f)
        val aMarginHorizontalPercentWidth = a.getFloat(R.styleable.JJRelativeLayout_clMarginHorizontalPerScWidth,0f)
        val aMarginHorizontalResponsive = a.getResourceId(R.styleable.JJRelativeLayout_clMarginHorizontalResponsive,NO_ID)
        val aMarginHorizontalResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_clMarginHorizontalResPerScHeight,NO_ID)
        val aMarginHorizontalResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_clMarginHorizontalResPerScWidth,NO_ID)


        val aVerticalBias = a.getFloat(R.styleable.JJRelativeLayout_clVerticalBias,0.5f)
        val aHorizontalBias = a.getFloat(R.styleable.JJRelativeLayout_clHorizontalBias,0.5f)

        val aStartToStartParent = a.getBoolean(R.styleable.JJRelativeLayout_clStartToStartParent,false)
        val aStartToEndParent = a.getBoolean(R.styleable.JJRelativeLayout_clStartToEndParent,false)
        val aEndToEndParent = a.getBoolean(R.styleable.JJRelativeLayout_clEndToEndParent,false)
        val aEndToStartParent = a.getBoolean(R.styleable.JJRelativeLayout_clEndToStartParent,false)
        val aTopToTopParent = a.getBoolean(R.styleable.JJRelativeLayout_clTopToTopParent,false)
        val aTopToBottomParent = a.getBoolean(R.styleable.JJRelativeLayout_clTopToBottomParent,false)
        val aBottomToBottomParent = a.getBoolean(R.styleable.JJRelativeLayout_clBottomToBottomParent,false)
        val aBottomToTopParent = a.getBoolean(R.styleable.JJRelativeLayout_clBottomToTopParent,false)

        val aStartToStartOf = a.getResourceId(R.styleable.JJRelativeLayout_clStartToStartOf,View.NO_ID)
        val aStartToEndOf = a.getResourceId(R.styleable.JJRelativeLayout_clStartToEndOf,View.NO_ID)
        val aEndToEndOf = a.getResourceId(R.styleable.JJRelativeLayout_clEndToEndOf,View.NO_ID)
        val aEndToStartOf = a.getResourceId(R.styleable.JJRelativeLayout_clEndToStartOf,View.NO_ID)
        val aTopToTopOf = a.getResourceId(R.styleable.JJRelativeLayout_clTopToTopOf,View.NO_ID)
        val aTopToBottomOf = a.getResourceId(R.styleable.JJRelativeLayout_clTopToBottomOf,View.NO_ID)
        val aBottomToBottomOf = a.getResourceId(R.styleable.JJRelativeLayout_clBottomToBottomOf,View.NO_ID)
        val aBottomToTopOf = a.getResourceId(R.styleable.JJRelativeLayout_clBottomToTopOf,View.NO_ID)

        val aHeightPercent = a.getFloat(R.styleable.JJRelativeLayout_clHeightPercent,0f)
        val aWidthPercent = a.getFloat(R.styleable.JJRelativeLayout_clWidthPercent,0f)
        val aHeightPercentScreenWidth = a.getFloat(R.styleable.JJRelativeLayout_clHeightPercentScreenWidth,0f)
        val aWidthPercentScreenWidth = a.getFloat(R.styleable.JJRelativeLayout_clWidthPercentScreenWidth,0f)
        val aHeightPercentScreenHeight = a.getFloat(R.styleable.JJRelativeLayout_clHeightPercentScreenHeight,0f)
        val aWidthPercentScreenHeight = a.getFloat(R.styleable.JJRelativeLayout_clWidthPercentScreenHeight,0f)

        val aHeightResponsive = a.getResourceId(R.styleable.JJRelativeLayout_clHeightResponsive,View.NO_ID)
        val aWidthResponsive = a.getResourceId(R.styleable.JJRelativeLayout_clWidthResponsive,View.NO_ID)

        val aHeightResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_clHeightResponsivePercentScreenHeight,View.NO_ID)
        val aWidthResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_clWidthResponsivePercentScreenWidth,View.NO_ID)
        val aHeightResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_clHeightResponsivePercentScreenWidth,View.NO_ID)
        val aWidthResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_clWidthResponsivePercentScreenHeight,View.NO_ID)

        val lHeightPercentScreenWidth = a.getFloat(R.styleable.JJRelativeLayout_lpHeightPercentScreenWidth,0f)
        val lWidthPercentScreenWidth = a.getFloat(R.styleable.JJRelativeLayout_lpWidthPercentScreenWidth,0f)
        val lHeightPercentScreenHeight = a.getFloat(R.styleable.JJRelativeLayout_lpHeightPercentScreenHeight,0f)
        val lWidthPercentScreenHeight = a.getFloat(R.styleable.JJRelativeLayout_lpWidthPercentScreenHeight,0f)

        val lHeightResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lpHeightResponsive,View.NO_ID)
        val lWidthResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lpWidthResponsive,View.NO_ID)
        val lHeightResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lpHeightResponsivePercentScreenHeight,View.NO_ID)
        val lWidthResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lpWidthResponsivePercentScreenWidth,View.NO_ID)
        val lHeightResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lpHeightResponsivePercentScreenWidth,View.NO_ID)
        val lWidthResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lpWidthResponsivePercentScreenHeight,View.NO_ID)

        val lMarginTopPercentScHeight = a.getFloat(R.styleable.JJRelativeLayout_lpMarginTopPerScHeight,0f)
        val lMarginLeftPercentScHeight = a.getFloat(R.styleable.JJRelativeLayout_lpMarginLeftPerScHeight,0f)
        val lMarginRightPercentScHeight = a.getFloat(R.styleable.JJRelativeLayout_lpMarginRightPerScHeight,0f)
        val lMarginBottomPercentScHeight = a.getFloat(R.styleable.JJRelativeLayout_lpMarginBottomPerScHeight,0f)

        val lMarginTopPercentScWidth = a.getFloat(R.styleable.JJRelativeLayout_lpMarginTopPerScWidth,0f)
        val lMarginLeftPercentScWidth = a.getFloat(R.styleable.JJRelativeLayout_lpMarginLeftPerScWidth,0f)
        val lMarginRightPercentScWidth = a.getFloat(R.styleable.JJRelativeLayout_lpMarginRightPerScWidth,0f)
        val lMarginBottomPercentScWidth = a.getFloat(R.styleable.JJRelativeLayout_lpMarginBottomPerScWidth,0f)

        val lMarginTopResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lpMarginTopResponsive,View.NO_ID)
        val lMarginLeftResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lpMarginLeftResponsive,View.NO_ID)
        val lMarginRightResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lpMarginRightResponsive,View.NO_ID)
        val lMarginBottomResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lpMarginBottomResponsive,View.NO_ID)

        val lMarginTopResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lpMarginTopResPerScWidth,View.NO_ID)
        val lMarginLeftResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lpMarginLeftResPerScWidth,View.NO_ID)
        val lMarginRightResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lpMarginRightResPerScWidth,View.NO_ID)
        val lMarginBottomResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lpMarginBottomResPerScWidth,View.NO_ID)

        val lMarginTopResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lpMarginTopResPerScHeight,View.NO_ID)
        val lMarginLeftResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lpMarginLeftResPerScHeight,View.NO_ID)
        val lMarginRightResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lpMarginRightResPerScHeight,View.NO_ID)
        val lMarginBottomResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lpMarginBottomResPerScHeight,View.NO_ID)

        val lMarginPerScHeight = a.getFloat(R.styleable.JJRelativeLayout_lpMarginPercentScHeight,0f)
        val lMarginPerScWidth = a.getFloat(R.styleable.JJRelativeLayout_lpMarginPercentScWidth,0f)
        val lMarginResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lpMarginResponsive,View.NO_ID)
        val lMarginResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lpMarginResPerScHeight,View.NO_ID)
        val lMarginResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lpMarginResPerScWidth,View.NO_ID)

        val lMarginVerticalPerScHeight = a.getFloat(R.styleable.JJRelativeLayout_lpMarginVerticalPerScHeight,0f)
        val lMarginVerticalPerScWidth = a.getFloat(R.styleable.JJRelativeLayout_lpMarginVerticalPerScWidth,0f)
        val lMarginVerticalResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lpMarginVerticalResponsive,View.NO_ID)
        val lMarginVerticalResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lpMarginVerticalResPerScWidth,View.NO_ID)
        val lMarginVerticalResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lpMarginVerticalResPerScHeight,View.NO_ID)

        val lMarginHorizontalPerScHeight = a.getFloat(R.styleable.JJRelativeLayout_lpMarginHorizontalPerScHeight,0f)
        val lMarginHorizontalPerScWidth = a.getFloat(R.styleable.JJRelativeLayout_lpMarginHorizontalPerScWidth,0f)
        val lMarginHorizontalResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lpMarginHorizontalResponsive,View.NO_ID)
        val lMarginHorizontalResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lpMarginHorizontalResPerScWidth,View.NO_ID)
        val lMarginHorizontalResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lpMarginHorizontalResPerScHeight,View.NO_ID)


        val lPaddingTopPercentScHeight = a.getFloat(R.styleable.JJRelativeLayout_lpPaddingTopPerScHeight,0f)
        val lPaddingLeftPercentScHeight = a.getFloat(R.styleable.JJRelativeLayout_lpPaddingLeftPerScHeight,0f)
        val lPaddingRightPercentScHeight = a.getFloat(R.styleable.JJRelativeLayout_lpPaddingRightPerScHeight,0f)
        val lPaddingBottomPercentScHeight = a.getFloat(R.styleable.JJRelativeLayout_lpPaddingBottomPerScHeight,0f)

        val lPaddingTopPercentScWidth = a.getFloat(R.styleable.JJRelativeLayout_lpPaddingTopPerScWidth,0f)
        val lPaddingLeftPercentScWidth = a.getFloat(R.styleable.JJRelativeLayout_lpPaddingLeftPerScWidth,0f)
        val lPaddingRightPercentScWidth = a.getFloat(R.styleable.JJRelativeLayout_lpPaddingRightPerScWidth,0f)
        val lPaddingBottomPercentScWidth = a.getFloat(R.styleable.JJRelativeLayout_lpPaddingBottomPerScWidth,0f)

        val lPaddingTopResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lpPaddingTopResponsive,View.NO_ID)
        val lPaddingLeftResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lpPaddingLeftResponsive,View.NO_ID)
        val lPaddingRightResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lpPaddingRightResponsive,View.NO_ID)
        val lPaddingBottomResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lpPaddingBottomResponsive,View.NO_ID)

        val lPaddingTopResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lpPaddingTopResPerScWidth,View.NO_ID)
        val lPaddingLeftResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lpPaddingLeftResPerScWidth,View.NO_ID)
        val lPaddingRightResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lpPaddingRightResPerScWidth,View.NO_ID)
        val lPaddingBottomResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lpPaddingBottomResPerScWidth,View.NO_ID)

        val lPaddingTopResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lpPaddingTopResPerScHeight,View.NO_ID)
        val lPaddingLeftResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lpPaddingLeftResPerScHeight,View.NO_ID)
        val lPaddingRightResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lpPaddingRightResPerScHeight,View.NO_ID)
        val lPaddingBottomResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lpPaddingBottomResPerScHeight,View.NO_ID)

        val lPaddingPerScHeight = a.getFloat(R.styleable.JJRelativeLayout_lpPaddingPercentScHeight,0f)
        val lPaddingPerScWidth = a.getFloat(R.styleable.JJRelativeLayout_lpPaddingPercentScWidth,0f)
        val lPaddingResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lpPaddingResponsive,View.NO_ID)
        val lPaddingResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lpPaddingResPerScHeight,View.NO_ID)
        val lPaddingResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lpPaddingResPerScWidth,View.NO_ID)

        val lPaddingVerticalPerScHeight = a.getFloat(R.styleable.JJRelativeLayout_lpPaddingVerticalPerScHeight,0f)
        val lPaddingVerticalPerScWidth = a.getFloat(R.styleable.JJRelativeLayout_lpPaddingVerticalPerScWidth,0f)
        val lPaddingVerticalResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lpPaddingVerticalResponsive,View.NO_ID)
        val lPaddingVerticalResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lpPaddingVerticalResPerScWidth,View.NO_ID)
        val lPaddingVerticalResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lpPaddingVerticalResPerScHeight,View.NO_ID)

        val lPaddingHorizontalPerScHeight = a.getFloat(R.styleable.JJRelativeLayout_lpPaddingHorizontalPerScHeight,0f)
        val lPaddingHorizontalPerScWidth = a.getFloat(R.styleable.JJRelativeLayout_lpPaddingHorizontalPerScWidth,0f)
        val lPaddingHorizontalResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lpPaddingHorizontalResponsive,View.NO_ID)
        val lPaddingHorizontalResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lpPaddingHorizontalResPerScWidth,View.NO_ID)
        val lPaddingHorizontalResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lpPaddingHorizontalResPerScHeight,View.NO_ID)


        //endregion

        //region attrs landscape

        val alsFillParent = a.getBoolean(R.styleable.JJRelativeLayout_cllFillParent,false)
        val alsFillParentHorizontal = a.getBoolean(R.styleable.JJRelativeLayout_cllFillParentHorizontally,false)
        val alsFillParentVertical = a.getBoolean(R.styleable.JJRelativeLayout_cllFillParentVertically,false)

        val alsCenterInParent = a.getBoolean(R.styleable.JJRelativeLayout_cllCenterInParent,false)
        val alsCenterInParentHorizontal = a.getBoolean(R.styleable.JJRelativeLayout_cllCenterInParentHorizontally,false)
        val alsCenterInParentVertical = a.getBoolean(R.styleable.JJRelativeLayout_cllCenterInParentVertically,false)
        val alsCenterInParentTopVertical = a.getBoolean(R.styleable.JJRelativeLayout_cllCenterInParentTopVertically,false)
        val alsCenterInParentBottomVertical = a.getBoolean(R.styleable.JJRelativeLayout_cllCenterInParentBottomVertically,false)
        val alsCenterInParentStartHorizontal = a.getBoolean(R.styleable.JJRelativeLayout_cllCenterInParentStartHorizontally,false)
        val alsCenterInParentEndHorizontal = a.getBoolean(R.styleable.JJRelativeLayout_cllCenterInParentEndHorizontally,false)

        val alsCenterInTopVerticalOf = a.getResourceId(R.styleable.JJRelativeLayout_cllCenterInTopVerticallyOf,View.NO_ID)
        val alsCenterInBottomVerticalOf = a.getResourceId(R.styleable.JJRelativeLayout_cllCenterInBottomVerticallyOf,View.NO_ID)
        val alsCenterInStartHorizontalOf= a.getResourceId(R.styleable.JJRelativeLayout_cllCenterInStartHorizontallyOf,View.NO_ID)
        val alsCenterInEndHorizontalOf = a.getResourceId(R.styleable.JJRelativeLayout_cllCenterInEndHorizontallyOf,View.NO_ID)

        val alsCenterVerticalOf = a.getResourceId(R.styleable.JJRelativeLayout_cllCenterVerticallyOf,View.NO_ID)
        val alsCenterHorizontalOf = a.getResourceId(R.styleable.JJRelativeLayout_cllCenterHorizontallyOf,View.NO_ID)

        val alsMarginEnd = a.getDimension(R.styleable.JJRelativeLayout_cllMarginEnd,0f)
        val alsMarginStart = a.getDimension(R.styleable.JJRelativeLayout_cllMarginStart,0f)
        val alsMarginTop = a.getDimension(R.styleable.JJRelativeLayout_cllMarginTop,0f)
        val alsMarginBottom = a.getDimension(R.styleable.JJRelativeLayout_cllMarginBottom,0f)
        val alsMarginEndPercent = a.getFloat(R.styleable.JJRelativeLayout_cllMarginEndPercentScreenHeight,0f)
        val alsMarginStartPercent = a.getFloat(R.styleable.JJRelativeLayout_cllMarginStartPercentScreenHeight,0f)
        val alsMarginTopPercent = a.getFloat(R.styleable.JJRelativeLayout_cllMarginTopPercentScreenHeight,0f)
        val alsMarginBottomPercent = a.getFloat(R.styleable.JJRelativeLayout_cllMarginBottomPercentScreenHeight,0f)
        val alsMarginEndPercentWidth = a.getFloat(R.styleable.JJRelativeLayout_cllMarginEndPercentScreenWidth,0f)
        val alsMarginStartPercentWidth = a.getFloat(R.styleable.JJRelativeLayout_cllMarginStartPercentScreenWidth,0f)
        val alsMarginTopPercentWidth = a.getFloat(R.styleable.JJRelativeLayout_cllMarginTopPercentScreenWidth,0f)
        val alsMarginBottomPercentWidth = a.getFloat(R.styleable.JJRelativeLayout_cllMarginBottomPercentScreenWidth,0f)

        val alsMargin = a.getDimension(R.styleable.JJRelativeLayout_cllMargin,0f)
        val alsMarginPercentHeight = a.getFloat(R.styleable.JJRelativeLayout_cllMarginPerScHeight,0f)
        val alsMarginPercentWidth = a.getFloat(R.styleable.JJRelativeLayout_cllMarginPerScWidth,0f)
        val alsMarginResponsive = a.getResourceId(R.styleable.JJRelativeLayout_cllMarginResponsive,NO_ID)
        val alsMarginResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_cllMarginResPerScHeight,NO_ID)
        val alsMarginResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_cllMarginResPerScWidth,NO_ID)

        val alsMarginEndResponsive = a.getResourceId(R.styleable.JJRelativeLayout_cllMarginEndResponsive,NO_ID)
        val alsMarginStartResponsive = a.getResourceId(R.styleable.JJRelativeLayout_cllMarginStartResponsive,NO_ID)
        val alsMarginTopResponsive = a.getResourceId(R.styleable.JJRelativeLayout_cllMarginTopResponsive,NO_ID)
        val alsMarginBottomResponsive = a.getResourceId(R.styleable.JJRelativeLayout_cllMarginBottomResponsive,NO_ID)
        val alsMarginEndResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_cllMarginEndResPerScHeight,NO_ID)
        val alsMarginStartResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_cllMarginStartResPerScHeight,NO_ID)
        val alsMarginTopResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_cllMarginTopResPerScHeight,NO_ID)
        val alsMarginBottomResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_cllMarginBottomResPerScHeight,NO_ID)
        val alsMarginEndResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_cllMarginEndResPerScWidth,NO_ID)
        val alsMarginStartResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_cllMarginStartResPerScWidth,NO_ID)
        val alsMarginTopResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_cllMarginTopResPerScWidth,NO_ID)
        val alsMarginBottomResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_cllMarginBottomResPerScWidth,NO_ID)

        val alsMarginVertical = a.getDimension(R.styleable.JJRelativeLayout_cllMarginVertical,0f)
        val alsMarginVerticalPercentHeight = a.getFloat(R.styleable.JJRelativeLayout_cllMarginVerticalPerScHeight,0f)
        val alsMarginVerticalPercentWidth = a.getFloat(R.styleable.JJRelativeLayout_cllMarginVerticalPerScWidth,0f)
        val alsMarginVerticalResponsive = a.getResourceId(R.styleable.JJRelativeLayout_cllMarginVerticalResponsive,NO_ID)
        val alsMarginVerticalResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_cllMarginVerticalResPerScHeight,NO_ID)
        val alsMarginVerticalResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_cllMarginVerticalResPerScWidth,NO_ID)

        val alsMarginHorizontal = a.getDimension(R.styleable.JJRelativeLayout_cllMarginHorizontal,0f)
        val alsMarginHorizontalPercentHeight = a.getFloat(R.styleable.JJRelativeLayout_cllMarginHorizontalPerScHeight,0f)
        val alsMarginHorizontalPercentWidth = a.getFloat(R.styleable.JJRelativeLayout_cllMarginHorizontalPerScWidth,0f)
        val alsMarginHorizontalResponsive = a.getResourceId(R.styleable.JJRelativeLayout_cllMarginHorizontalResponsive,NO_ID)
        val alsMarginHorizontalResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_cllMarginHorizontalResPerScHeight,NO_ID)
        val alsMarginHorizontalResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_cllMarginHorizontalResPerScWidth,NO_ID)


        val alsVerticalBias = a.getFloat(R.styleable.JJRelativeLayout_cllVerticalBias,0.5f)
        val alsHorizontalBias = a.getFloat(R.styleable.JJRelativeLayout_cllHorizontalBias,0.5f)

        val alsStartToStartParent = a.getBoolean(R.styleable.JJRelativeLayout_cllStartToStartParent,false)
        val alsStartToEndParent = a.getBoolean(R.styleable.JJRelativeLayout_cllStartToEndParent,false)
        val alsEndToEndParent = a.getBoolean(R.styleable.JJRelativeLayout_cllEndToEndParent,false)
        val alsEndToStartParent = a.getBoolean(R.styleable.JJRelativeLayout_cllEndToStartParent,false)
        val alsTopToTopParent = a.getBoolean(R.styleable.JJRelativeLayout_cllTopToTopParent,false)
        val alsTopToBottomParent = a.getBoolean(R.styleable.JJRelativeLayout_cllTopToBottomParent,false)
        val alsBottomToBottomParent = a.getBoolean(R.styleable.JJRelativeLayout_cllBottomToBottomParent,false)
        val alsBottomToTopParent = a.getBoolean(R.styleable.JJRelativeLayout_cllBottomToTopParent,false)

        val alsStartToStartOf = a.getResourceId(R.styleable.JJRelativeLayout_cllStartToStartOf,View.NO_ID)
        val alsStartToEndOf = a.getResourceId(R.styleable.JJRelativeLayout_cllStartToEndOf,View.NO_ID)
        val alsEndToEndOf = a.getResourceId(R.styleable.JJRelativeLayout_cllEndToEndOf,View.NO_ID)
        val alsEndToStartOf = a.getResourceId(R.styleable.JJRelativeLayout_cllEndToStartOf,View.NO_ID)
        val alsTopToTopOf = a.getResourceId(R.styleable.JJRelativeLayout_cllTopToTopOf,View.NO_ID)
        val alsTopToBottomOf = a.getResourceId(R.styleable.JJRelativeLayout_cllTopToBottomOf,View.NO_ID)
        val alsBottomToBottomOf = a.getResourceId(R.styleable.JJRelativeLayout_cllBottomToBottomOf,View.NO_ID)
        val alsBottomToTopOf = a.getResourceId(R.styleable.JJRelativeLayout_cllBottomToTopOf,View.NO_ID)

        val alsHeightPercent = a.getFloat(R.styleable.JJRelativeLayout_cllHeightPercent,0f)
        val alsWidthPercent = a.getFloat(R.styleable.JJRelativeLayout_cllWidthPercent,0f)
        val alsHeightPercentScreenWidth = a.getFloat(R.styleable.JJRelativeLayout_cllHeightPercentScreenWidth,0f)
        val alsWidthPercentScreenWidth = a.getFloat(R.styleable.JJRelativeLayout_cllWidthPercentScreenWidth,0f)
        val alsHeightPercentScreenHeight = a.getFloat(R.styleable.JJRelativeLayout_cllHeightPercentScreenHeight,0f)
        val alsWidthPercentScreenHeight = a.getFloat(R.styleable.JJRelativeLayout_cllWidthPercentScreenHeight,0f)

        val alsHeightResponsive = a.getResourceId(R.styleable.JJRelativeLayout_cllHeightResponsive,View.NO_ID)
        val alsWidthResponsive = a.getResourceId(R.styleable.JJRelativeLayout_cllWidthResponsive,View.NO_ID)

        val alsHeightResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_cllHeightResponsivePercentScreenHeight,View.NO_ID)
        val alsWidthResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_cllWidthResponsivePercentScreenWidth,View.NO_ID)
        val alsHeightResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_cllHeightResponsivePercentScreenWidth,View.NO_ID)
        val alsWidthResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_cllWidthResponsivePercentScreenHeight,View.NO_ID)


        val attrHeightLs = a.getLayoutDimension(R.styleable.JJRelativeLayout_layout_height_landscape,0)
        val attrWidthLs = a.getLayoutDimension(R.styleable.JJRelativeLayout_layout_width_landscape,0)

        val llsPadding = a.getDimension(R.styleable.JJRelativeLayout_lplPadding,-100f)
        val llsPaddingVertical = a.getDimension(R.styleable.JJRelativeLayout_lplPaddingVertical,-100f)
        val llsPaddingHorizontal = a.getDimension(R.styleable.JJRelativeLayout_lplPaddingHorizontal,-100f)
        val llsPaddingStart = a.getDimension(R.styleable.JJRelativeLayout_lplPaddingStart,-100f)
        val llsPaddingEnd = a.getDimension(R.styleable.JJRelativeLayout_lplPaddingEnd,-100f)
        val llsPaddingBottom = a.getDimension(R.styleable.JJRelativeLayout_lplPaddingBottom,-100f)
        val llsPaddingTop = a.getDimension(R.styleable.JJRelativeLayout_lplPaddingTop,-100f)

        val llsMargin = a.getDimension(R.styleable.JJRelativeLayout_lplMargin,-100f)
        val llsMarginVertical = a.getDimension(R.styleable.JJRelativeLayout_lplMarginVertical,-100f)
        val llsMarginHorizontal = a.getDimension(R.styleable.JJRelativeLayout_lplMarginHorizontal,-100f)
        val llsMarginStart = a.getDimension(R.styleable.JJRelativeLayout_lplMarginStart,-100f)
        val llsMarginEnd = a.getDimension(R.styleable.JJRelativeLayout_lplMarginEnd,-100f)
        val llsMarginBottom = a.getDimension(R.styleable.JJRelativeLayout_lplMarginBottom,-100f)
        val llsMarginTop = a.getDimension(R.styleable.JJRelativeLayout_lplMarginTop,-100f)

        val llsHeightPercentScreenWidth = a.getFloat(R.styleable.JJRelativeLayout_lplHeightPercentScreenWidth,0f)
        val llsWidthPercentScreenWidth = a.getFloat(R.styleable.JJRelativeLayout_lplWidthPercentScreenWidth,0f)
        val llsHeightPercentScreenHeight = a.getFloat(R.styleable.JJRelativeLayout_lplHeightPercentScreenHeight,0f)
        val llsWidthPercentScreenHeight = a.getFloat(R.styleable.JJRelativeLayout_lplWidthPercentScreenHeight,0f)

        val llsHeightResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lplHeightResponsive,View.NO_ID)
        val llsWidthResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lplWidthResponsive,View.NO_ID)
        val llsHeightResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lplHeightResponsivePercentScreenHeight,View.NO_ID)
        val llsWidthResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lplWidthResponsivePercentScreenWidth,View.NO_ID)
        val llsHeightResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lplHeightResponsivePercentScreenWidth,View.NO_ID)
        val llsWidthResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lplWidthResponsivePercentScreenHeight,View.NO_ID)

        val llsMarginTopPercentScHeight = a.getFloat(R.styleable.JJRelativeLayout_lplMarginTopPerScHeight,0f)
        val llsMarginLeftPercentScHeight = a.getFloat(R.styleable.JJRelativeLayout_lplMarginLeftPerScHeight,0f)
        val llsMarginRightPercentScHeight = a.getFloat(R.styleable.JJRelativeLayout_lplMarginRightPerScHeight,0f)
        val llsMarginBottomPercentScHeight = a.getFloat(R.styleable.JJRelativeLayout_lplMarginBottomPerScHeight,0f)

        val llsMarginTopPercentScWidth = a.getFloat(R.styleable.JJRelativeLayout_lplMarginTopPerScWidth,0f)
        val llsMarginLeftPercentScWidth = a.getFloat(R.styleable.JJRelativeLayout_lplMarginLeftPerScWidth,0f)
        val llsMarginRightPercentScWidth = a.getFloat(R.styleable.JJRelativeLayout_lplMarginRightPerScWidth,0f)
        val llsMarginBottomPercentScWidth = a.getFloat(R.styleable.JJRelativeLayout_lplMarginBottomPerScWidth,0f)

        val llsMarginTopResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lplMarginTopResponsive,View.NO_ID)
        val llsMarginLeftResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lplMarginLeftResponsive,View.NO_ID)
        val llsMarginRightResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lplMarginRightResponsive,View.NO_ID)
        val llsMarginBottomResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lplMarginBottomResponsive,View.NO_ID)

        val llsMarginTopResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lplMarginTopResPerScWidth,View.NO_ID)
        val llsMarginLeftResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lplMarginLeftResPerScWidth,View.NO_ID)
        val llsMarginRightResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lplMarginRightResPerScWidth,View.NO_ID)
        val llsMarginBottomResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lplMarginBottomResPerScWidth,View.NO_ID)

        val llsMarginTopResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lplMarginTopResPerScHeight,View.NO_ID)
        val llsMarginLeftResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lplMarginLeftResPerScHeight,View.NO_ID)
        val llsMarginRightResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lplMarginRightResPerScHeight,View.NO_ID)
        val llsMarginBottomResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lplMarginBottomResPerScHeight,View.NO_ID)

        val llsMarginPerScHeight = a.getFloat(R.styleable.JJRelativeLayout_lplMarginPercentScHeight,0f)
        val llsMarginPerScWidth = a.getFloat(R.styleable.JJRelativeLayout_lplMarginPercentScWidth,0f)
        val llsMarginResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lplMarginResponsive,View.NO_ID)
        val llsMarginResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lplMarginResPerScHeight,View.NO_ID)
        val llsMarginResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lplMarginResPerScWidth,View.NO_ID)

        val llsMarginVerticalPerScHeight = a.getFloat(R.styleable.JJRelativeLayout_lplMarginVerticalPerScHeight,0f)
        val llsMarginVerticalPerScWidth = a.getFloat(R.styleable.JJRelativeLayout_lplMarginVerticalPerScWidth,0f)
        val llsMarginVerticalResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lplMarginVerticalResponsive,View.NO_ID)
        val llsMarginVerticalResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lplMarginVerticalResPerScWidth,View.NO_ID)
        val llsMarginVerticalResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lplMarginVerticalResPerScHeight,View.NO_ID)

        val llsMarginHorizontalPerScHeight = a.getFloat(R.styleable.JJRelativeLayout_lplMarginHorizontalPerScHeight,0f)
        val llsMarginHorizontalPerScWidth = a.getFloat(R.styleable.JJRelativeLayout_lplMarginHorizontalPerScWidth,0f)
        val llsMarginHorizontalResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lplMarginHorizontalResponsive,View.NO_ID)
        val llsMarginHorizontalResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lplMarginHorizontalResPerScWidth,View.NO_ID)
        val llsMarginHorizontalResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lplMarginHorizontalResPerScHeight,View.NO_ID)


        val llsPaddingTopPercentScHeight = a.getFloat(R.styleable.JJRelativeLayout_lplPaddingTopPerScHeight,0f)
        val llsPaddingLeftPercentScHeight = a.getFloat(R.styleable.JJRelativeLayout_lplPaddingLeftPerScHeight,0f)
        val llsPaddingRightPercentScHeight = a.getFloat(R.styleable.JJRelativeLayout_lplPaddingRightPerScHeight,0f)
        val llsPaddingBottomPercentScHeight = a.getFloat(R.styleable.JJRelativeLayout_lplPaddingBottomPerScHeight,0f)

        val llsPaddingTopPercentScWidth = a.getFloat(R.styleable.JJRelativeLayout_lplPaddingTopPerScWidth,0f)
        val llsPaddingLeftPercentScWidth = a.getFloat(R.styleable.JJRelativeLayout_lplPaddingLeftPerScWidth,0f)
        val llsPaddingRightPercentScWidth = a.getFloat(R.styleable.JJRelativeLayout_lplPaddingRightPerScWidth,0f)
        val llsPaddingBottomPercentScWidth = a.getFloat(R.styleable.JJRelativeLayout_lplPaddingBottomPerScWidth,0f)

        val llsPaddingTopResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lplPaddingTopResponsive,View.NO_ID)
        val llsPaddingLeftResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lplPaddingLeftResponsive,View.NO_ID)
        val llsPaddingRightResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lplPaddingRightResponsive,View.NO_ID)
        val llsPaddingBottomResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lplPaddingBottomResponsive,View.NO_ID)

        val llsPaddingTopResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lplPaddingTopResPerScWidth,View.NO_ID)
        val llsPaddingLeftResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lplPaddingLeftResPerScWidth,View.NO_ID)
        val llsPaddingRightResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lplPaddingRightResPerScWidth,View.NO_ID)
        val llsPaddingBottomResponsivePercentScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lplPaddingBottomResPerScWidth,View.NO_ID)

        val llsPaddingTopResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lplPaddingTopResPerScHeight,View.NO_ID)
        val llsPaddingLeftResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lplPaddingLeftResPerScHeight,View.NO_ID)
        val llsPaddingRightResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lplPaddingRightResPerScHeight,View.NO_ID)
        val llsPaddingBottomResponsivePercentScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lplPaddingBottomResPerScHeight,View.NO_ID)

        val llsPaddingPerScHeight = a.getFloat(R.styleable.JJRelativeLayout_lplPaddingPercentScHeight,0f)
        val llsPaddingPerScWidth = a.getFloat(R.styleable.JJRelativeLayout_lplPaddingPercentScWidth,0f)
        val llsPaddingResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lplPaddingResponsive,View.NO_ID)
        val llsPaddingResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lplPaddingResPerScHeight,View.NO_ID)
        val llsPaddingResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lplPaddingResPerScWidth,View.NO_ID)

        val llsPaddingVerticalPerScHeight = a.getFloat(R.styleable.JJRelativeLayout_lplPaddingVerticalPerScHeight,0f)
        val llsPaddingVerticalPerScWidth = a.getFloat(R.styleable.JJRelativeLayout_lplPaddingVerticalPerScWidth,0f)
        val llsPaddingVerticalResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lplPaddingVerticalResponsive,View.NO_ID)
        val llsPaddingVerticalResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lplPaddingVerticalResPerScWidth,View.NO_ID)
        val llsPaddingVerticalResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lplPaddingVerticalResPerScHeight,View.NO_ID)

        val llsPaddingHorizontalPerScHeight = a.getFloat(R.styleable.JJRelativeLayout_lplPaddingHorizontalPerScHeight,0f)
        val llsPaddingHorizontalPerScWidth = a.getFloat(R.styleable.JJRelativeLayout_lplPaddingHorizontalPerScWidth,0f)
        val llsPaddingHorizontalResponsive = a.getResourceId(R.styleable.JJRelativeLayout_lplPaddingHorizontalResponsive,View.NO_ID)
        val llsPaddingHorizontalResPerScWidth = a.getResourceId(R.styleable.JJRelativeLayout_lplPaddingHorizontalResPerScWidth,View.NO_ID)
        val llsPaddingHorizontalResPerScHeight = a.getResourceId(R.styleable.JJRelativeLayout_lplPaddingHorizontalResPerScHeight,View.NO_ID)

        //endregion

        a.recycle()

        //region standard portrait

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
            mlpMargins.top = mar ; mlpMargins.bottom = mar
        }
        if(lMarginVerticalPerScWidth > 0f) {
            val mar = JJScreen.percentWidth(lMarginVerticalPerScWidth)
            mlpMargins.top = mar ; mlpMargins.bottom = mar
        }

        if(lMarginVerticalResponsive != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lMarginVerticalResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            mlpMargins.top = mar ; mlpMargins.bottom = mar
        }

        if(lMarginVerticalResPerScHeight != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lMarginVerticalResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpMargins.top = mar ; mlpMargins.bottom = mar
        }

        if(lMarginVerticalResPerScWidth != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lMarginVerticalResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpMargins.top = mar ; mlpMargins.bottom = mar
        }
        //endregion

        //region Horizontal margin

        if(lMarginHorizontalPerScHeight > 0f) {
            val mar = JJScreen.percentHeight(lMarginHorizontalPerScHeight)
            mlpMargins.left = mar ; mlpMargins.right = mar
        }
        if(lMarginHorizontalPerScWidth > 0f) {
            val mar = JJScreen.percentWidth(lMarginHorizontalPerScWidth)
            mlpMargins.left = mar ; mlpMargins.right = mar
        }

        if(lMarginHorizontalResponsive != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lMarginHorizontalResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            mlpMargins.left = mar ; mlpMargins.right = mar
        }

        if(lMarginHorizontalResPerScHeight != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lMarginHorizontalResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpMargins.left = mar ; mlpMargins.right = mar
        }

        if(lMarginHorizontalResPerScWidth != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lMarginHorizontalResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpMargins.left = mar ; mlpMargins.right = mar
        }

        //endregion

        //region margin start end top bottom

        if(lMarginTopPercentScHeight > 0f)  mlpMargins.top = JJScreen.percentHeight(lMarginTopPercentScHeight)
        if(lMarginLeftPercentScHeight > 0f)  mlpMargins.left = JJScreen.percentHeight(lMarginLeftPercentScHeight)
        if(lMarginRightPercentScHeight > 0f) mlpMargins.right = JJScreen.percentHeight(lMarginRightPercentScHeight)
        if(lMarginBottomPercentScHeight > 0f) mlpMargins.bottom = JJScreen.percentHeight(lMarginBottomPercentScHeight)

        if(lMarginTopPercentScWidth > 0f)  mlpMargins.top = JJScreen.percentWidth(lMarginTopPercentScWidth)
        if(lMarginLeftPercentScWidth > 0f)  mlpMargins.left = JJScreen.percentWidth(lMarginLeftPercentScWidth)
        if(lMarginRightPercentScWidth > 0f) mlpMargins.right = JJScreen.percentWidth(lMarginRightPercentScWidth)
        if(lMarginBottomPercentScWidth > 0f) mlpMargins.bottom = JJScreen.percentWidth(lMarginBottomPercentScWidth)


        if(lMarginTopResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginTopResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlpMargins.top = v
        }

        if(lMarginLeftResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginLeftResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlpMargins.left = v
        }

        if(lMarginRightResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginRightResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlpMargins.right = v
        }

        if(lMarginBottomResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginBottomResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlpMargins.bottom = v
        }


        if(lMarginTopResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginTopResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpMargins.top = v
        }

        if(lMarginLeftResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginLeftResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpMargins.left = v
        }

        if(lMarginRightResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginRightResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpMargins.right = v
        }

        if(lMarginBottomResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginBottomResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpMargins.bottom = v
        }

        if(lMarginTopResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginTopResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpMargins.top = v
        }

        if(lMarginLeftResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginLeftResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpMargins.left = v
        }

        if(lMarginRightResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginRightResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpMargins.right = v
        }

        if(lMarginBottomResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lMarginBottomResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpMargins.bottom = v
        }

        //endregion

        //region padding

        if(lPaddingPerScHeight > 0f) {
            val mar = JJScreen.percentHeight(lPaddingPerScHeight)
            mlpPadding = JJPadding(mar,mar,mar,mar)
        }
        if(lPaddingPerScWidth > 0f) {
            val mar = JJScreen.percentWidth(lPaddingPerScWidth)
            mlpPadding = JJPadding(mar,mar,mar,mar)
        }

        if(lPaddingResponsive != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lPaddingResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            mlpPadding = JJPadding(mar,mar,mar,mar)
        }

        if(lPaddingResPerScHeight != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lPaddingResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpPadding = JJPadding(mar,mar,mar,mar)
        }

        if(lPaddingResPerScWidth != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lPaddingResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpPadding = JJPadding(mar,mar,mar,mar)
        }


        //endregion

        //region padding vertical

        if(lPaddingVerticalPerScHeight > 0f) {
            val mar = JJScreen.percentHeight(lPaddingVerticalPerScHeight)
            mlpPadding.top = mar ; mlpPadding.bottom = mar
        }
        if(lPaddingVerticalPerScWidth > 0f) {
            val mar = JJScreen.percentWidth(lMarginVerticalPerScWidth)
            mlpPadding.top = mar ; mlpPadding.bottom = mar
        }

        if(lPaddingVerticalResponsive != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lPaddingVerticalResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            mlpPadding.top = mar ; mlpPadding.bottom = mar
        }

        if(lPaddingVerticalResPerScHeight != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lPaddingVerticalResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpPadding.top = mar ; mlpPadding.bottom = mar
        }

        if(lPaddingVerticalResPerScWidth != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lPaddingVerticalResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpPadding.top = mar ; mlpPadding.bottom = mar
        }
        //endregion

        //region Horizontal padding

        if(lPaddingHorizontalPerScHeight > 0f) {
            val mar = JJScreen.percentHeight(lPaddingHorizontalPerScHeight)
            mlpPadding.left = mar ; mlpPadding.right = mar
        }
        if(lPaddingHorizontalPerScWidth > 0f) {
            val mar = JJScreen.percentWidth(lPaddingHorizontalPerScWidth)
            mlpPadding.left = mar ; mlpPadding.right = mar
        }

        if(lPaddingHorizontalResponsive != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lPaddingHorizontalResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            mlpPadding.left = mar ; mlpPadding.right = mar
        }

        if(lPaddingHorizontalResPerScHeight != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lPaddingHorizontalResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpPadding.left = mar ; mlpPadding.right = mar
        }

        if(lPaddingHorizontalResPerScWidth != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(lPaddingHorizontalResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpPadding.left = mar ; mlpPadding.right = mar
        }

        //endregion

        //region padding start end top bottom

        if(lPaddingTopPercentScHeight > 0f)  mlpPadding.top = JJScreen.percentHeight(lPaddingTopPercentScHeight)
        if(lPaddingLeftPercentScHeight > 0f)  mlpPadding.left = JJScreen.percentHeight(lPaddingLeftPercentScHeight)
        if(lPaddingRightPercentScHeight > 0f) mlpPadding.right = JJScreen.percentHeight(lPaddingRightPercentScHeight)
        if(lPaddingBottomPercentScHeight > 0f) mlpPadding.bottom = JJScreen.percentHeight(lPaddingBottomPercentScHeight)

        if(lPaddingTopPercentScWidth > 0f)  mlpPadding.top = JJScreen.percentWidth(lPaddingTopPercentScWidth)
        if(lPaddingLeftPercentScWidth > 0f)  mlpPadding.left = JJScreen.percentWidth(lPaddingLeftPercentScWidth)
        if(lPaddingRightPercentScWidth > 0f) mlpPadding.right = JJScreen.percentWidth(lPaddingRightPercentScWidth)
        if(lPaddingBottomPercentScWidth > 0f) mlpPadding.bottom = JJScreen.percentWidth(lPaddingBottomPercentScWidth)


        if(lPaddingTopResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingTopResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlpPadding.top = v
        }

        if(lPaddingLeftResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingLeftResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlpPadding.left = v
        }

        if(lPaddingRightResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingRightResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlpPadding.right = v
        }

        if(lPaddingBottomResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingBottomResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlpPadding.bottom = v
        }


        if(lPaddingTopResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingTopResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpPadding.top = v
        }

        if(lPaddingLeftResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingLeftResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpPadding.left = v
        }

        if(lPaddingRightResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingRightResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpPadding.right = v
        }

        if(lPaddingBottomResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingBottomResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlpPadding.bottom = v
        }

        if(lPaddingTopResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingTopResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpPadding.top = v
        }

        if(lPaddingLeftResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingLeftResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpPadding.left = v
        }

        if(lPaddingRightResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingRightResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpPadding.right = v
        }

        if(lPaddingBottomResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(lPaddingBottomResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlpPadding.bottom = v
        }

        //endregion

        //region params layout height width

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

        //endregion

        //endregion

        //region constraint Layout attr

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

        //region width height

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

        //endregion

        //region anchors

        if(aStartToStartOf != View.NO_ID) clStartToStart(aStartToStartOf)
        if(aStartToEndOf != View.NO_ID) clStartToEnd(aStartToEndOf)
        if(aEndToEndOf != View.NO_ID) clEndToEnd(aEndToEndOf)
        if(aEndToStartOf != View.NO_ID) clEndToStart(aEndToStartOf)
        if(aTopToTopOf != View.NO_ID) clTopToTop(aTopToTopOf)
        if(aTopToBottomOf != View.NO_ID) clTopToBottom(aTopToBottomOf)
        if(aBottomToBottomOf != View.NO_ID) clBottomToBottom(aBottomToBottomOf)
        if(aBottomToTopOf != View.NO_ID) clBottomToTop(aBottomToTopOf)

        if(aStartToStartParent) clStartToStartParent()
        if(aStartToEndParent) clStartToEndParent()
        if(aEndToEndParent) clEndToEndParent()
        if(aEndToStartParent) clEndToStartParent()
        if(aTopToTopParent) clTopToTopParent()
        if(aTopToBottomParent) clTopToBottomParent()
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

        //endregion

        //endregion

        //region standard landscape

        //region margin


        if(llsMargin > 0f) {
            mlsMargins.top =  llsMargin.toInt()
            mlsMargins.left =  llsMargin.toInt()
            mlsMargins.right =  llsMargin.toInt()
            mlsMargins.bottom =  llsMargin.toInt()
        }

        if(llsMarginPerScHeight > 0f) {
            val mar = JJScreen.percentHeight(llsMarginPerScHeight)
            mlsMargins = JJMargin(mar,mar,mar,mar)
        }
        if(llsMarginPerScWidth > 0f) {
            val mar = JJScreen.percentWidth(llsMarginPerScWidth)
            mlsMargins = JJMargin(mar,mar,mar,mar)
        }

        if(llsMarginResponsive != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(llsMarginResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            mlsMargins = JJMargin(mar,mar,mar,mar)
        }

        if(llsMarginResPerScHeight != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(llsMarginResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlsMargins = JJMargin(mar,mar,mar,mar)
        }

        if(llsMarginResPerScWidth != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(llsMarginResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlsMargins = JJMargin(mar,mar,mar,mar)
        }


//endregion

        //region margin vertical

        if(llsMarginVertical > 0f){
            mlsMargins.top = llsMarginVertical.toInt()
            mlsMargins.bottom = llsMarginVertical.toInt()
        }

        if(llsMarginVerticalPerScHeight > 0f) {
            val mar = JJScreen.percentHeight(llsMarginVerticalPerScHeight)
            mlsMargins.top = mar ; mlsMargins.bottom = mar
        }
        if(llsMarginVerticalPerScWidth > 0f) {
            val mar = JJScreen.percentWidth(llsMarginVerticalPerScWidth)
            mlsMargins.top = mar ; mlsMargins.bottom = mar
        }

        if(llsMarginVerticalResponsive != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(llsMarginVerticalResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            mlsMargins.top = mar ; mlsMargins.bottom = mar
        }

        if(llsMarginVerticalResPerScHeight != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(llsMarginVerticalResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlsMargins.top = mar ; mlsMargins.bottom = mar
        }

        if(llsMarginVerticalResPerScWidth != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(llsMarginVerticalResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlsMargins.top = mar ; mlsMargins.bottom = mar
        }
//endregion

        //region Horizontal margin

        if(llsMarginHorizontal > 0f){
            mlsMargins.left = llsMarginHorizontal.toInt()
            mlsMargins.right = llsMarginHorizontal.toInt()
        }

        if(llsMarginHorizontalPerScHeight > 0f) {
            val mar = JJScreen.percentHeight(llsMarginHorizontalPerScHeight)
            mlsMargins.left = mar ; mlsMargins.right = mar
        }
        if(llsMarginHorizontalPerScWidth > 0f) {
            val mar = JJScreen.percentWidth(llsMarginHorizontalPerScWidth)
            mlsMargins.left = mar ; mlsMargins.right = mar
        }

        if(llsMarginHorizontalResponsive != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(llsMarginHorizontalResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            mlsMargins.left = mar ; mlsMargins.right = mar
        }

        if(llsMarginHorizontalResPerScHeight != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(llsMarginHorizontalResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlsMargins.left = mar ; mlsMargins.right = mar
        }

        if(llsMarginHorizontalResPerScWidth != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(llsMarginHorizontalResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlsMargins.left = mar ; mlsMargins.right = mar
        }

//endregion

        //region margin start end top bottom

        if(llsMarginTop > 0f) mlsMargins.top = llsMarginTop.toInt()
        if(llsMarginBottom > 0f) mlsMargins.bottom = llsMarginBottom.toInt()
        if(llsMarginEnd > 0f) mlsMargins.right = llsMarginEnd.toInt()
        if(llsMarginStart > 0f) mlsMargins.left = llsMarginStart.toInt()

        if(llsMarginTopPercentScHeight > 0f)  mlsMargins.top = JJScreen.percentHeight(llsMarginTopPercentScHeight)
        if(llsMarginLeftPercentScHeight > 0f)  mlsMargins.left = JJScreen.percentHeight(llsMarginLeftPercentScHeight)
        if(llsMarginRightPercentScHeight > 0f) mlsMargins.right = JJScreen.percentHeight(llsMarginRightPercentScHeight)
        if(llsMarginBottomPercentScHeight > 0f) mlsMargins.bottom = JJScreen.percentHeight(llsMarginBottomPercentScHeight)

        if(llsMarginTopPercentScWidth > 0f)  mlsMargins.top = JJScreen.percentWidth(llsMarginTopPercentScWidth)
        if(llsMarginLeftPercentScWidth > 0f)  mlsMargins.left = JJScreen.percentWidth(llsMarginLeftPercentScWidth)
        if(llsMarginRightPercentScWidth > 0f) mlsMargins.right = JJScreen.percentWidth(llsMarginRightPercentScWidth)
        if(llsMarginBottomPercentScWidth > 0f) mlsMargins.bottom = JJScreen.percentWidth(llsMarginBottomPercentScWidth)


        if(llsMarginTopResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsMarginTopResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlsMargins.top = v
        }

        if(llsMarginLeftResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsMarginLeftResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlsMargins.left = v
        }

        if(llsMarginRightResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsMarginRightResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlsMargins.right = v
        }

        if(llsMarginBottomResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsMarginBottomResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlsMargins.bottom = v
        }


        if(llsMarginTopResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsMarginTopResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlsMargins.top = v
        }

        if(llsMarginLeftResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsMarginLeftResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlsMargins.left = v
        }

        if(llsMarginRightResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsMarginRightResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlsMargins.right = v
        }

        if(llsMarginBottomResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsMarginBottomResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlsMargins.bottom = v
        }

        if(llsMarginTopResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsMarginTopResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlsMargins.top = v
        }

        if(llsMarginLeftResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsMarginLeftResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlsMargins.left = v
        }

        if(llsMarginRightResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsMarginRightResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlsMargins.right = v
        }

        if(llsMarginBottomResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsMarginBottomResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlsMargins.bottom = v
        }

//endregion

        //region padding

        if(llsPadding > 0f) {
            mlsPadding.top = llsPadding.toInt()
            mlsPadding.left = llsPadding.toInt()
            mlsPadding.right = llsPadding.toInt()
            mlsPadding.bottom = llsPadding.toInt()
        }


        if(llsPaddingPerScHeight > 0f) {
            val mar = JJScreen.percentHeight(llsPaddingPerScHeight)
            mlsPadding = JJPadding(mar,mar,mar,mar)
        }
        if(llsPaddingPerScWidth > 0f) {
            val mar = JJScreen.percentWidth(llsPaddingPerScWidth)
            mlsPadding = JJPadding(mar,mar,mar,mar)
        }

        if(llsPaddingResponsive != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(llsPaddingResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            mlsPadding = JJPadding(mar,mar,mar,mar)
        }

        if(llsPaddingResPerScHeight != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(llsPaddingResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlsPadding = JJPadding(mar,mar,mar,mar)
        }

        if(llsPaddingResPerScWidth != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(llsPaddingResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlsPadding = JJPadding(mar,mar,mar,mar)
        }


//endregion

        //region padding vertical

        if(llsPaddingVertical > 0f){
            mlsPadding.top = llsPaddingVertical.toInt()
            mlsPadding.bottom = llsPaddingVertical.toInt()
        }

        if(llsPaddingVerticalPerScHeight > 0f) {
            val mar = JJScreen.percentHeight(llsPaddingVerticalPerScHeight)
            mlsPadding.top = mar ; mlsPadding.bottom = mar
        }
        if(llsPaddingVerticalPerScWidth > 0f) {
            val mar = JJScreen.percentWidth(lMarginVerticalPerScWidth)
            mlsPadding.top = mar ; mlsPadding.bottom = mar
        }

        if(llsPaddingVerticalResponsive != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(llsPaddingVerticalResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            mlsPadding.top = mar ; mlsPadding.bottom = mar
        }

        if(llsPaddingVerticalResPerScHeight != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(llsPaddingVerticalResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlsPadding.top = mar ; mlsPadding.bottom = mar
        }

        if(llsPaddingVerticalResPerScWidth != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(llsPaddingVerticalResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlsPadding.top = mar ; mlsPadding.bottom = mar
        }
//endregion

        //region Horizontal padding

        if(llsPaddingHorizontal > 0f){
            mlsPadding.left = llsPaddingHorizontal.toInt()
            mlsPadding.right = llsPaddingHorizontal.toInt()
        }

        if(llsPaddingHorizontalPerScHeight > 0f) {
            val mar = JJScreen.percentHeight(llsPaddingHorizontalPerScHeight)
            mlsPadding.left = mar ; mlsPadding.right = mar
        }
        if(llsPaddingHorizontalPerScWidth > 0f) {
            val mar = JJScreen.percentWidth(llsPaddingHorizontalPerScWidth)
            mlsPadding.left = mar ; mlsPadding.right = mar
        }

        if(llsPaddingHorizontalResponsive != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(llsPaddingHorizontalResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            mlsPadding.left = mar ; mlsPadding.right = mar
        }

        if(llsPaddingHorizontalResPerScHeight != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(llsPaddingHorizontalResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlsPadding.left = mar ; mlsPadding.right = mar
        }

        if(llsPaddingHorizontalResPerScWidth != View.NO_ID) {
            val arrayDimen = resources.obtainTypedArray(llsPaddingHorizontalResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlsPadding.left = mar ; mlsPadding.right = mar
        }

//endregion

        //region padding start end top bottom

        if(llsPaddingStart > 0f) mlsPadding.left = llsPaddingStart.toInt()
        if(llsPaddingTop > 0f) mlsPadding.top = llsPaddingTop.toInt()
        if(llsPaddingEnd > 0f) mlsPadding.right = llsPaddingEnd.toInt()
        if(llsPaddingBottom > 0f) mlsPadding.bottom = llsPaddingBottom.toInt()

        if(llsPaddingTopPercentScHeight > 0f)  mlsPadding.top = JJScreen.percentHeight(llsPaddingTopPercentScHeight)
        if(llsPaddingLeftPercentScHeight > 0f)  mlsPadding.left = JJScreen.percentHeight(llsPaddingLeftPercentScHeight)
        if(llsPaddingRightPercentScHeight > 0f) mlsPadding.right = JJScreen.percentHeight(llsPaddingRightPercentScHeight)
        if(llsPaddingBottomPercentScHeight > 0f) mlsPadding.bottom = JJScreen.percentHeight(llsPaddingBottomPercentScHeight)

        if(llsPaddingTopPercentScWidth > 0f)  mlsPadding.top = JJScreen.percentWidth(llsPaddingTopPercentScWidth)
        if(llsPaddingLeftPercentScWidth > 0f)  mlsPadding.left = JJScreen.percentWidth(llsPaddingLeftPercentScWidth)
        if(llsPaddingRightPercentScWidth > 0f) mlsPadding.right = JJScreen.percentWidth(llsPaddingRightPercentScWidth)
        if(llsPaddingBottomPercentScWidth > 0f) mlsPadding.bottom = JJScreen.percentWidth(llsPaddingBottomPercentScWidth)


        if(llsPaddingTopResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsPaddingTopResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlsPadding.top = v
        }

        if(llsPaddingLeftResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsPaddingLeftResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlsPadding.left = v
        }

        if(llsPaddingRightResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsPaddingRightResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlsPadding.right = v
        }

        if(llsPaddingBottomResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsPaddingBottomResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            mlsPadding.bottom = v
        }


        if(llsPaddingTopResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsPaddingTopResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlsPadding.top = v
        }

        if(llsPaddingLeftResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsPaddingLeftResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlsPadding.left = v
        }

        if(llsPaddingRightResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsPaddingRightResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlsPadding.right = v
        }

        if(llsPaddingBottomResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsPaddingBottomResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlsPadding.bottom = v
        }

        if(llsPaddingTopResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsPaddingTopResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlsPadding.top = v
        }

        if(llsPaddingLeftResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsPaddingLeftResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlsPadding.left = v
        }

        if(llsPaddingRightResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsPaddingRightResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlsPadding.right = v
        }

        if(llsPaddingBottomResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsPaddingBottomResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlsPadding.bottom = v
        }

//endregion

        //region params layout height width

        mlsHeight = attrHeightLs
        mlsWidth = attrWidthLs

        if(llsHeightPercentScreenWidth > 0f)  mlsHeight = JJScreen.percentWidth(llsHeightPercentScreenWidth)
        if(llsHeightPercentScreenHeight > 0f)  mlsHeight = JJScreen.percentHeight(llsHeightPercentScreenHeight)
        if(llsWidthPercentScreenWidth > 0f) mlsWidth = JJScreen.percentWidth(llsWidthPercentScreenWidth)
        if(llsWidthPercentScreenHeight > 0f) mlsWidth = JJScreen.percentHeight(llsWidthPercentScreenHeight)

        if(llsHeightResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsHeightResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val h = JJScreen.responsiveSize(first,second,three,four)
            mlsHeight = h
        }

        if(llsWidthResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsWidthResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val w = JJScreen.responsiveSize(first,second,three,four)
            mlsWidth = w
        }


        if(llsHeightResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsHeightResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val h = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            mlsHeight = h
        }

        if(llsWidthResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsWidthResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val w = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)

            mlsWidth = w
        }

        if(llsHeightResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsHeightResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val h = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            mlsHeight = h
        }

        if(llsWidthResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(llsWidthResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val w = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)

            mlsWidth = w
        }

        //endregion

        //endregion

        //region constraint Layout landscape

        //region margin
        var lsMargins = JJMargin()

        if(alsMargin > 0f) lsMargins = JJMargin(alsMargin.toInt(),alsMargin.toInt(),alsMargin.toInt(),alsMargin.toInt())

        if(alsMarginPercentHeight > 0f) {
            val mar = JJScreen.percentHeight(alsMarginPercentHeight)
            lsMargins = JJMargin(mar,mar,mar,mar)
        }

        if(alsMarginPercentWidth > 0f) {
            val mar = JJScreen.percentWidth(alsMarginPercentWidth)
            lsMargins = JJMargin(mar,mar,mar,mar)
        }

        if(alsMarginResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(alsMarginResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val v = JJScreen.responsiveSize(first,second,three,four)
            lsMargins = JJMargin(v,v,v,v)
        }

        if(alsMarginResPerScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(alsMarginResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            lsMargins = JJMargin(v,v,v,v)
        }

        if(alsMarginResPerScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(alsMarginResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val v = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            lsMargins = JJMargin(v,v,v,v)
        }

        //endregion

        //region margin Vertical

        if(alsMarginVertical > 0f) { lsMargins.top = alsMarginVertical.toInt() ; lsMargins.bottom = alsMarginVertical.toInt() }

        if(alsMarginVerticalPercentHeight > 0f) {
            val mar = JJScreen.percentHeight(alsMarginVerticalPercentHeight)
            lsMargins.top = mar ; lsMargins.bottom = mar
        }

        if(alsMarginVerticalPercentWidth > 0f) {
            val mar = JJScreen.percentWidth(alsMarginVerticalPercentWidth)
            lsMargins.top = mar ; lsMargins.bottom = mar
        }

        if(alsMarginVerticalResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(alsMarginVerticalResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            lsMargins.top = mar ; lsMargins.bottom = mar
        }

        if(alsMarginVerticalResPerScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(alsMarginVerticalResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            lsMargins.top = mar ; lsMargins.bottom = mar
        }

        if(alsMarginVerticalResPerScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(alsMarginVerticalResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            lsMargins.top = mar ; lsMargins.bottom = mar
        }

//endregion

        // region margin Horizontal

        if(alsMarginHorizontal > 0f) { lsMargins.left = alsMarginHorizontal.toInt() ; lsMargins.right = alsMarginHorizontal.toInt() }

        if(alsMarginHorizontalPercentHeight > 0f) {
            val mar = JJScreen.percentHeight(alsMarginHorizontalPercentHeight)
            lsMargins.left = mar ; lsMargins.right = mar
        }

        if(alsMarginHorizontalPercentWidth > 0f) {
            val mar = JJScreen.percentWidth(alsMarginHorizontalPercentWidth)
            lsMargins.left = mar ; lsMargins.right = mar
        }

        if(alsMarginHorizontalResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(alsMarginHorizontalResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            lsMargins.left = mar ; lsMargins.right = mar
        }

        if(alsMarginHorizontalResPerScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(alsMarginHorizontalResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            lsMargins.left = mar ; lsMargins.right = mar
        }

        if(alsMarginHorizontalResPerScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(alsMarginHorizontalResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            lsMargins.left = mar ; lsMargins.right = mar
        }

//endregion

        //region margin start end top bottom

        if(alsMarginStart > 0f)   lsMargins.left = alsMarginStart.toInt()
        if(alsMarginEnd > 0f)   lsMargins.right = alsMarginEnd.toInt()
        if(alsMarginTop > 0f)   lsMargins.top = alsMarginTop.toInt()
        if(alsMarginBottom > 0f)   lsMargins.bottom = alsMarginBottom.toInt()

        if(alsMarginStartPercent > 0f) lsMargins.left = JJScreen.percentHeight(alsMarginStartPercent)
        if(alsMarginTopPercent > 0f)  lsMargins.top = JJScreen.percentHeight(alsMarginTopPercent)
        if(alsMarginEndPercent > 0f)  lsMargins.right = JJScreen.percentHeight(alsMarginEndPercent)
        if(alsMarginBottomPercent > 0f)  lsMargins.bottom = JJScreen.percentHeight(alsMarginBottomPercent)

        if(alsMarginStartPercentWidth > 0f)  lsMargins.left = JJScreen.percentWidth(alsMarginStartPercentWidth)
        if(alsMarginTopPercentWidth > 0f) lsMargins.top = JJScreen.percentWidth(alsMarginTopPercentWidth)
        if(alsMarginEndPercentWidth > 0f) lsMargins.right = JJScreen.percentWidth(alsMarginEndPercentWidth)
        if(alsMarginBottomPercentWidth > 0f) lsMargins.bottom = JJScreen.percentWidth(alsMarginBottomPercentWidth)

        if(alsMarginStartResponsive > 0f)  {
            val arrayDimen = resources.obtainTypedArray(alsMarginStartResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            lsMargins.left = mar
        }
        if(alsMarginEndResponsive > 0f)   {
            val arrayDimen = resources.obtainTypedArray(alsMarginEndResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            lsMargins.right = mar
        }
        if(alsMarginTopResponsive > 0f)  {
            val arrayDimen = resources.obtainTypedArray(alsMarginTopResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            lsMargins.top = mar
        }
        if(alsMarginBottomResponsive > 0f)  {
            val arrayDimen = resources.obtainTypedArray(alsMarginBottomResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSize(first,second,three,four)
            lsMargins.bottom = mar
        }

        if(alsMarginStartResPerScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(alsMarginStartResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            lsMargins.left = mar
        }

        if(alsMarginEndResPerScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(alsMarginEndResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            lsMargins.right = mar
        }

        if(alsMarginTopResPerScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(alsMarginTopResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            lsMargins.top = mar
        }

        if(alsMarginBottomResPerScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(alsMarginBottomResPerScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            lsMargins.bottom = mar
        }

        if(alsMarginStartResPerScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(alsMarginStartResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            lsMargins.left = mar
        }

        if(alsMarginEndResPerScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(alsMarginEndResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            lsMargins.right = mar
        }

        if(alsMarginTopResPerScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(alsMarginTopResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            lsMargins.top = mar
        }

        if(alsMarginBottomResPerScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(alsMarginBottomResPerScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val mar = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            lsMargins.bottom = mar
        }


        //endregion

        //region width height

        if(attrWidthLs > 0 || attrWidthLs == -2 ) cllWidth(attrWidthLs)
        if(attrHeightLs > 0 || attrHeightLs == -2 ) cllHeight(attrHeightLs)

        if( alsHeightPercent > 0f) cllPercentHeight( alsHeightPercent)

        if( alsHeightPercentScreenWidth > 0f) cllHeight(JJScreen.percentWidth( alsHeightPercentScreenWidth))
        if( alsHeightPercentScreenHeight > 0f) cllHeight(JJScreen.percentHeight( alsHeightPercentScreenHeight))

        if(alsWidthPercent > 0f) cllPercentWidth(alsWidthPercent)
        if(alsWidthPercentScreenWidth > 0f) cllWidth(JJScreen.percentWidth(alsWidthPercentScreenWidth))
        if(alsWidthPercentScreenHeight > 0f) cllWidth(JJScreen.percentHeight(alsWidthPercentScreenHeight))

        if( alsHeightResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray( alsHeightResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val h = JJScreen.responsiveSize(first,second,three,four)
            cllHeight(h)
        }

        if(alsWidthResponsive != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(alsWidthResponsive)
            val first = arrayDimen.getDimension(0, 0f).toInt()
            val second = arrayDimen.getDimension(1, 0f).toInt()
            val three = arrayDimen.getDimension(2, 0f).toInt()
            val four = arrayDimen.getDimension(3, 0f).toInt()
            arrayDimen.recycle()

            val w = JJScreen.responsiveSize(first,second,three,four)

            cllWidth(w)
        }

        if( alsHeightResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray( alsHeightResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val h = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)
            cllHeight(h)
        }

        if(alsWidthResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(alsWidthResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val w = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)

            cllWidth(w)
        }

        if( alsHeightResponsivePercentScWidth != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray( alsHeightResponsivePercentScWidth)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val h = JJScreen.responsiveSizePercentScreenWidth(first,second,three,four)
            cllHeight(h)
        }

        if(alsWidthResponsivePercentScHeight != View.NO_ID){
            val arrayDimen = resources.obtainTypedArray(alsWidthResponsivePercentScHeight)
            val first = arrayDimen.getFloat(0, 0f)
            val second = arrayDimen.getFloat(1, 0f)
            val three = arrayDimen.getFloat(2, 0f)
            val four = arrayDimen.getFloat(3, 0f)
            arrayDimen.recycle()

            val w = JJScreen.responsiveSizePercentScreenHeight(first,second,three,four)

            cllWidth(w)
        }

//endregion

        //region anchors

        if(alsStartToStartOf != View.NO_ID) cllStartToStart(alsStartToStartOf)
        if(alsStartToEndOf != View.NO_ID) cllStartToEnd(alsStartToEndOf)
        if(alsEndToEndOf != View.NO_ID) cllEndToEnd(alsEndToEndOf)
        if(alsEndToStartOf != View.NO_ID) cllEndToStart(alsEndToStartOf)
        if(alsTopToTopOf != View.NO_ID) cllTopToTop(alsTopToTopOf)
        if(alsTopToBottomOf != View.NO_ID) cllTopToBottom(alsTopToBottomOf)
        if(alsBottomToBottomOf != View.NO_ID) cllBottomToBottom(alsBottomToBottomOf)
        if(alsBottomToTopOf != View.NO_ID) cllBottomToTop(alsBottomToTopOf)

        if(alsStartToStartParent) cllStartToStartParent()
        if(alsStartToEndParent) cllStartToEndParent()
        if(alsEndToEndParent) cllEndToEndParent()
        if(alsEndToStartParent) cllEndToStartParent()
        if(alsTopToTopParent) cllTopToTopParent()
        if(alsTopToBottomParent) cllTopToBottomParent()
        if(alsBottomToBottomParent) cllBottomToBottomParent()
        if(alsBottomToTopParent) cllBottomToTopParent()


        if(alsCenterInParentTopVertical) cllCenterInParentTopVertically()
        if(alsCenterInParentBottomVertical) cllCenterInParentBottomVertically()
        if(alsCenterInParentStartHorizontal) cllCenterInParentStartHorizontally()
        if(alsCenterInParentEndHorizontal) cllCenterInParentEndHorizontally()

        if(alsCenterInTopVerticalOf != View.NO_ID) cllCenterInTopVertically(alsCenterInTopVerticalOf)
        if(alsCenterInBottomVerticalOf != View.NO_ID) cllCenterInBottomVertically(alsCenterInBottomVerticalOf)
        if(alsCenterInStartHorizontalOf != View.NO_ID) cllCenterInStartHorizontally(alsCenterInStartHorizontalOf)
        if(alsCenterInEndHorizontalOf != View.NO_ID) cllCenterInEndHorizontally(alsCenterInEndHorizontalOf)

        if(alsCenterVerticalOf != View.NO_ID) cllCenterVerticallyOf(alsCenterVerticalOf)
        if(alsCenterHorizontalOf != View.NO_ID) cllCenterHorizontallyOf(alsCenterHorizontalOf)

        if(alsCenterInParentHorizontal) cllCenterInParentHorizontally()
        if(alsCenterInParentVertical)  cllCenterInParentVertically()

        if(alsFillParentHorizontal) cllFillParentHorizontally()
        if(alsFillParentVertical) cllFillParentVertically()

        if(alsCenterInParent) cllCenterInParent()
        if(alsFillParent) cllFillParent()

        if(alsVerticalBias > 0f)  cllVerticalBias(alsVerticalBias)
        if(alsHorizontalBias > 0f)  cllHorizontalBias(alsHorizontalBias)

        cllMargins(lsMargins)

        //endregion

        //endregion
    }

    var mInit = true
    private var mlsHeight = 0
    private var mlsWidth = 0
    private var mlsMargins = JJMargin()
    private var mlsPadding = JJPadding()
    private var mConfigurationChanged = false
    private var mlpHeight = 0
    private var mlpWidth = 0
    private var mlpMargins = JJMargin()
    private var mlpPadding = JJPadding()
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val isLandScale = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        if(mInit){
            if(isLandScale && mSupportLandScape) applyLayoutParamsLandScape() else applyLayoutParamsPortrait()
            mInit = false
        }
    }
    private fun applyLayoutParamsPortrait(){
        val csParent = parent as? ConstraintLayout
        val mlParent = parent as? MotionLayout
        when {
            mlParent != null -> Log.e("JJKIT","PARENT MOTION LAYOUT")
            csParent != null -> {
                if(!mIgnoreCl){
                    clApply()
                }
            }
            else -> {
                layoutParams.height = mlpHeight
                layoutParams.width = mlpWidth
                val margin = layoutParams as? ViewGroup.MarginLayoutParams
                margin?.topMargin = mlpMargins.top
                margin?.marginStart =  mlpMargins.left
                margin?.marginEnd =  mlpMargins.right
                margin?.bottomMargin =  mlpMargins.bottom

            }
        }

        var pl = paddingLeft
        var pr = paddingRight
        if (paddingStart > 0) pl = paddingStart
        if (paddingEnd > 0) pr = paddingEnd

        if(mlpPadding.top <= 0 && paddingTop > 0) mlpPadding.top = paddingTop
        if(mlpPadding.bottom <= 0 && paddingBottom > 0) mlpPadding.bottom = paddingBottom
        if(mlpPadding.left <= 0 && pl > 0) mlpPadding.left = pl
        if(mlpPadding.right <= 0 && pr > 0) mlpPadding.right = pr

        setPaddingRelative(mlpPadding.left,mlpPadding.top,mlpPadding.right,mlpPadding.bottom)
    }
    private fun applyLayoutParamsLandScape(){
        val csParent = parent as? ConstraintLayout
        val mlParent = parent as? MotionLayout
        when {
            mlParent != null -> Log.e("JJKIT", "PARENT MOTION LAYOUT")
            csParent != null -> {
                if (!mIgnoreCl) {
                    cllApply()
                }
            }
            else -> {
                layoutParams.height = mlsHeight
                layoutParams.width = mlsWidth
                val margin = layoutParams as? ViewGroup.MarginLayoutParams
                margin?.topMargin = mlsMargins.top
                margin?.marginStart = mlsMargins.left
                margin?.marginEnd = mlsMargins.right
                margin?.bottomMargin = mlsMargins.bottom
            }
        }
        setPaddingRelative(mlsPadding.left,mlsPadding.top,mlsPadding.right,mlsPadding.bottom)
    }
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if(mConfigurationChanged){
            if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE && mSupportLandScape) {
                applyLayoutParamsLandScape()
            } else  {
                applyLayoutParamsPortrait()
            }
        }
    }

    //endregion

    //region method set get

    fun ssSupportLandScape(support:Boolean) : JJRelativeLayout {
        mSupportLandScape = support
        return this
    }

    fun ssSupportConfigurationChanged(support:Boolean) : JJRelativeLayout {
        mConfigurationChanged = support
        return this
    }


    private var mIdentifier = 0
    fun ssIdentifier(value: Int): JJRelativeLayout {
        mIdentifier = value
        return this
    }

    fun ggIdentifier():Int{
        return mIdentifier
    }

    private var mState = 0
    fun ssState(state: Int): JJRelativeLayout {
        mState = state
        return this
    }

    fun ggState():Int{
        return mState
    }

    private var mAttribute = ""
    fun ssAttribute(string:String): JJRelativeLayout {
        mAttribute = string
        return this
    }

    fun ggAttribute(): String {
        return mAttribute
    }

    fun ssBackgroundColor(color: Int): JJRelativeLayout {
        setBackgroundColor(color)
        return this
    }


    fun ssBackground(drawable: Drawable?): JJRelativeLayout {
        background = drawable
        return this
    }

    fun ssPadding(pad: JJPadding) : JJRelativeLayout {
        mlpPadding = pad
        setPaddingRelative(pad.left,pad.top,pad.right,pad.bottom)
        return this
    }


    fun ssVisibility(type: Int): JJRelativeLayout {
        visibility = type
        return this
    }



    fun ssMinimumHeight(h:Int): JJRelativeLayout {
        minimumHeight = h
        return this
    }

    fun ssMinimumWidth(w:Int): JJRelativeLayout {
        minimumWidth = w
        return this
    }

    //endregion


    //region ConstraintLayout LandScape Params
    protected val mConstraintSetLandScape = ConstraintSet()


    fun cllFloatCustomAttribute(attrName: String, value: Float): JJRelativeLayout {
        mConstraintSet.setFloatValue(id,attrName,value)
        return this
    }

    fun cllIntCustomAttribute(attrName: String, value: Int): JJRelativeLayout {
        mConstraintSet.setIntValue(id,attrName,value)
        return this
    }

    fun cllColorCustomAttribute(attrName: String, value: Int): JJRelativeLayout {
        mConstraintSet.setColorValue(id,attrName,value)
        return this
    }

    fun cllStringCustomAttribute(attrName: String, value: String): JJRelativeLayout {
        mConstraintSet.setStringValue(id,attrName,value)
        return this
    }

    fun cllRotation(float: Float): JJRelativeLayout {
        mConstraintSet.setRotation(id,float)
        return this
    }

    fun cllRotationX(float: Float): JJRelativeLayout {
        mConstraintSet.setRotationX(id,float)
        return this
    }

    fun cllRotationY(float: Float): JJRelativeLayout {
        mConstraintSet.setRotationY(id,float)
        return this
    }

    fun cllTranslation(x: Float,y: Float): JJRelativeLayout {
        mConstraintSet.setTranslation(id,x,y)
        return this
    }
    fun cllTranslationX(x: Float): JJRelativeLayout {
        mConstraintSet.setTranslationX(id,x)
        return this
    }

    fun cllTranslationY(y: Float): JJRelativeLayout {
        mConstraintSet.setTranslationY(id,y)
        return this
    }

    fun cllTranslationZ(z: Float): JJRelativeLayout {
        mConstraintSet.setTranslationZ(id,z)
        return this
    }

    fun cllTransformPivot(x: Float, y: Float): JJRelativeLayout {
        mConstraintSet.setTransformPivot(id,x,y)
        return this
    }

    fun cllTransformPivotX(x: Float): JJRelativeLayout {
        mConstraintSet.setTransformPivotX(id,x)
        return this
    }

    fun cllTransformPivotY(y: Float): JJRelativeLayout {
        mConstraintSet.setTransformPivotY(id,y)
        return this
    }

    fun cllScaleX(x: Float): JJRelativeLayout {
        mConstraintSet.setScaleX(id,x)
        return this
    }

    fun cllScaleY(y: Float): JJRelativeLayout {
        mConstraintSet.setScaleY(id,y)
        return this
    }

    fun cllDimensionRatio(ratio: String): JJRelativeLayout {
        mConstraintSet.setDimensionRatio(id,ratio)
        return this
    }

    fun cllAlpha(alpha: Float): JJRelativeLayout {
        mConstraintSet.setAlpha(id,alpha)
        return this
    }


    fun cllApply(): JJRelativeLayout {
        mConstraintSetLandScape.applyTo(parent as ConstraintLayout)
        return this
    }

    fun cllVisibilityMode(visibility: Int): JJRelativeLayout {
        mConstraintSetLandScape.setVisibilityMode(id, visibility)
        return this
    }

    fun cllVerticalBias(float: Float): JJRelativeLayout {
        mConstraintSetLandScape.setVerticalBias(id,float)
        return this
    }
    fun cllHorizontalBias(float: Float): JJRelativeLayout {
        mConstraintSetLandScape.setHorizontalBias(id,float)
        return this
    }

    fun cllCenterHorizontallyOf(viewId: Int, marginStart: Int = 0, marginEnd: Int = 0): JJRelativeLayout {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.START, marginStart)
        mConstraintSetLandScape.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.END, marginEnd)
        mConstraintSetLandScape.setHorizontalBias(id,0.5f)
        return this
    }
    fun cllCenterVerticallyOf(viewId: Int,marginTop: Int = 0, marginBottom: Int = 0): JJRelativeLayout {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, marginTop)
        mConstraintSetLandScape.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, marginBottom)
        mConstraintSetLandScape.setVerticalBias(id,0.5f)
        return this
    }

    fun cllMargins(margins: JJMargin) : JJRelativeLayout {
        mConstraintSetLandScape.setMargin(id,ConstraintSet.TOP,margins.top)
        mConstraintSetLandScape.setMargin(id,ConstraintSet.BOTTOM,margins.bottom)
        mConstraintSetLandScape.setMargin(id,ConstraintSet.END,margins.right)
        mConstraintSetLandScape.setMargin(id,ConstraintSet.START,margins.left)
        return this
    }


    fun cllTopToTop(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, margin)
        return this
    }

    fun cllTopToTopParent(margin: Int = 0): JJRelativeLayout {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin)
        return this
    }


    fun cllTopToBottom(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun cllTopToBottomParent(margin: Int = 0): JJRelativeLayout {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun cllBottomToTop(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.TOP, margin)
        return this
    }

    fun cllBottomToTopParent(margin: Int = 0): JJRelativeLayout {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin)
        return this
    }

    fun cllBottomToBottom(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun cllBottomToBottomParent(margin: Int = 0): JJRelativeLayout {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun cllStartToStart(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.START, margin)
        return this
    }

    fun cllStartToStartParent(margin: Int = 0): JJRelativeLayout {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin)
        return this
    }

    fun cllStartToEnd(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.END, margin)
        return this
    }

    fun cllStartToEndParent(margin: Int = 0): JJRelativeLayout {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END, margin)
        return this
    }

    fun cllEndToEnd(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.END, margin)
        return this
    }

    fun cllEndToEndParent(margin: Int = 0): JJRelativeLayout {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin)
        return this
    }


    fun cllEndToStart(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.START, margin)
        return this
    }

    fun cllEndToStartParent(margin: Int = 0): JJRelativeLayout {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START, margin)
        return this
    }


    fun cllWidth(width: Int): JJRelativeLayout {
        mConstraintSetLandScape.constrainWidth(id, width)
        return this
    }

    fun cllHeight(height: Int): JJRelativeLayout {
        mConstraintSetLandScape.constrainHeight(id, height)
        return this
    }

    fun cllPercentWidth(width: Float): JJRelativeLayout {
        mConstraintSetLandScape.constrainPercentWidth(id, width)
        return this
    }

    fun cllPercentHeight(height: Float): JJRelativeLayout {
        mConstraintSetLandScape.constrainPercentHeight(id, height)
        return this
    }

    fun cllCenterInParent(): JJRelativeLayout {
        mConstraintSetLandScape.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSetLandScape.setVerticalBias(id, 0.5f)
        mConstraintSetLandScape.setHorizontalBias(id, 0.5f)
        return this
    }

    fun cllCenterInParent(verticalBias: Float, horizontalBias: Float, margin: JJMargin): JJRelativeLayout {
        mConstraintSetLandScape.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin.left)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin.right)
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin.top)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin.bottom)
        mConstraintSetLandScape.setVerticalBias(id, verticalBias)
        mConstraintSetLandScape.setHorizontalBias(id, horizontalBias)
        return this
    }

    fun cllCenterInParentVertically(): JJRelativeLayout {
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSetLandScape.setVerticalBias(id, 0.5f)
        return this
    }

    fun cllCenterInParentHorizontally(): JJRelativeLayout {
        mConstraintSetLandScape.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSetLandScape.setHorizontalBias(id, 0.5f)
        return this
    }

    fun cllCenterInParentVertically(bias: Float, topMargin: Int, bottomMargin: Int): JJRelativeLayout {
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
        mConstraintSetLandScape.setVerticalBias(id, bias)
        return this
    }

    fun cllCenterInParentHorizontally(bias: Float, startMargin: Int, endtMargin: Int): JJRelativeLayout {
        mConstraintSetLandScape.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, startMargin)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, endtMargin)
        mConstraintSetLandScape.setHorizontalBias(id, bias)
        return this
    }


    fun cllCenterInParentTopVertically(): JJRelativeLayout {
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSetLandScape.setVerticalBias(id, 0.5f)
        return this
    }


    fun cllCenterInParentBottomVertically(): JJRelativeLayout {
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSetLandScape.setVerticalBias(id, 0.5f)
        return this
    }

    fun cllCenterInParentStartHorizontally(): JJRelativeLayout {
        mConstraintSetLandScape.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSetLandScape.setHorizontalBias(id, 0.5f)
        return this
    }

    fun cllCenterInParentEndHorizontally(): JJRelativeLayout {
        mConstraintSetLandScape.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSetLandScape.setHorizontalBias(id, 0.5f)
        return this
    }

    fun cllCenterInTopVertically(topId: Int): JJRelativeLayout {
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, topId, ConstraintSet.TOP, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, topId, ConstraintSet.TOP, 0)
        mConstraintSetLandScape.setVerticalBias(id, 0.5f)
        return this
    }


    fun cllCenterInBottomVertically(bottomId: Int): JJRelativeLayout {
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, bottomId, ConstraintSet.BOTTOM, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, bottomId, ConstraintSet.BOTTOM, 0)
        mConstraintSetLandScape.setVerticalBias(id, 0.5f)
        return this
    }

    fun cllCenterInStartHorizontally(startId: Int): JJRelativeLayout {
        mConstraintSetLandScape.connect(id, ConstraintSet.START, startId, ConstraintSet.START, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, startId, ConstraintSet.START, 0)
        mConstraintSetLandScape.setHorizontalBias(id, 0.5f)
        return this
    }

    fun cllCenterInEndHorizontally(endId: Int): JJRelativeLayout {
        mConstraintSetLandScape.connect(id, ConstraintSet.START, endId, ConstraintSet.END, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, endId, ConstraintSet.END, 0)
        mConstraintSetLandScape.setHorizontalBias(id, 0.5f)
        return this
    }

    fun cllCenterVertically(topId: Int, topSide: Int, topMargin: Int, bottomId: Int, bottomSide: Int, bottomMargin: Int, bias: Float): JJRelativeLayout {
        mConstraintSetLandScape.centerVertically(id, topId, topSide, topMargin, bottomId, bottomSide, bottomMargin, bias)
        return this
    }

    fun cllCenterHorizontally(startId: Int, startSide: Int, startMargin: Int, endId: Int, endSide: Int, endMargin: Int, bias: Float): JJRelativeLayout {
        mConstraintSetLandScape.centerHorizontally(id, startId, startSide, startMargin, endId, endSide, endMargin, bias)
        return this
    }


    fun cllFillParent(): JJRelativeLayout {
        mConstraintSetLandScape.constrainWidth(id,0)
        mConstraintSetLandScape.constrainHeight(id,0)
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        return this
    }

    fun cllFillParent(margin: JJMargin): JJRelativeLayout {
        mConstraintSetLandScape.constrainWidth(id,0)
        mConstraintSetLandScape.constrainHeight(id,0)
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin.top)
        mConstraintSetLandScape.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin.left)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin.right)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin.bottom)
        return this
    }

    fun cllFillParentHorizontally(): JJRelativeLayout {
        mConstraintSetLandScape.constrainWidth(id,0)
        mConstraintSetLandScape.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        return this
    }

    fun cllFillParentVertically(): JJRelativeLayout {
        mConstraintSetLandScape.constrainHeight(id,0)
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        return this
    }

    fun cllFillParentHorizontally(startMargin: Int, endMargin: Int): JJRelativeLayout {
        mConstraintSetLandScape.constrainWidth(id,0)
        mConstraintSetLandScape.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, startMargin)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, endMargin)
        return this
    }

    fun cllFillParentVertically(topMargin: Int, bottomMargin: Int): JJRelativeLayout {
        mConstraintSetLandScape.constrainHeight(id,0)
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
        return this
    }

    fun cllVisibility(visibility: Int): JJRelativeLayout {
        mConstraintSetLandScape.setVisibility(id, visibility)
        return this
    }



    fun cllElevation(elevation: Float): JJRelativeLayout {
        mConstraintSetLandScape.setElevation(id, elevation)

        return this
    }

    fun cllConstraintSet() : ConstraintSet {
        return mConstraintSetLandScape
    }

    fun cllMinWidth(w:Int): JJRelativeLayout {
        mConstraintSetLandScape.constrainMinWidth(id,w)
        return this
    }

    fun cllMinHeight(h:Int): JJRelativeLayout {
        mConstraintSetLandScape.constrainMinHeight(id,h)
        return this
    }

    fun cllMaxWidth(w:Int): JJRelativeLayout {
        mConstraintSetLandScape.constrainMaxWidth(id,w)
        return this
    }

    fun cllMaxHeight(h:Int): JJRelativeLayout {
        mConstraintSetLandScape.constrainMaxHeight(id,h)
        return this
    }






//endregion
    
    //region MotionLayout Params

    var mMotionConstraintSet: ConstraintSet? = null


    fun mlVisibilityMode(visibility: Int): JJRelativeLayout {
        mMotionConstraintSet?.setVisibilityMode(id, visibility)
        return this
    }

    fun mlVerticalBias(float: Float): JJRelativeLayout {
        mMotionConstraintSet?.setVerticalBias(id,float)
        return this
    }
    fun mlHorizontalBias(float: Float): JJRelativeLayout {
        mMotionConstraintSet?.setHorizontalBias(id,float)
        return this
    }

    fun mlCenterHorizontallyOf(viewId: Int, marginStart: Int = 0, marginEnd: Int = 0): JJRelativeLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.START, marginStart)
        mMotionConstraintSet?.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.END, marginEnd)
        mMotionConstraintSet?.setHorizontalBias(viewId,0.5f)
        return this
    }
    fun mlCenterVerticallyOf(viewId: Int,marginTop: Int = 0, marginBottom: Int = 0): JJRelativeLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, marginTop)
        mMotionConstraintSet?.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, marginBottom)
        mMotionConstraintSet?.setVerticalBias(viewId,0.5f)
        return this
    }

    fun mlMargins(margins: JJMargin) : JJRelativeLayout {
        mMotionConstraintSet?.setMargin(id,ConstraintSet.TOP,margins.top)
        mMotionConstraintSet?.setMargin(id,ConstraintSet.BOTTOM,margins.bottom)
        mMotionConstraintSet?.setMargin(id,ConstraintSet.END,margins.right)
        mMotionConstraintSet?.setMargin(id,ConstraintSet.START,margins.left)
        return this
    }


    fun mlFloatCustomAttribute(attrName: String, value: Float): JJRelativeLayout {
        mMotionConstraintSet?.setFloatValue(id,attrName,value)
        return this
    }

    fun mlIntCustomAttribute(attrName: String, value: Int): JJRelativeLayout {
        mMotionConstraintSet?.setIntValue(id,attrName,value)
        return this
    }

    fun mlColorCustomAttribute(attrName: String, value: Int): JJRelativeLayout {
        mMotionConstraintSet?.setColorValue(id,attrName,value)
        return this
    }

    fun mlStringCustomAttribute(attrName: String, value: String): JJRelativeLayout {
        mMotionConstraintSet?.setStringValue(id,attrName,value)
        return this
    }

    fun mlRotation(float: Float): JJRelativeLayout {
        mMotionConstraintSet?.setRotation(id,float)
        return this
    }

    fun mlRotationX(float: Float): JJRelativeLayout {
        mMotionConstraintSet?.setRotationX(id,float)
        return this
    }

    fun mlRotationY(float: Float): JJRelativeLayout {
        mMotionConstraintSet?.setRotationY(id,float)
        return this
    }

    fun mlTranslation(x: Float,y: Float): JJRelativeLayout {
        mMotionConstraintSet?.setTranslation(id,x,y)
        return this
    }
    fun mlTranslationX(x: Float): JJRelativeLayout {
        mMotionConstraintSet?.setTranslationX(id,x)
        return this
    }

    fun mlTranslationY(y: Float): JJRelativeLayout {
        mMotionConstraintSet?.setTranslationY(id,y)
        return this
    }

    fun mlTranslationZ(z: Float): JJRelativeLayout {
        mMotionConstraintSet?.setTranslationZ(id,z)
        return this
    }

    fun mlTransformPivot(x: Float, y: Float): JJRelativeLayout {
        mMotionConstraintSet?.setTransformPivot(id,x,y)
        return this
    }

    fun mlTransformPivotX(x: Float): JJRelativeLayout {
        mMotionConstraintSet?.setTransformPivotX(id,x)
        return this
    }

    fun mlTransformPivotY(y: Float): JJRelativeLayout {
        mMotionConstraintSet?.setTransformPivotY(id,y)
        return this
    }

    fun mlScaleX(x: Float): JJRelativeLayout {
        mMotionConstraintSet?.setScaleX(id,x)
        return this
    }

    fun mlScaleY(y: Float): JJRelativeLayout {
        mMotionConstraintSet?.setScaleY(id,y)
        return this
    }

    fun mlDimensionRatio(ratio: String): JJRelativeLayout {
        mMotionConstraintSet?.setDimensionRatio(id,ratio)
        return this
    }

    fun mlAlpha(alpha: Float): JJRelativeLayout {
        mMotionConstraintSet?.setAlpha(id,alpha)
        return this
    }



    fun mlTopToTop(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, margin)
        return this
    }

    fun mlTopToTopParent(margin: Int = 0): JJRelativeLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin)
        return this
    }


    fun mlTopToBottomOf(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun mlTopToBottomParent(margin: Int = 0): JJRelativeLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun mlBottomToTopOf(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.TOP, margin)

        return this
    }

    fun mlBottomToTopParent(margin: Int = 0): JJRelativeLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin)

        return this
    }

    fun mlBottomToBottomOf(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, margin)

        return this
    }

    fun mlBottomToBottomParent(margin: Int = 0): JJRelativeLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin)

        return this
    }

    fun mlStartToStartOf(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.START, margin)

        return this
    }

    fun mlStartToStartParent(margin: Int = 0): JJRelativeLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin)

        return this
    }

    fun mlStartToEndOf(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.END, margin)

        return this
    }

    fun mlStartToEndParent(margin: Int = 0): JJRelativeLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END, margin)

        return this
    }

    fun mlEndToEndOf(viewId: Int, margin: Int= 0): JJRelativeLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.END, margin)

        return this
    }

    fun mlEndToEndParent(margin: Int = 0): JJRelativeLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin)

        return this
    }


    fun mlEndToStartOf(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.START, margin)
        return this
    }

    fun mlEndToStartParent(margin: Int = 0): JJRelativeLayout {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START, margin)
        return this
    }


    fun mlWidth(width: Int): JJRelativeLayout {
        mMotionConstraintSet?.constrainWidth(id, width)
        return this
    }

    fun mlHeight(height: Int): JJRelativeLayout {
        mMotionConstraintSet?.constrainHeight(id, height)
        return this
    }

    fun mlPercentWidth(width: Float): JJRelativeLayout {
        mMotionConstraintSet?.constrainPercentWidth(id, width)
        return this
    }

    fun mlPercentHeight(height: Float): JJRelativeLayout {
        mMotionConstraintSet?.constrainPercentHeight(id, height)
        return this
    }

    fun mlCenterInParent(): JJRelativeLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)

        return this
    }

    fun mlCenterInParent(verticalBias: Float, horizontalBias: Float, margin: JJMargin): JJRelativeLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin.left)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin.right)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin.top)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin.bottom)
        mMotionConstraintSet?.setVerticalBias(id, verticalBias)
        mMotionConstraintSet?.setHorizontalBias(id, horizontalBias)
        return this
    }

    fun mlCenterInParentVertically(): JJRelativeLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)

        return this
    }

    fun mlCenterInParentHorizontally(): JJRelativeLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)
        return this
    }

    fun mlCenterInParentVertically(bias: Float, topMargin: Int, bottomMargin: Int): JJRelativeLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
        mMotionConstraintSet?.setVerticalBias(id, bias)
        return this
    }

    fun mlCenterInParentHorizontally(bias: Float, startMargin: Int, endtMargin: Int): JJRelativeLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, startMargin)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, endtMargin)
        mMotionConstraintSet?.setHorizontalBias(id, bias)
        return this
    }


    fun mlCenterInParentTopVertically(): JJRelativeLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)
        return this
    }


    fun mlCenterInParentBottomVertically(): JJRelativeLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)
        return this
    }

    fun mlCenterInParentStartHorizontally(): JJRelativeLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)
        return this
    }

    fun mlCenterInParentEndHorizontally(): JJRelativeLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)
        return this
    }

    fun mlCenterInTopVerticallyOf(viewId: Int): JJRelativeLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, viewId, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)
        return this
    }


    fun mlCenterInBottomVerticallyOf(viewId: Int): JJRelativeLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, viewId, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)
        return this
    }

    fun mlCenterInStartHorizontallyOf(viewId: Int): JJRelativeLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, viewId, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, viewId, ConstraintSet.START, 0)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)
        return this
    }

    fun mlCenterInEndHorizontallyOf(viewId: Int): JJRelativeLayout {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, viewId, ConstraintSet.END, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, viewId, ConstraintSet.END, 0)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)
        return this
    }

    fun mlCenterVertically(topId: Int, topSide: Int, topMargin: Int, bottomId: Int, bottomSide: Int, bottomMargin: Int, bias: Float): JJRelativeLayout {
        mMotionConstraintSet?.centerVertically(id, topId, topSide, topMargin, bottomId, bottomSide, bottomMargin, bias)
        return this
    }

    fun mlCenterHorizontally(startId: Int, startSide: Int, startMargin: Int, endId: Int, endSide: Int, endMargin: Int, bias: Float): JJRelativeLayout {
        mMotionConstraintSet?.centerHorizontally(id, startId, startSide, startMargin, endId, endSide, endMargin, bias)
        return this
    }


    fun mlFillParent(): JJRelativeLayout {
        mMotionConstraintSet?.constrainWidth(id,0)
        mMotionConstraintSet?.constrainHeight(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        return this
    }

    fun mlFillParent(margin: JJMargin): JJRelativeLayout {
        mMotionConstraintSet?.constrainWidth(id,0)
        mMotionConstraintSet?.constrainHeight(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin.top)
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin.left)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin.right)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin.bottom)
        return this
    }

    fun mlFillParentHorizontally(): JJRelativeLayout {
        mMotionConstraintSet?.constrainWidth(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        return this
    }

    fun mlFillParentVertically(): JJRelativeLayout {
        mMotionConstraintSet?.constrainHeight(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        return this
    }

    fun mlFillParentHorizontally(startMargin: Int, endMargin: Int): JJRelativeLayout {
        mMotionConstraintSet?.constrainWidth(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, startMargin)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, endMargin)
        return this
    }

    fun mlFillParentVertically(topMargin: Int, bottomMargin: Int): JJRelativeLayout {
        mMotionConstraintSet?.constrainHeight(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
        return this
    }

    fun mlVisibility(visibility: Int): JJRelativeLayout {
        mMotionConstraintSet?.setVisibility(id, visibility)
        return this
    }

    fun mlElevation(elevation: Float): JJRelativeLayout {
        mMotionConstraintSet?.setElevation(id, elevation)
        return this
    }

    fun mlApply(): JJRelativeLayout {
        mMotionConstraintSet?.applyTo(parent as ConstraintLayout)
        return this
    }

    fun mlSetConstraint(cs : ConstraintSet?): JJRelativeLayout {
        mMotionConstraintSet = cs
        return this
    }

    fun mlDisposeConstraint(): JJRelativeLayout {
        mMotionConstraintSet = null
        return this
    }

    //endregion

    //region ConstraintLayout Params
    protected val mConstraintSet = ConstraintSet()


    fun clFloatCustomAttribute(attrName: String, value: Float): JJRelativeLayout {
        mConstraintSet.setFloatValue(id,attrName,value)
        return this
    }

    fun clIntCustomAttribute(attrName: String, value: Int): JJRelativeLayout {
        mConstraintSet.setIntValue(id,attrName,value)
        return this
    }

    fun clColorCustomAttribute(attrName: String, value: Int): JJRelativeLayout {
        mConstraintSet.setColorValue(id,attrName,value)
        return this
    }

    fun clStringCustomAttribute(attrName: String, value: String): JJRelativeLayout {
        mConstraintSet.setStringValue(id,attrName,value)
        return this
    }

    fun clRotation(float: Float): JJRelativeLayout {
        mConstraintSet.setRotation(id,float)
        return this
    }

    fun clRotationX(float: Float): JJRelativeLayout {
        mConstraintSet.setRotationX(id,float)
        return this
    }

    fun clRotationY(float: Float): JJRelativeLayout {
        mConstraintSet.setRotationY(id,float)
        return this
    }

    fun clTranslation(x: Float,y: Float): JJRelativeLayout {
        mConstraintSet.setTranslation(id,x,y)
        return this
    }
    fun clTranslationX(x: Float): JJRelativeLayout {
        mConstraintSet.setTranslationX(id,x)
        return this
    }

    fun clTranslationY(y: Float): JJRelativeLayout {
        mConstraintSet.setTranslationY(id,y)
        return this
    }

    fun clTranslationZ(z: Float): JJRelativeLayout {
        mConstraintSet.setTranslationZ(id,z)
        return this
    }

    fun clTransformPivot(x: Float, y: Float): JJRelativeLayout {
        mConstraintSet.setTransformPivot(id,x,y)
        return this
    }

    fun clTransformPivotX(x: Float): JJRelativeLayout {
        mConstraintSet.setTransformPivotX(id,x)
        return this
    }

    fun clTransformPivotY(y: Float): JJRelativeLayout {
        mConstraintSet.setTransformPivotY(id,y)
        return this
    }

    fun clScaleX(x: Float): JJRelativeLayout {
        mConstraintSet.setScaleX(id,x)
        return this
    }

    fun clScaleY(y: Float): JJRelativeLayout {
        mConstraintSet.setScaleY(id,y)
        return this
    }

    fun clDimensionRatio(ratio: String): JJRelativeLayout {
        mConstraintSet.setDimensionRatio(id,ratio)
        return this
    }

    fun clAlpha(alpha: Float): JJRelativeLayout {
        mConstraintSet.setAlpha(id,alpha)
        return this
    }


    fun clGetConstraint() : ConstraintSet {
        return mConstraintSet
    }

    fun clMinWidth(w:Int): JJRelativeLayout {
        mConstraintSet.constrainMinWidth(id,w)
        return this
    }

    fun clMinHeight(h:Int): JJRelativeLayout {
        mConstraintSet.constrainMinHeight(id,h)
        return this
    }

    fun clMaxWidth(w:Int): JJRelativeLayout {
        mConstraintSet.constrainMaxWidth(id,w)
        return this
    }

    fun clMaxHeight(h:Int): JJRelativeLayout {
        mConstraintSet.constrainMaxHeight(id,h)
        return this
    }


    fun clVisibilityMode(mode: Int): JJRelativeLayout {
        mConstraintSet.setVisibilityMode(id,mode)
        return this
    }


    fun clApply(): JJRelativeLayout {
        mConstraintSet.applyTo(parent as ConstraintLayout)
        return this
    }

    fun clVerticalBias(float: Float): JJRelativeLayout {
        mConstraintSet.setVerticalBias(id,float)
        return this
    }
    fun clHorizontalBias(float: Float): JJRelativeLayout {
        mConstraintSet.setHorizontalBias(id,float)
        return this
    }

    fun clCenterHorizontallyOf(viewId: Int): JJRelativeLayout {
        mConstraintSet.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.START, 0)
        mConstraintSet.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.END, 0)
        mConstraintSet.setHorizontalBias(id,0.5f)
        return this
    }
    fun clCenterVerticallyOf(viewId: Int): JJRelativeLayout {
        mConstraintSet.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, 0)
        mConstraintSet.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, 0)
        mConstraintSet.setVerticalBias(id,0.5f)
        return this
    }

    fun clMargins(margins: JJMargin) : JJRelativeLayout {
        mConstraintSet.setMargin(id,ConstraintSet.TOP,margins.top)
        mConstraintSet.setMargin(id,ConstraintSet.BOTTOM,margins.bottom)
        mConstraintSet.setMargin(id,ConstraintSet.END,margins.right)
        mConstraintSet.setMargin(id,ConstraintSet.START,margins.left)
        return this
    }


    fun clTopToTop(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mConstraintSet.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, margin)
        return this
    }

    fun clTopToTopParent(margin: Int = 0): JJRelativeLayout {
        mConstraintSet.connect(this.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin)
        return this
    }


    fun clTopToBottom(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mConstraintSet.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun clTopToBottomParent(margin: Int = 0): JJRelativeLayout {
        mConstraintSet.connect(this.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun clBottomToTop(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mConstraintSet.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.TOP, margin)
        return this
    }

    fun clBottomToTopParent(margin: Int = 0): JJRelativeLayout {
        mConstraintSet.connect(this.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin)
        return this
    }

    fun clBottomToBottom(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mConstraintSet.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun clBottomToBottomParent(margin: Int = 0): JJRelativeLayout {
        mConstraintSet.connect(this.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun clStartToStart(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mConstraintSet.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.START, margin)
        return this
    }

    fun clStartToStartParent(margin: Int = 0): JJRelativeLayout {
        mConstraintSet.connect(this.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin)
        return this
    }

    fun clStartToEnd(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mConstraintSet.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.END, margin)
        return this
    }

    fun clStartToEndParent(margin: Int = 0): JJRelativeLayout {
        mConstraintSet.connect(this.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END, margin)
        return this
    }

    fun clEndToEnd(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mConstraintSet.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.END, margin)
        return this
    }

    fun clEndToEndParent(margin: Int = 0): JJRelativeLayout {
        mConstraintSet.connect(this.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin)
        return this
    }


    fun clEndToStart(viewId: Int, margin: Int = 0): JJRelativeLayout {
        mConstraintSet.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.START, margin)
        return this
    }

    fun clEndToStartParent(margin: Int = 0): JJRelativeLayout {
        mConstraintSet.connect(this.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START, margin)
        return this
    }


    fun clWidth(width: Int): JJRelativeLayout {
        mConstraintSet.constrainWidth(id, width)
        return this
    }

    fun clHeight(height: Int): JJRelativeLayout {
        mConstraintSet.constrainHeight(id, height)
        return this
    }

    fun clPercentWidth(width: Float): JJRelativeLayout {
        mConstraintSet.constrainPercentWidth(id, width)
        return this
    }

    fun clPercentHeight(height: Float): JJRelativeLayout {
        mConstraintSet.constrainPercentHeight(id, height)
        return this
    }

    fun clCenterInParent(): JJRelativeLayout {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterInParent(verticalBias: Float, horizontalBias: Float, margin: JJMargin): JJRelativeLayout {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin.left)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin.right)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin.top)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin.bottom)
        mConstraintSet.setVerticalBias(id, verticalBias)
        mConstraintSet.setHorizontalBias(id, horizontalBias)
        return this
    }

    fun clCenterInParentVertically(): JJRelativeLayout {
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        return this
    }

    fun clCenterInParentHorizontally(): JJRelativeLayout {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterInParentVertically(bias: Float, topMargin: Int, bottomMargin: Int): JJRelativeLayout {
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
        mConstraintSet.setVerticalBias(id, bias)
        return this
    }

    fun clCenterInParentHorizontally(bias: Float, startMargin: Int, endtMargin: Int): JJRelativeLayout {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, startMargin)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, endtMargin)
        mConstraintSet.setHorizontalBias(id, bias)
        return this
    }


    fun clCenterInParentTopVertically(): JJRelativeLayout {
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        return this
    }


    fun clCenterInParentBottomVertically(): JJRelativeLayout {
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        return this
    }

    fun clCenterInParentStartHorizontally(): JJRelativeLayout {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterInParentEndHorizontally(): JJRelativeLayout {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterInTopVertically(topId: Int): JJRelativeLayout {
        mConstraintSet.connect(id, ConstraintSet.TOP, topId, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, topId, ConstraintSet.TOP, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        return this
    }


    fun clCenterInBottomVertically(bottomId: Int): JJRelativeLayout {
        mConstraintSet.connect(id, ConstraintSet.TOP, bottomId, ConstraintSet.BOTTOM, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, bottomId, ConstraintSet.BOTTOM, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        return this
    }

    fun clCenterInStartHorizontally(startId: Int): JJRelativeLayout {
        mConstraintSet.connect(id, ConstraintSet.START, startId, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, startId, ConstraintSet.START, 0)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterInEndHorizontally(endId: Int): JJRelativeLayout {
        mConstraintSet.connect(id, ConstraintSet.START, endId, ConstraintSet.END, 0)
        mConstraintSet.connect(id, ConstraintSet.END, endId, ConstraintSet.END, 0)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterVertically(topId: Int, topSide: Int, topMargin: Int, bottomId: Int, bottomSide: Int, bottomMargin: Int, bias: Float): JJRelativeLayout {
        mConstraintSet.centerVertically(id, topId, topSide, topMargin, bottomId, bottomSide, bottomMargin, bias)
        return this
    }

    fun clCenterHorizontally(startId: Int, startSide: Int, startMargin: Int, endId: Int, endSide: Int, endMargin: Int, bias: Float): JJRelativeLayout {
        mConstraintSet.centerHorizontally(id, startId, startSide, startMargin, endId, endSide, endMargin, bias)
        return this
    }


    fun clFillParent(): JJRelativeLayout {
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.constrainHeight(id,0)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        return this
    }

    fun clFillParent(margin: JJMargin): JJRelativeLayout {
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.constrainHeight(id,0)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin.top)
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin.left)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin.right)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin.bottom)
        return this
    }

    fun clFillParentHorizontally(): JJRelativeLayout {
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        return this
    }

    fun clFillParentVertically(): JJRelativeLayout {
        mConstraintSet.constrainHeight(id,0)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        return this
    }

    fun clFillParentHorizontally(startMargin: Int, endMargin: Int): JJRelativeLayout {
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, startMargin)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, endMargin)
        return this
    }

    fun clFillParentVertically(topMargin: Int, bottomMargin: Int): JJRelativeLayout {
        mConstraintSet.constrainHeight(id,0)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
        return this
    }

    fun clVisibility(visibility: Int): JJRelativeLayout {
        mConstraintSet.setVisibility(id, visibility)
        return this
    }

    fun clElevation(elevation: Float): JJRelativeLayout {
        mConstraintSet.setElevation(id, elevation)

        return this
    }

    //endregion
    
    //region LinearLayout Params

    private var mLlp: LinearLayout.LayoutParams? = null


    private fun setupLlp() {
        if (mLlp == null) {
            mLlp =  layoutParams as? LinearLayout.LayoutParams
            layoutParams = mLlp
        }
    }

    fun llWidth(width: Int): JJRelativeLayout {
        setupLlp()
        mLlp!!.width = width
        return this
    }

    fun llHeight(height: Int): JJRelativeLayout {
        setupLlp()
        mLlp!!.height = height
        return this
    }

    fun llWeight(weigth: Float): JJRelativeLayout {
        setupLlp()
        mLlp!!.weight = weigth
        return this
    }

    fun llGravity(gravity: Int): JJRelativeLayout {
        setupLlp()
        mLlp!!.gravity = gravity
        return this
    }

    //endregion

    //region ScrollView Params

    private var mSvp: FrameLayout.LayoutParams? = null
    private fun setupSvp() {
        if (mSvp == null) {
            mSvp =  layoutParams as? FrameLayout.LayoutParams
            layoutParams = mSvp
        }
    }

    fun svWidth(width: Int): JJRelativeLayout {
        setupSvp()
        mSvp!!.width = width
        return this
    }

    fun svHeight(height: Int): JJRelativeLayout {
        setupSvp()
        mSvp!!.height = height
        return this
    }
    //endregion

}
