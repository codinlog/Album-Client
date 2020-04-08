package com.codinlog.album.util;

import androidx.room.TypeConverter;

import java.util.Date;

public class TypeConverterUtitl {
    @TypeConverter
    public static Date revertDate(long value) {
        return new Date(value);
    }

    @TypeConverter
    public static long converterDate(Date value) {
        return value.getTime();
    }
}
