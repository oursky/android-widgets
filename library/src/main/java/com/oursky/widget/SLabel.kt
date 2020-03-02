package com.oursky.widget

import android.content.Context
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

@Suppress("UNUSED_PARAMETER", "UNUSED_ANONYMOUS_PARAMETER")
open class SLabel : LinearLayout {
    enum class IconIndex { LEFT, TOP, RIGHT, BOTTOM }

    private val wIconLeft: ImageView
    private val wIconRight: ImageView
    private val wIconTop: ImageView
    private val wIconBottom: ImageView
    private val wText: TextView

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs) {
        wIconLeft = ImageView(context).apply {
            scaleType = ImageView.ScaleType.FIT_CENTER
            visibility = View.GONE
        }
        wIconRight = ImageView(context).apply {
            scaleType = ImageView.ScaleType.FIT_CENTER
            visibility = View.GONE
        }
        wIconTop = ImageView(context).apply {
            scaleType = ImageView.ScaleType.FIT_CENTER
            visibility = View.GONE
        }
        wIconBottom = ImageView(context).apply {
            scaleType = ImageView.ScaleType.FIT_CENTER
            visibility = View.GONE
        }
        wText = TextView(context).apply {
            setAllCaps(true)
        }

        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        super.addView(wIconTop, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        super.addView(LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            addView(wIconLeft, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
            addView(wText, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
            addView(wIconRight, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        super.addView(wIconBottom, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
    }
    fun setIcon(index: IconIndex, @DrawableRes resid: Int, size: Int) {
        when (index) {
            IconIndex.LEFT -> {
                wIconLeft.setImageResource(resid)
                val lpLeft = wIconLeft.layoutParams as LinearLayout.LayoutParams
                lpLeft.width = size
                lpLeft.height = size
                lpLeft.rightMargin = size / 2
                wIconLeft.layoutParams = lpLeft
                wIconLeft.visibility = View.VISIBLE
            }
            IconIndex.TOP -> {
                wIconTop.setImageResource(resid)
                val lpTop = wIconTop.layoutParams as LinearLayout.LayoutParams
                lpTop.width = size
                lpTop.height = size
                lpTop.bottomMargin = size / 4
                wIconTop.layoutParams = lpTop
                wIconTop.visibility = View.VISIBLE
            }
            IconIndex.RIGHT -> {
                wIconRight.setImageResource(resid)
                val lpRight = wIconRight.layoutParams as LinearLayout.LayoutParams
                lpRight.width = size
                lpRight.height = size
                lpRight.leftMargin = size / 2
                wIconRight.layoutParams = lpRight
                wIconRight.visibility = View.VISIBLE
            }
            IconIndex.BOTTOM -> {
                wIconBottom.setImageResource(resid)
                val lpBottom = wIconBottom.layoutParams as LinearLayout.LayoutParams
                lpBottom.width = size
                lpBottom.height = size
                lpBottom.topMargin = size / 4
                wIconBottom.layoutParams = lpBottom
                wIconBottom.visibility = View.VISIBLE
            }
        }
    }
    fun setIconSpacing(index: IconIndex, dp: Int) {
        when (index) {
            IconIndex.LEFT -> {
                val lpLeft = wIconLeft.layoutParams as LinearLayout.LayoutParams
                lpLeft.rightMargin = dp(dp)
                wIconLeft.layoutParams = lpLeft
            }
            IconIndex.TOP -> {
                val lpTop = wIconTop.layoutParams as LinearLayout.LayoutParams
                lpTop.bottomMargin = dp(dp)
                wIconTop.layoutParams = lpTop
            }
            IconIndex.RIGHT -> {
                val lpRight = wIconRight.layoutParams as LinearLayout.LayoutParams
                lpRight.leftMargin = dp(dp)
                wIconRight.layoutParams = lpRight
            }
            IconIndex.BOTTOM -> {
                val lpBottom = wIconBottom.layoutParams as LinearLayout.LayoutParams
                lpBottom.topMargin = dp(dp)
                wIconBottom.layoutParams = lpBottom
            }
        }
    }
    fun setIconSpacing(dpRect: Rect) {
        val lpLeft = wIconLeft.layoutParams as LinearLayout.LayoutParams
        lpLeft.rightMargin = dp(dpRect.right)
        wIconLeft.layoutParams = lpLeft
        val lpTop = wIconTop.layoutParams as LinearLayout.LayoutParams
        lpTop.bottomMargin = dp(dpRect.top)
        wIconTop.layoutParams = lpTop
        val lpRight = wIconRight.layoutParams as LinearLayout.LayoutParams
        lpRight.leftMargin = dp(dpRect.left)
        wIconRight.layoutParams = lpRight
        val lpBottom = wIconBottom.layoutParams as LinearLayout.LayoutParams
        lpBottom.topMargin = dp(dpRect.bottom)
        wIconBottom.layoutParams = lpBottom
    }
    fun setIconSpacing(dp: Int) {
        setIconSpacing(Rect(dp, dp, dp, dp))
    }
    fun setFont(size: Int) {
        wText.textSize = size.toFloat()
    }
    fun setFont(size: Int, typeface: Typeface?, style: Int = Typeface.NORMAL) {
        wText.textSize = size.toFloat()
        wText.setTypeface(typeface, style)
    }
    fun setTextColor(@ColorInt color: Int) {
        wText.setTextColor(color)
    }
    fun setText(@StringRes resid: Int) {
        wText.setText(resid)
    }
    fun setText(text: String?) {
        wText.text = text
    }
    fun setTextAllCap(isCap: Boolean) {
        wText.setAllCaps(isCap)
    }
    fun setBackgroundColor(@ColorInt color: Int, corner: Float) {
        background = GradientDrawable().apply {
            setColor(color)
            cornerRadius = corner
        }
    }

    //region Helper functions
    private fun dp(v: Int): Int {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v.toFloat(), context.resources.displayMetrics))
    }
    //endregion
}
