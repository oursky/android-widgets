package com.oursky.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.support.annotation.StyleableRes
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

@Suppress("MemberVisibilityCanBePrivate", "UNUSED_ANONYMOUS_PARAMETER", "LocalVariableName")
open class SFormLabel : LinearLayout {
    private val wTitleIcon: ImageView
    private val wTitle: TextView
    private val wValue: TextView
    private val wRightIcon: ImageView
    private val wLeftIcon: ImageView
    private val wStatus: TextView
    private val wValueLayout: LinearLayout
    private val wBorder: View
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int): super(context, attrs, defStyleAttr) {
        wTitleIcon = ImageView(context, attrs, defStyleAttr).apply {
            visibility = View.GONE
            scaleType = ImageView.ScaleType.FIT_CENTER
        }
        wTitle = TextView(context, attrs, defStyleAttr).apply {
        }
        wValue = TextView(context, attrs, defStyleAttr).apply {
        }
        wLeftIcon = ImageView(context, attrs, defStyleAttr).apply {
            visibility = View.GONE
            scaleType = ImageView.ScaleType.FIT_CENTER
        }
        wRightIcon = ImageView(context, attrs, defStyleAttr).apply {
            visibility = View.GONE
            scaleType = ImageView.ScaleType.FIT_CENTER
        }
        wStatus = TextView(context, attrs, defStyleAttr).apply {
            visibility = View.GONE
        }
        wValueLayout = LinearLayout(context, attrs, defStyleAttr).apply {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            addView(wLeftIcon,
                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                        setMargins(0, 0, dp(8), 0)
                    })
            addView(wValue,
                    LayoutParams(0, LayoutParams.WRAP_CONTENT).apply {
                        weight = 1.0f
                    })
            addView(wRightIcon,
                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                        setMargins(dp(8), 0, 0, 0)
                    })
        }
        wBorder = View(context).apply {
            setBackgroundColor(Color.argb(128, 0, 0, 0))
        }
        orientation = VERTICAL
        super.addView(LinearLayout(context, attrs, defStyleAttr).apply {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            addView(wTitleIcon,
                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                        setMargins(0, 0, dp(6), 0)
                    })
            addView(wTitle, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            setMargins(0, 0, 0, dp(4))
        })
        super.addView(wValueLayout, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            setMargins(0, 0, 0, dp(2))
        })
        super.addView(wBorder, LayoutParams(LayoutParams.MATCH_PARENT, 1).apply {
            setMargins(0, 0, 0, dp(4))
        })
        super.addView(wStatus, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        // Auto re-layout images
        wTitle.addOnLayoutChangeListener { _, left, top, right, bottom, oleft, otop, oright, obottom ->
            val height = bottom - top
            val oheight = obottom - otop
            if (height != oheight) {
                wTitleIcon.post {
                    setIconToSquare(wTitleIcon, height)
                }
            }
        }
        wValue.addOnLayoutChangeListener { _, left, top, right, bottom, oleft, otop, oright, obottom ->
            val height = bottom - top
            val oheight = obottom - otop
            if (height != oheight) {
                wRightIcon.post {
                    setIconToSquare(wRightIcon, height)
                }
                wLeftIcon.post {
                    setIconToSquare(wLeftIcon, height)
                }
            }
        }
        //region defaults and styles
        val a = context.obtainStyledAttributes(attrs, R.styleable.SFormLabel, defStyleAttr, defStyleRes)
        // title
        if (a.hasValue(R.styleable.SFormLabel_x_title_icon)) {
            setTitleIcon(a.getResourceId(R.styleable.SFormLabel_x_title_icon, -1))
        }
        setTitleColor(a.getColor(R.styleable.SFormLabel_x_title_textcolor, Color.rgb(96, 96, 96)))
        val title_textsize = getTextSizeInSp(a, R.styleable.SFormLabel_x_title_textsize, 14)
        val title_font = a.getResourceId(R.styleable.SFormLabel_x_title_typeface, -1)
        if (title_font != -1) {
            setTitleFont(title_textsize, ResourcesCompat.getFont(context, title_font), Typeface.BOLD)
        } else {
            setTitleFont(title_textsize, null, Typeface.BOLD)
        }
        // Content
        setHintColor(a.getColor(R.styleable.SFormLabel_x_hint_textcolor, Color.rgb(160, 160, 160)))
        setTextColor(a.getColor(R.styleable.SFormLabel_x_content_textcolor, Color.rgb(0, 0, 0)))
        val content_textsize = getTextSizeInSp(a, R.styleable.SFormLabel_x_content_textsize, 16)
        val content_font = a.getResourceId(R.styleable.SFormLabel_x_content_typeface, -1)
        if (content_font != -1) {
            setTextFont(content_textsize, ResourcesCompat.getFont(context, content_font), Typeface.NORMAL)
        } else {
            setTextFont(content_textsize, null, Typeface.NORMAL)
        }
        if (a.hasValue(R.styleable.SFormLabel_x_content_top_spacing)) {
            setTopSpacing(a.getDimension(R.styleable.SFormLabel_x_content_top_spacing, 2f).toInt())
        }
        if (a.hasValue(R.styleable.SFormLabel_x_content_text_selectable)) {
            setSelectable(a.getBoolean(R.styleable.SFormLabel_x_content_text_selectable, true))
        }
        // Content - icons
        if (a.hasValue(R.styleable.SFormLabel_x_content_left_icon)) {
            setLeftIcon(a.getResourceId(R.styleable.SFormLabel_x_content_left_icon, -1))
        }
        if (a.hasValue(R.styleable.SFormLabel_x_content_left_icon_spacing)) {
            val left_icon_space = a.getDimension(R.styleable.SFormLabel_x_content_left_icon_spacing, 8f).toInt()
            setLeftIconSpacing(left_icon_space)
        }
        if (a.hasValue(R.styleable.SFormLabel_x_content_right_icon)) {
            setLeftIcon(a.getResourceId(R.styleable.SFormLabel_x_content_right_icon, -1))
        }
        if (a.hasValue(R.styleable.SFormLabel_x_content_right_icon_spacing)) {
            val right_icon_space = a.getDimension(R.styleable.SFormLabel_x_content_right_icon_spacing, 8f).toInt()
            setLeftIconSpacing(right_icon_space)
        }
        // border
        if (a.hasValue(R.styleable.SFormLabel_x_border_color)) {
            setBorderColor(a.getColor(R.styleable.SFormLabel_x_border_color, Color.argb(128, 0, 0, 0)))
        }
        // Status
        val status_textsize = a.getDimensionPixelSize(R.styleable.SFormLabel_x_status_textsize, 10)
        setStatusColor(a.getColor(R.styleable.SFormLabel_x_status_textcolor, Color.rgb(192, 96, 96)))
        val status_font = a.getResourceId(R.styleable.SFormLabel_x_status_typeface, -1)
        if (status_font != -1) {
            setStatusFont(status_textsize, ResourcesCompat.getFont(context, status_font), Typeface.NORMAL)
        } else {
            setStatusFont(status_textsize, null, Typeface.NORMAL)
        }
        a.recycle()
        //endregion
    }
    //region Font, Color & Appearance
    fun setTitleFont(size: Int) {
        wTitle.textSize = size.toFloat()
    }
    fun setTitleFont(size: Int, typeface: Typeface?, style: Int = Typeface.NORMAL) {
        wTitle.textSize = size.toFloat()
        wTitle.setTypeface(typeface, style)
    }
    fun setTitleColor(color: Int) {
        wTitle.setTextColor(color)
    }
    fun setTextFont(size: Int) {
        wValue.textSize = size.toFloat()
    }
    fun setTextFont(size: Int, typeface: Typeface?, style: Int = Typeface.NORMAL) {
        wValue.textSize = size.toFloat()
        wValue.setTypeface(typeface, style)
    }
    fun setTextColor(color: Int) {
        wValue.setTextColor(color)
    }
    fun setHintColor(color: Int) {
        wValue.setHintTextColor(color)
    }
    fun setStatusFont(size: Int) {
        wStatus.textSize = size.toFloat()
    }
    fun setStatusFont(size: Int, typeface: Typeface?, style: Int = Typeface.NORMAL) {
        wStatus.textSize = size.toFloat()
        wStatus.setTypeface(typeface, style)
    }
    fun setStatusColor(color: Int) {
        wStatus.setTextColor(color)
    }
    //endregion

    //region Text Value
    fun setTitleIcon(resid: Int) {
        wTitleIcon.setImageResource(resid)
        wTitleIcon.visibility = if (resid == 0) View.GONE else View.VISIBLE
    }
    fun setTitle(resid: Int) {
        wTitle.setText(resid)
    }
    fun setTitle(text: String?) {
        wTitle.setText(text, TextView.BufferType.EDITABLE)
    }
    fun setHint(resid: Int) {
        wValue.setHint(resid)
    }
    fun setHint(text: String?) {
        wValue.hint = text
    }
    fun setText(resid: Int) {
        wValue.setText(resid)
    }
    fun setText(text: String?) {
        wValue.setText(text, TextView.BufferType.EDITABLE)
    }
    fun getText(): String {
        return wValue.text.toString()
    }
    fun setSelectable(selectable: Boolean) {
        wValue.setTextIsSelectable(selectable)
    }
    fun setRightIcon(resid: Int) {
        wRightIcon.setImageResource(resid)
        wRightIcon.visibility = if (resid == 0) View.GONE else View.VISIBLE
    }
    fun setLeftIcon(resid: Int) {
        wLeftIcon.setImageResource(resid)
        wLeftIcon.visibility = if (resid == 0) View.GONE else View.VISIBLE
    }
    fun setRightIconSpacing(dp: Int) {
        wRightIcon.post {
            (wRightIcon.layoutParams as? MarginLayoutParams)?.let { lp ->
                lp.leftMargin = dp(dp)
            }
        }
    }
    fun setLeftIconSpacing(dp: Int) {
        wLeftIcon.post {
            (wLeftIcon.layoutParams as? MarginLayoutParams)?.let { lp ->
                lp.rightMargin = dp(dp)
            }
        }
    }

    fun setTopSpacing(dp: Int) {
        wValueLayout.post {
            (wValueLayout.layoutParams as? MarginLayoutParams)?.let { lp ->
                lp.topMargin = dp(dp)
            }
        }
    }

    /**
     * Set the top and bottom margin of text as if setting through its MarginLayoutParams.
     * Unit is dp. Non-RTL-aware.
     */
    fun setTextMargin(top: Int, bottom: Int) {
        wValueLayout.post {
            (wValueLayout.layoutParams as? MarginLayoutParams)?.let { lp ->
                lp.topMargin = dp(top)
                lp.bottomMargin = dp(bottom)
            }
        }
    }

    fun setBorderColor(color: Int) {
        wBorder.setBackgroundColor(color)
    }
    fun setStatus(resid: Int) {
        wStatus.setText(resid)
        wStatus.visibility = View.VISIBLE
    }
    fun setStatus(text: String?) {
        wStatus.text = text
        wStatus.visibility = if (text != null) View.VISIBLE else View.GONE
    }
    //endregion

    //region Helper functions
    private fun dp(v: Int): Int {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v.toFloat(), context.resources.displayMetrics))
    }
    private fun getTextSizeInSp(a: TypedArray, @StyleableRes attr: Int, defValue: Int): Int {
        val density = resources.displayMetrics.scaledDensity
        return (a.getDimensionPixelSize(attr, (defValue * density).toInt()) / density).toInt()
    }
    private fun setIconToSquare(view: View, size: Int) {
        val lp = view.layoutParams
        lp.width = size
        lp.height = size
        view.layoutParams = lp
    }
    //endregion
}