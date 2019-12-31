package com.jjlf.library_layout.views

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.jjlf.library_layout.JJScreen


class JJAlertDialog : AlertDialog {

    private var mText: TextView? = null
    private var progressBar: ProgressBar? = null
    private var activity: Activity? = null
    internal var type: Int = 0

    constructor(context: Context) : super(context) {
        activity = context as? Activity

    }

     constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        activity = context as? Activity
    }

     constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {
        activity = context as? Activity

    }


    fun alertProgressCircleTextHorizontal(): JJAlertDialog {
        val rl = RelativeLayout(activity)
        val pRl = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        rl.layoutParams = pRl
        rl.gravity = Gravity.CENTER
        val ll = LinearLayout(activity)
        ll.orientation = LinearLayout.HORIZONTAL
        val llParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
        ll.layoutParams = llParam

        progressBar = ProgressBar(activity)
        progressBar!!.isIndeterminate = true
        val hr = JJScreen.height() > 1000
        val paddingProgrees = if (hr) 30 else 12
        progressBar!!.setPadding(paddingProgrees, paddingProgrees, paddingProgrees, paddingProgrees)

        mText = TextView(activity)
        mText!!.textSize = 17f
        mText!!.gravity = Gravity.CENTER_VERTICAL

        ll.addView(progressBar)
        ll.addView(mText)
        rl.addView(ll)

        val builder = JJAlertDialog(activity!!)
        builder.mText = mText
        builder.progressBar = progressBar
        builder.type = PROGRESS_CIRCLE_TEXTRIGTH
        builder.setCancelable(false)
        builder.setView(rl)

        return builder
    }

    fun setText(t: String): JJAlertDialog {
        if (mText != null) {
            mText?.text = t
        }
        return this
    }

    fun setText(resid: Int): JJAlertDialog {
        if (mText != null) {
            mText?.setText(resid)
        }
        return this
    }

    private var mIdentifier = 0
    fun ssIdentifier(value: Int): JJAlertDialog {
        mIdentifier = value
        return this
    }

    fun ggIdentifier():Int{
        return mIdentifier
    }

    private var mState = 0
    fun ssState(state: Int): JJAlertDialog {
        mState = state
        return this
    }

    fun ggState():Int{
        return mState
    }

    private var mAttribute = ""
    fun ssAttribute(string:String): JJAlertDialog {
        mAttribute = string
        return this
    }

    fun ggAttribute(): String {
        return mAttribute
    }

    override fun show() {
        super.show()
        handleSizes()

    }

    private fun handleSizes() {
        when (type) {
            PROGRESS_CIRCLE_TEXTRIGTH -> {
                val width = (JJScreen.width() * 0.7).toInt()
                val height = (JJScreen.height() * 0.15).toInt()
                window!!.setLayout(width, height)
                progressBar!!.layoutParams = LinearLayout.LayoutParams((width * 0.3).toInt(), LinearLayout.LayoutParams.MATCH_PARENT)
                mText!!.layoutParams = LinearLayout.LayoutParams((width * 0.55).toInt(), LinearLayout.LayoutParams.MATCH_PARENT)
            }
        }
    }

    companion object {

        val PROGRESS_CIRCLE_TEXTRIGTH = 1
    }
}
