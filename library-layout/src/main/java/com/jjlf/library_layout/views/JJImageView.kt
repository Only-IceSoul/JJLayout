package com.jjlf.library_layout.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.animation.Interpolator
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.jjlf.library_layout.R
import com.jjlf.jjkit_layoututils.JJScreen
import com.jjlf.jjkit_layoututils.JJMargin
import com.jjlf.jjkit_layoututils.JJPadding

@SuppressLint("ResourceType")
open class JJImageView : AppCompatImageView {

    //region init

    constructor(context: Context) : this(context,null)

    private var mSupportLandScape = false
    private var mIgnoreCl = false
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        setupInitConstraint()
        setupLayout(attrs)
    }

    private var mClMargin = JJMargin()
    private var mCllMargin = JJMargin()

    @SuppressLint("CustomViewStyleable")
    private fun setupLayout(attrs: AttributeSet?){
        val a = context.obtainStyledAttributes(attrs,
            R.styleable.JJConstraintLayout, 0, 0)
        mIgnoreCl = a.getBoolean(R.styleable.JJConstraintLayout_layout_ignoreCl,false)
        mConfigurationChanged = a.getBoolean(R.styleable.JJConstraintLayout_support_configuration_changed,false)
        mSupportLandScape = a.getBoolean(R.styleable.JJConstraintLayout_support_landscape,false)

        setupAndroidBase(attrs)

        for (i in 0 until a.indexCount) {
            //region standard portrait
            setupMarginLp(a, i)
            setupPaddingLp(a,i)
            setupSizeLp(a,i)
            //endregion
            //region constraint Layout portrait
            setupSizeCl(a,i)
            setupAnchorsCl(a, i)
            setupMarginCl(a,i)
            //endregion

            //region standard landscape
            setupMarginLpl(a,i)
            setupPaddingLpl(a,i)
            setupSizeLpl(a,i)
            //endregion
            //region constraint Layout landscape
            setupSizeCll(a,i)
            setupAnchorsCll(a, i)
            setupMarginCll(a,i)
            //endregion
        }
        a.recycle()

        clMargins(mClMargin)
        cllMargins(mCllMargin)

    }
    private fun setupAndroidBase(attrs: AttributeSet?){
        val attrsArray = intArrayOf(
            android.R.attr.id,
            android.R.attr.layout_width, // 1
            android.R.attr.layout_height // 2
        )
        val ba = context.obtainStyledAttributes(attrs,
            attrsArray, 0, 0)
        val resId = ba.getResourceId(0,View.generateViewId())
        id = resId
        val attrWidth = ba.getLayoutDimension(1, 0)
        val attrHeight = ba.getLayoutDimension(2, 0)

        if(attrWidth > 0 || attrWidth == -2) clWidth(attrWidth)
        if(attrHeight > 0 || attrHeight == -2) clHeight(attrHeight)

        mlpHeight = attrHeight
        mlpWidth = attrWidth

        ba.recycle()

    }

    private fun setupSizeLp(a: TypedArray, index:Int){
        when(a.getIndex(index)){
            R.styleable.JJConstraintLayout_lpHeightPercentScreenWidth -> {
                mlpHeight = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_lpHeightPercentScreenWidth,0f))
            }
            R.styleable.JJConstraintLayout_lpHeightPercentScreenHeight -> {
                mlpHeight = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_lpHeightPercentScreenHeight,0f))
            }
            R.styleable.JJConstraintLayout_lpWidthPercentScreenWidth -> {
                mlpWidth = JJScreen.percentWidth( a.getFloat(R.styleable.JJConstraintLayout_lpWidthPercentScreenWidth,0f))
            }
            R.styleable.JJConstraintLayout_lpWidthPercentScreenHeight -> {
                mlpWidth = JJScreen.percentHeight( a.getFloat(R.styleable.JJConstraintLayout_lpWidthPercentScreenHeight,0f))
            }
            R.styleable.JJConstraintLayout_lpHeightResponsive -> {
                mlpHeight =  responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lpHeightResponsive)
            }
            R.styleable.JJConstraintLayout_lpWidthResponsive -> {
                mlpWidth =  responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lpWidthResponsive)
            }
            R.styleable.JJConstraintLayout_lpHeightResponsivePercentScreenHeight -> {
                mlpHeight = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lpHeightResponsivePercentScreenHeight)
            }
            R.styleable.JJConstraintLayout_lpWidthResponsivePercentScreenHeight -> {
                mlpWidth = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lpWidthResponsivePercentScreenHeight)
            }
            R.styleable.JJConstraintLayout_lpHeightResponsivePercentScreenWidth -> {
                mlpHeight = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lpHeightResponsivePercentScreenWidth)
            }
            R.styleable.JJConstraintLayout_lpWidthResponsivePercentScreenWidth -> {
                mlpWidth = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lpWidthResponsivePercentScreenWidth)
            }
        }
    }
    private fun setupMarginLp(a: TypedArray, index: Int){
        when(a.getIndex(index)){
            R.styleable.JJConstraintLayout_lpMarginTopPerScHeight -> {
                mlpMargins.top = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_lpMarginTopPerScHeight,0f))
            }
            R.styleable.JJConstraintLayout_lpMarginLeftPerScHeight -> {
                mlpMargins.left = JJScreen.percentHeight( a.getFloat(R.styleable.JJConstraintLayout_lpMarginLeftPerScHeight,0f))
            }
            R.styleable.JJConstraintLayout_lpMarginRightPerScHeight -> {
                mlpMargins.right = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_lpMarginRightPerScHeight,0f))
            }
            R.styleable.JJConstraintLayout_lpMarginBottomPerScHeight -> {
                mlpMargins.bottom = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_lpMarginBottomPerScHeight,0f))
            }
            R.styleable.JJConstraintLayout_lpMarginTopPerScWidth -> {
                mlpMargins.top = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_lpMarginTopPerScWidth,0f))
            }
            R.styleable.JJConstraintLayout_lpMarginLeftPerScWidth -> {
                mlpMargins.left = JJScreen.percentWidth( a.getFloat(R.styleable.JJConstraintLayout_lpMarginLeftPerScWidth,0f))
            }
            R.styleable.JJConstraintLayout_lpMarginRightPerScWidth->{
                mlpMargins.right = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_lpMarginRightPerScWidth,0f))
            }
            R.styleable.JJConstraintLayout_lpMarginBottomPerScWidth -> {
                mlpMargins.bottom = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_lpMarginBottomPerScWidth,0f))
            }
            R.styleable.JJConstraintLayout_lpMarginTopResponsive -> {
                mlpMargins.top = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lpMarginTopResponsive)
            }
            R.styleable.JJConstraintLayout_lpMarginLeftResponsive ->{
                mlpMargins.left =  responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lpMarginLeftResponsive)
            }
            R.styleable.JJConstraintLayout_lpMarginRightResponsive -> {
                mlpMargins.right =   responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lpMarginRightResponsive)
            }
            R.styleable.JJConstraintLayout_lpMarginBottomResponsive -> {
                mlpMargins.bottom =  responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lpMarginBottomResponsive)
            }
            R.styleable.JJConstraintLayout_lpMarginTopResPerScWidth -> {
                mlpMargins.top  = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lpMarginTopResPerScWidth)
            }
            R.styleable.JJConstraintLayout_lpMarginLeftResPerScWidth -> {
                mlpMargins.left = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lpMarginLeftResPerScWidth)
            }
            R.styleable.JJConstraintLayout_lpMarginRightResPerScWidth -> {
                mlpMargins.right =  responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lpMarginRightResPerScWidth)
            }
            R.styleable.JJConstraintLayout_lpMarginBottomResPerScWidth -> {
                mlpMargins.bottom = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lpMarginBottomResPerScWidth)
            }
            R.styleable.JJConstraintLayout_lpMarginTopResPerScHeight ->{
                mlpMargins.top = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lpMarginTopResPerScHeight)
            }
            R.styleable.JJConstraintLayout_lpMarginLeftResPerScHeight ->{
                mlpMargins.left = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lpMarginLeftResPerScHeight)
            }
            R.styleable.JJConstraintLayout_lpMarginRightResPerScHeight ->{
                mlpMargins.right = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lpMarginRightResPerScHeight)
            }
            R.styleable.JJConstraintLayout_lpMarginBottomResPerScHeight ->{
                mlpMargins.bottom = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lpMarginBottomResPerScHeight)
            }
            R.styleable.JJConstraintLayout_lpMarginPercentScHeight -> {
                mlpMargins = JJMargin.all(JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_lpMarginPercentScHeight,0f)))
            }
            R.styleable.JJConstraintLayout_lpMarginPercentScWidth -> {
                mlpMargins = JJMargin.all(JJScreen.percentWidth( a.getFloat(R.styleable.JJConstraintLayout_lpMarginPercentScWidth,0f)))
            }
            R.styleable.JJConstraintLayout_lpMarginResponsive -> {
                mlpMargins = JJMargin.all(responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lpMarginResponsive))
            }
            R.styleable.JJConstraintLayout_lpMarginResPerScHeight -> {
                mlpMargins = JJMargin.all(responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lpMarginResPerScHeight))
            }
            R.styleable.JJConstraintLayout_lpMarginResPerScWidth -> {
                mlpMargins =  JJMargin.all(responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lpMarginResPerScWidth))
            }
            R.styleable.JJConstraintLayout_lpMarginVerticalPerScHeight -> {
                val mar = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_lpMarginVerticalPerScHeight,0f))
                mlpMargins.top = mar ; mlpMargins.bottom = mar
            }
            R.styleable.JJConstraintLayout_lpMarginVerticalPerScWidth ->{
                val mar = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_lpMarginVerticalPerScWidth,0f))
                mlpMargins.top = mar ; mlpMargins.bottom = mar
            }
            R.styleable.JJConstraintLayout_lpMarginVerticalResponsive -> {
                val mar = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lpMarginVerticalResponsive)
                mlpMargins.top = mar ; mlpMargins.bottom = mar
            }
            R.styleable.JJConstraintLayout_lpMarginVerticalResPerScWidth -> {
                val mar = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lpMarginVerticalResPerScWidth )
                mlpMargins.top = mar ; mlpMargins.bottom = mar
            }
            R.styleable.JJConstraintLayout_lpMarginVerticalResPerScHeight -> {
                val mar = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lpMarginVerticalResPerScHeight)
                mlpMargins.top = mar ; mlpMargins.bottom = mar
            }
            R.styleable.JJConstraintLayout_lpMarginHorizontalPerScHeight -> {
                val mar = JJScreen.percentHeight( a.getFloat(R.styleable.JJConstraintLayout_lpMarginHorizontalPerScHeight,0f))
                mlpMargins.left = mar ; mlpMargins.right = mar
            }
            R.styleable.JJConstraintLayout_lpMarginHorizontalPerScWidth -> {
                val mar = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_lpMarginHorizontalPerScWidth,0f))
                mlpMargins.left = mar ; mlpMargins.right = mar
            }
            R.styleable.JJConstraintLayout_lpMarginHorizontalResponsive -> {
                val mar = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lpMarginHorizontalResponsive)
                mlpMargins.left = mar ; mlpMargins.right = mar
            }
            R.styleable.JJConstraintLayout_lpMarginHorizontalResPerScWidth -> {
                val mar = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lpMarginHorizontalResPerScWidth)
                mlpMargins.left = mar ; mlpMargins.right = mar
            }
            R.styleable.JJConstraintLayout_lpMarginHorizontalResPerScHeight -> {
                val mar = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lpMarginHorizontalResPerScHeight)
                mlpMargins.left = mar ; mlpMargins.right = mar
            }
        }

    }
    private fun setupPaddingLp(a: TypedArray, index:Int){
        when(a.getIndex(index)){
            R.styleable.JJConstraintLayout_lpPaddingTopPerScHeight -> {
                mlpPadding.top = JJScreen.percentHeight( a.getFloat(R.styleable.JJConstraintLayout_lpPaddingTopPerScHeight,0f))
            }
            R.styleable.JJConstraintLayout_lpPaddingLeftPerScHeight -> {
                mlpPadding.left = JJScreen.percentHeight( a.getFloat(R.styleable.JJConstraintLayout_lpPaddingLeftPerScHeight,0f))
            }
            R.styleable.JJConstraintLayout_lpPaddingRightPerScHeight -> {
                mlpPadding.right = JJScreen.percentHeight( a.getFloat(R.styleable.JJConstraintLayout_lpPaddingRightPerScHeight,0f))
            }
            R.styleable.JJConstraintLayout_lpPaddingBottomPerScHeight -> {
                mlpPadding.bottom = JJScreen.percentHeight( a.getFloat(R.styleable.JJConstraintLayout_lpPaddingBottomPerScHeight,0f))
            }
            R.styleable.JJConstraintLayout_lpPaddingTopPerScWidth -> {
                mlpPadding.top = JJScreen.percentWidth( a.getFloat(R.styleable.JJConstraintLayout_lpPaddingTopPerScWidth,0f))
            }
            R.styleable.JJConstraintLayout_lpPaddingLeftPerScWidth -> {
                mlpPadding.left = JJScreen.percentWidth( a.getFloat(R.styleable.JJConstraintLayout_lpPaddingLeftPerScWidth,0f))
            }
            R.styleable.JJConstraintLayout_lpPaddingRightPerScWidth -> {
                mlpPadding.right = JJScreen.percentWidth( a.getFloat(R.styleable.JJConstraintLayout_lpPaddingRightPerScWidth,0f))
            }
            R.styleable.JJConstraintLayout_lpPaddingBottomPerScWidth -> {
                mlpPadding.bottom = JJScreen.percentWidth( a.getFloat(R.styleable.JJConstraintLayout_lpPaddingBottomPerScWidth,0f))
            }
            R.styleable.JJConstraintLayout_lpPaddingTopResponsive -> {
                mlpPadding.top = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lpPaddingTopResponsive)
            }
            R.styleable.JJConstraintLayout_lpPaddingLeftResponsive -> {
                mlpPadding.left = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lpPaddingLeftResponsive)
            }
            R.styleable.JJConstraintLayout_lpPaddingRightResponsive -> {
                mlpPadding.right = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lpPaddingRightResponsive)
            }
            R.styleable.JJConstraintLayout_lpPaddingBottomResponsive -> {
                mlpPadding.bottom = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lpPaddingBottomResponsive)
            }
            R.styleable.JJConstraintLayout_lpPaddingTopResPerScWidth -> {
                mlpPadding.top = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lpPaddingTopResPerScWidth )
            }
            R.styleable.JJConstraintLayout_lpPaddingLeftResPerScWidth -> {
                mlpPadding.left = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lpPaddingLeftResPerScWidth )
            }
            R.styleable.JJConstraintLayout_lpPaddingRightResPerScWidth -> {
                mlpPadding.right = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lpPaddingRightResPerScWidth )
            }
            R.styleable.JJConstraintLayout_lpPaddingBottomResPerScWidth -> {
                mlpPadding.bottom = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lpPaddingBottomResPerScWidth )
            }

            R.styleable.JJConstraintLayout_lpPaddingTopResPerScHeight -> {
                mlpPadding.top = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lpPaddingTopResPerScHeight )
            }
            R.styleable.JJConstraintLayout_lpPaddingLeftResPerScHeight -> {
                mlpPadding.left = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lpPaddingLeftResPerScHeight )
            }
            R.styleable.JJConstraintLayout_lpPaddingRightResPerScHeight -> {
                mlpPadding.right = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lpPaddingRightResPerScHeight )
            }
            R.styleable.JJConstraintLayout_lpPaddingBottomResPerScHeight -> {
                mlpPadding.bottom = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lpPaddingBottomResPerScHeight )
            }
            R.styleable.JJConstraintLayout_lpPaddingPercentScHeight -> {
                mlpPadding = JJPadding.all(JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_lpPaddingPercentScHeight,0f)))
            }
            R.styleable.JJConstraintLayout_lpPaddingPercentScWidth -> {
                mlpPadding = JJPadding.all(JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_lpPaddingPercentScWidth,0f)))
            }
            R.styleable.JJConstraintLayout_lpPaddingResponsive -> {
                mlpPadding = JJPadding.all(responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lpPaddingResponsive))
            }
            R.styleable.JJConstraintLayout_lpPaddingResPerScHeight -> {
                mlpPadding = JJPadding.all(responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lpPaddingResPerScHeight))
            }
            R.styleable.JJConstraintLayout_lpPaddingResPerScWidth -> {
                mlpPadding = JJPadding.all(responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lpPaddingResPerScWidth))
            }
            R.styleable.JJConstraintLayout_lpPaddingVerticalPerScHeight -> {
                val mar = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_lpPaddingVerticalPerScHeight,0f))
                mlpPadding.top = mar ; mlpPadding.bottom = mar
            }
            R.styleable.JJConstraintLayout_lpPaddingVerticalPerScWidth -> {
                val mar = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_lpPaddingVerticalPerScWidth,0f))
                mlpPadding.top = mar ; mlpPadding.bottom = mar
            }
            R.styleable.JJConstraintLayout_lpPaddingVerticalResponsive -> {
                val mar = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lpPaddingVerticalResponsive)
                mlpPadding.top = mar ; mlpPadding.bottom = mar
            }
            R.styleable.JJConstraintLayout_lpPaddingVerticalResPerScWidth -> {
                val mar = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lpPaddingVerticalResPerScWidth)
                mlpPadding.top = mar ; mlpPadding.bottom = mar
            }
            R.styleable.JJConstraintLayout_lpPaddingVerticalResPerScHeight -> {
                val mar = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lpPaddingVerticalResPerScHeight)
                mlpPadding.top = mar ; mlpPadding.bottom = mar
            }
            R.styleable.JJConstraintLayout_lpPaddingHorizontalPerScHeight -> {
                val mar = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_lpPaddingHorizontalPerScHeight,0f))
                mlpPadding.left = mar ; mlpPadding.right = mar
            }
            R.styleable.JJConstraintLayout_lpPaddingHorizontalPerScWidth -> {
                val mar = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_lpPaddingHorizontalPerScWidth,0f))
                mlpPadding.left = mar ; mlpPadding.right = mar
            }
            R.styleable.JJConstraintLayout_lpPaddingHorizontalResponsive -> {
                val mar = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lpPaddingHorizontalResponsive)
                mlpPadding.left = mar ; mlpPadding.right = mar
            }
            R.styleable.JJConstraintLayout_lpPaddingHorizontalResPerScWidth ->{
                val mar = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lpPaddingHorizontalResPerScWidth)
                mlpPadding.left = mar ; mlpPadding.right = mar
            }
            R.styleable.JJConstraintLayout_lpPaddingHorizontalResPerScHeight ->{
                val mar = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lpPaddingHorizontalResPerScHeight)
                mlpPadding.left = mar ; mlpPadding.right = mar
            }

        }

    }

    private fun setupSizeCl(a: TypedArray, index:Int){
        when(a.getIndex(index)){
            R.styleable.JJConstraintLayout_clHeightPercent -> {
                clPercentHeight( a.getFloat(R.styleable.JJConstraintLayout_clHeightPercent,0f))
            }
            R.styleable.JJConstraintLayout_clWidthPercent -> {
                clPercentWidth( a.getFloat(R.styleable.JJConstraintLayout_clWidthPercent,0f))
            }
            R.styleable.JJConstraintLayout_clHeightPercentScreenWidth -> {
                clHeight(JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_clHeightPercentScreenWidth,0f)))
            }
            R.styleable.JJConstraintLayout_clWidthPercentScreenWidth -> {
                clWidth(JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_clWidthPercentScreenWidth,0f)))
            }

            R.styleable.JJConstraintLayout_clHeightPercentScreenHeight -> {
                clHeight(JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_clHeightPercentScreenHeight,0f)))
            }
            R.styleable.JJConstraintLayout_clWidthPercentScreenHeight -> {
                clWidth(JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_clWidthPercentScreenHeight,0f)))
            }
            R.styleable.JJConstraintLayout_clHeightResponsive -> {
                clHeight(responsiveSizeDimension(a, R.styleable.JJConstraintLayout_clHeightResponsive))
            }
            R.styleable.JJConstraintLayout_clWidthResponsive -> {
                clWidth(responsiveSizeDimension(a, R.styleable.JJConstraintLayout_clWidthResponsive))
            }
            R.styleable.JJConstraintLayout_clHeightResponsivePercentScreenHeight ->{
                clHeight(responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_clHeightResponsivePercentScreenHeight))
            }
            R.styleable.JJConstraintLayout_clWidthResponsivePercentScreenHeight ->{
                clWidth(responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_clWidthResponsivePercentScreenHeight))
            }

            R.styleable.JJConstraintLayout_clHeightResponsivePercentScreenWidth ->{
                clHeight(responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_clHeightResponsivePercentScreenWidth))
            }
            R.styleable.JJConstraintLayout_clWidthResponsivePercentScreenWidth ->{
                clWidth(responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_clWidthResponsivePercentScreenWidth))
            }
        }




    }
    private fun setupAnchorsCl(a: TypedArray, index:Int){
        when(a.getIndex(index)){
            R.styleable.JJConstraintLayout_clFillParent -> {
                if(a.getBoolean(R.styleable.JJConstraintLayout_clFillParent,false)) clFillParent()
            }
            R.styleable.JJConstraintLayout_clFillParentHorizontally -> {
                if(a.getBoolean(R.styleable.JJConstraintLayout_clFillParentHorizontally,false)) clFillParentHorizontally()
            }
            R.styleable.JJConstraintLayout_clFillParentVertically -> {
                if(a.getBoolean(R.styleable.JJConstraintLayout_clFillParentVertically,false)) clFillParentVertically()
            }
            R.styleable.JJConstraintLayout_clCenterInParent -> {
                if(a.getBoolean(R.styleable.JJConstraintLayout_clCenterInParent,false)) clCenterInParent()
            }
            R.styleable.JJConstraintLayout_clCenterInParentHorizontally -> {
                if(a.getBoolean(R.styleable.JJConstraintLayout_clCenterInParentHorizontally,false)) clCenterInParentHorizontally()
            }
            R.styleable.JJConstraintLayout_clCenterInParentVertically -> {
                if(a.getBoolean(R.styleable.JJConstraintLayout_clCenterInParentVertically,false)) clCenterInParentVertically()
            }
            R.styleable.JJConstraintLayout_clCenterInParentTopVertically -> {
                if(a.getBoolean(R.styleable.JJConstraintLayout_clCenterInParentTopVertically,false))  clCenterInParentTopVertically()
            }
            R.styleable.JJConstraintLayout_clCenterInParentBottomVertically -> {
                if(a.getBoolean(R.styleable.JJConstraintLayout_clCenterInParentBottomVertically,false)) clCenterInParentBottomVertically()
            }
            R.styleable.JJConstraintLayout_clCenterInParentStartHorizontally -> {
                if(a.getBoolean(R.styleable.JJConstraintLayout_clCenterInParentStartHorizontally,false)) clCenterInParentStartHorizontally()
            }
            R.styleable.JJConstraintLayout_clCenterInParentEndHorizontally -> {
                if(a.getBoolean(R.styleable.JJConstraintLayout_clCenterInParentEndHorizontally,false)) clCenterInParentEndHorizontally()
            }

            R.styleable.JJConstraintLayout_clCenterInTopVerticallyOf -> {
                clCenterInTopVertically(a.getResourceId(
                    R.styleable.JJConstraintLayout_clCenterInTopVerticallyOf,
                    View.NO_ID))
            }
            R.styleable.JJConstraintLayout_clCenterInBottomVerticallyOf -> {
                clCenterInBottomVertically(a.getResourceId(
                    R.styleable.JJConstraintLayout_clCenterInBottomVerticallyOf,
                    View.NO_ID))
            }
            R.styleable.JJConstraintLayout_clCenterInStartHorizontallyOf -> {
                clCenterInStartHorizontally(a.getResourceId(
                    R.styleable.JJConstraintLayout_clCenterInStartHorizontallyOf,
                    View.NO_ID))
            }
            R.styleable.JJConstraintLayout_clCenterInEndHorizontallyOf -> {
                clCenterInEndHorizontally(a.getResourceId(
                    R.styleable.JJConstraintLayout_clCenterInEndHorizontallyOf,
                    View.NO_ID))
            }
            R.styleable.JJConstraintLayout_clCenterVerticallyOf -> {
                clCenterVerticallyOf(a.getResourceId(
                    R.styleable.JJConstraintLayout_clCenterVerticallyOf,
                    View.NO_ID))
            }
            R.styleable.JJConstraintLayout_clCenterHorizontallyOf -> {
                clCenterHorizontallyOf(a.getResourceId(
                    R.styleable.JJConstraintLayout_clCenterHorizontallyOf,
                    View.NO_ID))
            }
            R.styleable.JJConstraintLayout_clVerticalBias -> {
                clVerticalBias(a.getFloat(R.styleable.JJConstraintLayout_clVerticalBias,0.5f))
            }
            R.styleable.JJConstraintLayout_clHorizontalBias -> {
                clHorizontalBias( a.getFloat(R.styleable.JJConstraintLayout_clHorizontalBias,0.5f))
            }
            R.styleable.JJConstraintLayout_clStartToStartParent -> {
                if(a.getBoolean(R.styleable.JJConstraintLayout_clStartToStartParent,false)) clStartToStartParent()
            }
            R.styleable.JJConstraintLayout_clStartToEndParent -> {
                if(a.getBoolean(R.styleable.JJConstraintLayout_clStartToEndParent,false)) clStartToEndParent()
            }
            R.styleable.JJConstraintLayout_clEndToEndParent -> {
                if(a.getBoolean(R.styleable.JJConstraintLayout_clEndToEndParent,false)) clEndToEndParent()
            }
            R.styleable.JJConstraintLayout_clEndToStartParent -> {
                if(a.getBoolean(R.styleable.JJConstraintLayout_clEndToStartParent,false)) clEndToStartParent()
            }
            R.styleable.JJConstraintLayout_clTopToTopParent -> {
                if(a.getBoolean(R.styleable.JJConstraintLayout_clTopToTopParent,false)) clTopToTopParent()
            }
            R.styleable.JJConstraintLayout_clTopToBottomParent -> {
                if(a.getBoolean(R.styleable.JJConstraintLayout_clTopToBottomParent,false)) clTopToBottomParent()
            }
            R.styleable.JJConstraintLayout_clBottomToBottomParent -> {
                if(a.getBoolean(R.styleable.JJConstraintLayout_clBottomToBottomParent,false)) clBottomToBottomParent()
            }
            R.styleable.JJConstraintLayout_clBottomToTopParent -> {
                if(a.getBoolean(R.styleable.JJConstraintLayout_clBottomToTopParent,false)) clBottomToTopParent()
            }

            R.styleable.JJConstraintLayout_clStartToStartOf -> {
                clStartToStart(a.getResourceId(R.styleable.JJConstraintLayout_clStartToStartOf, View.NO_ID))
            }
            R.styleable.JJConstraintLayout_clStartToEndOf -> {
                clStartToEnd(a.getResourceId(R.styleable.JJConstraintLayout_clStartToEndOf, View.NO_ID))
            }
            R.styleable.JJConstraintLayout_clEndToEndOf -> {
                clEndToEnd(a.getResourceId(R.styleable.JJConstraintLayout_clEndToEndOf, View.NO_ID))
            }
            R.styleable.JJConstraintLayout_clEndToStartOf -> {
                clEndToStart(a.getResourceId(R.styleable.JJConstraintLayout_clEndToStartOf, View.NO_ID))
            }
            R.styleable.JJConstraintLayout_clTopToTopOf -> {
                clTopToTop(a.getResourceId(R.styleable.JJConstraintLayout_clTopToTopOf, View.NO_ID))
            }
            R.styleable.JJConstraintLayout_clTopToBottomOf -> {
                clTopToBottom(a.getResourceId(R.styleable.JJConstraintLayout_clTopToBottomOf, View.NO_ID))
            }
            R.styleable.JJConstraintLayout_clBottomToBottomOf -> {
                clBottomToBottom(a.getResourceId(R.styleable.JJConstraintLayout_clBottomToBottomOf, View.NO_ID))
            }
            R.styleable.JJConstraintLayout_clBottomToTopOf -> {
                clBottomToTop(a.getResourceId(R.styleable.JJConstraintLayout_clBottomToTopOf, View.NO_ID))
            }

        }
    }
    private fun setupMarginCl(a: TypedArray, index:Int){
        when(a.getIndex(index)){
            R.styleable.JJConstraintLayout_clMarginEnd ->{
                mClMargin.right = a.getDimension(R.styleable.JJConstraintLayout_clMarginEnd,0f).toInt()
            }
            R.styleable.JJConstraintLayout_clMarginStart ->{
                mClMargin.left = a.getDimension(R.styleable.JJConstraintLayout_clMarginStart,0f).toInt()
            }
            R.styleable.JJConstraintLayout_clMarginTop ->{
                mClMargin.top = a.getDimension(R.styleable.JJConstraintLayout_clMarginTop,0f).toInt()
            }
            R.styleable.JJConstraintLayout_clMarginBottom ->{
                mClMargin.bottom = a.getDimension(R.styleable.JJConstraintLayout_clMarginBottom,0f).toInt()
            }

            R.styleable.JJConstraintLayout_clMarginEndPercentScreenHeight -> {
                mClMargin.right = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_clMarginEndPercentScreenHeight,0f))
            }
            R.styleable.JJConstraintLayout_clMarginStartPercentScreenHeight -> {
                mClMargin.left = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_clMarginStartPercentScreenHeight,0f))
            }
            R.styleable.JJConstraintLayout_clMarginTopPercentScreenHeight -> {
                mClMargin.top = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_clMarginTopPercentScreenHeight,0f))
            }
            R.styleable.JJConstraintLayout_clMarginBottomPercentScreenHeight -> {
                mClMargin.bottom = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_clMarginBottomPercentScreenHeight,0f))
            }

            R.styleable.JJConstraintLayout_clMarginEndPercentScreenWidth -> {
                mClMargin.right = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_clMarginEndPercentScreenWidth,0f))
            }
            R.styleable.JJConstraintLayout_clMarginStartPercentScreenWidth -> {
                mClMargin.left = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_clMarginStartPercentScreenWidth,0f))
            }
            R.styleable.JJConstraintLayout_clMarginTopPercentScreenWidth -> {
                mClMargin.top = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_clMarginTopPercentScreenWidth,0f))
            }
            R.styleable.JJConstraintLayout_clMarginBottomPercentScreenWidth -> {
                mClMargin.bottom = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_clMarginBottomPercentScreenWidth,0f))
            }
            R.styleable.JJConstraintLayout_clMargin -> {
                mClMargin = JJMargin.all(a.getDimension(R.styleable.JJConstraintLayout_clMargin,0f).toInt())
            }
            R.styleable.JJConstraintLayout_clMarginPerScHeight -> {
                mClMargin = JJMargin.all(JJScreen.percentHeight( a.getFloat(R.styleable.JJConstraintLayout_clMarginPerScHeight,0f)))
            }
            R.styleable.JJConstraintLayout_clMarginPerScWidth -> {
                mClMargin = JJMargin.all(JJScreen.percentWidth( a.getFloat(R.styleable.JJConstraintLayout_clMarginPerScWidth,0f)))
            }
            R.styleable.JJConstraintLayout_clMarginResponsive -> {
                mClMargin = JJMargin.all(responsiveSizeDimension(a, R.styleable.JJConstraintLayout_clMarginResponsive))
            }
            R.styleable.JJConstraintLayout_clMarginResPerScHeight -> {
                mClMargin = JJMargin.all(responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_clMarginResPerScHeight))
            }
            R.styleable.JJConstraintLayout_clMarginResPerScWidth -> {
                mClMargin = JJMargin.all(responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_clMarginResPerScWidth))
            }
            R.styleable.JJConstraintLayout_clMarginEndResponsive -> {
                mClMargin.right = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_clMarginEndResponsive)
            }
            R.styleable.JJConstraintLayout_clMarginStartResponsive -> {
                mClMargin.left = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_clMarginStartResponsive)
            }
            R.styleable.JJConstraintLayout_clMarginTopResponsive -> {
                mClMargin.top = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_clMarginTopResponsive)
            }
            R.styleable.JJConstraintLayout_clMarginBottomResponsive -> {
                mClMargin.bottom = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_clMarginBottomResponsive)
            }

            R.styleable.JJConstraintLayout_clMarginEndResPerScHeight -> {
                mClMargin.right = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_clMarginEndResPerScHeight)
            }
            R.styleable.JJConstraintLayout_clMarginStartResPerScHeight -> {
                mClMargin.left = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_clMarginStartResPerScHeight)
            }
            R.styleable.JJConstraintLayout_clMarginTopResPerScHeight -> {
                mClMargin.top = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_clMarginTopResPerScHeight)
            }
            R.styleable.JJConstraintLayout_clMarginBottomResPerScHeight -> {
                mClMargin.bottom = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_clMarginBottomResPerScHeight)
            }

            R.styleable.JJConstraintLayout_clMarginEndResPerScWidth -> {
                mClMargin.right = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_clMarginEndResPerScWidth)
            }
            R.styleable.JJConstraintLayout_clMarginStartResPerScWidth -> {
                mClMargin.left = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_clMarginStartResPerScWidth)
            }
            R.styleable.JJConstraintLayout_clMarginTopResPerScWidth -> {
                mClMargin.top = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_clMarginTopResPerScWidth)
            }
            R.styleable.JJConstraintLayout_clMarginBottomResPerScWidth -> {
                mClMargin.bottom = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_clMarginBottomResPerScWidth)
            }
            R.styleable.JJConstraintLayout_clMarginVertical -> {
                val mar = a.getDimension(R.styleable.JJConstraintLayout_clMarginVertical,0f).toInt()
                mClMargin.top = mar ; mClMargin.bottom = mar
            }
            R.styleable.JJConstraintLayout_clMarginVerticalPerScHeight -> {
                val mar = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_clMarginVerticalPerScHeight,0f))
                mClMargin.top = mar ; mClMargin.bottom = mar
            }
            R.styleable.JJConstraintLayout_clMarginVerticalPerScWidth -> {
                val mar = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_clMarginVerticalPerScWidth,0f))
                mClMargin.top = mar ; mClMargin.bottom = mar
            }
            R.styleable.JJConstraintLayout_clMarginVerticalResponsive -> {
                val mar = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_clMarginVerticalResponsive)
                mClMargin.top = mar ; mClMargin.bottom = mar
            }
            R.styleable.JJConstraintLayout_clMarginVerticalResPerScHeight -> {
                val mar = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_clMarginVerticalResPerScHeight)
                mClMargin.top = mar ; mClMargin.bottom = mar
            }
            R.styleable.JJConstraintLayout_clMarginVerticalResPerScWidth -> {
                val mar = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_clMarginVerticalResPerScWidth)
                mClMargin.top = mar ; mClMargin.bottom = mar
            }

            R.styleable.JJConstraintLayout_clMarginHorizontal -> {
                val mar = a.getDimension(R.styleable.JJConstraintLayout_clMarginHorizontal,0f).toInt()
                mClMargin.left = mar ; mClMargin.right = mar
            }
            R.styleable.JJConstraintLayout_clMarginHorizontalPerScHeight -> {
                val mar = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_clMarginHorizontalPerScHeight,0f))
                mClMargin.left = mar ; mClMargin.right = mar
            }
            R.styleable.JJConstraintLayout_clMarginHorizontalPerScWidth -> {
                val mar = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_clMarginHorizontalPerScWidth,0f))
                mClMargin.left = mar ; mClMargin.right = mar
            }
            R.styleable.JJConstraintLayout_clMarginHorizontalResponsive -> {
                val mar = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_clMarginHorizontalResponsive)
                mClMargin.left = mar ; mClMargin.right = mar
            }
            R.styleable.JJConstraintLayout_clMarginHorizontalResPerScHeight -> {
                val mar = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_clMarginHorizontalResPerScHeight)
                mClMargin.left = mar ; mClMargin.right = mar
            }
            R.styleable.JJConstraintLayout_clMarginHorizontalResPerScWidth -> {
                val mar = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_clMarginHorizontalResPerScWidth)
                mClMargin.left = mar ; mClMargin.right = mar
            }

        }

    }

    private fun setupMarginLpl(a: TypedArray, index:Int) {
        when (a.getIndex(index)) {
            R.styleable.JJConstraintLayout_lplMargin -> {
                mlsMargins =
                    JJMargin.all(a.getDimension(R.styleable.JJConstraintLayout_lplMargin, 0f).toInt())
            }
            R.styleable.JJConstraintLayout_lplMarginVertical -> {
                val mar = a.getDimension(R.styleable.JJConstraintLayout_lplMarginVertical, 0f).toInt()
                mlsMargins.top = mar; mlsMargins.bottom = mar
            }
            R.styleable.JJConstraintLayout_lplMarginHorizontal -> {
                val mar =
                    a.getDimension(R.styleable.JJConstraintLayout_lplMarginHorizontal, 0f).toInt()
                mlsMargins.left = mar; mlsMargins.right = mar
            }

            R.styleable.JJConstraintLayout_lplMarginStart -> {
                mlsMargins.left =
                    a.getDimension(R.styleable.JJConstraintLayout_lplMarginStart, 0f).toInt()
            }
            R.styleable.JJConstraintLayout_lplMarginEnd -> {
                mlsMargins.right =
                    a.getDimension(R.styleable.JJConstraintLayout_lplMarginEnd, 0f).toInt()
            }
            R.styleable.JJConstraintLayout_lplMarginBottom -> {
                mlsMargins.bottom =
                    a.getDimension(R.styleable.JJConstraintLayout_lplMarginBottom, 0f).toInt()
            }
            R.styleable.JJConstraintLayout_lplMarginTop -> {
                mlsMargins.top =
                    a.getDimension(R.styleable.JJConstraintLayout_lplMarginTop, 0f).toInt()
            }

            R.styleable.JJConstraintLayout_lplMarginLeftPerScHeight -> {
                mlsMargins.left = JJScreen.percentHeight(
                    a.getFloat(
                        R.styleable.JJConstraintLayout_lplMarginLeftPerScHeight,
                        0f
                    )
                )
            }
            R.styleable.JJConstraintLayout_lplMarginRightPerScHeight -> {
                mlsMargins.right = JJScreen.percentHeight(
                    a.getFloat(
                        R.styleable.JJConstraintLayout_lplMarginRightPerScHeight,
                        0f
                    )
                )
            }
            R.styleable.JJConstraintLayout_lplMarginBottomPerScHeight -> {
                mlsMargins.bottom = JJScreen.percentHeight(
                    a.getFloat(
                        R.styleable.JJConstraintLayout_lplMarginBottomPerScHeight,
                        0f
                    )
                )
            }
            R.styleable.JJConstraintLayout_lplMarginTopPerScHeight -> {
                mlsMargins.top = JJScreen.percentHeight(
                    a.getFloat(
                        R.styleable.JJConstraintLayout_lplMarginTopPerScHeight,
                        0f
                    )
                )
            }

            R.styleable.JJConstraintLayout_lplMarginLeftPerScWidth -> {
                mlsMargins.left = JJScreen.percentWidth(
                    a.getFloat(
                        R.styleable.JJConstraintLayout_lplMarginLeftPerScWidth,
                        0f
                    )
                )
            }
            R.styleable.JJConstraintLayout_lplMarginRightPerScWidth -> {
                mlsMargins.right = JJScreen.percentWidth(
                    a.getFloat(
                        R.styleable.JJConstraintLayout_lplMarginRightPerScWidth,
                        0f
                    )
                )
            }
            R.styleable.JJConstraintLayout_lplMarginBottomPerScWidth -> {
                mlsMargins.bottom = JJScreen.percentWidth(
                    a.getFloat(
                        R.styleable.JJConstraintLayout_lplMarginBottomPerScWidth,
                        0f
                    )
                )
            }
            R.styleable.JJConstraintLayout_lplMarginTopPerScWidth -> {
                mlsMargins.top = JJScreen.percentWidth(
                    a.getFloat(
                        R.styleable.JJConstraintLayout_lplMarginTopPerScWidth,
                        0f
                    )
                )
            }

            R.styleable.JJConstraintLayout_lplMarginTopResponsive -> {
                mlsMargins.top =
                    responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lplMarginTopResponsive)
            }
            R.styleable.JJConstraintLayout_lplMarginLeftResponsive -> {
                mlsMargins.left =
                    responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lplMarginLeftResponsive)
            }
            R.styleable.JJConstraintLayout_lplMarginRightResponsive -> {
                mlsMargins.right = responsiveSizeDimension(
                    a,
                    R.styleable.JJConstraintLayout_lplMarginRightResponsive
                )
            }
            R.styleable.JJConstraintLayout_lplMarginBottomResponsive -> {
                mlsMargins.bottom = responsiveSizeDimension(
                    a,
                    R.styleable.JJConstraintLayout_lplMarginBottomResponsive
                )
            }

            R.styleable.JJConstraintLayout_lplMarginTopResPerScWidth -> {
                mlsMargins.top = responsiveSizePercentScreenWidth(
                    a,
                    R.styleable.JJConstraintLayout_lplMarginTopResPerScWidth
                )
            }
            R.styleable.JJConstraintLayout_lplMarginLeftResPerScWidth -> {
                mlsMargins.left = responsiveSizePercentScreenWidth(
                    a,
                    R.styleable.JJConstraintLayout_lplMarginLeftResPerScWidth
                )
            }
            R.styleable.JJConstraintLayout_lplMarginRightResPerScWidth -> {
                mlsMargins.right = responsiveSizePercentScreenWidth(
                    a,
                    R.styleable.JJConstraintLayout_lplMarginRightResPerScWidth
                )
            }
            R.styleable.JJConstraintLayout_lplMarginBottomResPerScWidth -> {
                mlsMargins.bottom = responsiveSizePercentScreenWidth(
                    a,
                    R.styleable.JJConstraintLayout_lplMarginBottomResPerScWidth
                )
            }

            R.styleable.JJConstraintLayout_lplMarginTopResPerScHeight -> {
                mlsMargins.top = responsiveSizePercentScreenHeight(
                    a,
                    R.styleable.JJConstraintLayout_lplMarginTopResPerScHeight
                )
            }
            R.styleable.JJConstraintLayout_lplMarginLeftResPerScHeight -> {
                mlsMargins.left = responsiveSizePercentScreenHeight(
                    a,
                    R.styleable.JJConstraintLayout_lplMarginLeftResPerScHeight
                )
            }
            R.styleable.JJConstraintLayout_lplMarginRightResPerScHeight -> {
                mlsMargins.right = responsiveSizePercentScreenHeight(
                    a,
                    R.styleable.JJConstraintLayout_lplMarginRightResPerScHeight
                )
            }
            R.styleable.JJConstraintLayout_lplMarginBottomResPerScHeight -> {
                mlsMargins.bottom = responsiveSizePercentScreenHeight(
                    a,
                    R.styleable.JJConstraintLayout_lplMarginBottomResPerScHeight
                )
            }
            R.styleable.JJConstraintLayout_lplMarginPercentScHeight -> {
                mlsMargins = JJMargin.all(
                    JJScreen.percentHeight(
                        a.getFloat(
                            R.styleable.JJConstraintLayout_lplMarginPercentScHeight,
                            0f
                        )
                    )
                )
            }
            R.styleable.JJConstraintLayout_lplMarginPercentScWidth -> {
                mlsMargins = JJMargin.all(
                    JJScreen.percentWidth(
                        a.getFloat(
                            R.styleable.JJConstraintLayout_lplMarginPercentScWidth,
                            0f
                        )
                    )
                )
            }
            R.styleable.JJConstraintLayout_lplMarginResponsive -> {
                mlsMargins = JJMargin.all(
                    responsiveSizeDimension(
                        a,
                        R.styleable.JJConstraintLayout_lplMarginResponsive
                    )
                )
            }
            R.styleable.JJConstraintLayout_lplMarginResPerScHeight -> {
                mlsMargins = JJMargin.all(
                    responsiveSizePercentScreenHeight(
                        a,
                        R.styleable.JJConstraintLayout_lplMarginResPerScHeight
                    )
                )
            }
            R.styleable.JJConstraintLayout_lplMarginResPerScWidth -> {
                mlsMargins = JJMargin.all(
                    responsiveSizePercentScreenWidth(
                        a,
                        R.styleable.JJConstraintLayout_lplMarginResPerScWidth
                    )
                )
            }

            R.styleable.JJConstraintLayout_lplMarginVerticalPerScHeight -> {
                val mar = JJScreen.percentHeight(
                    a.getFloat(
                        R.styleable.JJConstraintLayout_lplMarginVerticalPerScHeight,
                        0f
                    )
                )
                mlsMargins.top = mar; mlsMargins.bottom = mar
            }
            R.styleable.JJConstraintLayout_lplMarginVerticalPerScWidth -> {
                val mar = JJScreen.percentWidth(
                    a.getFloat(
                        R.styleable.JJConstraintLayout_lplMarginVerticalPerScWidth,
                        0f
                    )
                )
                mlsMargins.top = mar; mlsMargins.bottom = mar
            }
            R.styleable.JJConstraintLayout_lplMarginVerticalResponsive -> {
                val mar = responsiveSizeDimension(
                    a,
                    R.styleable.JJConstraintLayout_lplMarginVerticalResponsive
                )
                mlsMargins.top = mar; mlsMargins.bottom = mar
            }
            R.styleable.JJConstraintLayout_lplMarginVerticalResPerScWidth -> {
                val mar = responsiveSizePercentScreenWidth(
                    a,
                    R.styleable.JJConstraintLayout_lplMarginVerticalResPerScWidth
                )
                mlsMargins.top = mar; mlsMargins.bottom = mar
            }
            R.styleable.JJConstraintLayout_lplMarginVerticalResPerScHeight -> {
                val mar = responsiveSizePercentScreenHeight(
                    a,
                    R.styleable.JJConstraintLayout_lplMarginVerticalResPerScHeight
                )
                mlsMargins.top = mar; mlsMargins.bottom = mar
            }


            R.styleable.JJConstraintLayout_lplMarginHorizontalPerScHeight -> {
                val mar = JJScreen.percentHeight(
                    a.getFloat(
                        R.styleable.JJConstraintLayout_lplMarginHorizontalPerScHeight,
                        0f
                    )
                )
                mlsMargins.left = mar; mlsMargins.right = mar
            }
            R.styleable.JJConstraintLayout_lplMarginHorizontalPerScWidth -> {
                val mar = JJScreen.percentWidth(
                    a.getFloat(
                        R.styleable.JJConstraintLayout_lplMarginHorizontalPerScWidth,
                        0f
                    )
                )
                mlsMargins.left = mar; mlsMargins.right = mar
            }
            R.styleable.JJConstraintLayout_lplMarginHorizontalResponsive -> {
                val mar = responsiveSizeDimension(
                    a,
                    R.styleable.JJConstraintLayout_lplMarginHorizontalResponsive
                )
                mlsMargins.left = mar; mlsMargins.right = mar
            }
            R.styleable.JJConstraintLayout_lplMarginHorizontalResPerScWidth -> {
                val mar = responsiveSizePercentScreenWidth(
                    a,
                    R.styleable.JJConstraintLayout_lplMarginHorizontalResPerScWidth
                )
                mlsMargins.left = mar; mlsMargins.right = mar
            }
            R.styleable.JJConstraintLayout_lplMarginHorizontalResPerScHeight -> {
                val mar = responsiveSizePercentScreenHeight(
                    a,
                    R.styleable.JJConstraintLayout_lplMarginHorizontalResPerScHeight
                )
                mlsMargins.left = mar; mlsMargins.right = mar
            }

        }
    }
    private fun setupPaddingLpl(a: TypedArray, index: Int){
        when(a.getIndex(index)){
            R.styleable.JJConstraintLayout_lplPadding -> {
                mlsPadding = JJPadding.all( a.getDimension(R.styleable.JJConstraintLayout_lplPadding,0f).toInt())
            }
            R.styleable.JJConstraintLayout_lplPaddingVertical -> {
                val mar = a.getDimension(R.styleable.JJConstraintLayout_lplPaddingVertical,0f).toInt()
                mlsPadding.top = mar ; mlsPadding.bottom = mar
            }
            R.styleable.JJConstraintLayout_lplPaddingHorizontal -> {
                val mar = a.getDimension(R.styleable.JJConstraintLayout_lplPaddingHorizontal,0f).toInt()
                mlsPadding.left = mar ; mlsPadding.right = mar
            }
            R.styleable.JJConstraintLayout_lplPaddingStart -> {
                mlsPadding.left = a.getDimension(R.styleable.JJConstraintLayout_lplPaddingStart,0f).toInt()
            }
            R.styleable.JJConstraintLayout_lplPaddingEnd -> {
                mlsPadding.right = a.getDimension(R.styleable.JJConstraintLayout_lplPaddingEnd,0f).toInt()
            }
            R.styleable.JJConstraintLayout_lplPaddingTop -> {
                mlsPadding.top = a.getDimension(R.styleable.JJConstraintLayout_lplPaddingTop,0f).toInt()
            }
            R.styleable.JJConstraintLayout_lplPaddingBottom -> {
                mlsPadding.bottom = a.getDimension(R.styleable.JJConstraintLayout_lplPaddingBottom,0f).toInt()
            }

            R.styleable.JJConstraintLayout_lplPaddingTopPerScHeight -> {
                mlsPadding.top = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_lplPaddingTopPerScHeight,0f))
            }
            R.styleable.JJConstraintLayout_lplPaddingLeftPerScHeight -> {
                mlsPadding.left = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_lplPaddingLeftPerScHeight,0f))
            }
            R.styleable.JJConstraintLayout_lplPaddingRightPerScHeight -> {
                mlsPadding.right = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_lplPaddingRightPerScHeight,0f))
            }
            R.styleable.JJConstraintLayout_lplPaddingBottomPerScHeight -> {
                mlsPadding.bottom = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_lplPaddingBottomPerScHeight,0f))
            }

            R.styleable.JJConstraintLayout_lplPaddingTopPerScWidth -> {
                mlsPadding.top = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_lplPaddingTopPerScWidth,0f))
            }
            R.styleable.JJConstraintLayout_lplPaddingLeftPerScWidth -> {
                mlsPadding.left = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_lplPaddingLeftPerScWidth,0f))
            }
            R.styleable.JJConstraintLayout_lplPaddingRightPerScWidth -> {
                mlsPadding.right = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_lplPaddingRightPerScWidth,0f))
            }
            R.styleable.JJConstraintLayout_lplPaddingBottomPerScWidth -> {
                mlsPadding.bottom = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_lplPaddingBottomPerScWidth,0f))
            }

            R.styleable.JJConstraintLayout_lplPaddingTopResponsive -> {
                mlsPadding.top = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lplPaddingTopResponsive)
            }
            R.styleable.JJConstraintLayout_lplPaddingLeftResponsive -> {
                mlsPadding.left = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lplPaddingLeftResponsive)
            }
            R.styleable.JJConstraintLayout_lplPaddingRightResponsive -> {
                mlsPadding.right = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lplPaddingRightResponsive)
            }
            R.styleable.JJConstraintLayout_lplPaddingBottomResponsive -> {
                mlsPadding.bottom = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lplPaddingBottomResponsive)
            }

            R.styleable.JJConstraintLayout_lplPaddingTopResPerScWidth -> {
                mlsPadding.top = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lplPaddingTopResPerScWidth)
            }
            R.styleable.JJConstraintLayout_lplPaddingLeftResPerScWidth -> {
                mlsPadding.left = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lplPaddingLeftResPerScWidth)
            }
            R.styleable.JJConstraintLayout_lplPaddingRightResPerScWidth -> {
                mlsPadding.right = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lplPaddingRightResPerScWidth)
            }
            R.styleable.JJConstraintLayout_lplPaddingBottomResPerScWidth -> {
                mlsPadding.bottom = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lplPaddingBottomResPerScWidth)
            }

            R.styleable.JJConstraintLayout_lplPaddingTopResPerScHeight -> {
                mlsPadding.top = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lplPaddingTopResPerScHeight)
            }
            R.styleable.JJConstraintLayout_lplPaddingLeftResPerScHeight -> {
                mlsPadding.left = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lplPaddingLeftResPerScHeight)
            }
            R.styleable.JJConstraintLayout_lplPaddingRightResPerScHeight -> {
                mlsPadding.right = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lplPaddingRightResPerScHeight)
            }
            R.styleable.JJConstraintLayout_lplPaddingBottomResPerScHeight -> {
                mlsPadding.bottom = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lplPaddingBottomResPerScHeight)
            }
            R.styleable.JJConstraintLayout_lplPaddingPercentScHeight->{
                mlsPadding = JJPadding.all(JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_lplPaddingPercentScHeight,0f)))
            }
            R.styleable.JJConstraintLayout_lplPaddingPercentScWidth->{
                mlsPadding = JJPadding.all(JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_lplPaddingPercentScWidth,0f)))
            }
            R.styleable.JJConstraintLayout_lplPaddingResponsive->{
                mlsPadding = JJPadding.all(responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lplPaddingResponsive))
            }
            R.styleable.JJConstraintLayout_lplPaddingResPerScHeight->{
                mlsPadding = JJPadding.all(responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lplPaddingResPerScHeight))
            }
            R.styleable.JJConstraintLayout_lplPaddingResPerScWidth->{
                mlsPadding = JJPadding.all(responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lplPaddingResPerScWidth))
            }

            R.styleable.JJConstraintLayout_lplPaddingVerticalPerScHeight -> {
                val mar = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_lplPaddingVerticalPerScHeight,0f))
                mlsPadding.top = mar ; mlsPadding.bottom = mar
            }
            R.styleable.JJConstraintLayout_lplPaddingVerticalPerScWidth -> {
                val mar = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_lplPaddingVerticalPerScWidth,0f))
                mlsPadding.top = mar ; mlsPadding.bottom = mar
            }
            R.styleable.JJConstraintLayout_lplPaddingVerticalResponsive -> {
                val mar = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lplPaddingVerticalResponsive)
                mlsPadding.top = mar ; mlsPadding.bottom = mar
            }
            R.styleable.JJConstraintLayout_lplPaddingVerticalResPerScWidth -> {
                val mar = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lplPaddingVerticalResPerScWidth)
                mlsPadding.top = mar ; mlsPadding.bottom = mar
            }
            R.styleable.JJConstraintLayout_lplPaddingVerticalResPerScHeight -> {
                val mar = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lplPaddingVerticalResPerScHeight)
                mlsPadding.top = mar ; mlsPadding.bottom = mar
            }

            R.styleable.JJConstraintLayout_lplPaddingHorizontalPerScHeight -> {
                val mar = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_lplPaddingHorizontalPerScHeight,0f))
                mlsPadding.left = mar ; mlsPadding.right = mar
            }
            R.styleable.JJConstraintLayout_lplPaddingHorizontalPerScWidth -> {
                val mar = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_lplPaddingHorizontalPerScWidth,0f))
                mlsPadding.left = mar ; mlsPadding.right = mar
            }
            R.styleable.JJConstraintLayout_lplPaddingHorizontalResponsive -> {
                val mar = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lplPaddingHorizontalResponsive)
                mlsPadding.left = mar ; mlsPadding.right = mar
            }
            R.styleable.JJConstraintLayout_lplPaddingHorizontalResPerScWidth -> {
                val mar = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lplPaddingHorizontalResPerScWidth)
                mlsPadding.left = mar ; mlsPadding.right = mar
            }
            R.styleable.JJConstraintLayout_lplPaddingHorizontalResPerScHeight -> {
                val mar = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lplPaddingHorizontalResPerScHeight)
                mlsPadding.left = mar ; mlsPadding.right = mar
            }
        }
    }
    private fun setupSizeLpl(a: TypedArray, index:Int){
        when (a.getIndex(index)) {
            R.styleable.JJConstraintLayout_layout_height_landscape -> {
                mlsHeight = a.getLayoutDimension(R.styleable.JJConstraintLayout_layout_height_landscape,0)
            }
            R.styleable.JJConstraintLayout_layout_width_landscape -> {
                mlsWidth = a.getLayoutDimension(R.styleable.JJConstraintLayout_layout_width_landscape,0)
            }
            R.styleable.JJConstraintLayout_lplHeightPercentScreenWidth -> {
                mlsHeight = JJScreen.percentWidth( a.getFloat(R.styleable.JJConstraintLayout_lplHeightPercentScreenWidth,0f))
            }
            R.styleable.JJConstraintLayout_lplWidthPercentScreenWidth -> {
                mlsWidth = JJScreen.percentWidth( a.getFloat(R.styleable.JJConstraintLayout_lplWidthPercentScreenWidth,0f))
            }

            R.styleable.JJConstraintLayout_lplHeightPercentScreenHeight -> {
                mlsHeight = JJScreen.percentHeight( a.getFloat(R.styleable.JJConstraintLayout_lplHeightPercentScreenHeight,0f))
            }
            R.styleable.JJConstraintLayout_lplWidthPercentScreenHeight -> {
                mlsWidth = JJScreen.percentHeight( a.getFloat(R.styleable.JJConstraintLayout_lplWidthPercentScreenHeight,0f))
            }
            R.styleable.JJConstraintLayout_lplHeightResponsive -> {
                mlsHeight = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lplHeightResponsive)
            }
            R.styleable.JJConstraintLayout_lplWidthResponsive -> {
                mlsWidth = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_lplWidthResponsive)
            }
            R.styleable.JJConstraintLayout_lplHeightResponsivePercentScreenHeight -> {
                mlsHeight = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lplHeightResponsivePercentScreenHeight)
            }
            R.styleable.JJConstraintLayout_lplWidthResponsivePercentScreenHeight -> {
                mlsWidth = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_lplWidthResponsivePercentScreenHeight)
            }
            R.styleable.JJConstraintLayout_lplHeightResponsivePercentScreenWidth -> {
                mlsHeight = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lplHeightResponsivePercentScreenWidth)
            }
            R.styleable.JJConstraintLayout_lplWidthResponsivePercentScreenWidth -> {
                mlsWidth = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_lplWidthResponsivePercentScreenWidth)
            }

        }
    }

    private fun setupMarginCll(a: TypedArray, index:Int){
        when (a.getIndex(index)) {
            R.styleable.JJConstraintLayout_cllMarginEnd -> {
                mCllMargin.right = a.getDimension(R.styleable.JJConstraintLayout_cllMarginEnd,0f).toInt()
            }
            R.styleable.JJConstraintLayout_cllMarginStart -> {
                mCllMargin.left = a.getDimension(R.styleable.JJConstraintLayout_cllMarginStart,0f).toInt()
            }
            R.styleable.JJConstraintLayout_cllMarginTop -> {
                mCllMargin.top = a.getDimension(R.styleable.JJConstraintLayout_cllMarginTop,0f).toInt()
            }
            R.styleable.JJConstraintLayout_cllMarginBottom -> {
                mCllMargin.bottom = a.getDimension(R.styleable.JJConstraintLayout_cllMarginBottom,0f).toInt()
            }

            R.styleable.JJConstraintLayout_cllMarginEndPercentScreenHeight -> {
                mCllMargin.right = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_cllMarginEndPercentScreenHeight,0f))
            }
            R.styleable.JJConstraintLayout_cllMarginStartPercentScreenHeight -> {
                mCllMargin.left = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_cllMarginStartPercentScreenHeight,0f))
            }
            R.styleable.JJConstraintLayout_cllMarginTopPercentScreenHeight -> {
                mCllMargin.top = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_cllMarginTopPercentScreenHeight,0f))
            }
            R.styleable.JJConstraintLayout_cllMarginBottomPercentScreenHeight -> {
                mCllMargin.bottom = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_cllMarginBottomPercentScreenHeight,0f))
            }

            R.styleable.JJConstraintLayout_cllMarginEndPercentScreenWidth -> {
                mCllMargin.right = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_cllMarginEndPercentScreenWidth,0f))
            }
            R.styleable.JJConstraintLayout_cllMarginStartPercentScreenWidth -> {
                mCllMargin.left = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_cllMarginStartPercentScreenWidth,0f))
            }
            R.styleable.JJConstraintLayout_cllMarginTopPercentScreenWidth -> {
                mCllMargin.top = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_cllMarginTopPercentScreenWidth,0f))
            }
            R.styleable.JJConstraintLayout_cllMarginBottomPercentScreenWidth -> {
                mCllMargin.bottom = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_cllMarginBottomPercentScreenWidth,0f))
            }

            R.styleable.JJConstraintLayout_cllMargin -> {
                mCllMargin = JJMargin.all(a.getDimension(R.styleable.JJConstraintLayout_cllMargin,0f).toInt())
            }
            R.styleable.JJConstraintLayout_cllMarginPerScHeight -> {
                mCllMargin = JJMargin.all(JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_cllMarginPerScHeight,0f)))
            }
            R.styleable.JJConstraintLayout_cllMarginPerScWidth -> {
                mCllMargin = JJMargin.all(JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_cllMarginPerScWidth,0f)))
            }
            R.styleable.JJConstraintLayout_cllMarginResponsive -> {
                mCllMargin = JJMargin.all(responsiveSizeDimension(a, R.styleable.JJConstraintLayout_cllMarginResponsive))
            }
            R.styleable.JJConstraintLayout_cllMarginResPerScHeight -> {
                mCllMargin = JJMargin.all(responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_cllMarginResPerScHeight))
            }
            R.styleable.JJConstraintLayout_cllMarginResPerScWidth -> {
                mCllMargin = JJMargin.all(responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_cllMarginResPerScWidth))
            }
            R.styleable.JJConstraintLayout_cllMarginEndResponsive ->{
                mCllMargin.right = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_cllMarginEndResponsive)
            }
            R.styleable.JJConstraintLayout_cllMarginStartResponsive ->{
                mCllMargin.left = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_cllMarginStartResponsive)
            }
            R.styleable.JJConstraintLayout_cllMarginTopResponsive ->{
                mCllMargin.top = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_cllMarginTopResponsive)
            }
            R.styleable.JJConstraintLayout_cllMarginBottomResponsive ->{
                mCllMargin.bottom = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_cllMarginBottomResponsive)
            }

            R.styleable.JJConstraintLayout_cllMarginEndResPerScHeight ->{
                mCllMargin.right = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_cllMarginEndResPerScHeight)
            }
            R.styleable.JJConstraintLayout_cllMarginStartResPerScHeight ->{
                mCllMargin.left = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_cllMarginStartResPerScHeight)
            }
            R.styleable.JJConstraintLayout_cllMarginTopResPerScHeight ->{
                mCllMargin.top = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_cllMarginTopResPerScHeight)
            }
            R.styleable.JJConstraintLayout_cllMarginBottomResPerScHeight ->{
                mCllMargin.bottom = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_cllMarginBottomResPerScHeight)
            }

            R.styleable.JJConstraintLayout_cllMarginEndResPerScWidth ->{
                mCllMargin.right = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_cllMarginEndResPerScWidth)
            }
            R.styleable.JJConstraintLayout_cllMarginStartResPerScWidth ->{
                mCllMargin.left = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_cllMarginStartResPerScWidth)
            }
            R.styleable.JJConstraintLayout_cllMarginTopResPerScWidth ->{
                mCllMargin.top = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_cllMarginTopResPerScWidth)
            }
            R.styleable.JJConstraintLayout_cllMarginBottomResPerScWidth ->{
                mCllMargin.bottom = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_cllMarginBottomResPerScWidth)
            }

            R.styleable.JJConstraintLayout_cllMarginVertical->{
                val mar = a.getDimension(R.styleable.JJConstraintLayout_cllMarginVertical,0f).toInt()
                mCllMargin.top = mar ; mCllMargin.bottom = mar
            }
            R.styleable.JJConstraintLayout_cllMarginVerticalPerScHeight->{
                val mar = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_cllMarginVerticalPerScHeight,0f))
                mCllMargin.top = mar ; mCllMargin.bottom = mar
            }
            R.styleable.JJConstraintLayout_cllMarginVerticalPerScWidth->{
                val mar = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_cllMarginVerticalPerScWidth,0f))
                mCllMargin.top = mar ; mCllMargin.bottom = mar
            }
            R.styleable.JJConstraintLayout_cllMarginVerticalResponsive->{
                val mar = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_cllMarginVerticalResponsive)
                mCllMargin.top = mar ; mCllMargin.bottom = mar
            }
            R.styleable.JJConstraintLayout_cllMarginVerticalResPerScHeight->{
                val mar = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_cllMarginVerticalResPerScHeight)
                mCllMargin.top = mar ; mCllMargin.bottom = mar
            }
            R.styleable.JJConstraintLayout_cllMarginVerticalResPerScWidth->{
                val mar = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_cllMarginVerticalResPerScWidth)
                mCllMargin.top = mar ; mCllMargin.bottom = mar
            }

            R.styleable.JJConstraintLayout_cllMarginHorizontal->{
                val mar = a.getDimension(R.styleable.JJConstraintLayout_cllMarginHorizontal,0f).toInt()
                mCllMargin.top = mar ; mCllMargin.bottom = mar
            }
            R.styleable.JJConstraintLayout_cllMarginHorizontalPerScHeight->{
                val mar = JJScreen.percentHeight(a.getFloat(R.styleable.JJConstraintLayout_cllMarginHorizontalPerScHeight,0f))
                mCllMargin.left = mar ; mCllMargin.right = mar
            }
            R.styleable.JJConstraintLayout_cllMarginHorizontalPerScWidth->{
                val mar = JJScreen.percentWidth(a.getFloat(R.styleable.JJConstraintLayout_cllMarginHorizontalPerScWidth,0f))
                mCllMargin.left = mar ; mCllMargin.right = mar
            }
            R.styleable.JJConstraintLayout_cllMarginHorizontalResponsive->{
                val mar = responsiveSizeDimension(a, R.styleable.JJConstraintLayout_cllMarginHorizontalResponsive)
                mCllMargin.left = mar ; mCllMargin.right = mar
            }
            R.styleable.JJConstraintLayout_cllMarginHorizontalResPerScHeight->{
                val mar = responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_cllMarginHorizontalResPerScHeight)
                mCllMargin.left = mar ; mCllMargin.right = mar
            }
            R.styleable.JJConstraintLayout_cllMarginHorizontalResPerScWidth->{
                val mar = responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_cllMarginHorizontalResPerScWidth)
                mCllMargin.left = mar ; mCllMargin.right = mar
            }
        }
    }
    private fun setupSizeCll(a: TypedArray, index:Int){
        when (a.getIndex(index)) {
            R.styleable.JJConstraintLayout_layout_height_landscape->{
                val value = a.getLayoutDimension(R.styleable.JJConstraintLayout_layout_height_landscape,0)
                if(value > 0 || value == -2 ) cllHeight(value)
            }
            R.styleable.JJConstraintLayout_layout_width_landscape->{
                val value = a.getLayoutDimension(R.styleable.JJConstraintLayout_layout_width_landscape,0)
                if(value > 0 || value == -2 ) cllWidth(value)
            }
            R.styleable.JJConstraintLayout_cllHeightPercent -> {
                cllPercentHeight( a.getFloat(R.styleable.JJConstraintLayout_cllHeightPercent,0f))
            }
            R.styleable.JJConstraintLayout_cllWidthPercent -> {
                cllPercentWidth( a.getFloat(R.styleable.JJConstraintLayout_cllWidthPercent,0f))
            }
            R.styleable.JJConstraintLayout_cllHeightPercentScreenWidth -> {
                cllHeight(JJScreen.percentWidth( a.getFloat(R.styleable.JJConstraintLayout_cllHeightPercentScreenWidth,0f)))
            }
            R.styleable.JJConstraintLayout_cllWidthPercentScreenWidth -> {
                cllWidth(JJScreen.percentWidth( a.getFloat(R.styleable.JJConstraintLayout_cllWidthPercentScreenWidth,0f)))
            }
            R.styleable.JJConstraintLayout_cllHeightPercentScreenHeight -> {
                cllHeight(JJScreen.percentHeight( a.getFloat(R.styleable.JJConstraintLayout_cllHeightPercentScreenHeight,0f)))
            }
            R.styleable.JJConstraintLayout_cllWidthPercentScreenHeight -> {
                cllWidth(JJScreen.percentHeight( a.getFloat(R.styleable.JJConstraintLayout_cllWidthPercentScreenHeight,0f)))
            }
            R.styleable.JJConstraintLayout_cllHeightResponsive -> {
                cllHeight(responsiveSizeDimension(a, R.styleable.JJConstraintLayout_cllHeightResponsive))
            }
            R.styleable.JJConstraintLayout_cllWidthResponsive -> {
                cllWidth(responsiveSizeDimension(a, R.styleable.JJConstraintLayout_cllWidthResponsive))
            }

            R.styleable.JJConstraintLayout_cllHeightResponsivePercentScreenHeight -> {
                cllHeight(responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_cllHeightResponsivePercentScreenHeight))
            }
            R.styleable.JJConstraintLayout_cllWidthResponsivePercentScreenHeight -> {
                cllWidth(responsiveSizePercentScreenHeight(a, R.styleable.JJConstraintLayout_cllWidthResponsivePercentScreenHeight))
            }
            R.styleable.JJConstraintLayout_cllWidthResponsivePercentScreenWidth -> {
                cllHeight(responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_cllWidthResponsivePercentScreenWidth))
            }
            R.styleable.JJConstraintLayout_cllHeightResponsivePercentScreenWidth -> {
                cllWidth(responsiveSizePercentScreenWidth(a, R.styleable.JJConstraintLayout_cllHeightResponsivePercentScreenWidth))
            }
        }

    }
    private fun setupAnchorsCll(a: TypedArray, index:Int){
        when (a.getIndex(index)) {
            R.styleable.JJConstraintLayout_cllFillParent ->{
                if(a.getBoolean(R.styleable.JJConstraintLayout_cllFillParent,false)) cllFillParent()
            }
            R.styleable.JJConstraintLayout_cllFillParentHorizontally ->{
                if(a.getBoolean(R.styleable.JJConstraintLayout_cllFillParentHorizontally,false)) cllFillParentHorizontally()
            }
            R.styleable.JJConstraintLayout_cllFillParentVertically ->{
                if(a.getBoolean(R.styleable.JJConstraintLayout_cllFillParentVertically,false)) cllFillParentVertically()
            }
            R.styleable.JJConstraintLayout_cllCenterInParent ->{
                if(a.getBoolean(R.styleable.JJConstraintLayout_cllCenterInParent,false)) cllCenterInParent()
            }
            R.styleable.JJConstraintLayout_cllCenterInParentHorizontally ->{
                if(a.getBoolean(R.styleable.JJConstraintLayout_cllCenterInParentHorizontally,false)) cllCenterInParentHorizontally()
            }
            R.styleable.JJConstraintLayout_cllCenterInParentVertically ->{
                if(a.getBoolean(R.styleable.JJConstraintLayout_cllCenterInParentVertically,false)) cllCenterInParentVertically()
            }
            R.styleable.JJConstraintLayout_cllCenterInParentTopVertically ->{
                if(a.getBoolean(R.styleable.JJConstraintLayout_cllCenterInParentTopVertically,false)) cllCenterInParentTopVertically()
            }
            R.styleable.JJConstraintLayout_cllCenterInParentBottomVertically ->{
                if(a.getBoolean(R.styleable.JJConstraintLayout_cllCenterInParentBottomVertically,false)) cllCenterInParentBottomVertically()
            }
            R.styleable.JJConstraintLayout_cllCenterInParentStartHorizontally ->{
                if(a.getBoolean(R.styleable.JJConstraintLayout_cllCenterInParentStartHorizontally,false)) cllCenterInParentStartHorizontally()
            }
            R.styleable.JJConstraintLayout_cllCenterInParentEndHorizontally ->{
                if(a.getBoolean(R.styleable.JJConstraintLayout_cllCenterInParentEndHorizontally,false)) cllCenterInParentEndHorizontally()
            }
            R.styleable.JJConstraintLayout_cllCenterInTopVerticallyOf ->{
                cllCenterInTopVertically(a.getResourceId(
                    R.styleable.JJConstraintLayout_cllCenterInTopVerticallyOf,
                    View.NO_ID))
            }
            R.styleable.JJConstraintLayout_cllCenterInBottomVerticallyOf ->{
                cllCenterInBottomVertically(a.getResourceId(
                    R.styleable.JJConstraintLayout_cllCenterInBottomVerticallyOf,
                    View.NO_ID))
            }
            R.styleable.JJConstraintLayout_cllCenterInStartHorizontallyOf ->{
                cllCenterInStartHorizontally(a.getResourceId(
                    R.styleable.JJConstraintLayout_cllCenterInStartHorizontallyOf,
                    View.NO_ID))
            }
            R.styleable.JJConstraintLayout_cllCenterInEndHorizontallyOf ->{
                cllCenterInEndHorizontally(a.getResourceId(
                    R.styleable.JJConstraintLayout_cllCenterInEndHorizontallyOf,
                    View.NO_ID))
            }
            R.styleable.JJConstraintLayout_cllCenterVerticallyOf ->{
                cllCenterVerticallyOf(a.getResourceId(
                    R.styleable.JJConstraintLayout_cllCenterVerticallyOf,
                    View.NO_ID))
            }
            R.styleable.JJConstraintLayout_cllCenterHorizontallyOf ->{
                cllCenterHorizontallyOf(a.getResourceId(
                    R.styleable.JJConstraintLayout_cllCenterHorizontallyOf,
                    View.NO_ID))
            }
            R.styleable.JJConstraintLayout_cllVerticalBias -> {
                cllVerticalBias(a.getFloat(R.styleable.JJConstraintLayout_cllVerticalBias,0.5f))
            }
            R.styleable.JJConstraintLayout_cllHorizontalBias -> {
                cllHorizontalBias(a.getFloat(R.styleable.JJConstraintLayout_cllHorizontalBias,0.5f))
            }

            R.styleable.JJConstraintLayout_cllStartToStartParent ->{
                if(a.getBoolean(R.styleable.JJConstraintLayout_cllStartToStartParent,false)) cllStartToStartParent()
            }
            R.styleable.JJConstraintLayout_cllStartToEndParent ->{
                if(a.getBoolean(R.styleable.JJConstraintLayout_cllStartToEndParent,false)) cllStartToEndParent()
            }
            R.styleable.JJConstraintLayout_cllEndToEndParent ->{
                if(a.getBoolean(R.styleable.JJConstraintLayout_cllEndToEndParent,false)) cllEndToEndParent()
            }
            R.styleable.JJConstraintLayout_cllEndToStartParent ->{
                if(a.getBoolean(R.styleable.JJConstraintLayout_cllEndToStartParent,false)) cllEndToStartParent()
            }
            R.styleable.JJConstraintLayout_cllTopToTopParent ->{
                if(a.getBoolean(R.styleable.JJConstraintLayout_cllTopToTopParent,false)) cllTopToTopParent()
            }
            R.styleable.JJConstraintLayout_cllTopToBottomParent ->{
                if(a.getBoolean(R.styleable.JJConstraintLayout_cllTopToBottomParent,false)) cllTopToBottomParent()
            }
            R.styleable.JJConstraintLayout_cllBottomToBottomParent ->{
                if(a.getBoolean(R.styleable.JJConstraintLayout_cllBottomToBottomParent,false)) cllBottomToBottomParent()
            }
            R.styleable.JJConstraintLayout_cllBottomToTopParent ->{
                if(a.getBoolean(R.styleable.JJConstraintLayout_cllBottomToTopParent,false)) cllBottomToTopParent()
            }

            R.styleable.JJConstraintLayout_cllStartToStartOf -> {
                cllStartToStart(a.getResourceId(R.styleable.JJConstraintLayout_cllStartToStartOf, View.NO_ID))
            }
            R.styleable.JJConstraintLayout_cllStartToEndOf -> {
                cllStartToEnd(a.getResourceId(R.styleable.JJConstraintLayout_cllStartToEndOf, View.NO_ID))
            }
            R.styleable.JJConstraintLayout_cllEndToEndOf -> {
                cllEndToEnd(a.getResourceId(R.styleable.JJConstraintLayout_cllEndToEndOf, View.NO_ID))
            }
            R.styleable.JJConstraintLayout_cllEndToStartOf -> {
                cllEndToStart(a.getResourceId(R.styleable.JJConstraintLayout_cllEndToStartOf, View.NO_ID))
            }
            R.styleable.JJConstraintLayout_cllTopToTopOf -> {
                cllTopToTop(a.getResourceId(R.styleable.JJConstraintLayout_cllTopToTopOf, View.NO_ID))
            }
            R.styleable.JJConstraintLayout_cllTopToBottomOf -> {
                cllTopToBottom(a.getResourceId(R.styleable.JJConstraintLayout_cllTopToBottomOf, View.NO_ID))
            }
            R.styleable.JJConstraintLayout_cllBottomToBottomOf -> {
                cllBottomToBottom(a.getResourceId(R.styleable.JJConstraintLayout_cllBottomToBottomOf, View.NO_ID))
            }
            R.styleable.JJConstraintLayout_cllBottomToTopOf -> {
                cllBottomToTop(a.getResourceId(R.styleable.JJConstraintLayout_cllBottomToTopOf, View.NO_ID))
            }

        }

    }

    private fun setupInitConstraint(){
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.constrainHeight(id,0)
        mConstraintSetLandScape.constrainWidth(id,0)
        mConstraintSetLandScape.constrainHeight(id,0)
        mConstraintSet.setVisibilityMode(id,ConstraintSet.VISIBILITY_MODE_IGNORE)
        mConstraintSetLandScape.setVisibilityMode(id,ConstraintSet.VISIBILITY_MODE_IGNORE)
    }
    private fun responsiveSizeDimension(a: TypedArray, style:Int) : Int {
        val t = resources.obtainTypedArray(a.getResourceId(style,
            View.NO_ID))
        val re = JJScreen.responsiveSize(t.getDimension(0, 0f).toInt(),
            t.getDimension(1, 0f).toInt(),
            t.getDimension(2, 0f).toInt(),
            t.getDimension(3, 0f).toInt())
        t.recycle()
        return re
    }

    private fun responsiveSizePercentScreenWidth(a: TypedArray, style:Int) : Int {
        val t = resources.obtainTypedArray(a.getResourceId(style,
            View.NO_ID))
        val re = JJScreen.responsiveSizePercentScreenWidth(t.getFloat(0, 0f),
            t.getFloat(1, 0f),
            t.getFloat(2, 0f),
            t.getFloat(3, 0f))
        t.recycle()
        return re
    }
    private fun responsiveSizePercentScreenHeight(a: TypedArray, style:Int) : Int {
        val t = resources.obtainTypedArray(a.getResourceId(style,
            View.NO_ID))
        val re = JJScreen.responsiveSizePercentScreenHeight(t.getFloat(0, 0f),
            t.getFloat(1, 0f),
            t.getFloat(2, 0f),
            t.getFloat(3, 0f))
        t.recycle()
        return re
    }


    var mInit = true
    private var mlsHeight = -3
    private var mlsWidth = -3
    private var mlsMargins = JJMargin()
    private var mlsPadding = JJPadding()
    private var mConfigurationChanged = false
    private var mlpHeight = -3
    private var mlpWidth = -3
    private var mlpMargins = JJMargin()
    private var mlpPadding = JJPadding()
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if(mInit){
            applyLayoutParams(resources.configuration.orientation)
            mInit = false
        }
    }

    private fun applyLayoutParams(orientation:Int){
        if(layoutParams == null) layoutParams = ViewGroup.MarginLayoutParams(0, 0)
        val isLandScale = orientation == Configuration.ORIENTATION_LANDSCAPE
        if(isLandScale && mSupportLandScape) applyLayoutParamsLandScape() else applyLayoutParamsPortrait()
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
                if(mlpHeight != -3) layoutParams.height = mlpHeight
                if(mlpWidth != -3)  layoutParams.width = mlpWidth
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
                if(mlsHeight != -3) layoutParams.height = mlsHeight
                if(mlsWidth != -3)  layoutParams.width = mlsWidth
                val margin = layoutParams as? ViewGroup.MarginLayoutParams
                margin?.topMargin = mlsMargins.top
                margin?.marginStart = mlsMargins.left
                margin?.marginEnd = mlsMargins.right
                margin?.bottomMargin = mlsMargins.bottom
            }
        }
        setPaddingRelative(mlsPadding.left,mlsPadding.top,mlsPadding.right,mlsPadding.bottom)
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if(mConfigurationChanged){
            applyLayoutParams(newConfig.orientation)
        }
    }

    //endregion

    //region override
    
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
            mRectClip.left += mlpPadding.left.toFloat()
            mRectClip.right -= mlpPadding.right.toFloat()
            mRectClip.top += mlpPadding.top.toFloat()
            mRectClip.bottom -= mlpPadding.bottom.toFloat()

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
                    mClosurePathClipAll?.invoke(mRectClip, mPathClipAll,mlpPadding)
                }
                canvas.clipPath(mPathClipAll)
            }
            if (mIsClipOutPathAll) {
                canvas.restore()
                canvas.save()
                if (mIsPathClosureClipAll) {
                    mPathClipAll.reset()
                    mClosurePathClipAll?.invoke(mRectClip, mPathClipAll,mlpPadding)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) canvas.clipOutPath(mPathClipAll)
                else canvas.clipPath(mPathClipAll, Region.Op.DIFFERENCE)
            }

        }
        //all and riple
        super.draw(canvas)

    }


    //endregion

    //region method set get
    private var mIdentifier = 0
    private var mState = 0
    private var mAttribute = ""

    fun ssSupportLandScape(support:Boolean) : JJImageView {
        mSupportLandScape = support
        return this
    }

    fun ssSupportConfigurationChanged(support:Boolean) : JJImageView {
        mConfigurationChanged = support
        return this
    }


    fun ssClipChildrenToPath(path: Path): JJImageView {
        mPathClipChildren = path
        mIsPathClosureClipChildren = false
        mIsClipInPathChildren = true
        mIsClipChildrenEnabled = true
        mIsClipOutPathChildren = false
        return this
    }

    fun ssClipAllToPath(path: Path): JJImageView {
        mPathClipAll = path
        mIsPathClosureClipAll = false
        mIsClipInPathAll = true
        mIsClipAllEnabled = true
        mIsClipOutPathAll = false
        return this
    }


    fun ssClipOutChildrenToPath(path: Path): JJImageView {
        mPathClipChildren = path
        mIsPathClosureClipChildren = false
        mIsClipOutPathChildren = true
        mIsClipChildrenEnabled = true
        mIsClipInPathChildren = false
        return this
    }


    fun ssClipOutAllToPath(path: Path): JJImageView {
        mPathClipAll = path
        mIsPathClosureClipAll = false
        mIsClipOutPathAll = true
        mIsClipAllEnabled = true
        mIsClipInPathAll = false
        return this
    }

    fun ssClipChildrenToPath(closure:(RectF, Path)->Unit): JJImageView {
        mIsClipInPathChildren = true
        mIsPathClosureClipChildren = true
        mIsClipOutPathChildren = false
        mIsClipChildrenEnabled = true
        mClosurePathClipChildren = closure
        return this
    }

    fun ssClipAllToPath(closure:(RectF, Path, JJPadding)->Unit): JJImageView {
        mIsClipInPathAll = true
        mIsPathClosureClipAll = true
        mIsClipOutPathAll = false
        mIsClipAllEnabled = true
        mClosurePathClipAll = closure
        return this
    }

    fun ssClipOutChildrenToPath(closure:(RectF, Path)->Unit): JJImageView {
        mIsClipInPathChildren = false
        mIsPathClosureClipChildren = true
        mIsClipOutPathChildren = true
        mIsClipChildrenEnabled = true
        mClosurePathClipChildren = closure
        return this
    }

    fun ssClipOutAllToPath(closure:(RectF, Path, JJPadding)->Unit): JJImageView {
        mIsClipInPathAll = false
        mIsPathClosureClipAll = true
        mIsClipOutPathAll = true
        mIsClipAllEnabled = true
        mClosurePathClipAll = closure
        return this
    }

    fun disposeClipPathChildren(): JJImageView {
        mIsClipOutPathChildren = false
        mIsPathClosureClipChildren = false
        mIsClipChildrenEnabled = false
        mIsClipInPathChildren = false
        mClosurePathClipChildren = null
        return  this
    }
    fun disposeClipPathAll(): JJImageView {
        mIsClipOutPathAll = false
        mIsPathClosureClipAll = false
        mIsClipAllEnabled = false
        mIsClipInPathAll = false
        mClosurePathClipAll = null
        return  this
    }




    fun ssIdentifier(value: Int): JJImageView {
        mIdentifier = value
        return this
    }

    fun ggIdentifier():Int{
        return mIdentifier
    }


    fun ssState(state: Int): JJImageView {
        mState = state
        return this
    }

    fun ggState():Int{
        return mState
    }


    fun ssAttribute(string:String): JJImageView {
        mAttribute = string
        return this
    }

    fun ggAttribute(): String {
        return mAttribute
    }
    fun ssIsFocusable(boolean: Boolean): JJImageView {
        isFocusable = boolean
        return this
    }

    fun ssIsFocusableInTouchMode(boolean: Boolean): JJImageView {
        isFocusableInTouchMode = boolean
        return this
    }

    fun ssImageDrawable(drawable: Drawable?): JJImageView {
        setImageDrawable(drawable)
        return this
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun ssOutlineProvider(provider: ViewOutlineProvider): JJImageView {
        outlineProvider = provider
        return this
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun ssClipToOutline(boolean: Boolean): JJImageView {
        clipToOutline = boolean
        return this
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun ssClipBounds(rect: Rect): JJImageView {
        clipBounds = rect
        return this
    }

    fun ssImageBitmap(bitmap: Bitmap?): JJImageView {
        if(bitmap != null) setImageBitmap(bitmap)
        return this
    }

    fun ssImageResource(resDrawable: Int): JJImageView {
        setImageResource(resDrawable)
        return this
    }

    fun ssOnClickListener(listener: View.OnClickListener): JJImageView {
        setOnClickListener(listener)
        return this
    }


    fun ssScaleType(scaleType: ScaleType): JJImageView {
        setScaleType(scaleType)
        return this
    }

    fun ssBackgroundColor(color: Int): JJImageView {
        setBackgroundColor(color)
        return this
    }

    fun ssBackground(drawable: Drawable?): JJImageView {
          background = drawable
        return this
    }

    fun ssPadding(padding: JJPadding): JJImageView {
        mlpPadding = padding
        setPaddingRelative(padding.left,padding.top,padding.right,padding.bottom)
        return this
    }
    fun ssLayerType(type: Int, paint: Paint? = null) : JJImageView {
        setLayerType(type, paint)
        return this
    }

    fun ssLayerSoftware(): JJImageView {
        setLayerType(View.LAYER_TYPE_SOFTWARE,null)
        return this
    }

    fun ssVisibility(type: Int): JJImageView {
        visibility = type
        return this
    }

    fun ssMinimumHeight(h:Int): JJImageView {
        minimumHeight = h
        return this
    }

    fun ssMinimumWidth(w:Int): JJImageView {
        minimumWidth = w
        return this
    }


    fun ggPadding() : JJPadding {
        var pl = paddingLeft
        var pr = paddingRight
        if(paddingStart > 0) pl = paddingStart
        if(paddingEnd > 0) pr = paddingEnd
        return JJPadding(pl,paddingTop,pr,paddingBottom)
    }
    //endregion

    //region layout params 

     fun lpWidth(w: Int) : JJImageView{
        mlpWidth = w
         applyLayoutParams(resources.configuration.orientation)
        return this
    }
     fun lpHeight(h: Int) : JJImageView{
        mlpHeight = h
         applyLayoutParams(resources.configuration.orientation)
        return this
    }
     fun lpPadding(pad: JJPadding) : JJImageView{
        mlpPadding = pad
         applyLayoutParams(resources.configuration.orientation)
        return this
    }

     fun lpMargin(mar: JJMargin) : JJImageView{
        mlpMargins = mar
         applyLayoutParams(resources.configuration.orientation)
        return this
    }


    //endregion

    //region layout params landscape

     fun lplWidth(w: Int) : JJImageView{
        mlsWidth = w
         applyLayoutParams(resources.configuration.orientation)
        return this
    }
     fun lplHeight(h: Int) : JJImageView{
        mlsHeight = h
         applyLayoutParams(resources.configuration.orientation)
        return this
    }
     fun lplPadding(pad: JJPadding) : JJImageView{
        mlsPadding = pad
         applyLayoutParams(resources.configuration.orientation)
        return this
    }

     fun lplMargin(mar: JJMargin) : JJImageView{
        mlsMargins = mar
         applyLayoutParams(resources.configuration.orientation)
        return this
    }


    //endregion 

    //region CoordinatorLayout params

    private fun setupCol() {
        val lp = CoordinatorLayout.LayoutParams(0,0)
        if(mGravityCol != 0) lp.gravity = mGravityCol
        if(mBehavior != null) lp.behavior = mBehavior
        layoutParams = lp
        applyLayoutParams(resources.configuration.orientation)
    }

    private var mGravityCol = 0
    fun colGravity(gravity: Int): JJImageView {
        mGravityCol = gravity
        setupCol()
        return this
    }

    private var mBehavior : AppBarLayout.Behavior? = null
    fun colBehavior(behavior: AppBarLayout.Behavior): JJImageView{
        mBehavior = behavior
        setupCol()
        return this
    }

    //endregion

    //region AppBarLayout Params

    private  fun setupAblp(){
        val lp =  AppBarLayout.LayoutParams(0,0)
        if(mScrollFlags != 0) lp.scrollFlags = mScrollFlags
        if(mScrollInterpolator != null ) lp.scrollInterpolator = mScrollInterpolator
        layoutParams = lp
        applyLayoutParams(resources.configuration.orientation)
    }

    private var mScrollFlags = 0
    fun ablScrollFlags(flags: Int) : JJImageView {
        mScrollFlags = flags
        setupAblp()

        return this
    }

    private var mScrollInterpolator : Interpolator? = null
    fun ablScrollInterpolator(interpolator: Interpolator) : JJImageView {
        mScrollInterpolator = interpolator
        setupAblp()
        return this
    }

    //endregion

    //region LinearLayout Params
    private fun setupLlp() {
        val lp = LinearLayout.LayoutParams(0,0)
        if(mWeight != 0f) lp.weight = mWeight
        if(mGravity != -1) lp.gravity = mGravity
        layoutParams = lp
        applyLayoutParams(resources.configuration.orientation)
    }

    private var mWeight = 0f
    fun llWeight(w: Float): JJImageView {
        mWeight = w
        setupLlp()
        return this
    }
    private var mGravity = -1
    fun llGravity(gravity: Int): JJImageView {
        mGravity = gravity
        setupLlp()
        return this
    }

    //endregion



    //region ConstraintLayout LandScape Params
    protected val mConstraintSetLandScape = ConstraintSet()


    fun cllFloatCustomAttribute(attrName: String, value: Float): JJImageView {
        mConstraintSet.setFloatValue(id,attrName,value)
        return this
    }

    fun cllIntCustomAttribute(attrName: String, value: Int): JJImageView {
        mConstraintSet.setIntValue(id,attrName,value)
        return this
    }

    fun cllColorCustomAttribute(attrName: String, value: Int): JJImageView {
        mConstraintSet.setColorValue(id,attrName,value)
        return this
    }

    fun cllStringCustomAttribute(attrName: String, value: String): JJImageView {
        mConstraintSet.setStringValue(id,attrName,value)
        return this
    }

    fun cllRotation(float: Float): JJImageView {
        mConstraintSet.setRotation(id,float)
        return this
    }

    fun cllRotationX(float: Float): JJImageView {
        mConstraintSet.setRotationX(id,float)
        return this
    }

    fun cllRotationY(float: Float): JJImageView {
        mConstraintSet.setRotationY(id,float)
        return this
    }

    fun cllTranslation(x: Float,y: Float): JJImageView {
        mConstraintSet.setTranslation(id,x,y)
        return this
    }
    fun cllTranslationX(x: Float): JJImageView {
        mConstraintSet.setTranslationX(id,x)
        return this
    }

    fun cllTranslationY(y: Float): JJImageView {
        mConstraintSet.setTranslationY(id,y)
        return this
    }

    fun cllTranslationZ(z: Float): JJImageView {
        mConstraintSet.setTranslationZ(id,z)
        return this
    }

    fun cllTransformPivot(x: Float, y: Float): JJImageView {
        mConstraintSet.setTransformPivot(id,x,y)
        return this
    }

    fun cllTransformPivotX(x: Float): JJImageView {
        mConstraintSet.setTransformPivotX(id,x)
        return this
    }

    fun cllTransformPivotY(y: Float): JJImageView {
        mConstraintSet.setTransformPivotY(id,y)
        return this
    }

    fun cllScaleX(x: Float): JJImageView {
        mConstraintSet.setScaleX(id,x)
        return this
    }

    fun cllScaleY(y: Float): JJImageView {
        mConstraintSet.setScaleY(id,y)
        return this
    }

    fun cllDimensionRatio(ratio: String): JJImageView {
        mConstraintSet.setDimensionRatio(id,ratio)
        return this
    }

    fun cllAlpha(alpha: Float): JJImageView {
        mConstraintSet.setAlpha(id,alpha)
        return this
    }


    fun cllApply(): JJImageView {
        mConstraintSetLandScape.applyTo(parent as ConstraintLayout)
        return this
    }

    fun cllVisibilityMode(visibility: Int): JJImageView {
        mConstraintSetLandScape.setVisibilityMode(id, visibility)
        return this
    }

    fun cllVerticalBias(float: Float): JJImageView {
        mConstraintSetLandScape.setVerticalBias(id,float)
        return this
    }
    fun cllHorizontalBias(float: Float): JJImageView {
        mConstraintSetLandScape.setHorizontalBias(id,float)
        return this
    }

    fun cllCenterHorizontallyOf(viewId: Int, marginStart: Int = 0, marginEnd: Int = 0): JJImageView {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.START, marginStart)
        mConstraintSetLandScape.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.END, marginEnd)
        mConstraintSetLandScape.setHorizontalBias(id,0.5f)
        return this
    }
    fun cllCenterVerticallyOf(viewId: Int,marginTop: Int = 0, marginBottom: Int = 0): JJImageView {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, marginTop)
        mConstraintSetLandScape.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, marginBottom)
        mConstraintSetLandScape.setVerticalBias(id,0.5f)
        return this
    }

    fun cllMargins(margins: JJMargin) : JJImageView {
        mConstraintSetLandScape.setMargin(id,ConstraintSet.TOP,margins.top)
        mConstraintSetLandScape.setMargin(id,ConstraintSet.BOTTOM,margins.bottom)
        mConstraintSetLandScape.setMargin(id,ConstraintSet.END,margins.right)
        mConstraintSetLandScape.setMargin(id,ConstraintSet.START,margins.left)
        return this
    }


    fun cllTopToTop(viewId: Int, margin: Int = 0): JJImageView {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, margin)
        return this
    }

    fun cllTopToTopParent(margin: Int = 0): JJImageView {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin)
        return this
    }


    fun cllTopToBottom(viewId: Int, margin: Int = 0): JJImageView {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun cllTopToBottomParent(margin: Int = 0): JJImageView {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun cllBottomToTop(viewId: Int, margin: Int = 0): JJImageView {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.TOP, margin)
        return this
    }

    fun cllBottomToTopParent(margin: Int = 0): JJImageView {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin)
        return this
    }

    fun cllBottomToBottom(viewId: Int, margin: Int = 0): JJImageView {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun cllBottomToBottomParent(margin: Int = 0): JJImageView {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun cllStartToStart(viewId: Int, margin: Int = 0): JJImageView {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.START, margin)
        return this
    }

    fun cllStartToStartParent(margin: Int = 0): JJImageView {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin)
        return this
    }

    fun cllStartToEnd(viewId: Int, margin: Int = 0): JJImageView {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.END, margin)
        return this
    }

    fun cllStartToEndParent(margin: Int = 0): JJImageView {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END, margin)
        return this
    }

    fun cllEndToEnd(viewId: Int, margin: Int = 0): JJImageView {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.END, margin)
        return this
    }

    fun cllEndToEndParent(margin: Int = 0): JJImageView {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin)
        return this
    }


    fun cllEndToStart(viewId: Int, margin: Int = 0): JJImageView {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.START, margin)
        return this
    }

    fun cllEndToStartParent(margin: Int = 0): JJImageView {
        mConstraintSetLandScape.connect(this.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START, margin)
        return this
    }


    fun cllWidth(width: Int): JJImageView {
        mConstraintSetLandScape.constrainWidth(id, width)
        return this
    }

    fun cllHeight(height: Int): JJImageView {
        mConstraintSetLandScape.constrainHeight(id, height)
        return this
    }

    fun cllPercentWidth(width: Float): JJImageView {
        mConstraintSetLandScape.constrainPercentWidth(id, width)
        return this
    }

    fun cllPercentHeight(height: Float): JJImageView {
        mConstraintSetLandScape.constrainPercentHeight(id, height)
        return this
    }

    fun cllCenterInParent(): JJImageView {
        mConstraintSetLandScape.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSetLandScape.setVerticalBias(id, 0.5f)
        mConstraintSetLandScape.setHorizontalBias(id, 0.5f)
        return this
    }

    fun cllCenterInParent(verticalBias: Float, horizontalBias: Float, margin: JJMargin): JJImageView {
        mConstraintSetLandScape.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin.left)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin.right)
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin.top)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin.bottom)
        mConstraintSetLandScape.setVerticalBias(id, verticalBias)
        mConstraintSetLandScape.setHorizontalBias(id, horizontalBias)
        return this
    }

    fun cllCenterInParentVertically(): JJImageView {
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSetLandScape.setVerticalBias(id, 0.5f)
        return this
    }

    fun cllCenterInParentHorizontally(): JJImageView {
        mConstraintSetLandScape.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSetLandScape.setHorizontalBias(id, 0.5f)
        return this
    }

    fun cllCenterInParentVertically(bias: Float, topMargin: Int, bottomMargin: Int): JJImageView {
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
        mConstraintSetLandScape.setVerticalBias(id, bias)
        return this
    }

    fun cllCenterInParentHorizontally(bias: Float, startMargin: Int, endtMargin: Int): JJImageView {
        mConstraintSetLandScape.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, startMargin)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, endtMargin)
        mConstraintSetLandScape.setHorizontalBias(id, bias)
        return this
    }


    fun cllCenterInParentTopVertically(): JJImageView {
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSetLandScape.setVerticalBias(id, 0.5f)
        return this
    }


    fun cllCenterInParentBottomVertically(): JJImageView {
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSetLandScape.setVerticalBias(id, 0.5f)
        return this
    }

    fun cllCenterInParentStartHorizontally(): JJImageView {
        mConstraintSetLandScape.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSetLandScape.setHorizontalBias(id, 0.5f)
        return this
    }

    fun cllCenterInParentEndHorizontally(): JJImageView {
        mConstraintSetLandScape.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSetLandScape.setHorizontalBias(id, 0.5f)
        return this
    }

    fun cllCenterInTopVertically(topId: Int): JJImageView {
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, topId, ConstraintSet.TOP, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, topId, ConstraintSet.TOP, 0)
        mConstraintSetLandScape.setVerticalBias(id, 0.5f)
        return this
    }


    fun cllCenterInBottomVertically(bottomId: Int): JJImageView {
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, bottomId, ConstraintSet.BOTTOM, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, bottomId, ConstraintSet.BOTTOM, 0)
        mConstraintSetLandScape.setVerticalBias(id, 0.5f)
        return this
    }

    fun cllCenterInStartHorizontally(startId: Int): JJImageView {
        mConstraintSetLandScape.connect(id, ConstraintSet.START, startId, ConstraintSet.START, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, startId, ConstraintSet.START, 0)
        mConstraintSetLandScape.setHorizontalBias(id, 0.5f)
        return this
    }

    fun cllCenterInEndHorizontally(endId: Int): JJImageView {
        mConstraintSetLandScape.connect(id, ConstraintSet.START, endId, ConstraintSet.END, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, endId, ConstraintSet.END, 0)
        mConstraintSetLandScape.setHorizontalBias(id, 0.5f)
        return this
    }

    fun cllCenterVertically(topId: Int, topSide: Int, topMargin: Int, bottomId: Int, bottomSide: Int, bottomMargin: Int, bias: Float): JJImageView {
        mConstraintSetLandScape.centerVertically(id, topId, topSide, topMargin, bottomId, bottomSide, bottomMargin, bias)
        return this
    }

    fun cllCenterHorizontally(startId: Int, startSide: Int, startMargin: Int, endId: Int, endSide: Int, endMargin: Int, bias: Float): JJImageView {
        mConstraintSetLandScape.centerHorizontally(id, startId, startSide, startMargin, endId, endSide, endMargin, bias)
        return this
    }


    fun cllFillParent(): JJImageView {
        mConstraintSetLandScape.constrainWidth(id,0)
        mConstraintSetLandScape.constrainHeight(id,0)
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        return this
    }

    fun cllFillParent(margin: JJMargin): JJImageView {
        mConstraintSetLandScape.constrainWidth(id,0)
        mConstraintSetLandScape.constrainHeight(id,0)
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin.top)
        mConstraintSetLandScape.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin.left)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin.right)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin.bottom)
        return this
    }

    fun cllFillParentHorizontally(): JJImageView {
        mConstraintSetLandScape.constrainWidth(id,0)
        mConstraintSetLandScape.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        return this
    }

    fun cllFillParentVertically(): JJImageView {
        mConstraintSetLandScape.constrainHeight(id,0)
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        return this
    }

    fun cllFillParentHorizontally(startMargin: Int, endMargin: Int): JJImageView {
        mConstraintSetLandScape.constrainWidth(id,0)
        mConstraintSetLandScape.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, startMargin)
        mConstraintSetLandScape.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, endMargin)
        return this
    }

    fun cllFillParentVertically(topMargin: Int, bottomMargin: Int): JJImageView {
        mConstraintSetLandScape.constrainHeight(id,0)
        mConstraintSetLandScape.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
        mConstraintSetLandScape.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
        return this
    }

    fun cllVisibility(visibility: Int): JJImageView {
        mConstraintSetLandScape.setVisibility(id, visibility)
        return this
    }



    fun cllElevation(elevation: Float): JJImageView {
        mConstraintSetLandScape.setElevation(id, elevation)

        return this
    }

    fun cllConstraintSet() : ConstraintSet {
        return mConstraintSetLandScape
    }

    fun cllMinWidth(w:Int): JJImageView {
        mConstraintSetLandScape.constrainMinWidth(id,w)
        return this
    }

    fun cllMinHeight(h:Int): JJImageView {
        mConstraintSetLandScape.constrainMinHeight(id,h)
        return this
    }

    fun cllMaxWidth(w:Int): JJImageView {
        mConstraintSetLandScape.constrainMaxWidth(id,w)
        return this
    }

    fun cllMaxHeight(h:Int): JJImageView {
        mConstraintSetLandScape.constrainMaxHeight(id,h)
        return this
    }






