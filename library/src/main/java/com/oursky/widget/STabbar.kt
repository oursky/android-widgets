package com.oursky.widget

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.LinearLayout

open class STabbar : LinearLayout {
    data class Item (
        @DrawableRes val icon: Int,
        @StringRes val text: Int
    )
    var onClick: ((STabbar, index: Int) -> Unit)? = null
    var items: Array<Item>? = null
        set(v) {
            field = v
            recreateLayout()
        }
    var iconSize: Int = 20
        set(v) {
            field = v
            updateLayout()
        }
    var textSize: Int = 14
        set(v) {
            field = v
            updateLayout()
        }
    @ColorInt var textColor: Int = Color.WHITE
        set(v) {
            field = v
            updateLayout()
        }
    var font: Typeface? = null
        set(v) {
            field = v
            updateLayout()
        }

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val paddingV = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, context.resources.displayMetrics))
        setPadding(0, paddingV, 0, paddingV)
        orientation = HORIZONTAL
    }
    private fun recreateLayout() {
        removeAllViews()
        items?.forEach {
            addView(SButton(context).apply {
                setText(it.text)
                setTextColor(textColor)
                setFont(textSize, font)
                setIcon(SLabel.IconIndex.TOP, it.icon, iconSize)
                onClick = { v -> this@STabbar.onClick?.invoke(this@STabbar, this@STabbar.indexOfChild(v)) }
            }, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                weight = 1f
            })
        }
    }
    private fun updateLayout() {
        for (index in 0 until childCount) {
            val child = getChildAt(index) as SButton
            child.setTextColor(textColor)
            child.setFont(textSize, font)
            items?.let { child.setIcon(SLabel.IconIndex.TOP, it[index].icon, iconSize) }
        }
    }
}