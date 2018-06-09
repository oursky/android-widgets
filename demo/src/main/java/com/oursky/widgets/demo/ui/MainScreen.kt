package com.oursky.widgets.demo.ui

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.oursky.widgets.demo.R

class MainScreen : BaseController() {
    override fun onCreateView(context: Context): View {
        val layout = FrameLayout(context)
        val tv = TextView(context)
        tv.textSize = 32f
        tv.setText(R.string.app_name)
        layout.addView(tv, FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER))
        return layout
    }
}