//endregion

    //region MotionLayout Params

    private var mMotionConstraintSet: ConstraintSet? = null


    fun mlVisibilityMode(visibility: Int): JJImageView {
        mMotionConstraintSet?.setVisibilityMode(id, visibility)
        return this
    }

    fun mlVerticalBias(float: Float): JJImageView {
        mMotionConstraintSet?.setVerticalBias(id,float)
        return this
    }
    fun mlHorizontalBias(float: Float): JJImageView {
        mMotionConstraintSet?.setHorizontalBias(id,float)
        return this
    }

    fun mlCenterHorizontallyOf(viewId: Int, marginStart: Int = 0, marginEnd: Int = 0): JJImageView {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.START, marginStart)
        mMotionConstraintSet?.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.END, marginEnd)
        mMotionConstraintSet?.setHorizontalBias(viewId,0.5f)
        return this
    }
    fun mlCenterVerticallyOf(viewId: Int,marginTop: Int = 0, marginBottom: Int = 0): JJImageView {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, marginTop)
        mMotionConstraintSet?.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, marginBottom)
        mMotionConstraintSet?.setVerticalBias(viewId,0.5f)
        return this
    }

    fun mlMargins(margins: JJMargin) : JJImageView {
        mMotionConstraintSet?.setMargin(id,ConstraintSet.TOP,margins.top)
        mMotionConstraintSet?.setMargin(id,ConstraintSet.BOTTOM,margins.bottom)
        mMotionConstraintSet?.setMargin(id,ConstraintSet.END,margins.right)
        mMotionConstraintSet?.setMargin(id,ConstraintSet.START,margins.left)
        return this
    }


    fun mlFloatCustomAttribute(attrName: String, value: Float): JJImageView {
        mMotionConstraintSet?.setFloatValue(id,attrName,value)
        return this
    }

    fun mlIntCustomAttribute(attrName: String, value: Int): JJImageView {
        mMotionConstraintSet?.setIntValue(id,attrName,value)
        return this
    }

    fun mlColorCustomAttribute(attrName: String, value: Int): JJImageView {
        mMotionConstraintSet?.setColorValue(id,attrName,value)
        return this
    }

    fun mlStringCustomAttribute(attrName: String, value: String): JJImageView {
        mMotionConstraintSet?.setStringValue(id,attrName,value)
        return this
    }

    fun mlRotation(float: Float): JJImageView {
        mMotionConstraintSet?.setRotation(id,float)
        return this
    }

    fun mlRotationX(float: Float): JJImageView {
        mMotionConstraintSet?.setRotationX(id,float)
        return this
    }

    fun mlRotationY(float: Float): JJImageView {
        mMotionConstraintSet?.setRotationY(id,float)
        return this
    }

    fun mlTranslation(x: Float,y: Float): JJImageView {
        mMotionConstraintSet?.setTranslation(id,x,y)
        return this
    }
    fun mlTranslationX(x: Float): JJImageView {
        mMotionConstraintSet?.setTranslationX(id,x)
        return this
    }

    fun mlTranslationY(y: Float): JJImageView {
        mMotionConstraintSet?.setTranslationY(id,y)
        return this
    }

    fun mlTranslationZ(z: Float): JJImageView {
        mMotionConstraintSet?.setTranslationZ(id,z)
        return this
    }

    fun mlTransformPivot(x: Float, y: Float): JJImageView {
        mMotionConstraintSet?.setTransformPivot(id,x,y)
        return this
    }

    fun mlTransformPivotX(x: Float): JJImageView {
        mMotionConstraintSet?.setTransformPivotX(id,x)
        return this
    }

    fun mlTransformPivotY(y: Float): JJImageView {
        mMotionConstraintSet?.setTransformPivotY(id,y)
        return this
    }

    fun mlScaleX(x: Float): JJImageView {
        mMotionConstraintSet?.setScaleX(id,x)
        return this
    }

    fun mlScaleY(y: Float): JJImageView {
        mMotionConstraintSet?.setScaleY(id,y)
        return this
    }

    fun mlDimensionRatio(ratio: String): JJImageView {
        mMotionConstraintSet?.setDimensionRatio(id,ratio)
        return this
    }

    fun mlAlpha(alpha: Float): JJImageView {
        mMotionConstraintSet?.setAlpha(id,alpha)
        return this
    }



    fun mlTopToTop(viewId: Int, margin: Int = 0): JJImageView {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, margin)
        return this
    }

    fun mlTopToTopParent(margin: Int = 0): JJImageView {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin)
        return this
    }


    fun mlTopToBottomOf(viewId: Int, margin: Int = 0): JJImageView {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun mlTopToBottomParent(margin: Int = 0): JJImageView {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun mlBottomToTopOf(viewId: Int, margin: Int = 0): JJImageView {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.TOP, margin)

        return this
    }

    fun mlBottomToTopParent(margin: Int = 0): JJImageView {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin)

        return this
    }

    fun mlBottomToBottomOf(viewId: Int, margin: Int = 0): JJImageView {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, margin)

        return this
    }

    fun mlBottomToBottomParent(margin: Int = 0): JJImageView {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin)

        return this
    }

    fun mlStartToStartOf(viewId: Int, margin: Int = 0): JJImageView {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.START, margin)

        return this
    }

    fun mlStartToStartParent(margin: Int = 0): JJImageView {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin)

        return this
    }

    fun mlStartToEndOf(viewId: Int, margin: Int = 0): JJImageView {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.END, margin)

        return this
    }

    fun mlStartToEndParent(margin: Int = 0): JJImageView {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END, margin)

        return this
    }

    fun mlEndToEndOf(viewId: Int, margin: Int= 0): JJImageView {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.END, margin)

        return this
    }

    fun mlEndToEndParent(margin: Int = 0): JJImageView {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin)

        return this
    }


    fun mlEndToStartOf(viewId: Int, margin: Int = 0): JJImageView {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.START, margin)
        return this
    }

    fun mlEndToStartParent(margin: Int = 0): JJImageView {
        mMotionConstraintSet?.connect(this.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START, margin)
        return this
    }


    fun mlWidth(width: Int): JJImageView {
        mMotionConstraintSet?.constrainWidth(id, width)
        return this
    }

    fun mlHeight(height: Int): JJImageView {
        mMotionConstraintSet?.constrainHeight(id, height)
        return this
    }

    fun mlPercentWidth(width: Float): JJImageView {
        mMotionConstraintSet?.constrainPercentWidth(id, width)
        return this
    }

    fun mlPercentHeight(height: Float): JJImageView {
        mMotionConstraintSet?.constrainPercentHeight(id, height)
        return this
    }

    fun mlCenterInParent(): JJImageView {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)

        return this
    }

    fun mlCenterInParent(verticalBias: Float, horizontalBias: Float, margin: JJMargin): JJImageView {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin.left)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin.right)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin.top)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin.bottom)
        mMotionConstraintSet?.setVerticalBias(id, verticalBias)
        mMotionConstraintSet?.setHorizontalBias(id, horizontalBias)
        return this
    }

    fun mlCenterInParentVertically(): JJImageView {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)

        return this
    }

    fun mlCenterInParentHorizontally(): JJImageView {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)
        return this
    }

    fun mlCenterInParentVertically(bias: Float, topMargin: Int, bottomMargin: Int): JJImageView {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
        mMotionConstraintSet?.setVerticalBias(id, bias)
        return this
    }

    fun mlCenterInParentHorizontally(bias: Float, startMargin: Int, endtMargin: Int): JJImageView {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, startMargin)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, endtMargin)
        mMotionConstraintSet?.setHorizontalBias(id, bias)
        return this
    }


    fun mlCenterInParentTopVertically(): JJImageView {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)
        return this
    }


    fun mlCenterInParentBottomVertically(): JJImageView {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)
        return this
    }

    fun mlCenterInParentStartHorizontally(): JJImageView {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)
        return this
    }

    fun mlCenterInParentEndHorizontally(): JJImageView {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)
        return this
    }

    fun mlCenterInTopVerticallyOf(viewId: Int): JJImageView {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, viewId, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)
        return this
    }


    fun mlCenterInBottomVerticallyOf(viewId: Int): JJImageView {
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, viewId, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, 0)
        mMotionConstraintSet?.setVerticalBias(id, 0.5f)
        return this
    }

    fun mlCenterInStartHorizontallyOf(viewId: Int): JJImageView {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, viewId, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, viewId, ConstraintSet.START, 0)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)
        return this
    }

    fun mlCenterInEndHorizontallyOf(viewId: Int): JJImageView {
        mMotionConstraintSet?.connect(id, ConstraintSet.START, viewId, ConstraintSet.END, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, viewId, ConstraintSet.END, 0)
        mMotionConstraintSet?.setHorizontalBias(id, 0.5f)
        return this
    }

    fun mlCenterVertically(topId: Int, topSide: Int, topMargin: Int, bottomId: Int, bottomSide: Int, bottomMargin: Int, bias: Float): JJImageView {
        mMotionConstraintSet?.centerVertically(id, topId, topSide, topMargin, bottomId, bottomSide, bottomMargin, bias)
        return this
    }

    fun mlCenterHorizontally(startId: Int, startSide: Int, startMargin: Int, endId: Int, endSide: Int, endMargin: Int, bias: Float): JJImageView {
        mMotionConstraintSet?.centerHorizontally(id, startId, startSide, startMargin, endId, endSide, endMargin, bias)
        return this
    }


    fun mlFillParent(): JJImageView {
        mMotionConstraintSet?.constrainWidth(id,0)
        mMotionConstraintSet?.constrainHeight(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        return this
    }

    fun mlFillParent(margin: JJMargin): JJImageView {
        mMotionConstraintSet?.constrainWidth(id,0)
        mMotionConstraintSet?.constrainHeight(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin.top)
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin.left)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin.right)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin.bottom)
        return this
    }

    fun mlFillParentHorizontally(): JJImageView {
        mMotionConstraintSet?.constrainWidth(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        return this
    }

    fun mlFillParentVertically(): JJImageView {
        mMotionConstraintSet?.constrainHeight(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        return this
    }

    fun mlFillParentHorizontally(startMargin: Int, endMargin: Int): JJImageView {
        mMotionConstraintSet?.constrainWidth(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, startMargin)
        mMotionConstraintSet?.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, endMargin)
        return this
    }

    fun mlFillParentVertically(topMargin: Int, bottomMargin: Int): JJImageView {
        mMotionConstraintSet?.constrainHeight(id,0)
        mMotionConstraintSet?.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
        mMotionConstraintSet?.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
        return this
    }

    fun mlVisibility(visibility: Int): JJImageView {
        mMotionConstraintSet?.setVisibility(id, visibility)
        return this
    }

    fun mlElevation(elevation: Float): JJImageView {
        mMotionConstraintSet?.setElevation(id, elevation)
        return this
    }

    fun mlApply(): JJImageView {
        mMotionConstraintSet?.applyTo(parent as ConstraintLayout)
        return this
    }

    fun mlSetConstraint(cs : ConstraintSet?): JJImageView {
        mMotionConstraintSet = cs
        return this
    }

    fun mlDisposeConstraint(): JJImageView {
        mMotionConstraintSet = null
        return this
    }

    //endregion
    
    //region ConstraintLayout Params
    protected val mConstraintSet = ConstraintSet()


    fun clFloatCustomAttribute(attrName: String, value: Float): JJImageView {
        mConstraintSet.setFloatValue(id,attrName,value)
        return this
    }

    fun clIntCustomAttribute(attrName: String, value: Int): JJImageView {
        mConstraintSet.setIntValue(id,attrName,value)
        return this
    }

    fun clColorCustomAttribute(attrName: String, value: Int): JJImageView {
        mConstraintSet.setColorValue(id,attrName,value)
        return this
    }

    fun clStringCustomAttribute(attrName: String, value: String): JJImageView {
        mConstraintSet.setStringValue(id,attrName,value)
        return this
    }

    fun clRotation(float: Float): JJImageView {
        mConstraintSet.setRotation(id,float)
        return this
    }

    fun clRotationX(float: Float): JJImageView {
        mConstraintSet.setRotationX(id,float)
        return this
    }

    fun clRotationY(float: Float): JJImageView {
        mConstraintSet.setRotationY(id,float)
        return this
    }

    fun clTranslation(x: Float,y: Float): JJImageView {
        mConstraintSet.setTranslation(id,x,y)
        return this
    }
    fun clTranslationX(x: Float): JJImageView {
        mConstraintSet.setTranslationX(id,x)
        return this
    }

    fun clTranslationY(y: Float): JJImageView {
        mConstraintSet.setTranslationY(id,y)
        return this
    }

    fun clTranslationZ(z: Float): JJImageView {
        mConstraintSet.setTranslationZ(id,z)
        return this
    }

    fun clTransformPivot(x: Float, y: Float): JJImageView {
        mConstraintSet.setTransformPivot(id,x,y)
        return this
    }

    fun clTransformPivotX(x: Float): JJImageView {
        mConstraintSet.setTransformPivotX(id,x)
        return this
    }

    fun clTransformPivotY(y: Float): JJImageView {
        mConstraintSet.setTransformPivotY(id,y)
        return this
    }

    fun clScaleX(x: Float): JJImageView {
        mConstraintSet.setScaleX(id,x)
        return this
    }

    fun clScaleY(y: Float): JJImageView {
        mConstraintSet.setScaleY(id,y)
        return this
    }

    fun clDimensionRatio(ratio: String): JJImageView {
        mConstraintSet.setDimensionRatio(id,ratio)
        return this
    }

    fun clAlpha(alpha: Float): JJImageView {
        mConstraintSet.setAlpha(id,alpha)
        return this
    }


    fun clConstraintSet() : ConstraintSet {
        return mConstraintSet
    }

    fun clMinWidth(w:Int): JJImageView {
        mConstraintSet.constrainMinWidth(id,w)
        return this
    }

    fun clMinHeight(h:Int): JJImageView {
        mConstraintSet.constrainMinHeight(id,h)
        return this
    }

    fun clMaxWidth(w:Int): JJImageView {
        mConstraintSet.constrainMaxWidth(id,w)
        return this
    }

    fun clMaxHeight(h:Int): JJImageView {
        mConstraintSet.constrainMaxHeight(id,h)
        return this
    }


    fun clVisibilityMode(mode: Int): JJImageView {
        mConstraintSet.setVisibilityMode(id,mode)
        return this
    }


    fun clApply(): JJImageView {
        mConstraintSet.applyTo(parent as ConstraintLayout)
        return this
    }

    fun clVerticalBias(float: Float): JJImageView {
        mConstraintSet.setVerticalBias(id,float)
        return this
    }
    fun clHorizontalBias(float: Float): JJImageView {
        mConstraintSet.setHorizontalBias(id,float)
        return this
    }

    fun clCenterHorizontallyOf(viewId: Int): JJImageView {
        mConstraintSet.connect(id, ConstraintSet.START, viewId, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, viewId, ConstraintSet.END, 0)
        mConstraintSet.setHorizontalBias(id,0.5f)
        return this
    }
    fun clCenterVerticallyOf(viewId: Int): JJImageView {
        mConstraintSet.connect(id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, 0)
        mConstraintSet.setVerticalBias(id,0.5f)
        return this
    }

    fun clMargins(margins: JJMargin) : JJImageView {
        mConstraintSet.setMargin(id,ConstraintSet.TOP,margins.top)
        mConstraintSet.setMargin(id,ConstraintSet.BOTTOM,margins.bottom)
        mConstraintSet.setMargin(id,ConstraintSet.END,margins.right)
        mConstraintSet.setMargin(id,ConstraintSet.START,margins.left)
        return this
    }


    fun clTopToTop(viewId: Int, margin: Int = 0): JJImageView {
        mConstraintSet.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.TOP, margin)
        return this
    }

    fun clTopToTopParent(margin: Int = 0): JJImageView {
        mConstraintSet.connect(this.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin)
        return this
    }


    fun clTopToBottom(viewId: Int, margin: Int = 0): JJImageView {
        mConstraintSet.connect(this.id, ConstraintSet.TOP, viewId, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun clTopToBottomParent(margin: Int = 0): JJImageView {
        mConstraintSet.connect(this.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun clBottomToTop(viewId: Int, margin: Int = 0): JJImageView {
        mConstraintSet.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.TOP, margin)
        return this
    }

    fun clBottomToTopParent(margin: Int = 0): JJImageView {
        mConstraintSet.connect(this.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin)
        return this
    }

    fun clBottomToBottom(viewId: Int, margin: Int = 0): JJImageView {
        mConstraintSet.connect(this.id, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun clBottomToBottomParent(margin: Int = 0): JJImageView {
        mConstraintSet.connect(this.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin)
        return this
    }

    fun clStartToStart(viewId: Int, margin: Int = 0): JJImageView {
        mConstraintSet.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.START, margin)
        return this
    }

    fun clStartToStartParent(margin: Int = 0): JJImageView {
        mConstraintSet.connect(this.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin)
        return this
    }

    fun clStartToEnd(viewId: Int, margin: Int = 0): JJImageView {
        mConstraintSet.connect(this.id, ConstraintSet.START, viewId, ConstraintSet.END, margin)
        return this
    }

    fun clStartToEndParent(margin: Int = 0): JJImageView {
        mConstraintSet.connect(this.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END, margin)
        return this
    }

    fun clEndToEnd(viewId: Int, margin: Int = 0): JJImageView {
        mConstraintSet.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.END, margin)
        return this
    }

    fun clEndToEndParent(margin: Int = 0): JJImageView {
        mConstraintSet.connect(this.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin)
        return this
    }


    fun clEndToStart(viewId: Int, margin: Int = 0): JJImageView {
        mConstraintSet.connect(this.id, ConstraintSet.END, viewId, ConstraintSet.START, margin)
        return this
    }

    fun clEndToStartParent(margin: Int = 0): JJImageView {
        mConstraintSet.connect(this.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START, margin)
        return this
    }


    fun clWidth(width: Int): JJImageView {
        mConstraintSet.constrainWidth(id, width)
        return this
    }

    fun clHeight(height: Int): JJImageView {
        mConstraintSet.constrainHeight(id, height)
        return this
    }

    fun clPercentWidth(width: Float): JJImageView {
        mConstraintSet.constrainPercentWidth(id, width)
        return this
    }

    fun clPercentHeight(height: Float): JJImageView {
        mConstraintSet.constrainPercentHeight(id, height)
        return this
    }

    fun clCenterInParent(): JJImageView {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterInParent(verticalBias: Float, horizontalBias: Float, margin: JJMargin): JJImageView {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin.left)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin.right)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin.top)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin.bottom)
        mConstraintSet.setVerticalBias(id, verticalBias)
        mConstraintSet.setHorizontalBias(id, horizontalBias)
        return this
    }

    fun clCenterInParentVertically(): JJImageView {
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        return this
    }

    fun clCenterInParentHorizontally(): JJImageView {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterInParentVertically(bias: Float, topMargin: Int, bottomMargin: Int): JJImageView {
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
        mConstraintSet.setVerticalBias(id, bias)
        return this
    }

    fun clCenterInParentHorizontally(bias: Float, startMargin: Int, endtMargin: Int): JJImageView {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, startMargin)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, endtMargin)
        mConstraintSet.setHorizontalBias(id, bias)
        return this
    }


    fun clCenterInParentTopVertically(): JJImageView {
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        return this
    }


    fun clCenterInParentBottomVertically(): JJImageView {
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        return this
    }

    fun clCenterInParentStartHorizontally(): JJImageView {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterInParentEndHorizontally(): JJImageView {
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterInTopVertically(topId: Int): JJImageView {
        mConstraintSet.connect(id, ConstraintSet.TOP, topId, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, topId, ConstraintSet.TOP, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        return this
    }


    fun clCenterInBottomVertically(bottomId: Int): JJImageView {
        mConstraintSet.connect(id, ConstraintSet.TOP, bottomId, ConstraintSet.BOTTOM, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, bottomId, ConstraintSet.BOTTOM, 0)
        mConstraintSet.setVerticalBias(id, 0.5f)
        return this
    }

    fun clCenterInStartHorizontally(startId: Int): JJImageView {
        mConstraintSet.connect(id, ConstraintSet.START, startId, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, startId, ConstraintSet.START, 0)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterInEndHorizontally(endId: Int): JJImageView {
        mConstraintSet.connect(id, ConstraintSet.START, endId, ConstraintSet.END, 0)
        mConstraintSet.connect(id, ConstraintSet.END, endId, ConstraintSet.END, 0)
        mConstraintSet.setHorizontalBias(id, 0.5f)
        return this
    }

    fun clCenterVertically(topId: Int, topSide: Int, topMargin: Int, bottomId: Int, bottomSide: Int, bottomMargin: Int, bias: Float): JJImageView {
        mConstraintSet.centerVertically(id, topId, topSide, topMargin, bottomId, bottomSide, bottomMargin, bias)
        return this
    }

    fun clCenterHorizontally(startId: Int, startSide: Int, startMargin: Int, endId: Int, endSide: Int, endMargin: Int, bias: Float): JJImageView {
        mConstraintSet.centerHorizontally(id, startId, startSide, startMargin, endId, endSide, endMargin, bias)
        return this
    }


    fun clFillParent(): JJImageView {
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.constrainHeight(id,0)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        return this
    }

    fun clFillParent(margin: JJMargin): JJImageView {
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.constrainHeight(id,0)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin.top)
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, margin.left)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin.right)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, margin.bottom)
        return this
    }

    fun clFillParentHorizontally(): JJImageView {
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        return this
    }

    fun clFillParentVertically(): JJImageView {
        mConstraintSet.constrainHeight(id,0)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        return this
    }

    fun clFillParentHorizontally(startMargin: Int, endMargin: Int): JJImageView {
        mConstraintSet.constrainWidth(id,0)
        mConstraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, startMargin)
        mConstraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, endMargin)
        return this
    }

    fun clFillParentVertically(topMargin: Int, bottomMargin: Int): JJImageView {
        mConstraintSet.constrainHeight(id,0)
        mConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
        mConstraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
        return this
    }

    fun clVisibility(visibility: Int): JJImageView {
        mConstraintSet.setVisibility(id, visibility)
        return this
    }

    fun clElevation(elevation: Float): JJImageView {
        mConstraintSet.setElevation(id, elevation)

        return this
    }

    //endregion

   


}
