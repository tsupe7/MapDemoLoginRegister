package com.tushar.map.utils

import android.view.View
import androidx.core.view.isVisible

object ViewUtil {

    fun hideView(vararg views: View) {
        for (view in views) {
            view.isVisible = false
        }
    }

    fun showView(vararg views: View) {
        for (view in views) {
            view.isVisible = true
        }
    }
}