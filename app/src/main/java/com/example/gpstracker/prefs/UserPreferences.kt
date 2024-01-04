package com.example.gpstracker.prefs

import android.content.Context
import com.example.gpstracker.extensions.saveString
import javax.inject.Inject

class UserPreferences @Inject constructor(
    val context: Context
) {

    private val prefs = context.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun setUserId(id:String){
        prefs.saveString(USER_ID, id)
    }

    //fun getUserId() = prefs.getStringNotNull(USER_ID)
    fun getUserId(): String? {
        return prefs.getString(USER_ID, null)
    }

    companion object{
        const val USER_ID = "user_id"
        const val USER_PREFERENCES_NAME = "user_prefs"
    }
}