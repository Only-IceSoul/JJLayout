package com.jjlf.library_layout.views

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class JJViewPagerAdapter() : PagerAdapter() {

    private var mViews: Array<out View>? = null
    private var mNumberOfPages = 0

    fun addViews(vararg views: View): JJViewPagerAdapter {
        mViews = views
        return this
    }

    fun ssNumberOfPages(num: Int): JJViewPagerAdapter {
        mNumberOfPages = num
        return this
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        if (mViews != null && mViews!!.isNotEmpty()) {
            container.addView(mViews!![position], position)
            return mViews!![position]
        }
        Log.e("JJViewPagerAdapter", "Arrays of views is null or empty")
        return View(container.context)
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun getCount(): Int {
        return mNumberOfPages
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }
}