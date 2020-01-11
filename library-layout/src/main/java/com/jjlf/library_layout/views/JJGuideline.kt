package com.jjlf.library_layout.views

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline

open class JJGuideline : Guideline {

     constructor(context: Context): super(context){
        id = generateViewId()

    }

     constructor(context: Context, attrs: AttributeSet): super(context,attrs){
         throw IllegalAccessException("XML AttributeSet not implemented")
     }


    //region MotionLayout params

    private var mMotionConstraintSet: ConstraintSet? = null

    fun mlApplyConstraint(): JJGuideline {
        mMotionConstraintSet?.applyTo(parent as ConstraintLayout)
        return this
    }

    fun mlSetConstraint(cs : ConstraintSet?): JJGuideline {
        mMotionConstraintSet = cs
        return this
    }

    fun mlDisposeConstraint(): JJGuideline {
        mMotionConstraintSet = null
        return this
    }

    fun mlWidth(width: Int): JJGuideline {
        mMotionConstraintSet?.constrainWidth(id, width)
        return this
    }

    fun mlHeight(height: Int): JJGuideline {
        mMotionConstraintSet?.constrainHeight(id, height)
        return this
    }

    fun mlOrientation(orientation: Int): JJGuideline {
        mMotionConstraintSet?.create(id,orientation)
        return this
    }

    fun mlOrientationVertical(): JJGuideline {
        mMotionConstraintSet?.create(id,ConstraintSet.VERTICAL_GUIDELINE)
        return this
    }

    fun mlOrientationHorizontal(): JJGuideline {
        mMotionConstraintSet?.create(id,ConstraintSet.HORIZONTAL_GUIDELINE)
        return this
    }

    fun mlSizeWrapContent(): JJGuideline {
        mMotionConstraintSet?.constrainWidth(id, ConstraintSet.WRAP_CONTENT)
        mMotionConstraintSet?.constrainHeight(id, ConstraintSet.WRAP_CONTENT)
        return this
    }

    fun mlGuidelineBegin(distance: Int): JJGuideline {
        mMotionConstraintSet?.setGuidelineBegin(id,distance)
        return this
    }

    fun mlGuidelineEnd(distance: Int): JJGuideline {
        mMotionConstraintSet?.setGuidelineEnd(id,distance)
        return this
    }
    fun mlGuidelinePercent(percent: Float): JJGuideline {
        mMotionConstraintSet?.setGuidelinePercent(id,percent)
        return this
    }

    //endregion


}