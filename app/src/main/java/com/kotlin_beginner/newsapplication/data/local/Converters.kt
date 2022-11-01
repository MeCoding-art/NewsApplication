package com.kotlin_beginner.newsapplication.data.local

import androidx.room.TypeConverter
import com.kotlin_beginner.newsapplication.data.model.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source): String{
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source{
        return Source(name, name)
    }
}