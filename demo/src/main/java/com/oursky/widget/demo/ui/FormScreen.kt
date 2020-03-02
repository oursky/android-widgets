package com.oursky.widget.demo.ui

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
import com.oursky.widget.SFormComboWithDate
import com.oursky.widget.SFormComboWithList
import com.oursky.widget.SFormComboWithTime
import com.oursky.widget.SFormEdit
import com.oursky.widget.SFormLabel
import com.oursky.widget.demo.R

class FormScreen : BaseController() {
    override fun onCreateView(context: Context): View {
        val margin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16.0f, context.resources.displayMetrics))
        val edit1 = SFormEdit(context, null, 0, R.style.CustomFormEdit).apply {
            setTitleIcon(R.mipmap.ic_launcher)
            setTitle(R.string.formedit_title)
            setHint(R.string.formedit_hint)
        }
        val edit2 = SFormEdit(context).apply {
            setTitleIcon(R.mipmap.ic_launcher)
            setTitle(R.string.formedit_title)
            setHint(R.string.formedit_hint)
            setEyeIcon(R.drawable.ic_eye)
            setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
            setImeOptions(EditorInfo.IME_ACTION_SEND)
        }
        val listPicker = SFormComboWithList(context).apply {
            setTitleIcon(R.mipmap.ic_launcher)
            setTitle(R.string.formcombo_listtitle)
            setArrowIcon(R.drawable.ic_combo)
            data = arrayOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
        }
        val datePicker = SFormComboWithDate(context).apply {
            setTitleIcon(R.mipmap.ic_launcher)
            setTitle(R.string.formcombo_datetitle)
            setArrowIcon(R.drawable.ic_combo)
            setDateFormat("EEE, MMM d, yyyy")
            setDate(2018, 9, 19)
        }
        val timePicker = SFormComboWithTime(context).apply {
            setTitleIcon(R.mipmap.ic_launcher)
            setTitle(R.string.formcombo_timetitle)
            setArrowIcon(R.drawable.ic_combo)
            setTime(13, 20)
        }
        val labelContentOnly = SFormLabel(context).apply {
            setTitleIcon(R.mipmap.ic_launcher)
            setTitle(R.string.formlabel_title)
            setText(R.string.formlabel_content_no_icon)
        }
        val labelLeftIconOnly = SFormLabel(context).apply {
            setTitleIcon(R.mipmap.ic_launcher)
            setTitle(R.string.formlabel_title)
            setText(R.string.formlabel_content_left_icon)
            setLeftIconSpacing(10)
            setLeftIcon(R.mipmap.ic_launcher_round)
        }
        val labelRightIconOnly = SFormLabel(context).apply {
            setTitleIcon(R.mipmap.ic_launcher)
            setTitle(R.string.formlabel_title)
            setText(R.string.formlabel_content_right_icon)
            setRightIconSpacing(10)
            setRightIcon(R.mipmap.ic_launcher_round)
        }
        val labelBothIcon = SFormLabel(context).apply {
            setTitleIcon(R.mipmap.ic_launcher)
            setTitle(R.string.formlabel_title)
            setText(R.string.formlabel_content_both_icon)
            setLeftIcon(R.mipmap.ic_launcher_round)
            setRightIcon(R.mipmap.ic_launcher_round)
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
        contentView.addView(listPicker, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(margin, margin, margin, 0)
        })
        contentView.addView(datePicker, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(margin, margin, margin, 0)
        })
        contentView.addView(timePicker, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(margin, margin, margin, 0)
        })
        contentView.addView(labelContentOnly, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(margin, margin, margin, 0)
        })
        contentView.addView(labelLeftIconOnly, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(margin, margin, margin, 0)
        })
        contentView.addView(labelRightIconOnly, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(margin, margin, margin, 0)
        })
        contentView.addView(labelBothIcon, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(margin, margin, margin, 0)
        })

        // Setup Events
        edit1.onTextChange = { v, s ->
            if (s.length > 5) {
                v.setStatus(R.string.formedit_err_toolong)
            } else {
                v.setStatus(null)
            }
        }
        edit2.onTextChange = { v, s ->
            if (s.length > 7) {
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
        listPicker.onSelect = { _, selected ->
            Log.d("Form", "listPicker selected: $selected")
        }
        datePicker.onSelect = { picker, selected ->
            Log.d("Form", "datePicker selected: ${picker.getDateFormat().format(selected.time)}")
        }
        timePicker.onSelect = { picker, hour, minute ->
            Log.d("Form", "timePicker selected: $hour:$minute")
            // Truncate to 5-minute interval
            val truncated = (minute / 5) * 5
            picker.setTime(hour, truncated)
        }
        return ScrollView(context).apply {
            addView(contentView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        }
    }
}
