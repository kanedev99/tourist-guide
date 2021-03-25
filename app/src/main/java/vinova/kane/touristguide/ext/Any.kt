package vinova.kane.touristguide.ext

object Any {
    fun Any?.isNull() = this == null

    fun Float.round(decimals: Int = 2): String
            = "%.${decimals}f".format(this)
}