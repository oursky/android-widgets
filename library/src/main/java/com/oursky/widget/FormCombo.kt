package com.oursky.widget

import android.animation.Animator
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.DatePicker
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.PopupWindow
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Suppress("MemberVisibilityCanBePrivate", "UNUSED_ANONYMOUS_PARAMETER", "PrivatePropertyName")
abstract class FormCombo : LinearLayout {
    private val ANIMATION_DURATION = 150L
    private val wTitleIcon: ImageView
    private val wTitle: TextView
    private val wText: TextView
    private val wArrow: ImageView
    // Picker
    private var wPopup: PopupWindow? = null
    private var wBackground: View? = null
    private var wPicker: View? = null
    private var mAnimating: Boolean = false
    abstract fun createPicker(): View
    abstract fun setupPicker()

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        wTitleIcon = ImageView(context, attrs, defStyleAttr).apply {
            visibility = View.GONE
            scaleType = ImageView.ScaleType.FIT_CENTER
        }
        wTitle = TextView(context, attrs, defStyleAttr).apply {
        }
        wText = TextView(context, attrs, defStyleAttr).apply {
            setSingleLine(true)
        }
        wArrow = ImageView(context, attrs, defStyleAttr).apply {
            scaleType = ImageView.ScaleType.FIT_CENTER
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
        super.addView(LinearLayout(context, attrs, defStyleAttr).apply {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            addView(wText,
                    LayoutParams(0, LayoutParams.WRAP_CONTENT).apply {
                        weight = 1.0f
                    })
            addView(wArrow,
                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                        setMargins(dp(8), 0, 0, 0)
                    })
        }, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            setMargins(0, 0, 0, dp(2))
        })
        super.addView(View(context).apply {
            setBackgroundColor(Color.argb(128, 0, 0, 0))
        }, LayoutParams(LayoutParams.MATCH_PARENT, 1).apply {
            setMargins(0, 0, 0, dp(4))
        })
        // Auto re-layout images
        wTitle.addOnLayoutChangeListener { _, left, top, right, bottom, oleft, otop, oright, obottom ->
            val height = bottom - top
            val oheight = obottom - otop
            if (height != oheight) {
                wTitleIcon.post {
                    val lp = wTitleIcon.layoutParams
                    lp.width = height
                    lp.height = height
                    wTitleIcon.layoutParams = lp
                }
            }
        }
        wText.addOnLayoutChangeListener { _, left, top, right, bottom, oleft, otop, oright, obottom ->
            val height = bottom - top
            val oheight = obottom - otop
            if (height != oheight) {
                wArrow.post {
                    val lp = wArrow.layoutParams
                    lp.width = height
                    lp.height = height
                    wArrow.layoutParams = lp
                }
            }
        }
        // Setup Events
        setOnClickListener {
            showPicker()
        }
        // Touch Effect
        setOnTouchListener { v, ev ->
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (v.isEnabled) {
                        v.animate().cancel()
                        v.animate().alpha(0.2f).duration = 150
                        return@setOnTouchListener true
                    }
                }
                MotionEvent.ACTION_CANCEL -> {
                    if (v.isEnabled) {
                        v.animate().cancel()
                        v.animate().alpha(1.0f).duration = 150
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (v.isEnabled) {
                        v.animate().cancel()
                        v.animate().alpha(1.0f).duration = 150
                        v.performClick()
                    }
                }
            }
            false
        }
        // defaults
        setTitleColor(Color.rgb(96, 96, 96))
        setTitleFont(14, null, Typeface.BOLD)
        setTextColor(Color.rgb(0, 0, 0))
        setTextFont(16)
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
        wText.textSize = size.toFloat()
    }
    fun setTextFont(size: Int, typeface: Typeface?, style: Int = Typeface.NORMAL) {
        wText.textSize = size.toFloat()
        wText.setTypeface(typeface, style)
    }
    fun setTextColor(color: Int) {
        wText.setTextColor(color)
    }
    //endregion

    //region Text Value
    fun setTitleIcon(resid: Int) {
        wTitleIcon.setImageResource(resid)
        wTitleIcon.visibility = View.VISIBLE
    }
    fun setTitle(resid: Int) {
        wTitle.setText(resid)
    }
    fun getTitle(): String {
        return wTitle.text.toString()
    }
    fun setArrowIcon(resid: Int) {
        wArrow.setImageResource(resid)
    }
    fun setText(text: String?) {
        wText.text = text
    }
    fun getText(): String {
        return wText.text.toString()
    }
    //endregion
    internal fun dp(v: Int): Int {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v.toFloat(), context.resources.displayMetrics))
    }
    //region Picker
    private fun showPicker() {
        initializeComponents()
        setupPicker()
        wPopup?.showAtLocation(rootView, Gravity.BOTTOM, 0, 0)
        animateShow(true)
    }
    private fun initializeComponents() {
        if (wBackground == null) {
            wBackground = View(context).apply {
                setBackgroundColor(Color.argb(192, 0, 0, 0))
                setOnClickListener { _ ->
                    if (!mAnimating) {
                        animateShow(false) {
                            wPopup?.dismiss()
                        }
                    }
                }
            }
        }
        if (wPicker == null) {
            wPicker = createPicker()
        }
        if (wPopup == null) {
            val contentView = FrameLayout(context)
            contentView.addView(wBackground, FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
            contentView.addView(wPicker, FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.BOTTOM))
            wPopup = PopupWindow(contentView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false)
        }
    }
    private fun animateShow(b: Boolean, cb: (() -> Unit)? = null) {
        if (b) {
            wBackground?.let {
                it.alpha = 0.0f
                it.animate().cancel()
                mAnimating = true
                it.animate().alpha(1.0f).setDuration(ANIMATION_DURATION).setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animator: Animator?) {
                    }
                    override fun onAnimationStart(animator: Animator?) {
                    }
                    override fun onAnimationCancel(animator: Animator?) {
                        mAnimating = false
                        cb?.invoke()
                    }
                    override fun onAnimationEnd(animator: Animator?) {
                        mAnimating = false
                        cb?.invoke()
                    }
                })
            }
            wPicker?.let {
                it.translationY = 400.0f
                it.animate().cancel()
                it.animate().translationY(0.0f).setDuration(ANIMATION_DURATION).setListener(null)
            }
        } else {
            wBackground?.let {
                it.alpha = 1.0f
                it.animate().cancel()
                mAnimating = true
                it.animate().alpha(0.0f).setDuration(ANIMATION_DURATION).setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animator: Animator?) {
                    }
                    override fun onAnimationStart(animator: Animator?) {
                    }
                    override fun onAnimationCancel(animator: Animator?) {
                        mAnimating = false
                        cb?.invoke()
                    }
                    override fun onAnimationEnd(animator: Animator?) {
                        mAnimating = false
                        cb?.invoke()
                    }
                })
            }
            wPicker?.let {
                it.translationY = 0.0f
                it.animate().cancel()
                it.animate().translationY(400.0f).setDuration(ANIMATION_DURATION).setListener(null)
            }
        }
    }
    //endregion
}

