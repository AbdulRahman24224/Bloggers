package com.example.bloggers.base.utils.datetime

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtil {

    @SuppressLint("SimpleDateFormat")
    fun getDateTime(dateTime: String): String? {
        return try {

            val serverFormat = SimpleDateFormat("yyyy-MM-dd")
            val myFormat = SimpleDateFormat("dd MMM yyyy ")

            val date = serverFormat.parse(dateTime) ?: Date()
            myFormat.format(date).toString()

        } catch (e: Exception) {
            return e.toString()
        }
    }
}


