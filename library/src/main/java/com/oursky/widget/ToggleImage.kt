package com.oursky.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.Checkable
import android.widget.ImageView

@Suppress("unused")
class ToggleImage : ImageView, Checkable {
    // delegates
    var onToggle: ((ToggleImage, Boolean) -> Unit)? = null
    private var mChecked: Boolean = false
    private var mPressed: Boolean = false
    private val mAlphaAnimator: ObjectAnimator

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        scaleType = ImageView.ScaleType.FIT_CENTER
        // Touch Effect
        mAlphaAnimator = ObjectAnimator.ofInt(this, "imageAlpha", 0, 0).apply {
            duration = 150
        }
        setOnTouchListener { v, ev ->
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (v.isEnabled) {
                        mPressed = true
                        animateAlpha()
                        return@setOnTouchListener true
                    }
                }
                MotionEvent.ACTION_CANCEL -> {
                    if (v.isEnabled) {
                        mPressed = false
                        animateAlpha()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (v.isEnabled) {
                        mPressed = false
                        animateAlpha()
                        v.performClick()
                    }
                }
            }
            false
        }
        imageAlpha = decideAlpha()
        setOnClickListener { _ -> toggle() }
    }
    //region Checkable
    override fun isChecked(): Boolean = mChecked
    override fun setChecked(checked: Boolean) {
        mChecked = checked
        animateAlpha()
        onToggle?.invoke(this, isChecked)
    }
    override fun toggle() {
        isChecked = !isChecked
    }
    //endregion

    //region Animation
    private fun animateAlpha() {
        mAlphaAnimator.cancel()
        mAlphaAnimator.setIntValues(imageAlpha, decideAlpha())
        mAlphaAnimator.start()
    }
    private fun decideAlpha(): Int {
        return if (mChecked) {
            if (mPressed) 32 else 255
        } else {
            if (mPressed) 32 else 64
        }
    }
    //endregion
}
