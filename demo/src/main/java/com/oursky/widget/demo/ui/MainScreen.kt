package com.oursky.widget.demo.ui

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import com.oursky.widget.demo.R

class MainScreen : BaseController() {
    override fun onCreateView(context: Context): View {
        val testbutton = Button(context).apply {
            setText(R.string.main_button)
            setOnClickListener { pushController(ButtonScreen()) }
        }
        val testform = Button(context).apply {
            setText(R.string.main_form)
            setOnClickListener { pushController(FormScreen()) }
        }
        val contentView = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            addView(testbutton, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT))
            addView(testform, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT))
        }
        return ScrollView(context).apply {
            addView(contentView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        }
    }
}