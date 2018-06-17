package com.oursky.widget

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.oursky.widget.helper.TouchEffect

@Suppress("UNUSED_PARAMETER", "UNUSED_ANONYMOUS_PARAMETER")
class SButton : LinearLayout {
    enum class IconIndex { LEFT, TOP, RIGHT, BOTTOM }
    // delegates
    var onClick: ((SButton) -> Unit)? = null
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

        setBackgroundColor(0x7fff00ff)
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        addView(wIconTop, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        addView(LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            addView(wIconLeft, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
            addView(wText, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
            addView(wIconRight, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        addView(wIconBottom, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))

        // Auto re-layout images
        wText.addOnLayoutChangeListener { _, left, top, right, bottom, oleft, otop, oright, obottom ->
            val height = bottom - top
            val oheight = obottom - otop
            if (height != oheight) {
                val width = right - left
                wText.post {
                    val lpLeft = wIconLeft.layoutParams as LinearLayout.LayoutParams
                    lpLeft.width = height
                    lpLeft.height = height
                    lpLeft.rightMargin = height / 2
                    wIconLeft.layoutParams = lpLeft
                    val lpRight = wIconRight.layoutParams as LinearLayout.LayoutParams
                    lpRight.width = height
                    lpRight.height = height
                    lpRight.leftMargin = height / 2
                    wIconRight.layoutParams = lpRight
                    val lpTop = wIconTop.layoutParams as LinearLayout.LayoutParams
                    lpTop.width = width / 2
                    lpTop.height = width / 2
                    lpTop.bottomMargin = height / 2
                    wIconTop.layoutParams = lpTop
                    val lpBottom = wIconBottom.layoutParams as LinearLayout.LayoutParams
                    lpBottom.width = width / 2
                    lpBottom.height = width / 2
                    lpBottom.topMargin = height / 2
                    wIconBottom.layoutParams = lpBottom
                }
            }
        }
        TouchEffect.dimmed(this)
        setOnClickListener { _ -> onClick?.invoke(this@SButton) }
    }
    fun setIcon(index: IconIndex, @DrawableRes resid: Int) {
        when (index) {
            IconIndex.LEFT -> {
                wIconLeft.setImageResource(resid)
                wIconLeft.visibility = View.VISIBLE
            }
            IconIndex.TOP -> {
                wIconTop.setImageResource(resid)
                wIconTop.visibility = View.VISIBLE
            }
            IconIndex.RIGHT -> {
                wIconRight.setImageResource(resid)
                wIconRight.visibility = View.VISIBLE
            }
            IconIndex.BOTTOM -> {
                wIconBottom.setImageResource(resid)
                wIconBottom.visibility = View.VISIBLE
            }
        }
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
    fun setBackgroundColor(@ColorInt color: Int, corner: Float) {
        background = GradientDrawable().apply {
            setColor(color)
            cornerRadius = corner
        }
    }
}
