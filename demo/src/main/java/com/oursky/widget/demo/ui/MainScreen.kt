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
        val testform = Button(context).apply {
            setText(R.string.main_form)
        }
        val contentView = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }
        contentView.addView(testform, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT))
        // Event Handlers
        testform.setOnClickListener {
            pushController(FormScreen())
        }
        return ScrollView(context).apply {
            addView(contentView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        }
    }
}