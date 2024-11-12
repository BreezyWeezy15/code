package com.rick.morty.db

import android.util.Base64
import androidx.room.TypeConverter


class Converters {
    @TypeConverter
    fun fromByteArrayToString(byteArray: ByteArray): String {
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    @TypeConverter
    fun fromStringToByteArray(encodedString: String): ByteArray {
        return Base64.decode(encodedString, Base64.DEFAULT)
    }
}