package com.ai.venice.util

import android.content.Context
import com.ai.venice.R

object ResourceUtils {
    fun getDrawableIdByName(context: Context, name: String?): Int {
        if (name.isNullOrBlank()) return R.drawable.ic_launcher_foreground
        val resId = context.resources.getIdentifier(name, "drawable", context.packageName)
        return if (resId != 0) resId else R.drawable.ic_launcher_foreground
    }
}
