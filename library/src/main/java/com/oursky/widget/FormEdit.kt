package com.oursky.widget

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.oursky.widget.helper.KeyboardHelper

@Suppress("MemberVisibilityCanBePrivate")
class FormEdit : LinearLayout {
    // delegates
    var onTextChange: ((FormEdit, String) -> Unit)? = null
    var onAction: ((FormEdit, actionId: Int, ev: KeyEvent?) -> Boolean)? = null

    private val wTitleIcon: ImageView
    private val wTitle: TextView
    private val wEdit: EditText
    private val wEye: ToggleImage
    private val wStatus: TextView
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        wTitleIcon = ImageView(context, attrs, defStyleAttr).apply {
            visibility = View.GONE
            scaleType = ImageView.ScaleType.FIT_CENTER
        }
        wTitle = TextView(context, attrs, defStyleAttr).apply {
        }
        wEdit = EditText(context, attrs, defStyleAttr).apply {
            setTextIsSelectable(true)
            setSingleLine(true)
        }
        wEye = ToggleImage(context, attrs, defStyleAttr).apply {
            visibility = View.GONE
            scaleType = ImageView.ScaleType.FIT_CENTER
        }
        wStatus = TextView(context, attrs, defStyleAttr).apply {
            visibility = View.GONE
        }
        orientation = VERTICAL
        addView(LinearLayout(context, attrs, defStyleAttr).apply {
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
        addView(LinearLayout(context, attrs, defStyleAttr).apply {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            addView(wEdit,
                    LayoutParams(0, LayoutParams.WRAP_CONTENT).apply {
                        weight = 1.0f
                    })
            addView(wEye,
                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                        setMargins(dp(8), 0, 0, 0)
                    })
        }, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            setMargins(0, 0, 0, dp(2))
        })
        addView(View(context).apply {
            setBackgroundColor(Color.argb(128, 0, 0, 0))
        }, LayoutParams(LayoutParams.MATCH_PARENT, 1).apply {
            setMargins(0, 0, 0, dp(4))
        })
        addView(wStatus, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
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
        wEdit.addOnLayoutChangeListener { _, left, top, right, bottom, oleft, otop, oright, obottom ->
            val height = bottom - top
            val oheight = obottom - otop
            if (height != oheight) {
                wEye.post {
                    val lp = wEye.layoutParams
                    lp.width = height
                    lp.height = height
                    wEye.layoutParams = lp
                }
            }
        }
        // Setup Events
        // Gain focus on click
        setOnClickListener {
            wEdit.requestFocus()
        }
        wEdit.setOnFocusChangeListener { _, b ->
            if (b) KeyboardHelper.showAndFocus(context as Activity, wEdit)
        }
        // Track Text Chnges
        wEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                this@FormEdit.onTextChange?.invoke(this@FormEdit, s.toString())
            }
        })
        wEdit.setOnEditorActionListener { _, actionId, keyEvent ->
            onAction?.invoke(this@FormEdit, actionId, keyEvent) == true
        }
        wEye.onToggle = { _, _ -> updatePasswordVisibility() }
        // defaults
        setTitleColor(Color.rgb(96, 96, 96))
        setTitleFont(14, null, Typeface.BOLD)
        setEditColor(Color.rgb(0, 0, 0))
        setHintColor(Color.rgb(160, 160, 160))
        setEditFont(16)
        setStatusColor(Color.rgb(192, 0, 0))
        setStatusFont(10)
        setInputType(InputType.TYPE_CLASS_TEXT)
    }
    //region Font, Color & Appearance
    fun setTitleFont(size: Int) {
        wTitle.textSize = size.toFloat()
    }
    fun setEditFont(size: Int) {
        wEdit.textSize = size.toFloat()
    }
    fun setStatusFont(size: Int) {
        wStatus.textSize = size.toFloat()
    }
    fun setTitleFont(size: Int, typeface: Typeface?, style: Int = Typeface.NORMAL) {
        wTitle.textSize = size.toFloat()
        wTitle.setTypeface(typeface, style)
    }
    fun setEditFont(size: Int, typeface: Typeface?, style: Int = Typeface.NORMAL) {
        wEdit.textSize = size.toFloat()
        wEdit.setTypeface(typeface, style)
    }
    fun setStatusFont(size: Int, typeface: Typeface?, style: Int = Typeface.NORMAL) {
        wStatus.textSize = size.toFloat()
        wStatus.setTypeface(typeface, style)
    }
    fun setTitleColor(color: Int) {
        wTitle.setTextColor(color)
    }
    fun setHintColor(color: Int) {
        wEdit.setHintTextColor(color)
    }
    fun setEditColor(color: Int) {
        wEdit.setTextColor(color)
    }
    fun setStatusColor(color: Int) {
        wStatus.setTextColor(color)
    }
    //endregion

    //region Text Value
    fun setTitleIcon(resid: Int) {
        wTitleIcon.setImageResource(resid)
        wTitleIcon.visibility = View.VISIBLE
    }
    fun setEyeIcon(resid: Int) {
        wEye.setImageResource(resid)
    }
    fun setTitle(resid: Int) {
        wTitle.setText(resid)
    }
    fun setHint(resid: Int) {
        wEdit.setHint(resid)
    }
    fun setHint(text: String?) {
        wEdit.hint = text
    }
    fun setEdit(resid: Int) {
        wEdit.setText(resid)
    }
    fun setEdit(text: String?) {
        wEdit.setText(text, TextView.BufferType.EDITABLE)
    }
    fun setStatus(resid: Int) {
        wStatus.setText(resid)
        wStatus.visibility = View.VISIBLE
    }
    fun setStatus(text: String?) {
        wStatus.text = text
        wStatus.visibility = if (text != null) View.VISIBLE else View.GONE
    }
    fun getEdit(): String {
        return wEdit.text.toString()
    }
    //endregion

    //region misc
    fun setInputType(type: Int = InputType.TYPE_CLASS_TEXT) {
        val passwordTypes = arrayOf(
                InputType.TYPE_NUMBER_VARIATION_PASSWORD,
                InputType.TYPE_TEXT_VARIATION_PASSWORD,
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD,
                InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD)
        wEdit.setRawInputType(type)
        if (passwordTypes.contains(type)) {
            updatePasswordVisibility()
            wEye.visibility = View.VISIBLE
        } else {
            wEdit.transformationMethod = null
            wEye.visibility = View.GONE
        }
    }
    fun setImeOptions(option: Int = EditorInfo.IME_ACTION_DONE) {
        wEdit.imeOptions = option
    }
    //endregion
    private fun updatePasswordVisibility() {
        val s = wEdit.selectionStart
        val e = wEdit.selectionEnd
        wEdit.transformationMethod = if (!wEye.isChecked) PasswordTransformationMethod.getInstance() else null
        wEdit.setSelection(s, e)
    }
    private fun dp(v: Int): Int {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v.toFloat(), context.resources.displayMetrics))
    }
}
