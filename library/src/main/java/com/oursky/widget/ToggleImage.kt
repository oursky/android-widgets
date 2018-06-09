package com.oursky.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.Checkable
import android.widget.ImageView

@Suppress("unused")
class ToggleImage : ImageView, Checkable {
    // delegates
    var onToggle: ((ToggleImage, Boolean) -> Unit)? = null
    private var mChecked: Boolean = false

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        scaleType = ImageView.ScaleType.FIT_CENTER
        imageAlpha = if (mChecked) 255 else 64
        setOnClickListener { _ -> toggle() }
    }
    override fun isChecked(): Boolean = mChecked
    override fun setChecked(checked: Boolean) {
        mChecked = checked
        imageAlpha = if (mChecked) 255 else 64
    }
    override fun toggle() {
        isChecked = !isChecked
        onToggle?.invoke(this, isChecked)
    }
}
