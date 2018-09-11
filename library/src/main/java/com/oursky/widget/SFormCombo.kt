package com.oursky.widget

import android.animation.Animator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.annotation.StyleableRes
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.DatePicker
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.TimePicker
import com.oursky.widget.helper.TouchEffect
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Suppress("MemberVisibilityCanBePrivate", "UNUSED_ANONYMOUS_PARAMETER", "PrivatePropertyName", "LocalVariableName")
abstract class SFormCombo : LinearLayout {
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
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int): super(context, attrs, defStyleAttr) {
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
                    LayoutParams(dp(24), dp(24)).apply {
                        setMargins(dp(8), 0, dp(8), 0)
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
        TouchEffect.dimmed(this)
        // Setup Events
        setOnClickListener {
            showPicker()
        }
        //region defaults and styles
        val a = context.obtainStyledAttributes(attrs, R.styleable.SFormCombo, defStyleAttr, defStyleRes)
        // title
        if (a.hasValue(R.styleable.SFormCombo_x_title_icon)) {
            setTitleIcon(a.getResourceId(R.styleable.SFormCombo_x_title_icon, -1))
        }
        setTitleColor(a.getColor(R.styleable.SFormCombo_x_title_textcolor, Color.rgb(96, 96, 96)))
        val title_textsize = getTextSizeInSp(a, R.styleable.SFormCombo_x_title_textsize, 14)
        if (a.hasValue(R.styleable.SFormCombo_x_title_typeface)) {
            val title_font = a.getResourceId(R.styleable.SFormCombo_x_title_typeface, -1)
            setTitleFont(title_textsize, ResourcesCompat.getFont(context, title_font), Typeface.BOLD)
        } else {
            setTitleFont(title_textsize, null, Typeface.BOLD)
        }
        // Content
        setTextColor(a.getColor(R.styleable.SFormCombo_x_content_textcolor, Color.rgb(0, 0, 0)))
        val content_textsize = getTextSizeInSp(a, R.styleable.SFormCombo_x_content_textsize, 16)
        if (a.hasValue(R.styleable.SFormCombo_x_content_typeface)) {
            val content_font = a.getResourceId(R.styleable.SFormCombo_x_content_typeface, -1)
            setTextFont(content_textsize, ResourcesCompat.getFont(context, content_font), Typeface.NORMAL)
        } else {
            setTextFont(content_textsize, null, Typeface.NORMAL)
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
    fun setTitleColor(@ColorInt color: Int) {
        wTitle.setTextColor(color)
    }
    fun setTextFont(size: Int) {
        wText.textSize = size.toFloat()
    }
    fun setTextFont(size: Int, typeface: Typeface?, style: Int = Typeface.NORMAL) {
        wText.textSize = size.toFloat()
        wText.setTypeface(typeface, style)
    }
    fun setTextColor(@ColorInt color: Int) {
        wText.setTextColor(color)
    }
    //endregion

    //region Text Value
    fun setTitleIcon(@DrawableRes resid: Int) {
        wTitleIcon.setImageResource(resid)
        wTitleIcon.visibility = View.VISIBLE
    }
    fun setTitle(@StringRes resid: Int) {
        wTitle.setText(resid)
    }
    fun getTitle(): String {
        return wTitle.text.toString()
    }

    fun setArrowIcon(@DrawableRes resid: Int) {
        wArrow.setImageResource(resid)
    }

    /**
     * Set the scale type of arrow icon. Default is FIT_CENTER.
     */
    fun setArrowIconScaleType(type: ImageView.ScaleType) {
        wArrow.scaleType = type
    }

    fun setText(text: String?) {
        wText.text = text
    }

    /**
     * Set the left and right margin of text as if setting through its MarginLayoutParams.
     * Unit is dp. Non-RTL-aware.
     */
    fun setTextMargin(top: Int, bottom: Int) {
        wText.post {
            (wText.layoutParams as? MarginLayoutParams)?.apply {
                topMargin = dp(top)
                bottomMargin = dp(bottom)
            }
        }
    }

    /**
     * Set the dimension of arrow icon as if setting through its LayoutParams. Can be constants such
     * as MATCH_PARENT, or sizes in px.
     */
    fun setArrowDimen(width: Int, height: Int) {
        wArrow.post {
            (wArrow.layoutParams as? MarginLayoutParams)?.apply {
                this.width = width
                this.height = height
            }
        }
    }

    /**
     * Set the left and right margin of the arrow icon as if setting through its
     * MarginLayoutParams. Unit is dp. Non-RTL-aware.
     */
    fun setArrowMargin(left: Int, right: Int) {
        wArrow.post {
            (wArrow.layoutParams as? MarginLayoutParams)?.apply {
                leftMargin = dp(left)
                rightMargin = dp(right)
            }
        }
    }

    fun getText(): String {
        return wText.text.toString()
    }
    //endregion
    internal fun dp(v: Int): Int {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v.toFloat(), context.resources.displayMetrics))
    }
    private fun getTextSizeInSp(a: TypedArray, @StyleableRes attr: Int, defValue: Int): Int {
        val density = resources.displayMetrics.scaledDensity
        return (a.getDimensionPixelSize(attr, (defValue * density).toInt()) / density).toInt()
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
class SFormComboWithList : SFormCombo {
    // delegates
    var onSelect: ((SFormComboWithList, index: Int) -> Unit)? = null
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
                descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                wrapSelectorWheel = false
                setOnValueChangedListener { _, oldValue, newValue ->
                    selected = newValue
                    onSelect?.invoke(this@SFormComboWithList, selected)
                }
            }
        }
        if (wContentView == null) {
            wContentView = LinearLayout(context).apply {
                orientation = VERTICAL
                gravity = Gravity.CENTER_HORIZONTAL
                isClickable = true
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
class SFormComboWithDate : SFormCombo {
    // delegates
    var onSelect: ((SFormComboWithDate, date: Calendar) -> Unit)? = null
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
        updateLabel()
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
                    updateLabel()
                    onSelect?.invoke(this@SFormComboWithDate, getDate())
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
        mMaxDate = if (date != null) {
            date.timeInMillis
        } else {
            val d = Calendar.getInstance()
            d.add(Calendar.YEAR, 10)
            d.timeInMillis
        }
        wPicker?.maxDate = mMaxDate
    }
    fun setDate(date: Calendar) {
        mYear = date.get(Calendar.YEAR)
        mMonth = date.get(Calendar.MONTH)
        mDay = date.get(Calendar.DAY_OF_MONTH)
        updateLabel()
        if (wPicker != null) {
            wPicker?.updateDate(mYear, mMonth, mDay)
        } else {
            onSelect?.invoke(this@SFormComboWithDate, getDate())
        }
    }
    fun setDate(year: Int, month: Int, day: Int) {
        mYear = year
        mMonth = month - 1
        mDay = day
        updateLabel()
        if (wPicker != null) {
            wPicker?.updateDate(mYear, mMonth, mDay)
        } else {
            onSelect?.invoke(this@SFormComboWithDate, getDate())
        }
    }
    fun getDate(): Calendar {
        val date = Calendar.getInstance()
        date.set(mYear, mMonth, mDay, 0, 0, 0)
        return date
    }
    fun setDateFormat(pattern: String) {
        mDateFormatter.applyPattern(pattern)
        updateLabel()
    }
    fun getDateFormat(): SimpleDateFormat = mDateFormatter
    private fun updateLabel() {
        setText(mDateFormatter.format(getDate().time))
    }
}
@Suppress("MemberVisibilityCanBePrivate")
class SFormComboWithTime : SFormCombo {
    // delegates
    var onSelect: ((SFormComboWithTime, hour: Int, minute: Int) -> Unit)? = null
    private var wContentView: FrameLayout? = null
    private var wPicker: TimePicker? = null
    private var mHour: Int
    private var mMinute: Int
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        val now = Calendar.getInstance()
        mHour = now.get(Calendar.HOUR)
        mMinute = now.get(Calendar.MINUTE)
        updateLabel()
    }
    override fun createPicker(): View {
        if (wPicker == null) {
            wPicker = TimePicker(context).apply {
                isClickable = true
                setBackgroundColor(Color.WHITE)
                descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                setIs24HourView(true)
                @Suppress("DEPRECATION")
                if (Build.VERSION.SDK_INT < 23) {
                    currentHour = mHour
                    currentMinute = mMinute
                } else {
                    hour = mHour
                    minute = mMinute
                }
                setOnTimeChangedListener { _, hour, minute ->
                    mHour = hour
                    mMinute = minute
                    updateLabel()
                    onSelect?.invoke(this@SFormComboWithTime, mHour, mMinute)
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
        wPicker?.let {
            @Suppress("DEPRECATION")
            if (Build.VERSION.SDK_INT < 23) {
                it.currentHour = mHour
                it.currentMinute = mMinute
            } else {
                it.hour = mHour
                it.minute = mMinute
            }
        }
    }
    fun setTime(hour: Int, minute: Int) {
        mHour = hour
        mMinute = minute
        updateLabel()
        if (wPicker != null ) {
            setupPicker()
        } else {
            onSelect?.invoke(this@SFormComboWithTime, mHour, mMinute)
        }
    }
    fun getHour(): Int = mHour
    fun getMinute(): Int = mMinute
    private fun updateLabel() {
        setText(String.format(Locale.US, "%02d:%02d", mHour, mMinute))
    }
}
