package vinova.kane.touristguide.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SaveSharedPreference {
    private fun getPreferences(context: Context): SharedPreferences{
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun setImageUri(context: Context, imageUri: String){
        val editor: SharedPreferences.Editor = getPreferences(context).edit()
        editor.putString("image_uri", imageUri)
        editor.apply()
    }

    fun getImageUri(context: Context): String? {
        return getPreferences(context).getString("image_uri", "")
    }

    fun setUsername(context: Context, username: String){
        val editor: SharedPreferences.Editor = getPreferences(context).edit()
        editor.putString("username", username)
        editor.apply()
    }

    fun getUsername(context: Context): String? {
        return getPreferences(context).getString("username", "")
    }
}