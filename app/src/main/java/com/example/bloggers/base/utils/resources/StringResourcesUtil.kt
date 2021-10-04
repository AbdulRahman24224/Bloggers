package com.example.bloggers.base.utils.resources

import android.content.Context
import com.example.bloggers.R

object StringResourcesUtil {

    fun getStringValueOrNull(context: Context, string: String) =
        when (string) {
            "no more data" -> context.resources.getString(R.string.all_showed)
            "showing cached" -> context.resources.getString(R.string.showing_cached)
            "couldn't refresh" -> context.resources.getString(R.string.couldnt_refresh)
            else -> null
        }

}