@Suppress("UNUSED_ANONYMOUS_PARAMETER", "PrivatePropertyName", "MemberVisibilityCanBePrivate")
class FormComboWithList : FormCombo {
    // delegates
    var onSelect: ((FormComboWithList, index: Int) -> Unit)? = null
    var selected: Int = 0
        set(value) {
            field = value
            setText(data?.getOrNull(value))
        }
    var data: Array<String>? = null
        set(value) {
            field = value
            setText(value?.getOrNull(selected))
        }
    private var wContentView: LinearLayout? = null
    private var wPickerTitle: TextView? = null
    private var wPicker: NumberPicker? = null
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
    }
    override fun createPicker(): View {
        if (wPickerTitle == null) {
            wPickerTitle = TextView(context).apply {
                setTypeface(null, Typeface.BOLD)
                textSize = 18.0f
            }
        }
        if (wPicker == null) {
            wPicker = NumberPicker(context).apply {
                isClickable = true
                descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                wrapSelectorWheel = false
                setOnValueChangedListener { _, oldValue, newValue ->
                    selected = newValue
                    onSelect?.invoke(this@FormComboWithList, selected)
                }
            }
        }
        if (wContentView == null) {
            wContentView = LinearLayout(context).apply {
                orientation = VERTICAL
                gravity = Gravity.CENTER_HORIZONTAL
                setBackgroundColor(Color.WHITE)
                addView(wPickerTitle, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                    setMargins(0, dp(8), 0, dp(8))
                })
                addView(View(context).apply {
                    setBackgroundColor(Color.argb(128, 0, 0, 0))
                }, LayoutParams(LayoutParams.MATCH_PARENT, 1).apply {
                    setMargins(0, 0, 0, dp(2))
                })
                addView(wPicker, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
            }
        }
        return wContentView as View
    }
    override fun setupPicker() {
        wPickerTitle?.let {
            it.text = getTitle()
        }
        wPicker?.let {
            it.minValue = 0
            it.maxValue = (data?.size ?: 0) - 1
            it.displayedValues = data
            it.value = selected
        }
    }
}
@Suppress("MemberVisibilityCanBePrivate")
class FormComboWithDate : FormCombo {
    // delegates
    var onSelect: ((FormComboWithDate, date: Calendar) -> Unit)? = null
    private var wContentView: FrameLayout? = null
    private var wPicker: DatePicker? = null
    private var mYear: Int
    private var mMonth: Int
    private var mDay: Int
    private var mMinDate: Long
    private var mMaxDate: Long
    private val mDateFormatter: SimpleDateFormat
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        mDateFormatter = SimpleDateFormat("EEE, MMM d, yyyy", Locale.US)
        val now = Calendar.getInstance()
        mYear = now.get(Calendar.YEAR)
        mMonth = now.get(Calendar.MONTH)
        mDay = now.get(Calendar.DAY_OF_MONTH)
        mMinDate = 0
        mMaxDate = 0
        setMinDate(null)
        setMaxDate(null)
        setText(mDateFormatter.format(getDate().time))
    }
    override fun createPicker(): View {
        if (wPicker == null) {
            wPicker = DatePicker(context).apply {
                isClickable = true
                setBackgroundColor(Color.WHITE)
                descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                minDate = mMinDate
                maxDate = mMaxDate
                init(mYear, mMonth, mDay) { _, year, month, day ->
                    mYear = year
                    mMonth = month
                    mDay = day
                    setText(mDateFormatter.format(getDate().time))
                    onSelect?.invoke(this@FormComboWithDate, getDate())
                }
            }
        }
        if (wContentView == null) {
            wContentView = FrameLayout(context).apply {
                addView(wPicker, FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL))
            }
        }
        return wContentView as View
    }
    override fun setupPicker() {
        wPicker?.updateDate(mYear, mMonth, mDay)
    }
    fun setMinDate(date: Calendar?) {
        mMinDate = date?.timeInMillis ?: 0
        wPicker?.minDate = mMinDate
    }
    fun setMaxDate(date: Calendar?) {
        if (date != null) {
            mMaxDate = date.timeInMillis
        } else {
            val d = Calendar.getInstance()
            d.add(Calendar.YEAR, 10)
            mMaxDate = d.timeInMillis
        }
        wPicker?.maxDate = mMaxDate
    }
    fun setDate(date: Calendar) {
        mYear = date.get(Calendar.YEAR)
        mMonth = date.get(Calendar.MONTH)
        mDay = date.get(Calendar.DAY_OF_MONTH)
        wPicker?.updateDate(mYear, mMonth, mDay)
    }
    fun setDate(year: Int, month: Int, day: Int) {
        mYear = year
        mMonth = month - 1
        mDay = day
        if (wPicker != null) {
            wPicker?.updateDate(mYear, mMonth, mDay)
        } else {
            setText(mDateFormatter.format(getDate().time))
        }
    }
    fun getDate(): Calendar {
        val date = Calendar.getInstance()
        date.set(mYear, mMonth, mDay, 0, 0, 0)
        return date
    }
    fun setDateFormat(pattern: String) {
        mDateFormatter.applyPattern(pattern)
        setText(mDateFormatter.format(getDate().time))
    }
    fun getDateFormat(): SimpleDateFormat = mDateFormatter
}
