package com.oursky.widgets.demo.ui

import android.content.Context
import android.text.InputType
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.ScrollView
import com.oursky.widget.FormComboWithDate
import com.oursky.widget.FormComboWithList
import com.oursky.widget.FormEdit
import com.oursky.widgets.demo.R

class FormScreen : BaseController() {
    override fun onCreateView(context: Context): View {
        val margin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16.0f, context.resources.displayMetrics))
        val edit1 = FormEdit(context).apply {
            setTitleIcon(R.mipmap.ic_launcher)
            setTitle(R.string.formedit_title)
            setHint(R.string.formedit_hint)
        }
        val edit2 = FormEdit(context).apply {
            setTitleIcon(R.mipmap.ic_launcher)
            setTitle(R.string.formedit_title)
            setHint(R.string.formedit_hint)
            setEyeIcon(R.drawable.ic_eye)
            setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
            setImeOptions(EditorInfo.IME_ACTION_SEND)
        }
        val picker1 = FormComboWithList(context).apply {
            setTitleIcon(R.mipmap.ic_launcher)
            setTitle(R.string.formcombo_title)
            setArrowIcon(R.drawable.ic_combo)
            data = arrayOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
        }
        val picker2 = FormComboWithDate(context).apply {
            setTitleIcon(R.mipmap.ic_launcher)
            setTitle(R.string.formcombo_datetitle)
            setArrowIcon(R.drawable.ic_combo)
            setDateFormat("EEE, MMM d, yyyy")
            setDate(2018, 9, 19)
        }
        val contentView = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }
        contentView.addView(edit1, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(margin, margin, margin, 0)
        })
        contentView.addView(edit2, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(margin, margin, margin, 0)
        })
        contentView.addView(picker1, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(margin, margin, margin, 0)
        })
        contentView.addView(picker2, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(margin, margin, margin, 0)
        })
        // Setup Events
        edit1.onTextChange = { v, s ->
            if (s.length > 5 ) {
                v.setStatus(R.string.formedit_err_toolong)
            } else {
                v.setStatus(null)
            }
        }
        edit2.onTextChange = { v, s ->
            if (s.length > 7 ) {
                v.setStatus(R.string.formedit_err_toolong)
            } else {
                v.setStatus(null)
            }
        }
        edit2.onAction = { _, action, ev ->
            if (action == EditorInfo.IME_ACTION_SEND ||
                    (ev?.action == KeyEvent.ACTION_DOWN && ev.keyCode == KeyEvent.KEYCODE_ENTER)) {
                Log.d("Form", "edit2 action")
                true
            } else {
                false
            }
        }
        picker1.onSelect = { _, selected ->
            Log.d("Form", "picker1 selected: $selected")
        }
        picker2.onSelect = { _, selected ->
            Log.d("Form", "picker2 selected: ${picker2.getDateFormat().format(selected.time)}")
        }
        return ScrollView(context).apply {
            addView(contentView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        }
    }
}
