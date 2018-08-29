package com.oursky.widget

import android.content.Context
import android.util.AttributeSet
import com.oursky.widget.helper.TouchEffect

@Suppress("UNUSED_PARAMETER", "UNUSED_ANONYMOUS_PARAMETER")
open class SButton : SLabel {
    // delegates
    var onClick: ((SButton) -> Unit)? = null

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs) {
        TouchEffect.dimmed(this)
        setOnClickListener { _ -> onClick?.invoke(this@SButton) }
    }
}
