package com.kotlin_beginner.newsapplication.util

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class DateUtil {

    companion object {
        fun changeDateFormat(strDate: String?) : String{
            if (strDate.isNullOrEmpty()){
                return ""
            }

            return try {
                val sourceSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val requireSdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
                requireSdf.format(sourceSdf.parse(strDate))

            } catch (e: Exception) {
                ""
            }
        }
    }

}