package vinova.kane.touristguide.vo

import android.net.Uri

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val dateOfBirth: String = "",
    var imgUri: Uri? = null
)