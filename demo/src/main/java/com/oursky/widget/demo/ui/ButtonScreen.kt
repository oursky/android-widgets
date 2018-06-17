package com.oursky.widget.demo.ui

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import com.oursky.widget.SButton
import com.oursky.widget.STabbar
import com.oursky.widget.demo.R

class ButtonScreen : BaseController() {
    override fun onCreateView(context: Context): View {
        val margin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, context.resources.displayMetrics))
        val paddingH = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, context.resources.displayMetrics))
        val paddingV = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, context.resources.displayMetrics))
        val iconSize = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28f, context.resources.displayMetrics))
        val corner = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, context.resources.displayMetrics)

        val button1 = SButton(context).apply {
            setPadding(paddingH, paddingV, paddingH, paddingV)
            setBackgroundColor(Color.argb(255, 51, 102, 255), corner)
            setIcon(SButton.IconIndex.LEFT, R.mipmap.ic_launcher, iconSize)
            setTextColor(Color.WHITE)
            setText("Button 1")
            onClick = { _ -> Log.d("ButtonScreen", "button1 clicked") }
        }
        val button2 = SButton(context).apply {
            setPadding(paddingH, paddingV, paddingH, paddingV)
            setBackgroundColor(Color.argb(255, 51, 102, 255), corner)
            setIcon(SButton.IconIndex.TOP, R.mipmap.ic_launcher, iconSize)
            setTextColor(Color.WHITE)
            setText("Button 2")
            onClick = { _ -> Log.d("ButtonScreen", "button2 clicked") }
        }
        val button3 = SButton(context).apply {
            setPadding(paddingH, paddingV, paddingH, paddingV)
            setBackgroundColor(Color.argb(255, 51, 102, 255), corner)
            setIcon(SButton.IconIndex.RIGHT, R.mipmap.ic_launcher, iconSize)
            setTextColor(Color.WHITE)
            setText("Button 3")
            onClick = { _ -> Log.d("ButtonScreen", "button3 clicked") }
        }
        val button4 = SButton(context).apply {
            setPadding(paddingH, paddingV, paddingH, paddingV)
            setBackgroundColor(Color.argb(255, 51, 102, 255), corner)
            setIcon(SButton.IconIndex.BOTTOM, R.mipmap.ic_launcher, iconSize)
            setTextColor(Color.WHITE)
            setText("Button 4")
            onClick = { _ -> Log.d("ButtonScreen", "button4 clicked") }
        }
        val contentView = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            addView(button1, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(margin, margin, margin, 0)
            })
            addView(button2, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(margin, margin, margin, 0)
            })
            addView(button3, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(margin, margin, margin, 0)
            })
            addView(button4, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(margin, margin, margin, 0)
            })
        }
        val scrollView = ScrollView(context).apply {
            addView(contentView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        }
        val tabbar = STabbar(context).apply {
            setBackgroundColor(Color.argb(255, 51, 102, 255))
            this.iconSize = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36f, context.resources.displayMetrics))
            textSize = 14
            textColor = Color.WHITE
            items = arrayOf(
                    STabbar.Item(R.mipmap.ic_launcher, R.string.tab_home),
                    STabbar.Item(R.mipmap.ic_launcher, R.string.tab_shop),
                    STabbar.Item(R.mipmap.ic_launcher, R.string.tab_setting),
                    STabbar.Item(R.mipmap.ic_launcher, R.string.tab_about)
            )
        }
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            addView(scrollView, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0).apply {
                weight = 1f
            })
            addView(tabbar, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
        }
    }
}
