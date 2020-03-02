package com.oursky.widget.helper

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.view.inputmethod.InputMethodManager

@Suppress("unused")
internal object KeyboardHelper {
    fun showAndFocus(activity: Activity?, view: View) {
        if (activity == null) return
        // Uncomment below if you want to skip with hardware keyboard
        if (activity.resources.configuration.keyboard == Configuration.KEYBOARD_QWERTY) return
        view.requestFocus()
        view.postDelayed({
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(activity.currentFocus, InputMethodManager.SHOW_FORCED)
        }, 500)
        // NOTE: We add a delay here to resolve a race condition which transit from A screen to B screen,
        // where A's onDetach() dismiss keyboard and B's onAttach() want to show keyboard
        // Since we cannot assume the lifecycle managed by Conductor, we add a delay here to make sure A is gone and the keyboard will be shown.
    }
    fun hide(activity: Activity?) {
        if (activity == null) return
        var view = activity.currentFocus
        // If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = activity.window.decorView
        }
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
    fun setAdjustMode(activity: Activity, mode: Int) {
        activity.window.setSoftInputMode(mode)
    }
}
