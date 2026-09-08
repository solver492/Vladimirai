package com.ai.venice.util

import android.content.Context
import com.ai.venice.R

object ResourceUtils {
    fun getDrawableIdByName(context: Context, name: String?): Int {
        if (name.isNullOrBlank()) return R.drawable.vlad_ai_logo
        return try {
            val resId = context.resources.getIdentifier(name, "drawable", context.packageName)
            if (resId != 0) resId else R.drawable.vlad_ai_logo
        } catch (_: Exception) {
            R.drawable.vlad_ai_logo
        }
    }
}
