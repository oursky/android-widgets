package com.oursky.widgets.demo.ui

import android.content.Context
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.ScrollView
import com.oursky.widget.FormEdit
import com.oursky.widgets.demo.R

class FormScreen : BaseController() {
    override fun onCreateView(context: Context): View {
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
        val contentView = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }
        contentView.addView(edit1, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(32, 32, 32, 0)
        })
        contentView.addView(edit2, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(32, 32, 32, 0)
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
        return ScrollView(context).apply {
            addView(contentView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        }
    }
}
