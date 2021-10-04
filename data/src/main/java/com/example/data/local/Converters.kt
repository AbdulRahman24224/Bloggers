package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.entities.Address
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun fromStringToAddress(json: String) : Address{
    return  Gson().fromJson(json  , Address::class.java)
    }

    @TypeConverter
    fun addressToString(address: Address): String {
        return Gson().toJson(address).toString()
    }

}