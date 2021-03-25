package vinova.kane.touristguide.ext

import android.view.View

object View {
    fun View.show(){
        this.visibility = View.VISIBLE
    }

    fun View.hide(){
        this.visibility = View.GONE
    }

    fun View.invisible(){
        this.visibility = View.INVISIBLE
    }
}