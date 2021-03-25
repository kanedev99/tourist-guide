package vinova.kane.touristguide.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import vinova.kane.touristguide.R

fun ImageView.setImageUrl(url: String) {
    if (!url.isNullOrEmpty()) {
        Glide.with(context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(RequestOptions().transform(CenterCrop()))
            .placeholder(R.mipmap.photo_loading_landscape)
            .error(R.mipmap.photo_loading_landscape)
            .into(this)
    } else {
        setImageResource(R.mipmap.photo_placeholder)
    }
}
