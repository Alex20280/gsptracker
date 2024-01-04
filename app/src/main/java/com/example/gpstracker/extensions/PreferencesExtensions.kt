package com.example.gpstracker.extensions

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.preferences.preferencesDataStore

private const val DEFAULT_INT = -1
private const val DEFAULT_STRING = ""

val Context.myDataStore by preferencesDataStore(name = "login_details")

fun SharedPreferences.saveString(key: String, value: String? = null) {
    this.edit().putString(key, value).apply()
}

fun SharedPreferences.getStringNotNull(key: String): String {

    return this.getString(key, DEFAULT_STRING) ?: DEFAULT_STRING
}

fun SharedPreferences.saveBoolean(key: String, value: Boolean = false){
    this.edit().putBoolean(key, value).apply()
}
fun SharedPreferences.getSaveBoolean(key:String):Boolean{
    return this.getBoolean(key, false)
}
fun SharedPreferences.getSaveBooleanPinKey(key:String):Boolean{
    return this.getBoolean(key, true)
}

fun SharedPreferences.saveInt(key: String, value: Int = DEFAULT_INT){
    this.edit().putInt(key, value).apply()
}

