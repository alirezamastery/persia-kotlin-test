package com.persia.test.data.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.persia.test.data.domain.models.Variant
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.*


@ProvidedTypeConverter
class Converters() {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromVariantSelector(data: Variant.VariantSelector): String {
        return moshi.adapter(Variant.VariantSelector::class.java).toJson(data)
    }

    @TypeConverter
    fun toVariantSelector(json: String): Variant.VariantSelector {
        return moshi.adapter(Variant.VariantSelector::class.java).fromJson(json)!!
    }
}
