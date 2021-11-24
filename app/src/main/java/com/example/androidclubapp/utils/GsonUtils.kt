package com.example.androidclubapp.utils

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.util.*

object GsonUtils {

    class LocalDateTimeTypeAdapter : TypeAdapter<LocalDateTime>() {

        override fun write(out: JsonWriter, value: LocalDateTime) {
            out.value(value.toString())
        }

        override fun read(input: JsonReader): LocalDateTime = LocalDateTime.parse(input.nextString())
    }

    val gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter().nullSafe())
        .create()

    abstract class GsonTypeConverters<T> {

        @TypeConverter
        fun jsonDataToObjectList(data: String?): List<T> {
            if (data == null) {
                return Collections.emptyList()
            }

            val itemType: Type = object : com.google.gson.reflect.TypeToken<T>() {}.type
            return gson.fromJson(data, itemType)
        }

        @TypeConverter
        fun objectListToJsonData(someObjects: List<T?>?): String {
            return gson.toJson(someObjects)
        }
    }
}